package visitor;

import backend.BaseTwitterAnalytics;

public interface TwitterAnalyticsVisitor {

    double visitPositiveMessagesPercentage(BaseTwitterAnalytics analytics);

}
