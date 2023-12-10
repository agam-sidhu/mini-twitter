// In BaseTwitter class
package backend;


import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public class BaseTwitter implements Twitter {
    private Map<String, User> users;
    private Map<String, Group> groups;
    private Map<String, Set<String>> messages;
    private Map<String, String> memberships;
    private String[] postiveWords = {"good", "great", "excellent"};

    // Constructor
    private BaseTwitter() {
        this.users = new HashMap<>();
        this.groups = new HashMap<>();
        this.messages = new HashMap<>();
        this.memberships = new HashMap<>();
        this.groups.put("Root", new Group("Root"));
    }

    private static BaseTwitter instance;

    public static synchronized BaseTwitter getInstance(){
        if(instance == null){
            instance = new BaseTwitter();
        }
        return instance;
    }
    public User createUser(String username) {
        return createUserInGroup(username, "Root");
    }

    public User createUserInGroup(String username, String groupname) {
        if(users.containsKey(username)) throw new IllegalArgumentException("User already exists");
        User user = new User(username);
        users.put(user.getUsername(), user);
        memberships.put(username, groupname);
        return user;
    }
    
    public Group createGroup(String parentGroupName, String groupName) {
        // Create a new group
        if(groups.containsKey(groupName)) throw new IllegalArgumentException("Group already exists");
        if(groupName == "") throw new IllegalArgumentException("Please provide a group name");
 
        Group group = new Group(groupName);

        // Add the group to the BaseTwitter groups map
        groups.put(groupName, group);

        Group parentGroup = groups.get("Root");
        if(groups.containsKey(parentGroupName)){
            parentGroup = groups.get(parentGroupName);
        }

        parentGroup.addSubgroup(group);
        return group;
    }

    public Map<String, Group> getGroups() {
        return groups;
    }

    public Map<String, User> getUsers() {
        return users;
    }

    public void assignUserToGroup(User user, Group group) {
        if (user != null && group != null) {
            group.addUser(user);
            group.setGroupID(UUID.randomUUID()); // Set a new ID for the group
            memberships.put(user.getUsername(), group.getGroupName());
        }
    }

    public void followerUser(String follower, String following) {
        if (follower != null && following != null) {
            users.get(follower).follow(users.get(following));
        }
    }

    public void postMessage(String username, String message) {
        if (username != null) {
            Set<String> msgs = messages.get(username);
            if (msgs == null) {
                msgs = new LinkedHashSet<>();
            }
            msgs.add(message);
            messages.put(username, msgs);
            User user = users.get(username);
    
            if (user != null) {
                user.setLastUpdate();
                System.out.println("Last update time for user " + username + ": " + user.getLastUpdateTime());
    
                // Update lastUpdate for followers
                Set<String> followers = user.getFollowings();
                for (String follower : followers) {
                    User followerUser = users.get(follower);
                    if (followerUser != null) {
                        followerUser.setLastUpdate();
                        System.out.println("Last update time for follower " + follower + ": " + followerUser.getLastUpdateTime());
                    }
                }
            } else {
                System.out.println("User not found: " + username);
            }
        }
    }

    public Set<String> getNewsFeed(String username) {
        Set<String> newsFeed = new LinkedHashSet<>();
        User user = users.get(username);

        if (user != null) {
            Set<String> currentUserMessages = messages.get(username);
            if (currentUserMessages != null) {
                newsFeed.addAll(currentUserMessages);
            }

            Set<String> followings = user.getFollowings();
            for (String following : followings) {
                Set<String> followingMessages = messages.get(following);
                if (followingMessages != null) {
                    newsFeed.addAll(followingMessages);
                }
            }
        }

    return newsFeed;
}

    public int getMessageCount(){
        Set<String> answer = new LinkedHashSet();
        for(String key : messages.keySet()) {
            Set<String> followingUserMessages = messages.get(key);    
            answer.addAll(followingUserMessages);

        }
        return answer.size();
    }

    public int getPositiveMessageCount(){

        int i=0;
        for(String username : messages.keySet()) {
            Set<String> followingUserMessages = messages.get(username);
            for(String message : followingUserMessages){
                    for(String word : postiveWords){
                        if(message.contains(word)){
                            //answer.add(message);
                            i++;
                        }
                    }

            }    
        }
        return i;
    }
    public User findLatestUpdateUser() {
        User latestUpdateUser = null;
        long latestUpdateTime = Long.MIN_VALUE;


        for (User user : users.values()) {
            if (user.getLastUpdateTime() > latestUpdateTime) {
                latestUpdateTime = user.getLastUpdateTime();
                latestUpdateUser = user;
                System.out.println("This is the latestUpodate: " + latestUpdateUser);
            }
        }

        return latestUpdateUser;
        }

}
