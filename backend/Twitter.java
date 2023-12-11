package backend;


import java.util.Map;
import java.util.Set;


public interface Twitter {
    User createUser(String username);
    Group createGroup(String parentGroupID, String groupName);
    Map<String, Group> getGroups();
    Map<String, User> getUsers();
    void assignUserToGroup(User user, Group group);
    void followerUser(String follower, String following);
    void postMessage(String username, String message);
    Set<String> getNewsFeed(String username);
    int getMessageCount();
    int getPositiveMessageCount();
    Set<String> getFollowerList(String username);
    void updateFollowersLastUpdateTime(String username);

}
