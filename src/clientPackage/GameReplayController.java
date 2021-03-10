package clientPackage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.util.Duration;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import static clientPackage.ClientApp.primaryStageClone;

public class GameReplayController implements Initializable {

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
    private Button BackToMenu;

    private ClientApp clientApp;
    private String myEmail;
    private String character;
    private int coordinates;
    private String jsonBoard;
    
    @FXML
    public void GoToGameModes(ActionEvent event) throws IOException
    {
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
        // TODO
    }

    public void ShowGameGUI(ClientApp clientApp, String jsonBoard) throws ParseException {
        this.jsonBoard = jsonBoard;
        this.clientApp = clientApp;
        myEmail = clientApp.clientEmail;

        button0.setUserData(0);
        button1.setUserData(1);
        button2.setUserData(2);
        button3.setUserData(3);
        button4.setUserData(4);
        button5.setUserData(5);
        button6.setUserData(6);
        button7.setUserData(7);
        button8.setUserData(8);

        BackToMenu.addEventHandler(ActionEvent.ACTION, (ActionEvent e) -> {
            switchScene();
        });

    }

    public void printMoves(String jsonBoard) throws JSONException {
        JSONArray arrayJson = new JSONArray(jsonBoard);
        Timeline timeline = new Timeline();
        JSONParser parser = new JSONParser();
        int j = 0;
        for (int i=0; i < arrayJson.length(); i++) {
//        for (Object object : arrayJson) {

            JSONObject objectInArray = (JSONObject) arrayJson.getJSONObject(i);
            timeline.getKeyFrames().add(
                    new KeyFrame(Duration.millis((j+1)*1000),
                            e -> {
                                try {
                                    character = objectInArray.get("Character").toString();
                                } catch (JSONException jsonException) {
                                    jsonException.printStackTrace();
                                }
                                try {
                                    coordinates = Integer.parseInt(objectInArray.get("Coordinates").toString());
                                } catch (JSONException jsonException) {
                                    jsonException.printStackTrace();
                                }
                                switch (coordinates) {
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
                            })
            );
            j++;
        }
        Platform.runLater(timeline::play);
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
