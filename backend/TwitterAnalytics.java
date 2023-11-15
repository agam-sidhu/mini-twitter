package backend;

public interface TwitterAnalytics {
    int getTotalUsers();
    int getTotalGroups();
    int getTotalMessages();
    double getPositiveMessagesPercentage();

    
}
