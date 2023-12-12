// In BaseTwitter class
package backend;


import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import javax.swing.SwingUtilities;

public class BaseTwitter implements Twitter {
    private Map<String, User> users;
    private Map<String, Group> groups;
    private Map<String, Set<String>> messages;
    private Map<String, String> memberships;
    private Map<String, User> followerList;
    private String[] postiveWords = {"good", "great", "excellent"};

    // Constructor
    private BaseTwitter() {
        this.users = new HashMap<>();
        this.groups = new HashMap<>();
        this.messages = new HashMap<>();
        this.memberships = new HashMap<>();
        this.followerList = new HashMap<>();
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
        //if(users.containsKey(username)) throw new IllegalArgumentException("User already exists");
        User user = new User(username);
        users.put(user.getUsername(), user);
        memberships.put(username, groupname);
        return user;
    }
    
    public Group createGroup(String parentGroupName, String groupName) {
        // Create a new group
        //if(groups.containsKey(groupName)) throw new IllegalArgumentException("Group already exists");
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
        /* 
        //This will print the new follower for whomever so if I have a follow b it will print a
        User followerUser = users.get(follower);

        //This will print the new following for whomever so if I have a follow b it will print b
        User followingUser = users.get(following);

    
        if (followerUser != null && followingUser != null) {
            followerUser.follow(followingUser);
            // Update the follower list for the user being followed
            followingUser.addFollower(follower);
            //this is in format of a is now following b
            followingUser.addFollowing(following);
*/
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
            user.setLastUpdate();
            long userLastUpdateTime = user.getLastUpdateTime();
            /* 
            if(isFollowing(username)){
                user.getLastUpdateTime();
                Set<String> followings = user.getFollowings();
                for(String follow : followings){
                    User followingUser = users.get(follow);
                    if (followingUser != null) {
                        long followingUserLastUpdateTime = followingUser.getLastUpdateTime();
                        System.out.println(username + " is following " + follow);
                        System.out.println("User's last update time: " + userLastUpdateTime);
                        System.out.println(follow + "'s last update time: " + followingUserLastUpdateTime);

                        // Compare update times
                        if (userLastUpdateTime < followingUserLastUpdateTime) {
                            user.setLastUpdateTime(followingUserLastUpdateTime);
                            System.out.println(username + " should update their feed!");
                            // Add logic here to handle the case where the user should update their feed
                        } else {
                            System.out.println(username + " doesn't need to update their feed.");
                        }
                }
                */

                //}

            }
            /* 
            if (user.hasFollowers()) {
            // If the user has followers, update the last update time for the user and their followers
                long userLastUpdateTime = user.getLastUpdateTime();

                Set<String> followers = user.getFollowers();
                for (String follower : followers) {
                    User followerUser = users.get(follower);
                    if (followerUser != null) {
                        // Update the last update time for each follower
                        followerUser.setLastUpdateTime(userLastUpdateTime);
                        System.out.println("This is new update time for user " + followerUser + " " + userLastUpdateTime);
                    }
                }

            } 
            */
        //}
    }

    public Set<String> getNewsFeed(String username) {
        Set<String> answer = new LinkedHashSet();
        User currentUser = users.get(username);
        
        for (String followingUser : currentUser.getFollowings()) {
            Set<String> followingUserMessages = messages.get(followingUser);
            if (followingUserMessages != null) {
                answer.addAll(followingUserMessages);
            }
            //answer.addAll(followingUserMessages);
        }
        return answer;
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
    public Set<String> getFollowerList(String username) {
        User user = users.get(username);
        if (user != null) {
            return user.getFollowers();
        } else {
            System.out.println("User not found: " + username);
            return null;
        }
    }
    public boolean hasFollowers(String username){
        User user = users.get(username);
        Set<String> followers = user.getFollowers();
        if(followers == null){
            return false;
        }
        return true;
    }

    public boolean isFollowing(String follower) {
        User followerUser = users.get(follower);
    
        if (followerUser != null) {
            return !followerUser.getFollowings().isEmpty();
        }
    
        return false;
    }

    public void updateFollowersLastUpdateTime(String username) {
        User user = users.get(username);
        
        if (hasFollowers(username)) {
            long userLastUpdateTime = user.getLastUpdateTime();
    
            Set<String> followers = user.getFollowers();
            for (String follower : followers) {
                User followerUser = users.get(follower);
                if (followerUser != null) {
                    // Update the last update time for each follower
                    followerUser.setLastUpdateTime(userLastUpdateTime);
                    System.out.println("This is new updatetime for user"+ followerUser+ " "+ userLastUpdateTime);
                }
            }
        } else {
            System.out.println("User not found: " + username);
        }
    }
    

}
