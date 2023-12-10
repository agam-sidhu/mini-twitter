package backend;


import java.util.LinkedHashSet;
import java.util.Set;
import java.util.UUID;

public class User {
    private UUID userID;
    private String userName;
    private Set<String> following;
    private long creationTime;
    private long lastUpdateTime;

    public User() {
        this.userID = UUID.randomUUID();
        this.userName = "";
        this.following = new LinkedHashSet<>();
        this.creationTime = System.currentTimeMillis();
  
    }

    public User(String username) {
        this.userID = UUID.randomUUID();
        this.userName = username;
        this.following = new LinkedHashSet<>();
        this.creationTime = System.currentTimeMillis();

    }

    public UUID getUserID() {
        return userID;
    }

    public String getUsername() {
        return userName;
    }

    public String toString() {
        return userName;
    }
    public void follow(User userToFollow) {
        following.add(userToFollow.getUsername());
    }
    public Set<String> getFollowings(){
        return following;
    }
    public long getTime(){
        return creationTime;
    }
    public long getLastUpdateTime() {
        return lastUpdateTime;
    }
    public long setLastUpdate(){
        return lastUpdateTime = System.currentTimeMillis();
    }
}
