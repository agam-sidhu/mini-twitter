package gui;
import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

import backend.Group;
import backend.User;

import java.util.Map;
import java.util.UUID;

public class CustomTreeModel extends DefaultTreeModel {
    public CustomTreeModel(Map<String, Group> groups, Map<String, User> users) {
        super(new DefaultMutableTreeNode("Root")); // Root node

        DefaultMutableTreeNode rootNode = (DefaultMutableTreeNode) getRoot();

        // Populate the tree with groups
        for (Group group : groups.values()) {
            DefaultMutableTreeNode groupNode = new DefaultMutableTreeNode(group);
            rootNode.add(groupNode);

            // Add users under the group
            for (User user : group.getUsers().values()) {
                DefaultMutableTreeNode userNode = new DefaultMutableTreeNode(user);
                groupNode.add(userNode);
            }
        }

        // Add users not in any group directly under the root
        for (User user : users.values()) {
            if (!userBelongsToAnyGroup(user, groups)) {
                DefaultMutableTreeNode userNode = new DefaultMutableTreeNode(user);
                rootNode.add(userNode);
            }
        }
    }

    private boolean userBelongsToAnyGroup(User user, Map<String, Group> groups) {
        for (Group group : groups.values()) {
            if (group.getUsers().containsKey(user)) {
                return true;
            }
        }
        return false;
    }
}
