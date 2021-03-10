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
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import org.json.simple.JSONObject;
import static clientPackage.ClientApp.primaryStageClone;

public class SignInController implements Initializable {
    
    @FXML
    private Label label;
    @FXML
    private TextField LoginMail;
    @FXML
    private Button Loginbtn;
    @FXML
    private Button GoSignUpbtn;
    @FXML
    private PasswordField LoginPassword;
    
    private ClientApp clientApp;

    public void setSignIn(ClientApp cp)
    {
      clientApp = cp;
    }
    
    @FXML
    public void GoToSignUp(ActionEvent event) throws IOException
    {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("SignUp.fxml"));
        Parent root = loader.load();
        SignUpController setSignUp = loader.getController();
        setSignUp.setSignUp(clientApp);
        Scene scene2 = new Scene(root);
        primaryStageClone.setScene(scene2);
        primaryStageClone.show();
    }
    
    @FXML
     public void GoToGameModes(ActionEvent event) throws IOException
    {
        JSONObject jsonSignIn = new JSONObject();
        jsonSignIn.put("REQUEST", "signIn");
        jsonSignIn.put("Email", LoginMail.getText());
        jsonSignIn.put("Password", LoginPassword.getText());
        clientApp.sendToServer(jsonSignIn.toJSONString());
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }
}
