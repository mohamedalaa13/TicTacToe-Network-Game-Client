package clientPackage;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.TilePane;
import javafx.stage.Popup;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.json.JSONException;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.Level;
import java.util.logging.Logger;

public  class ClientApp extends Application {

    private AtomicBoolean shutDown = new AtomicBoolean(false);
    public static Socket clientSocket;
    private DataInputStream clientInputStreamFromServer;
    private PrintStream clientPrintStreamToServer;
    private TilePane tilepane;
    private Label label;
    private Popup popup;

    private String headline;
    public static Stage primaryStageClone = null;
    OnlineGameController OnlineG;
    private FlowPane fp2;
    private String onlineJsonList = "[{},{}]";
    private Thread mainThread;

    public String clientEmail;
    public String leftEmail;

    public ClientApp() {
        try {
            clientSocket = new Socket("127.0.0.1", 5005);
            clientInputStreamFromServer = new DataInputStream(clientSocket.getInputStream());
            clientPrintStreamToServer = new PrintStream(clientSocket.getOutputStream());
        } catch (IOException ex) {
            Logger.getLogger(ClientApp.class.getName()).log(Level.SEVERE, null, ex);
        }

        mainThread = new Thread(() -> {
            while(!shutDown.get() || !clientSocket.isClosed()) {
                String msg;
                try {
                    msg = clientInputStreamFromServer.readLine();
                    handleServerMessage(msg);
                } catch (Exception ex) {
                    System.out.println("Server Closed, Try Again");
                    connectionClosed();
                    Logger.getLogger(ClientApp.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
    }

    public void sendToServer(String str){
        clientPrintStreamToServer.println(str);
    }

    public void init() {
        label = new Label("Invitation Sent");
        popup = new Popup();
    }

    @Override
    public void start(Stage primaryStage) throws Exception{
        primaryStageClone = primaryStage;
        popup.getContent().add(label);

        mainThread.start();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("SignIn.fxml"));
        Parent root = loader.load();
        SignInController setSignIn = loader.getController();
        setSignIn.setSignIn(this);
        primaryStage.initStyle(StageStyle.DECORATED);
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    @Override
    public void stop() {
            connectionClosed ();
    }
    private void handleServerMessage(String message){
        JSONParser parser = new JSONParser();
        try {
            JSONObject json = (JSONObject) parser.parse(message);
            String type = json.get("RESPONSE").toString();
            switch (type) {
                case "playOnlineToClient":
                    handleListPlayers(json);
                    break;
                case "sendInvitationResponse":
                    handleSendInvitation(json);
                    break;
                case "signInResponse":
                    handleSignInResponse(json);
                    break;
                case "playOffline":
                    handlePlayOffline();
                    break;
                case "acceptInvitation":
                    handleAcception(json);
                    break;
                case "refusedInvitation" :
                    handleRejection(json);
                    break;
                case "myTurn" :
                    handleMyTurn(json);
                    break;
                case "showGames":
                    handleShowGames(json);
                    break;
                case "replayMoves":
                    handleReplayMoves(json);
                    break;
                case "clientLeft":
                    handleClientLeftGame(json);
                    break;
                case "leftPlayer":
                    handleLeftPlayer();
                    break;

                case "handleErrorsRes":
                    handleErrors(json);
                    break;
                default:
                    break;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    private void handleErrors(JSONObject json) {
        Platform.runLater(() -> {
            String EmailExists="";
            if(json.containsKey("EmailExists")){
                EmailExists = json.get("EmailExists").toString();
            }
            String EmailValid="";
            if(json.containsKey("EmailValid")){
                EmailValid = json.get("EmailValid").toString();
            }
            String NameExists="";
            if(json.containsKey("NameExists")){
                NameExists = json.get("NameExists").toString();
            }
            String NameValid="";
            if(json.containsKey("NameValid")){
                NameValid = json.get("NameValid").toString();
            }
            String PassValid="";
            if(json.containsKey("PassValid")){
                PassValid = json.get("PassValid").toString();
            }
            String SignUpSuccess="";
            if(json.containsKey("SignUpSuccess")){
                SignUpSuccess = json.get("SignUpSuccess").toString();

                Alert ErrorAlert = new Alert(Alert.AlertType.CONFIRMATION);
                ErrorAlert.setTitle("Success");
                ErrorAlert.setContentText(SignUpSuccess);
                ErrorAlert.show();

            }else
            {
                Alert ErrorAlert = new Alert(Alert.AlertType.ERROR);
                ErrorAlert.setTitle("Errors");
                ErrorAlert.setContentText(EmailExists+"\n"+EmailValid+"\n"+NameExists+"\n"+NameValid+"\n"+PassValid);
                ErrorAlert.show();
            }

        });
    }

    private void handleLeftPlayer() {
        this.leftEmail = "";
    }

    private void handleClientLeftGame(JSONObject json) {
        String str;
        try {
            str = json.get("Email").toString();
        } catch (Exception e) {
            return;
        }
        if(str.equals(leftEmail)) {
            PlayerSchema.updateStatus(clientEmail, 1);
            Platform.runLater(() -> {
                Alert playerLeft = new Alert(Alert.AlertType.ERROR);
                playerLeft.setTitle("Player Left");
                playerLeft.setHeaderText("Message");
                String s = "Player Left";
                playerLeft.setContentText(s);
                playerLeft.show();
                FXMLLoader loader = new FXMLLoader(getClass().getResource("GameMode.fxml"));
                Parent root;
                try {
                    root = loader.load();
                    GameModeController chooseC = loader.getController();
                    chooseC.choose(this,clientEmail);
                    Scene scene2 = new Scene(root);
                    primaryStageClone.setScene(scene2);
                    primaryStageClone.setTitle(clientEmail);
                    primaryStageClone.show();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        }
    }

    private void handleMyTurn(JSONObject json) {
        int cord = Integer.parseInt(json.get("cord").toString());
        String place = json.get("Character").toString();
        OnlineG.myTurn(cord,place);
    }

    private void handleSignInResponse(JSONObject json) {
        headline = json.get("Email").toString();
        String validResult = json.get("result").toString();
        if(validResult == "true"){
            clientEmail = headline;
            Platform.runLater(() -> {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("GameMode.fxml"));
                Parent root;
                try {
                    root = loader.load();
                    GameModeController chooseC = loader.getController();
                    chooseC.choose(this,headline);
                    Scene scene2 = new Scene(root);
                    primaryStageClone.setScene(scene2);
                    primaryStageClone.setTitle(headline);
                    primaryStageClone.show();
                } catch (IOException ex) {
                    Logger.getLogger(ClientApp.class.getName()).log(Level.SEVERE, null, ex);
                }
            });
        }
        else  {
            Platform.runLater(() -> {
                String str = json.get("Email").toString();
                Alert RefusedAlert = new Alert(Alert.AlertType.ERROR);
                RefusedAlert.setTitle("Error");
                RefusedAlert.setHeaderText("Message");
                String s = "Invalid email or password";
                RefusedAlert.setContentText(s);
                RefusedAlert.show();
            });
        }
    }
    private void handleSendInvitation(JSONObject json) {
        String playerEmail = json.get("PlayerInvitationEmail").toString();
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Game invitation");
            alert.setContentText(playerEmail + " wants to play with you");
            Optional<ButtonType> result = alert.showAndWait();

            if (result.get() == ButtonType.OK) {
                JSONObject jsonAcceptInvitation = new JSONObject();
                jsonAcceptInvitation.put("REQUEST", "acceptInvitation");
                jsonAcceptInvitation.put("firstPlayerEmail", playerEmail);
                jsonAcceptInvitation.put("secondPlayerEmail", clientEmail);
                clientPrintStreamToServer.println(jsonAcceptInvitation.toString());
            } else if (result.get() == ButtonType.CANCEL) {
                JSONObject jsonNoReply = new JSONObject();
                jsonNoReply.put("REQUEST", "refuseInvitation");
                jsonNoReply.put("playerEmail", playerEmail);
                clientPrintStreamToServer.println(jsonNoReply.toString());
            }
        });
    }
    private void handleListPlayers(JSONObject json) {
        String str = json.get("PlayersJson").toString();
        onlineJsonList = json.get("PlayersJson").toString();
        Platform.runLater(() -> {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Invitation.fxml"));
            Parent root;
            try {
                root = loader.load();
                InvitationController OnlineL = loader.getController();
                OnlineL.OnlineList(this,str,clientEmail);
                Scene scene2 = new Scene(root);
                primaryStageClone.setScene(scene2);
                primaryStageClone.setTitle(clientEmail);
                primaryStageClone.show();
            } catch (IOException ex) {
                Logger.getLogger(ClientApp.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
    }
    private void handleShowGames(JSONObject json) {
        String str = json.get("GamesJson").toString();
        onlineJsonList = json.get("GamesJson").toString();
        Platform.runLater(() -> {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("RecordedGames.fxml"));
            Parent root;
            try {
                root = loader.load();
                RecordedGamesController GameR = loader.getController();
                GameR.gamesList(this,str);
                Scene scene6 = new Scene(root);
                primaryStageClone.setScene(scene6);
                primaryStageClone.setTitle(clientEmail);
                primaryStageClone.show();
            } catch (IOException ex) {
                Logger.getLogger(ClientApp.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
    }
    private void handleReplayMoves(JSONObject json) {
        JSONObject jsonGames = (JSONObject) json.get("GamesJson");
        String jsonMoves = jsonGames.get("board").toString();
        Platform.runLater(() -> {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("GameReplay.fxml"));
            Parent root;
            try {
                root = loader.load();
                GameReplayController GameR = loader.getController();
                GameR.ShowGameGUI(this,jsonMoves);
                GameR.printMoves(jsonMoves);
                Scene scene6 = new Scene(root);
                primaryStageClone.setScene(scene6);
                primaryStageClone.setTitle(clientEmail);
                primaryStageClone.show();
            } catch (IOException | ParseException | JSONException ex) {
                Logger.getLogger(ClientApp.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
    }
    private void handlePlayOffline() {
        Platform.runLater(() -> {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("SingleGame.fxml"));
            Parent root;
            try {
                root = loader.load();
                SingleGameController startG = loader.getController();
                startG.startGame(this);
                Scene scene2 = new Scene(root);
                primaryStageClone.setScene(scene2);
                primaryStageClone.setTitle(this.clientEmail);
                primaryStageClone.show();
            } catch (IOException ex) {
                Logger.getLogger(ClientApp.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
    }
    private void handleAcception(JSONObject json) {
        String opponentEmail = json.get("OpponentEmail").toString();
        leftEmail = opponentEmail;
        String playerSender = json.get("PlayerSender").toString();
        int gameID = Integer.parseInt(json.get("GameID").toString());
        Platform.runLater(() -> {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("OnlineGame.fxml"));
            Parent root;
            try {
                root = loader.load();
                OnlineG = loader.getController();
                try {
                    OnlineG.OnlineGUI(this,opponentEmail, gameID);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                OnlineG.myChar(playerSender);
                Scene scene2 = new Scene(root);
                primaryStageClone.setScene(scene2);
                primaryStageClone.show();
            } catch (IOException ex) {
                Logger.getLogger(ClientApp.class.getName()).log(Level.SEVERE, null, ex);
            }

        });
    }
    private void handleRejection(JSONObject json) {
        Platform.runLater(() -> {
            String str = json.get("email").toString();
            Alert RefusedAlert = new Alert(Alert.AlertType.ERROR);
            RefusedAlert.setTitle("Invitation");
            RefusedAlert.setHeaderText("Message");
            String s = "Invitation Rejected ";
            RefusedAlert.setContentText(s);
            RefusedAlert.show();
        });
    }
    private void changeStatusOfClosedPlayer() {
        if (clientEmail != null){
            JSONObject jsonReq = new JSONObject();
            jsonReq.put("REQUEST", "statusOffline");
            jsonReq.put("playerEmail", clientEmail);
            clientPrintStreamToServer.println(jsonReq.toString());
        }
    }
    private void connectionClosed () {
        changeStatusOfClosedPlayer();
        mainThread.stop();
        try {
            clientInputStreamFromServer.close();;
            clientPrintStreamToServer.close();
            clientSocket.close();
        } catch (Exception e) {

        }
        shutDown.set(true);
    }
    public static void main(String[] args) {
        launch(args);
    }
}
