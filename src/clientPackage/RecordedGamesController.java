package clientPackage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.simple.JSONObject;
import static clientPackage.ClientApp.primaryStageClone;

public class RecordedGamesController implements Initializable {
    
     @FXML
    private TableView<GameForList> RecordedGames;
    @FXML
    private TableColumn<GameForList, String> GameIDCol;
    @FXML
    private TableColumn<GameForList, String> PlayerXCol;
    @FXML
    private TableColumn<GameForList, String> PlayerOCol;
    @FXML
    private TableColumn<GameForList, String> WinnerCol;
    @FXML
    private TableColumn<GameForList, String> ReplayCol;
    @FXML
    private Button GoToModes;

    private ClientApp clientApp;
    private String myMail;
    
     public void gamesList(ClientApp cp,String str) {
        try {
            clientApp = cp;
            myMail = clientApp.clientEmail;
            JSONArray arrayJson = new JSONArray(str);
            GameIDCol.setCellValueFactory(new PropertyValueFactory<GameForList,String>("id"));
            PlayerXCol.setCellValueFactory(new PropertyValueFactory<GameForList,String>("firstPlayer"));
            PlayerOCol.setCellValueFactory(new PropertyValueFactory<GameForList, String>("secondPlayer"));
            WinnerCol.setCellValueFactory(new PropertyValueFactory<GameForList, String>("winner"));
            ReplayCol.setCellValueFactory(new PropertyValueFactory<GameForList, String>("button"));

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
                RecordedGames.getItems().add(new GameForList(id, first_player, second_player, wiNNer, button));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    
    public void handleButtonAction(ActionEvent actionEvent) {
        Button clickedBtn = (Button)actionEvent.getSource();
        String board = clickedBtn.getUserData().toString();
        JSONObject jsonReplayMoves = new JSONObject();
        jsonReplayMoves.put("REQUEST", "replayMoves");
        jsonReplayMoves.put("board", board);
        clientApp.sendToServer(jsonReplayMoves.toJSONString());
    }

    @FXML
    public void GoToGameModes(ActionEvent event) throws IOException
    {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("GameMode.fxml"));
        Parent root;
        root = loader.load();
        GameModeController chooseC = loader.getController();
        chooseC.choose(clientApp,myMail);
        Scene scene2 = new Scene(root);
        primaryStageClone.setScene(scene2);
        primaryStageClone.setTitle(myMail);
        primaryStageClone.show();
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
}
