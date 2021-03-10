package clientPackage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PlayerSchema {

    private static ArrayList<Player> playersList;
    private static ArrayList<Player> playersOnline;

    public static ArrayList<Player> getPlayersList() {
        playersList = new ArrayList<>();
        Connection connection = DatabaseManager.getDBConnection();
        Statement statement = null;
        try {
            statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM player ORDER BY score DESC");

            while (resultSet.next()) {
                Player user = new Player(
                        resultSet.getInt("id"),
                        resultSet.getString("user_name"),
                        resultSet.getString("email"),
                        resultSet.getInt("status"),
                        resultSet.getInt("score")
                );
                playersList.add(user);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return playersList;
    }

    public static boolean updateOnlineGameScore(String email) {
        try {
            Connection connection = DatabaseManager.getDBConnection();
            PreparedStatement preparedStatement = connection.prepareStatement("UPDATE player SET score=score+10 WHERE email=?");
            preparedStatement.setString(1, email);
            int isUpdated = preparedStatement.executeUpdate();
            if (isUpdated > 0) {
                return true;
            }
        } catch (Exception ex) {
            Logger.getLogger(PlayerSchema.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }
    public static boolean updateOfflineGameScore(String email) {
        try {
            Connection connection = DatabaseManager.getDBConnection();
            PreparedStatement preparedStatement = connection.prepareStatement("UPDATE player SET score=score+3 WHERE email=?");
            preparedStatement.setString(1, email);
            int isUpdated = preparedStatement.executeUpdate();
            if(isUpdated > 0) {
                return true;
            }
        } catch (Exception ex) {
            Logger.getLogger(PlayerSchema.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    public static boolean updateStatus(String email, int status) {
        try {
            Connection connection = DatabaseManager.getDBConnection();
            PreparedStatement preparedStatement = connection.prepareStatement("UPDATE player SET status=? WHERE email=?");
            preparedStatement.setInt(1, status);
            preparedStatement.setString(2, email);
            int isUpdated = preparedStatement.executeUpdate();
            if (isUpdated > 0) {
                return true;
            }
        } catch (Exception ex) {
            Logger.getLogger(PlayerSchema.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    public static boolean createPlayer(Player player) {
        try {
            Connection connection = DatabaseManager.getDBConnection();
            PreparedStatement statement = connection.prepareStatement("INSERT INTO player SET user_name=?, password=?, email=?");
            statement.setString(1, player.getUserName());
            statement.setString(2, player.getPassword());
            statement.setString(3, player.getEmail());
            int isInserted = statement.executeUpdate();
            if (isInserted > 0) {
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean validatePlayer(String email, String password) {
        try {
            Connection connection = DatabaseManager.getDBConnection();
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM player WHERE email=? AND password=?");
            statement.setString(1, email);
            statement.setString(2, password);
            ResultSet res = statement.executeQuery();
            if (res.next()) {
                updateStatus(email, 1);
                return true;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static ArrayList<Player> getAllPlayersOnline() {
        playersOnline = new ArrayList<>();
        Connection connection = DatabaseManager.getDBConnection();
        Statement statement = null;
        try {
            statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM player WHERE status = 1");
            while (resultSet.next()) {
                Player user = new Player(
                        resultSet.getInt("id"),
                        resultSet.getString("user_name"),
                        resultSet.getString("email"),
                        resultSet.getInt("status"),
                        resultSet.getInt("score")
                );
                playersOnline.add(user);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return playersOnline;
    }
    public static boolean offlinePlayerStatus(String email) {

        try {
            Connection connection = DatabaseManager.getDBConnection();

            PreparedStatement statement = connection.prepareStatement("SELECT * FROM player WHERE email=?");
            statement.setString(1, email);
            ResultSet res = statement.executeQuery();
            if (res.next()) {
                updateStatus(email, 0);
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
    public static boolean setAllPlayersOffline() {
        try {
            Connection connection = DatabaseManager.getDBConnection();
            PreparedStatement preparedStatement = connection.prepareStatement("UPDATE player SET status = 0");
            int isUpdated = preparedStatement.executeUpdate();
            if (isUpdated > 0) {
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
