package clientPackage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.simple.JSONObject;
import static clientPackage.ClientApp.primaryStageClone;

public class InvitationController implements Initializable 
{

    @FXML
    private TableView<PlayerForListInClient> PlayerList;
    @FXML
    private TableColumn<PlayerForListInClient, String> PlayerName;
    @FXML
    private TableColumn<PlayerForListInClient, String> PlayerEmail;
    @FXML
    private TableColumn<PlayerForListInClient, Integer> PlayerScore;
    @FXML
    private TableColumn<PlayerForListInClient, String> PlayerLevel;
    @FXML
    private TableColumn<PlayerForListInClient, String> Invite;
    @FXML
    private Button GameModes;
    @FXML
    private Button RereshListBtn;

    private ClientApp clientApp;
    public String mail;
    
     @FXML
    public void GoToGameModes(ActionEvent event) throws IOException
    {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("GameMode.fxml"));
        Parent root;
        root = loader.load();
        GameModeController chooseC = loader.getController();
        chooseC.choose(clientApp,mail);
        Scene scene2 = new Scene(root);
        primaryStageClone.setScene(scene2);
        primaryStageClone.setTitle(mail);
        primaryStageClone.show();
    }
    @FXML
    public void RefreshListBtn(ActionEvent event) throws IOException
    {
        JSONObject jsonRefresh = new JSONObject();
        jsonRefresh.put("REQUEST", "refreshList");
        clientApp.sendToServer(jsonRefresh.toJSONString());
    }

    public void OnlineList(ClientApp cp,String str, String mail) {
        try {
            clientApp = cp;
            this.mail=mail;
            JSONArray arrayJson = new JSONArray(str);

            PlayerName.setCellValueFactory(new PropertyValueFactory<PlayerForListInClient,String>("username"));
            PlayerEmail.setCellValueFactory(new PropertyValueFactory<PlayerForListInClient,String>("email"));
            PlayerScore.setCellValueFactory(new PropertyValueFactory<PlayerForListInClient, Integer>("score"));
            PlayerLevel.setCellValueFactory(new PropertyValueFactory<PlayerForListInClient, String>("level"));
            Invite.setCellValueFactory(new PropertyValueFactory<PlayerForListInClient, String>("button"));

            for (int i = 0, size = arrayJson.length(); i < size; i++) {
                org.json.JSONObject objectInArray = arrayJson.getJSONObject(i);
                String username =  objectInArray.get("Username").toString();
                String email =  objectInArray.get("Email").toString();
                int score = Integer.parseInt(objectInArray.get("Score").toString()) ;
                Button button = new Button("Invite");
                button.setOnAction(this::handleButtonAction);
                button.setUserData(email);
                if(!email.equalsIgnoreCase(mail))
                {
                    PlayerList.getItems().add(new PlayerForListInClient(username, email, score,button));
                }
            }

        } catch (JSONException | NullPointerException Exception ) {
            Exception.printStackTrace();
        }
    }
    public void handleButtonAction(ActionEvent actionEvent) {

        Button clickedBtn = (Button)actionEvent.getSource();
        String email = clickedBtn.getUserData().toString();
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Invitation");
        alert.setHeaderText("Message");
        String s = "Invitation Sent Successfully";
        alert.setContentText(s);
        alert.show();

        JSONObject jsonSendInvitation = new JSONObject();
        jsonSendInvitation.put("REQUEST", "sendInvitation");
        jsonSendInvitation.put("myEmail", mail);
        jsonSendInvitation.put("emailOtherPlayer", email);
        clientApp.sendToServer(jsonSendInvitation.toJSONString());
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        RereshListBtn.setText("Refresh List");

    }    
    
}
