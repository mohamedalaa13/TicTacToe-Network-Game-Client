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
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.json.simple.JSONObject;
import clientPackage.ClientApp;
import static clientPackage.ClientApp.primaryStageClone;

public class SignUpController implements Initializable {

    @FXML
    private TextField SignUpName;
    @FXML
    private TextField SignUpMail;
    @FXML
    private PasswordField SignUpPassword;
    @FXML
    private Button SignUpBtn;
    @FXML
    private Button GoBackLoginBtn;
    private ClientApp clientApp;

    public void setSignUp(ClientApp cp)
    {
      clientApp = cp;
    }
    @FXML
    public void GoBackLogin(ActionEvent event) throws IOException
    {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("SignIn.fxml"));
        Parent root = loader.load();
        SignInController setSignIn = loader.getController();
        setSignIn.setSignIn(clientApp);
        Scene scene2 = new Scene(root);
        primaryStageClone.setScene(scene2);
        primaryStageClone.show();
        
    }

    public void GoToGameModes(ActionEvent event) throws IOException
    {

        JSONObject jsonSignUp = new JSONObject();
        jsonSignUp.put("REQUEST", "signUp");
        jsonSignUp.put("Name", SignUpName.getText());
        jsonSignUp.put("Email", SignUpMail.getText());
        jsonSignUp.put("Password", SignUpPassword.getText());
         clientApp.sendToServer(jsonSignUp.toJSONString());
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
}
