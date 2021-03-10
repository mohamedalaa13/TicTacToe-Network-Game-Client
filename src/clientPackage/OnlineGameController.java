package clientPackage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import static clientPackage.ClientApp.primaryStageClone;

public class OnlineGameController implements Initializable {

    @FXML
    private Button button0;
    @FXML
    private Button button1;
    @FXML
    private Button button2;
    @FXML
    private Button button3;
    @FXML
    private Button button4;
    @FXML
    private Button button5;
    @FXML
    private Button button6;
    @FXML
    private Button button7;
    @FXML
    private Button button8;
    @FXML
    private Label headerLabel;
    @FXML
    private Button RecordGameBtn;
    @FXML
    private Button BackToModeBtn;

    private int turn;
    private boolean gameOver = false;
    private boolean waitForAnotherPlayer = false;

    private ClientApp clientApp;
    private String opponentEmail;
    private String myEmail;
    private String myChar;
    private String otherChar;
    private int gameID;
    private JSONArray coordinatesArray;

    @FXML
    public void GoToGameModes(ActionEvent event) throws IOException
    {
        PlayerSchema.updateStatus(myEmail, 1);
        FXMLLoader loader = new FXMLLoader(getClass().getResource("GameMode.fxml"));
        Parent root;
        root = loader.load();
        GameModeController chooseC = loader.getController();
        chooseC.choose(clientApp,myEmail);
        Scene scene2 = new Scene(root);
        primaryStageClone.setScene(scene2);
        primaryStageClone.setTitle(myEmail);
        primaryStageClone.show();
    }
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        button0.setUserData(0);
        button0.setOnAction(this::doButtonAction);
        button1.setUserData(1);
        button1.setOnAction(this::doButtonAction);
        button2.setUserData(2);
        button2.setOnAction(this::doButtonAction);
        button3.setUserData(3);
        button3.setOnAction(this::doButtonAction);
        button4.setUserData(4);
        button4.setOnAction(this::doButtonAction);
        button5.setUserData(5);
        button5.setOnAction(this::doButtonAction);
        button6.setUserData(6);
        button6.setOnAction(this::doButtonAction);
        button7.setUserData(7);
        button7.setOnAction(this::doButtonAction);
        button8.setUserData(8);
        button8.setOnAction(this::doButtonAction);

        BackToModeBtn.setDisable(true);
        RecordGameBtn.setDisable(true);
        RecordGameBtn.addEventHandler(ActionEvent.ACTION, (ActionEvent e) -> {
            if(myChar == "X") {
                GameSchema.setRecordByFirstPlayer(gameID);
            } else if (myChar == "O") {
                GameSchema.setRecordBySecondPlayer(gameID);
            }
            Platform.runLater(() -> {
                Alert gameSaved = new Alert(Alert.AlertType.INFORMATION);
                gameSaved.setTitle("Game Saved");
                gameSaved.setHeaderText("Message");
                String s = "Game Saved";
                gameSaved.setContentText(s);
                gameSaved.show();
                switchScene();
            });
        });
    }
    public  void OnlineGUI(ClientApp clientApp, String opponentEmail, int gameID) {
        this.clientApp = clientApp;
        this.gameID = gameID;
        this.opponentEmail = opponentEmail;
        coordinatesArray = new JSONArray();
        myEmail = clientApp.clientEmail;

        headerLabel.setFont(new Font(24));
    }

    private void doButtonAction(ActionEvent event) {

        if(turn == 0 && myChar == "O"){
            return;
        }
        if(!gameOver && !waitForAnotherPlayer ){
            Button clickedBtn = (Button)event.getSource();
            int cord = (int) clickedBtn.getUserData();

            if(clickedBtn.getText().length() > 0){
                return;
            }

            JSONObject jsonButton = new JSONObject();
            jsonButton.put("REQUEST","GameCord");
            jsonButton.put("PlayerEmail",myEmail);
            jsonButton.put("OpponentEmail",opponentEmail);
            jsonButton.put("Coordinates", cord);
            jsonButton.put("Character", myChar);
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("Coordinates", cord);
            jsonObject.put("Character", myChar);
            coordinatesArray.add(jsonObject);

            clientApp.sendToServer(jsonButton.toString());
            waitForAnotherPlayer = true;

            turn++;
            clickedBtn.setFont(Font.font("Arial", FontWeight.BOLD, 60));
            clickedBtn.setText(myChar);
            if(turn >=5){
                if(checkIfWon(myChar)) {
                    BackToModeBtn.setDisable(false);
                    RecordGameBtn.setDisable(false);
                    leftPlayer();
                    headerLabel.setText(String.format("  %s Won!", myChar));
                    gameOver = true;
                    GameSchema.updateGameStatus(gameID, 1);
                    PlayerSchema.updateOnlineGameScore(myEmail);
                    GameSchema.setWinner(gameID, myEmail);
                    GameSchema.updateGameBoard(coordinatesArray.toJSONString(), gameID);
                    return;
                }

                else if(checkIfWon(otherChar)) {
                    BackToModeBtn.setDisable(false);
                    RecordGameBtn.setDisable(false);
                    leftPlayer();
                    headerLabel.setText(String.format("  %s Won!", otherChar));
                    gameOver = true;
                    GameSchema.updateGameBoard(coordinatesArray.toJSONString(), gameID);
                    GameSchema.updateGameStatus(gameID, 1);
                    PlayerSchema.updateOnlineGameScore(myEmail);
                    GameSchema.setWinner(gameID, opponentEmail);
                    return;
                }
            }
            if(turn == 9){
                BackToModeBtn.setDisable(false);
                RecordGameBtn.setDisable(false);
                gameOver = true;
                leftPlayer();
                GameSchema.updateGameBoard(coordinatesArray.toJSONString(), gameID);
                GameSchema.updateGameStatus(gameID, 1);
                GameSchema.setWinner(gameID, "No Winner");
                headerLabel.setText("  Draw! No Winner");

                return;
            }
            headerLabel.setText(String.format("  %s's turn",turn %2 == 0? "X" : "O"));
        }
    }

    private boolean checkIfWon(String player) {

        if(player.equals(button0.getText()) &&
                player.equals(button1.getText()) &&
                player.equals(button2.getText())){
            return true;
        }
        if(player.equals(button3.getText()) &&
                player.equals(button4.getText()) &&
                player.equals(button5.getText())){
            return true;
        }
        if(player.equals(button6.getText()) &&
                player.equals(button7.getText()) &&
                player.equals(button8.getText())){
            return true;
        }
        if(player.equals(button0.getText()) &&
                player.equals(button3.getText()) &&
                player.equals(button6.getText())){
            return true;
        }
        if(player.equals(button1.getText()) &&
                player.equals(button4.getText()) &&
                player.equals(button7.getText())){
            return true;
        }
        if(player.equals(button2.getText()) &&
                player.equals(button5.getText()) &&
                player.equals(button8.getText())){
            return true;
        }
        if(player.equals(button0.getText()) &&
                player.equals(button4.getText()) &&
                player.equals(button8.getText())){
            return true;
        }
        if(player.equals(button2.getText()) &&
                player.equals(button4.getText()) &&
                player.equals(button6.getText())){
            return true;
        }
        return false;
    }

    public void myTurn(int cord, String character){
        Platform.runLater(() ->{
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("Coordinates", cord);
            jsonObject.put("Character", character);
            coordinatesArray.add(jsonObject);
            switch (cord) {
                case 0:
                    button0.setFont(Font.font("Arial", FontWeight.BOLD, 60));
                    button0.setText(character);
                    break;
                case 1:
                    button1.setFont(Font.font("Arial", FontWeight.BOLD, 60));
                    button1.setText(character);
                    break;
                case 2:
                    button2.setFont(Font.font("Arial", FontWeight.BOLD, 60));
                    button2.setText(character);
                    break;
                case 3:
                    button3.setFont(Font.font("Arial", FontWeight.BOLD, 60));
                    button3.setText(character);
                    break;
                case 4:
                    button4.setFont(Font.font("Arial", FontWeight.BOLD, 60));
                    button4.setText(character);
                    break;
                case 5:
                    button5.setFont(Font.font("Arial", FontWeight.BOLD, 60));
                    button5.setText(character);
                    break;
                case 6:
                    button6.setFont(Font.font("Arial", FontWeight.BOLD, 60));
                    button6.setText(character);
                    break;
                case 7:
                    button7.setFont(Font.font("Arial", FontWeight.BOLD, 60));
                    button7.setText(character);
                    break;
                case 8:
                    button8.setFont(Font.font("Arial", FontWeight.BOLD, 60));
                    button8.setText(character);
                    break;
            }
            turn++;
            waitForAnotherPlayer = false;
            if(turn >=5){
                if(checkIfWon(myChar)) {
                    BackToModeBtn.setDisable(false);
                    RecordGameBtn.setDisable(false);
                    leftPlayer();
                    headerLabel.setText(String.format("  %s Won!", myChar));
                    gameOver = true;
                    GameSchema.updateGameBoard(coordinatesArray.toJSONString(), gameID);
                    return;
                }

                if(checkIfWon(otherChar)) {
                    BackToModeBtn.setDisable(false);
                    RecordGameBtn.setDisable(false);
                    leftPlayer();
                    headerLabel.setText(String.format("  %s Won!", otherChar));
                    gameOver = true;
                    GameSchema.updateGameBoard(coordinatesArray.toJSONString(), gameID);
                    return;
                }
            }
            if(turn == 9){
                BackToModeBtn.setDisable(false);
                RecordGameBtn.setDisable(false);
                gameOver = true;
                leftPlayer();
                GameSchema.updateGameBoard(coordinatesArray.toJSONString(), gameID);
                GameSchema.updateGameStatus(gameID, 1);
                GameSchema.setWinner(gameID, "No Winner");
                headerLabel.setText("Draw! No Winner");
                return;
            }
            headerLabel.setText(String.format("  %s's turn",turn %2 == 0? "X" : "O"));
        });
    }

    public void myChar(String bool){
        if(bool == "true"){
            myChar = "X";
            otherChar="O";
        }else{
            myChar= "O";
            otherChar= "X";
        }
    }

    public void leftPlayer() {
        JSONObject json = new JSONObject();
        json.put("REQUEST", "leftPlayer");
        clientApp.sendToServer(json.toJSONString());
    }
    private void switchScene(){
        FXMLLoader loader = new FXMLLoader(getClass().getResource("GameMode.fxml"));
        Parent root;
        try {
            root = loader.load();
            GameModeController chooseC = loader.getController();
            chooseC.choose(clientApp,myEmail);
            Scene scene2 = new Scene(root);
            primaryStageClone.setScene(scene2);
            primaryStageClone.setTitle(myEmail);
            primaryStageClone.show();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
