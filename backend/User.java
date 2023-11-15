package backend;


import java.util.LinkedHashSet;
import java.util.Set;
import java.util.UUID;

public class User {
    private UUID userID;
    private String userName;
    private Set<String> following;

    public User() {
        this.userID = UUID.randomUUID();
        this.userName = "";
        this.following = new LinkedHashSet<>();
  
    }

    public User(String username) {
        this.userID = UUID.randomUUID();
        this.userName = username;
        this.following = new LinkedHashSet<>();

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
}
