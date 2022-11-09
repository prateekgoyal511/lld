import lombok.NoArgsConstructor;
import model.ThrottlingRule;
import pojo.CheckRateLimitRequest;
import pojo.CheckRateLimitResponse;
import service.RateLimitingService;
import service.RateLimitingServiceFactory;
import service.RuleService;

import java.util.List;

@NoArgsConstructor
public class RateLimiterApplication {

    //Should be singleton.
    private RateLimitingServiceFactory rateLimitingServiceFactory;

    private RuleService ruleService;

    public RateLimiterApplication(final RateLimitingServiceFactory rateLimitingServiceFactory,
                                  final RuleService ruleService) {
        this.rateLimitingServiceFactory = rateLimitingServiceFactory;
        this.ruleService = ruleService;
    }

    public CheckRateLimitResponse checkRateLimit(final CheckRateLimitRequest checkRateLimitRequest) {
        List<ThrottlingRule> clientRules = ruleService.getRulesByClientId(checkRateLimitRequest.getClientId());
        CheckRateLimitResponse checkRateLimitResponse = null;

        //Get applicable client rules by passing list of rule and checkRateLimitRequest inside another class that returns filtered List<Rule>
        for(ThrottlingRule throttlingRule: clientRules) {
            RateLimitingService rateLimitingService = rateLimitingServiceFactory.getRateLimiter(throttlingRule);
            checkRateLimitResponse = rateLimitingService.allowRequest(checkRateLimitRequest);
        }
        return checkRateLimitResponse;
    }
}
