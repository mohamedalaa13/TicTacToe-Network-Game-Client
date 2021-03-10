package clientPackage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import org.json.simple.JSONObject;

public class GameModeController implements Initializable {

    @FXML
    private Button SingleModeBtn;
    @FXML
    private Button OnlineModeBtn;
    @FXML
    private Button RecordedGamesBtn;

    String clientEmail;     
    private ClientApp clientApp;
    
    public void choose(ClientApp cp,String ce)
    {
        clientApp = cp;
        clientEmail = ce;
    }
    
    @FXML
    private void GoToSingleGame(ActionEvent event) throws IOException 
    {
        JSONObject jsonPlayOffline = new JSONObject();
        jsonPlayOffline.put("REQUEST", "playOffline");
        jsonPlayOffline.put("Email", clientEmail);
        clientApp.sendToServer(jsonPlayOffline.toJSONString());
    }
    
    @FXML
    private void GoToInvitation(ActionEvent event) throws IOException
    {
       JSONObject jsonPlayOnline = new JSONObject();
        jsonPlayOnline.put("REQUEST", "playOnline");
        jsonPlayOnline.put("playerEmail", clientEmail);
        clientApp.sendToServer(jsonPlayOnline.toJSONString());
    }

    @FXML
     private void GoToRecordedGames(ActionEvent event) throws IOException 
    {
        JSONObject jsonShowGames = new JSONObject();
        jsonShowGames.put("REQUEST", "showGames");
        jsonShowGames.put("Email", clientEmail);
        clientApp.sendToServer(jsonShowGames.toJSONString());
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
}
