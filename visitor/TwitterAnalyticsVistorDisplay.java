package visitor;



import backend.BaseTwitterAnalytics;

public class TwitterAnalyticsVistorDisplay implements TwitterAnalyticsVisitor {
    public double visitPositiveMessagesPercentage(BaseTwitterAnalytics analytics) {
        double positive = analytics.getPositiveCount();
        double denom = analytics.getNumMessageCount();
        double val = positive / denom;
        double percent = val * 100;
        System.out.println("Positive Messages Percentage: " + percent + "%");
        return percent;
    }

}
