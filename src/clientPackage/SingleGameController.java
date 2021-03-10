package clientPackage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import static clientPackage.ClientApp.primaryStageClone;

public class SingleGameController implements Initializable {

    @FXML
    private Button TxtBtn1;
    @FXML
    private Button TxtBtn2;
    @FXML
    private Button TxtBtn3;
    @FXML
    private Button TxtBtn4;
    @FXML
    private Button TxtBtn5;
    @FXML
    private Button TxtBtn7;
    @FXML
    private Button TxtBtn8;
    @FXML
    private Button TxtBtn6;
    @FXML
    private Button TxtBtn9;
    @FXML
    private Button ExitGameBtn;

    private ClientApp clientApp;
    private String myEmail;
    private int turn;
    private boolean gameOver = false;
    private boolean aiTurn = false;
    javafx.scene.paint.Color xForeground = javafx.scene.paint.Color.GREEN;
    javafx.scene.paint.Color oForeground = javafx.scene.paint.Color.RED;

    public void startGame(ClientApp clientApp)
    {
        this.clientApp = clientApp;
        myEmail = clientApp.clientEmail;
    }

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
        TxtBtn1.setOnAction(this::doButtonAction);
        TxtBtn2.setOnAction(this::doButtonAction);
        TxtBtn3.setOnAction(this::doButtonAction);
        TxtBtn4.setOnAction(this::doButtonAction);
        TxtBtn5.setOnAction(this::doButtonAction);
        TxtBtn6.setOnAction(this::doButtonAction);
        TxtBtn7.setOnAction(this::doButtonAction);
        TxtBtn8.setOnAction(this::doButtonAction);
        TxtBtn9.setOnAction(this::doButtonAction);
    }
    @FXML
    private void doButtonAction(ActionEvent event){

        if(!gameOver && !aiTurn){
            Button clickedBtn = (Button)event.getSource();
            if(clickedBtn.getText().length() > 0){
                if (turn % 2 !=0){
                    TurnForAI();
                }
                return;
            }
            String place;
            if(turn %2 ==0){
                place = "X";
            }else{
                place = "O";
            }
            turn++;
            clickedBtn.setFont(Font.font("Arial", FontWeight.BOLD, 60));
            clickedBtn.setText(place);
            if(turn >=5){
                if(checkIfWon(place)) {
                    ExitGameBtn.setDisable(false);
                    gameOver = true;
                    if(place == "X") {
                        alertForWin("X");
                        PlayerSchema.updateOfflineGameScore(myEmail);
                    }
                    else if(place == "O"){
                        alertForWin("O");
                    }
                    return;
                }
            }
            if(turn == 9){
                ExitGameBtn.setDisable(false);
                alertForDraw();
                gameOver = true;
                return;
            }
            if (turn %2 != 0){
                aiTurn = true;
                Runnable runnable = new Runnable(){
                    @Override
                    public void run(){
                        Platform.runLater(() ->{
                            TurnForAI();

                        });
                    }
                };
                ScheduledExecutorService service = Executors.newSingleThreadScheduledExecutor();
                service.schedule(runnable, 1000, TimeUnit.MILLISECONDS);
            }
        }
    }

    private boolean checkIfWon(String player) {

        if(player.equals(TxtBtn1.getText()) &&
                player.equals(TxtBtn2.getText()) &&
                player.equals(TxtBtn3.getText())){
            return true;
        }
        if(player.equals(TxtBtn4.getText()) &&
                player.equals(TxtBtn5.getText()) &&
                player.equals(TxtBtn6.getText())){
            return true;
        }
        if(player.equals(TxtBtn7.getText()) &&
                player.equals(TxtBtn8.getText()) &&
                player.equals(TxtBtn9.getText())){
            return true;
        }
        if(player.equals(TxtBtn1.getText()) &&
                player.equals(TxtBtn4.getText()) &&
                player.equals(TxtBtn7.getText())){
            return true;
        }
        if(player.equals(TxtBtn2.getText()) &&
                player.equals(TxtBtn5.getText()) &&
                player.equals(TxtBtn8.getText())){
            return true;
        }
        if(player.equals(TxtBtn3.getText()) &&
                player.equals(TxtBtn6.getText()) &&
                player.equals(TxtBtn9.getText())){
            return true;
        }
        if(player.equals(TxtBtn1.getText()) &&
                player.equals(TxtBtn5.getText()) &&
                player.equals(TxtBtn9.getText())){
            return true;
        }
        if(player.equals(TxtBtn3.getText()) &&
                player.equals(TxtBtn5.getText()) &&
                player.equals(TxtBtn7.getText())){
            return true;
        }
        return false;
    }

    private void TurnForAI() {
        aiTurn = false;
        int i = (int)(Math.random()*9) + 1;
        switch (i) {
            case 1:
                TxtBtn1.fire();
                break;
            case 2:
                TxtBtn2.fire();
                break;
            case 3:
                TxtBtn3.fire();
                break;
            case 4:
                TxtBtn4.fire();
                break;
            case 5:
                TxtBtn5.fire();
                break;
            case 6:
                TxtBtn6.fire();
                break;
            case 7:
                TxtBtn7.fire();
                break;
            case 8:
                TxtBtn8.fire();
                break;
            case 9:
                TxtBtn9.fire();
                break;
        }
    }
    private void alertForDraw() {
        Platform.runLater(() -> {
            Alert RefusedAlert = new Alert(Alert.AlertType.INFORMATION);
            RefusedAlert.setTitle("End Game");
            RefusedAlert.setHeaderText("Message");
            String s = "Tie!";
            RefusedAlert.setContentText(s);
            RefusedAlert.show();
        });
    }
    private void alertForWin(String character) {
        Platform.runLater(() -> {
            Alert RefusedAlert = new Alert(Alert.AlertType.INFORMATION);
            RefusedAlert.setTitle("End Game");
            RefusedAlert.setHeaderText("Message");
            String s = character + " Won";
            RefusedAlert.setContentText(s);
            RefusedAlert.show();
        });
    }
}
