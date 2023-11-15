package backend;

import java.util.Map;
import java.util.UUID;

import visitor.TwitterAnalyticsVisitor;

public class BaseTwitterAnalytics implements TwitterAnalytics {
    private Map<UUID, User> users;
    private Map<UUID, Group> groups;

    private BaseTwitterAnalytics(){  
    }

    public double accept(TwitterAnalyticsVisitor visitor) {
        return visitor.visitPositiveMessagesPercentage(this);

    }
    
    private static BaseTwitterAnalytics instance;
    
    public static synchronized BaseTwitterAnalytics getInstance(){
        if(instance == null){
            instance = new BaseTwitterAnalytics();
        }
        return instance;
    }

    public int getTotalUsers() {
        return users.size();
    }

    public int getTotalGroups() {
        return groups.size();
    }

    public int getTotalMessages() {
        Twitter twitter = BaseTwitter.getInstance();
        return twitter.getMessageCount();
    }

    public int getPositiveCount(){
        Twitter twitter = BaseTwitter.getInstance();
        int num = twitter.getPositiveMessageCount();
        return num;
    }
    public int getNumMessageCount(){
        Twitter twitter = BaseTwitter.getInstance();
        int num = twitter.getMessageCount();
        return num;
    }
    public double getPositiveMessagesPercentage() {
        Twitter twitter = BaseTwitter.getInstance();
        int positive = twitter.getPositiveMessageCount();
        double numerator = positive;
        int total = twitter.getMessageCount();
        double denominator = total;
        double percentage = numerator/ denominator;
        double percent = percentage * 100.0;
        return percent;
    }
}

