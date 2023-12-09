package gui;
import javax.swing.*;

import backend.BaseTwitter;
import backend.Twitter;
import backend.User;

import java.awt.*;
import java.util.Set;

public class UserView extends BasePanel implements Observer{

    private String username;
    private long creationTime;
    private CustomConcereteObservable observables;
    private JTextArea userInfoTextArea;
    private JTextArea newsFeedTextArea;
    private JTextField newTweetTextField;
    private JButton postTweetButton;
    private JTextField followUserIdTextField;
    private JButton followUserButton;
    private DefaultListModel<String> followingsListModel;
    private JList<String> followingsList;

    private StringBuffer currentUserMessages = new StringBuffer();
    private JLabel creationTimeLabel = new JLabel("Creation Time: ");

    

    public UserView(String username, CustomConcereteObservable observables) {
        this.username = username;
        this.observables = observables;
        initializeComponents();
        buildUI();
        //updateTitle(user.getCreationTime());
    }
    public UserView(User user, CustomConcereteObservable observables) {
        this.username = user.getUsername();
        this.observables = observables;
        this.creationTime = user.getTime();
        initializeComponents();
        buildUI();
        // Your constructor logic here, using the User object
    }

    private void initializeComponents() {
        userInfoTextArea = new JTextArea();
        userInfoTextArea.setEditable(false);
        userInfoTextArea.append("User ID: " + username);

        newsFeedTextArea = new JTextArea();
        newsFeedTextArea.setEditable(false);

        newTweetTextField = new JTextField(20);

        postTweetButton = new JButton("Post Tweet");
        postTweetButton.addActionListener(e -> postTweet());

        followUserIdTextField = new JTextField(10);
        followUserButton = new JButton("Follow User");
        followUserButton.addActionListener(e -> followUser());

        // Initialize the JList for displaying current followings
        followingsListModel = new DefaultListModel<>();
        followingsList = new JList<>(followingsListModel);
    }

    private void buildUI() {
        setTitle(username + "'s User View Created Time: "+ creationTime);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());
    
        //followUserPanel.add(creationTimeLabel);
        // Follow User Panel
        JPanel followUserPanel = new JPanel(new FlowLayout());
        followUserPanel.setBorder(BorderFactory.createTitledBorder("Follow User"));
    
        followUserPanel.add(new JLabel("User ID:"));
        followUserPanel.add(followUserIdTextField);
        followUserPanel.add(followUserButton);
    
        // Current Following Panel
        JPanel currentFollowingPanel = new JPanel(new BorderLayout());
        currentFollowingPanel.setBorder(BorderFactory.createTitledBorder("Current Following"));
    
        currentFollowingPanel.add(new JScrollPane(followingsList), BorderLayout.CENTER);
    
        // New Tweet Panel
        JPanel newTweetPanel = new JPanel(new FlowLayout());
        newTweetPanel.setBorder(BorderFactory.createTitledBorder("New Tweet"));
    
        newTweetPanel.add(newTweetTextField);
        newTweetPanel.add(postTweetButton);
    
        // News Feed Panel
        JPanel newsFeedPanel = new JPanel(new BorderLayout());
        newsFeedPanel.setBorder(BorderFactory.createTitledBorder("News Feed"));
    
        JScrollPane newsFeedScrollPane = new JScrollPane(newsFeedTextArea);
        newsFeedPanel.add(newsFeedScrollPane, BorderLayout.CENTER);
    
        // Set the preferred size of the News Feed Panel based on Current Following Panel
        Dimension followingPanelSize = currentFollowingPanel.getPreferredSize();
        newsFeedPanel.setPreferredSize(new Dimension(followingPanelSize.width, followingPanelSize.height));
    
        // Panel to contain Current Following and New Tweet
        JPanel followingTweetPanel = new JPanel(new BorderLayout());
        followingTweetPanel.add(currentFollowingPanel, BorderLayout.NORTH);
        followingTweetPanel.add(newTweetPanel, BorderLayout.SOUTH);
    
        // Add Panels to the Frame
        add(followUserPanel, BorderLayout.NORTH);
        add(followingTweetPanel, BorderLayout.WEST);
        add(newsFeedPanel, BorderLayout.SOUTH);
    
        pack();
        setLocationRelativeTo(null);
    }
    
    private void postTweet() {
        String tweetText = newTweetTextField.getText()+ "\n";
        Twitter twitter = BaseTwitter.getInstance();
        twitter.postMessage(username, tweetText);
        currentUserMessages.append(tweetText);
        observables.notifyObservers(tweetText);
        
    }
    
    
    private void followUser() {
        String userIdToFollow = followUserIdTextField.getText();
        Twitter twitter = BaseTwitter.getInstance();
        twitter.followerUser(username, userIdToFollow);
        Set<String> followers = twitter.getUsers().get(username).getFollowings();
        for(String follow : followers){
            followingsListModel.addElement(follow);
            JOptionPane.showMessageDialog(this, "You are now following user: " + userIdToFollow);
        }
        followUserIdTextField.setText("");
    }
    public void update(String message) {
        newsFeedTextArea.setText("");
        Twitter twitter = BaseTwitter.getInstance();
        Set<String> msgs = twitter.getNewsFeed(username);
        StringBuffer buffer = new StringBuffer();
        buffer.append(currentUserMessages.toString());
        for(String msg : msgs) {
            buffer.append(msg);
        }
        newsFeedTextArea.setText(buffer.toString());
            
    }
    private void updateTitle(long creationTime) {
        setTitle(username + "'s User View - Created at: " + creationTime);
    }

}


