package backend;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class Group {
    private UUID groupID;
    private String groupName;
    private Map<String, User> users;
    private Map<String, Group> subgroups;
    private long creationTime;

    // Constructor
    public Group(String name) {
        this.groupID = UUID.randomUUID();
        this.groupName = name;
        this.creationTime = System.currentTimeMillis();
        this.users = new HashMap<>();
        this.subgroups = new HashMap<>();
    }

    public UUID getGroupID() {
        return groupID;
    }

    public void setGroupID(UUID groupID) {
        this.groupID = groupID;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public Map<String, User> getUsers() {
        return users;
    }

    public Map<String, Group> getSubgroups() {
        return subgroups;
    }

    public void addUser(User user) {
        users.put(user.getUsername(), user);
    }

    public void addSubgroup(Group subgroup) {
        subgroups.put(subgroup.getGroupName(), subgroup);
    }

    public String toString() {
        return groupName;
    }

    public long getGroupTime(){
        return creationTime;
    }

}
