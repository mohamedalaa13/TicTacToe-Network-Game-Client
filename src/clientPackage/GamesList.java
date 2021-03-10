package clientPackage;

import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import org.json.JSONArray;
import org.json.simple.JSONObject;

public class GamesList {

    private ClientApp clientApp;
    private TableView<GameForList> tableView;
    private VBox vbox;
    private Label label;

    public GamesList(ClientApp cp, String str, String mail) {
        try {
            clientApp = cp;
            tableView = new TableView<>();
            JSONArray arrayJson = new JSONArray(str);
            vbox = new VBox();
            tableView.setEditable(false);
            TableColumn gameID = new TableColumn("Game ID");
            TableColumn firstPlayer = new TableColumn("X");
            TableColumn secondPlayer = new TableColumn("O");
            TableColumn winner = new TableColumn("Winner");
            TableColumn replay = new TableColumn("Replay");
            label = new Label("Games List");
            tableView.getColumns().add(gameID);
            tableView.getColumns().add(firstPlayer);
            tableView.getColumns().add(secondPlayer);
            tableView.getColumns().add(winner);
            tableView.getColumns().add(replay);

            gameID.setCellValueFactory(new PropertyValueFactory<GameForList,String>("id"));
            firstPlayer.setCellValueFactory(new PropertyValueFactory<GameForList,String>("firstPlayer"));
            secondPlayer.setCellValueFactory(new PropertyValueFactory<GameForList, String>("secondPlayer"));
            winner.setCellValueFactory(new PropertyValueFactory<GameForList, String>("winner"));
            replay.setCellValueFactory(new PropertyValueFactory<GameForList, String>("button"));

            for (int i = 0, size = arrayJson.length(); i < size; i++) {
                org.json.JSONObject objectInArray = arrayJson.getJSONObject(i);
                String id =  objectInArray.get("Id").toString();
                String first_player =  objectInArray.get("firstPlayer").toString();
                String second_player = objectInArray.get("secondPlayer").toString();
                String wiNNer = objectInArray.get("winner").toString();
                String board = objectInArray.get("board").toString();
                Button button = new Button("Replay");
                button.setOnAction(this::handleButtonAction);
                button.setUserData(board);
                tableView.getItems().add(new GameForList(id, first_player, second_player, wiNNer, button));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        vbox.getChildren().addAll(label, tableView);
    }

    private void handleButtonAction(ActionEvent actionEvent) {
        Button clickedBtn = (Button)actionEvent.getSource();
        String board = clickedBtn.getUserData().toString();
        JSONObject jsonReplayMoves = new JSONObject();
        jsonReplayMoves.put("REQUEST", "replayMoves");
        jsonReplayMoves.put("board", board);
        clientApp.sendToServer(jsonReplayMoves.toJSONString());
    }

    public  VBox getVbox(){
        return vbox;
    }
}
