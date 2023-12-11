package backend;


import java.util.LinkedHashSet;
import java.util.Set;
import java.util.UUID;

public class User {
    private UUID userID;
    private String userName;
    private Set<String> following;
    private Set<String> followers;
    private long creationTime;
    public long lastUpdateTime;

    public User() {
        this.userID = UUID.randomUUID();
        this.userName = "";
        this.following = new LinkedHashSet<>();
        this.followers = new LinkedHashSet<>();
        this.creationTime = System.currentTimeMillis();
    }

    public User(String username) {
        this.userID = UUID.randomUUID();
        this.userName = username;
        this.following = new LinkedHashSet<>();
        this.followers = new LinkedHashSet<>();
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
        //userToFollow.addFollower(this.getUsername());
        
        userToFollow.setLastUpdateTime(this.getLastUpdateTime());
    }

    public void following(User userFollowing){
        followers.add(userFollowing.getUsername());
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
    public long setLastUpdateTime(long updateTime){
        return lastUpdateTime = updateTime;
    }

    public Set<String> getFollowers() {
        return followers;
    }

    public void addFollower(String followerId) {
        followers.add(followerId);
    }
    public void addFollowing(String userIdToFollow) {
        following.add(userIdToFollow);
    }
    public boolean hasFollowers(){
        return !followers.isEmpty(); 
    }

    public boolean hasFollowings(){
        return !following.isEmpty();
    }

}
