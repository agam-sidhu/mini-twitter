package gui;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

import backend.BaseTwitter;
import backend.BaseTwitterAnalytics;

import backend.Group;
import backend.TwitterAnalytics;

import backend.User;
import visitor.TwitterAnalyticsVisitor;
import visitor.TwitterAnalyticsVistorDisplay;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class AdminPanel extends BasePanel{

    private List<String> existingUsernames = new ArrayList<>();
    private CustomConcereteObservable observables = new CustomConcereteObservable();
    private TwitterAnalyticsVisitor analyticsVisitor = new TwitterAnalyticsVistorDisplay();

    private DefaultMutableTreeNode root;
    private JTree tree;
    private JTextArea userInfoTextArea;
    private JTextField userIdTextField;
    private JTextField groupIdTextField;
    private JButton addUserButton;
    private JButton addGroupButton;
    private JButton openUserViewButton;
    private JButton showUserTotalButton;
    private JButton showGroupTotalButton;
    private JButton showMessageTotalButton;
    private JButton showPositivePercentageButton;
    private JButton user_groupVerficationButton;
    private JButton lastUpdatedUserButton;
    private List<UserView> userViews = new ArrayList<>();

    public AdminPanel(int treeWidth) {
        initializeComponents();
        buildUI(treeWidth);
    }

    private void initializeComponents() {
        root = new DefaultMutableTreeNode("Root");
        tree = new JTree(root);
        userInfoTextArea = new JTextArea();
        userIdTextField = new JTextField();
        groupIdTextField = new JTextField();
        addUserButton = new JButton("Add User");
        addGroupButton = new JButton("Add Group");
        openUserViewButton = new JButton("Open User View");
        showUserTotalButton = new JButton("Show User Total");
        showGroupTotalButton = new JButton("Show Group Total");
        showMessageTotalButton = new JButton("Show Message Total");
        showPositivePercentageButton = new JButton("Show Positive Percentage");
        user_groupVerficationButton = new JButton("Show all id verification");
        lastUpdatedUserButton = new JButton("Show Last Updated User");

        // Add action listeners to buttons
        addUserButton.addActionListener(e -> addUser());
        addGroupButton.addActionListener(e -> addGroup());
        openUserViewButton.addActionListener(e -> openUserView());
        showUserTotalButton.addActionListener(e -> showUserTotal());
        showGroupTotalButton.addActionListener(e -> showGroupTotal());
        showMessageTotalButton.addActionListener(e -> showMessageTotal());
        showPositivePercentageButton.addActionListener(e -> showPositivePercentage());
        user_groupVerficationButton.addActionListener(e -> checkVerification());
        lastUpdatedUserButton.addActionListener(e -> printLastUser());

    }
    private void addUser() {
        String username = userIdTextField.getText();
        DefaultMutableTreeNode selectedNode = getSelectedNode();
        BaseTwitter.getInstance().createUser(username);
        if (username.isEmpty() || selectedNode == null) {
            showError("Please select a group and enter a username.");
            return;
        }
    
        // Check if the username is already taken
        if (existingUsernames.contains(username)) {

            showError("Username is already taken. Please choose a different username.");
            return;
        }
    
        // Create a new User instance with the given username
        User newUser = new User(username);
    
        // Add the user to the selected group (assuming it's a group node)
        DefaultMutableTreeNode userNode = new DefaultMutableTreeNode(newUser);
        selectedNode.add(userNode);
        ((DefaultTreeModel) tree.getModel()).reload(selectedNode);
    
        // Update the list of existing usernames
        existingUsernames.add(username);
    }

    public List<String> getUsernameList(){
        return existingUsernames;
    }
    
    private void addGroup() {
        String groupName = groupIdTextField.getText(); // Assuming the input field is for the group name
        DefaultMutableTreeNode selectedNode = getSelectedNode();
        
        try{
            Group newGroup = null;
            if(selectedNode.getUserObject() instanceof Group){
                Group group = (Group) selectedNode.getUserObject();
                newGroup = BaseTwitter.getInstance().createGroup(group.getGroupName(), groupName);
                DefaultMutableTreeNode groupNode = new DefaultMutableTreeNode(newGroup);
                selectedNode.add(groupNode); 
            }else{
                newGroup = BaseTwitter.getInstance().createGroup((String) selectedNode.getUserObject(), groupName);
                DefaultMutableTreeNode groupNode = new DefaultMutableTreeNode(newGroup);
                selectedNode.add(groupNode); 
            }

            ((DefaultTreeModel) tree.getModel()).reload(selectedNode);
            for(String key: newGroup.getSubgroups().keySet()){
                Map<String, Group> subgroups = (Map<String, Group>) newGroup.getSubgroups().get(key);
                for(String key2 : subgroups.keySet()){
                    Group subgroup = subgroups.get(key2);
                    DefaultMutableTreeNode subGroupNode = new DefaultMutableTreeNode(subgroup);
                    selectedNode.add(subGroupNode); 
                } 
                ((DefaultTreeModel) tree.getModel()).reload(selectedNode);
            }
        }catch(IllegalArgumentException e){
            showError(e.getMessage());
        }

    }

    private void showUserTotal() {
        int userTotal = countUsers(root);
        showMessage("Total Users: " + userTotal);
    }

    private void showGroupTotal() {
        int groupTotal = countGroups(root);
        showMessage("Total Groups: " + groupTotal);
    }

    private void showMessageTotal() {
        int messageTotal = countMessages();
        showMessage("Total Messages: " + messageTotal);
    }

    private void showPositivePercentage() {
        BaseTwitterAnalytics analytics = BaseTwitterAnalytics.getInstance();
        double number = analytics.accept(analyticsVisitor);
        if(Double.isNaN(number)){
            showMessage("There is no message displayed so no number can be calculated");
        }else{
            showMessage("Total Positive Percent: " + String.valueOf(number) + "%"); 
        }
    }

    private void checkVerification(){
        showMessage("This is a test");
    }

    private void printLastUser(){
        BaseTwitter baseTwitter = BaseTwitter.getInstance();
        User latestUpdateUser = baseTwitter.findLatestUpdateUser();

        if (latestUpdateUser != null) {
            long lastUpdateTime = latestUpdateUser.getLastUpdateTime();
            String username = latestUpdateUser.getUsername();
            showMessage("Latest Updated User: " + username + " (Last Update Time: " + lastUpdateTime + ")");
        } else {
            showMessage("No users found.");
        }
    }

    private DefaultMutableTreeNode getSelectedNode() {
        return (DefaultMutableTreeNode) tree.getLastSelectedPathComponent();
    }
    private void openUserView() {
        DefaultMutableTreeNode selectedNode = getSelectedNode();
    
        if (selectedNode == null) {
            showError("Please select a user from the tree view.");
            return;
        }
    
        String username;
    
        if (selectedNode.getUserObject() instanceof User) {
            // If a user is selected in the tree view, use that username
            User selectedUser = (User) selectedNode.getUserObject();
            username = selectedUser.getUsername();
            System.out.println("Selected User from tree view: " + username);
            displayUserInfo(selectedUser);

        } else {
            // If no user is selected or the selected node is not a user, show an error
            showError("Invalid selection. Please select a user from the tree view.");
            return;
        }
    
        System.out.println("Opening UserView for username: " + username);
       
    }
    
    
    private void displayUserInfo(User user) {
        System.out.println("Opening UserView for username: " + user.getUsername());
        System.out.println("Printing creation time" + user.getTime());
        // Open UserView for the selected user
        UserView userView = new UserView(user, observables);
        observables.addObserver(userView);
        userView.setVisible(true);

    }
    public List<UserView> getUserViewsList() {
        return userViews;
    }
    

    private int countUsers(DefaultMutableTreeNode node) {
        int userCount = 0;
        for (int i = 0; i < node.getChildCount(); i++) {
            DefaultMutableTreeNode childNode = (DefaultMutableTreeNode) node.getChildAt(i);
            if (childNode.isLeaf()) {
                userCount++;
            } else {
                userCount += countUsers(childNode);
            }
        }
        return userCount;
    }

    private int countGroups(DefaultMutableTreeNode node) {
        int groupCount = 0;
        for (int i = 0; i < node.getChildCount(); i++) {
            DefaultMutableTreeNode childNode = (DefaultMutableTreeNode) node.getChildAt(i);
            if (!childNode.isLeaf()) {
                groupCount++;
                groupCount += countGroups(childNode);
            }
        }
        return groupCount;
    }

    private int countMessages() { 
        TwitterAnalytics twitter = BaseTwitterAnalytics.getInstance();
        return twitter.getTotalMessages();
    }

    private void buildUI(int treeWidth) {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Left Panel with Tree View
        JPanel leftPanel = new JPanel(new BorderLayout());
        leftPanel.add(new JScrollPane(tree), BorderLayout.CENTER);
        leftPanel.setPreferredSize(new Dimension(treeWidth, 0));

        // Center Panel with User Info TextArea and Buttons
        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.add(new JScrollPane(userInfoTextArea), BorderLayout.CENTER);

        // ID TextFields Panel
        JPanel idPanel = new JPanel(new GridLayout(2, 3));
        idPanel.add(new JLabel("User ID:"));
        idPanel.add(userIdTextField);
        idPanel.add(addUserButton);
        idPanel.add(new JLabel("Group ID:"));
        idPanel.add(groupIdTextField);
        idPanel.add(addGroupButton);

        // Open User View Button Panel
        JPanel openUserViewPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        openUserViewButton.setMaximumSize(new Dimension(Integer.MAX_VALUE, openUserViewButton.getPreferredSize().height));
        openUserViewPanel.add(openUserViewButton, gbc);

        // Statistics Buttons Panel
        JPanel statisticsButtonsPanel = new JPanel(new GridLayout(3, 2)); // Two rows, two columns
        statisticsButtonsPanel.add(showUserTotalButton);
        statisticsButtonsPanel.add(showGroupTotalButton);
        statisticsButtonsPanel.add(showMessageTotalButton);
        statisticsButtonsPanel.add(showPositivePercentageButton);
        statisticsButtonsPanel.add(user_groupVerficationButton);
        statisticsButtonsPanel.add(lastUpdatedUserButton);

        // Add Panels to the Frame
        centerPanel.add(idPanel, BorderLayout.NORTH);
        centerPanel.add(openUserViewPanel, BorderLayout.CENTER);
        centerPanel.add(statisticsButtonsPanel, BorderLayout.SOUTH);

        add(leftPanel, BorderLayout.WEST);
        add(centerPanel, BorderLayout.CENTER);

        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }

    private void showMessage(String message) {
        JOptionPane.showMessageDialog(this, message);
    }
    
}