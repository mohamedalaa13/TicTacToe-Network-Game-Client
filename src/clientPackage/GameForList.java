package clientPackage;

import javafx.beans.property.SimpleStringProperty;
import javafx.scene.control.Button;

public class GameForList {
    private SimpleStringProperty id;
    private SimpleStringProperty firstPlayer;
    private SimpleStringProperty secondPlayer;
    private SimpleStringProperty winner;
    private SimpleStringProperty board;
    private Button button;

    public GameForList(String id, String firstPlayer, String secondPlayer, String winner, Button button){
        this.id = new SimpleStringProperty(id);
        this.firstPlayer = new SimpleStringProperty(firstPlayer);
        this.secondPlayer = new SimpleStringProperty(secondPlayer);
        this.winner = new SimpleStringProperty(winner);
        this.board = null;
        this.button = button;
    }

    public String getId(){
        return id.get();
    }

    public String getFirstPlayer(){ return firstPlayer.get(); }

    public String getSecondPlayer(){
        return secondPlayer.get();
    }

    public String getWinner(){
        return winner.get();
    }

    public String getBoard(){
        return board.get();
    }

    public Button getButton(){
        return button;
    }

    public void setButton(Button button) {
        this.button = button;
    }

    public void setFirstPlayer(String first_player)
    {
        this.firstPlayer.set(first_player);
    }

    public void setSecondPlayer(String second_player)
    {
        this.secondPlayer.set(second_player);
    }

    public void setWinner(String winner)
    {
        this.winner.set(winner);
    }

    public void setBoard(String board){
        this.board.set(board);
    }
}
