package clientPackage;

public class Game {

    private int id;
    private String first_player;
    private String second_player;
    private String winner;
    private int status;
    private String board;

    public Game(){}

    public Game(String first_player, String second_player){
        this.first_player = first_player;
        this.second_player = second_player;
        this.winner = null;
        this.status = 0; // 0 for request
        this.board = null;
    }

    public Game(int id, String first_player, String second_player, String winner, String board){
        this.id = id;
        this.first_player = first_player;
        this.second_player = second_player;
        this.winner = winner;
        this.board = board;
    }

    public Game(int id, String first_player, String second_player, String winner, int status, String board)
    {
        this.id = id;
        this.first_player = first_player;
        this.second_player = second_player;
        this.winner = winner;
        this.status = status;
        this.board = board;
    }
    public Game(String first_player, String second_player, String winner, int status, String board)
    {
        this.first_player = first_player;
        this.second_player = second_player;
        this.winner = winner;
        this.status = status;
        this.board = board;
    }

    public int getId(){
        return id;
    }

    public String getFirst_player(){
        return first_player;
    }

    public String getSecond_player(){
        return second_player;
    }

    public int getStatus(){
        return status;
    }

    public String getWinner(){
        return winner;
    }

    public String getBoard(){
        return board;
    }

    public void setFirst_player(String first_player)
    {
        this.first_player = first_player;
    }

    public void setSecond_player(String second_player)
    {
        this.second_player = second_player;
    }

    public void setWinner(String winner)
    {
        this.winner = winner;
    }

    public void setStatus(int status)
    {
        this.status = status;
    }

    public void setBoard(String board){
        this.board = board;
    }
}
