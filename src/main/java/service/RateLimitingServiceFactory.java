package service;

import dao.TokenBucketDao;
import dao.TokenBucketDaoImpl;
import lombok.NoArgsConstructor;
import model.ThrottlingRule;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class RateLimitingServiceFactory {

    private Map<Integer, RateLimitingService> rateLimitingServiceInstances;

    public RateLimitingServiceFactory() {
        this.rateLimitingServiceInstances = new ConcurrentHashMap<Integer, RateLimitingService>();
    }

    //Could be complicated also. It may also depend on apiId.
    public RateLimitingService getRateLimiter(final ThrottlingRule throttlingRule) {
        switch(throttlingRule.getRateLimitingAlgorithm()) {
            case TOKENBUCKET -> {
                if(rateLimitingServiceInstances.containsKey(throttlingRule.getRuleId())) {
                    return rateLimitingServiceInstances.get(throttlingRule.getRuleId());
                }
                TokenBucketDao tokenBucketDao = new TokenBucketDaoImpl();
                RateLimitingService rateLimitingServiceInstance = new TokenBucketRateLimitingService(throttlingRule.getBurstRate(),
                        throttlingRule.getAverageRate(), tokenBucketDao);
                rateLimitingServiceInstances.put(throttlingRule.getRuleId(), rateLimitingServiceInstance);
                return rateLimitingServiceInstance;
            }
        }
        return new TokenBucketRateLimitingService(throttlingRule.getBurstRate(), throttlingRule.getAverageRate(), new TokenBucketDaoImpl());
    }
}
