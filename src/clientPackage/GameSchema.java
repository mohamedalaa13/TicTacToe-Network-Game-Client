package clientPackage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class GameSchema {

    private static int id = getLastID();
    private static ArrayList<Game> savedGames;

    public static int getLastID() {
        try {
            Connection connection = DatabaseManager.getDBConnection();
            PreparedStatement statement = connection.prepareStatement("SELECT id FROM game ORDER BY id DESC LIMIT 1");
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt("id");
            }
        } catch (Exception ex) {
            Logger.getLogger(GameSchema.class.getName()).log(Level.SEVERE, null, ex);
        }
        return 0;
    }
    public static int createGame(String firstPlayerEmail, String secondPlayerEmail) {
        try {
            Connection connection = DatabaseManager.getDBConnection();
            PreparedStatement statement = connection.prepareStatement("INSERT INTO game SET first_player=?, second_player=?");
            statement.setString(1, firstPlayerEmail);
            statement.setString(2, secondPlayerEmail);
            int isInserted = statement.executeUpdate();
            if (isInserted > 0) {
                id++;
                return id;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }
    public static Game getGame(int id) {
        try {
            Connection connection = DatabaseManager.getDBConnection();
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM game WHERE id=?");
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                Game game = new Game(
                        resultSet.getInt("id"),
                        resultSet.getString("first_player"),
                        resultSet.getString("second_player"),
                        resultSet.getString("winner"),
                        resultSet.getInt("status"),
                        resultSet.getString("board"));
                statement.close();
                connection.close();
                return game;
            }
        } catch (Exception ex) {
            Logger.getLogger(GameSchema.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    public static Boolean updateGameStatus(int id, int status) {
        try {
            Connection connection = DatabaseManager.getDBConnection();
            PreparedStatement preparedStatement = connection.prepareStatement("UPDATE game SET status=? WHERE id=?");
            preparedStatement.setInt(1, status);
            preparedStatement.setInt(2, id);
            int isUpdated = preparedStatement.executeUpdate();
            if (isUpdated > 0) {
                return true;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return false;
    }
    public static Boolean updateGameBoard(String board, int game_id) {
        try {
            Connection connection = DatabaseManager.getDBConnection();
            PreparedStatement preparedStatement = connection.prepareStatement("UPDATE game SET board=? WHERE id=?");
            preparedStatement.setString(1, board);
            preparedStatement.setInt(2, game_id);
            int isUpdated = preparedStatement.executeUpdate();
            if (isUpdated > 0) {
                return true;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return false;
    }
    public static Boolean setWinner(int game_id, String winner) {
        try {
            Connection connection = DatabaseManager.getDBConnection();
            PreparedStatement preparedStatement = connection.prepareStatement("UPDATE game SET winner=? WHERE id=?");
            preparedStatement.setString(1, winner);
            preparedStatement.setInt(2, game_id);
            int isUpdated = preparedStatement.executeUpdate();
            if (isUpdated > 0) {
                return true;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return false;
    }
    public static boolean setRecordByFirstPlayer(int game_id) {
        try {
            Connection connection = DatabaseManager.getDBConnection();
            PreparedStatement preparedStatement = connection.prepareStatement("UPDATE game SET recorded_by_first_player=1 WHERE id=?");
            preparedStatement.setInt(1, game_id);
            int isRecorded = preparedStatement.executeUpdate();
            if (isRecorded > 0) {
                return true;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return false;
    }
    public static boolean setRecordBySecondPlayer(int game_id) {
        try {
            Connection connection = DatabaseManager.getDBConnection();
            PreparedStatement preparedStatement = connection.prepareStatement("UPDATE game SET recorded_by_second_player=1 WHERE id=?");
            preparedStatement.setInt(1, game_id);
            int isRecorded = preparedStatement.executeUpdate();
            if (isRecorded > 0) {
                return true;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return false;
    }
    public static ArrayList<Game> getSavedGames(String email) throws SQLException {
        savedGames = new ArrayList<>();
        Connection connection = DatabaseManager.getDBConnection();
        PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM game WHERE (first_player = ? AND recorded_by_first_player = 1) OR (second_player = ? AND recorded_by_second_player = 1)");
        preparedStatement.setString(1, email);
        preparedStatement.setString(2, email);
        ResultSet resultSet = preparedStatement.executeQuery();
        while (resultSet.next()) {
            Game game = new Game(
                    resultSet.getInt("id"),
                    resultSet.getString("first_player"),
                    resultSet.getString("second_player"),
                    resultSet.getString("winner"),
                    resultSet.getString("board")
            );
            savedGames.add(game);
        }
        return savedGames;
    }
    public static boolean removeGame(int game_id) {
        try {
            Connection connection = DatabaseManager.getDBConnection();
            PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM game WHERE id=?");
            preparedStatement.setInt(1, game_id);
            int isDeleted = preparedStatement.executeUpdate();
            if (isDeleted > 0) {
                return true;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return false;
    }
}
