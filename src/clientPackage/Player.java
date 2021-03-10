package clientPackage;

public class Player {
    private int id;
    private String username;
    private String password;
    private String email;
    private int status;
    private int score;

    public Player(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.score = 0;
        this.status = 0;
    }
    public Player(String username, String email, int score) {
        this.username = username;
        this.email = email;
        this.password = null;
        this.score = score;
        this.status = 0;
    }
    public Player(int id, String username, String email, int status, int score) {

        this.id = id;
        this.username = username;
        this.email = email;
        this.status = status;
        this.score = score;

    }
    public Player(String username, String email, int status, int score) {

        this.username = username;
        this.email = email;
        this.status = status;
        this.score = score;

    }
    public Player() {
    }

    public String getUserName() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getEmail() {
        return email;
    }

    public int getStatus() {
        return status;
    }

    public int getScore() {
        return score;
    }

    public int getId() {
        return id;
    }

    public void setUserName(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public void setScore(int score) {
        this.score = score;
    }
}
