package service;

import model.RateLimitingAlgorithm;
import model.ThrottlingRule;

import java.util.List;

public class RuleServiceImpl implements RuleService{
    @Override
    public List<ThrottlingRule> getRulesByClientId(int clientId) {
        return List.of(new ThrottlingRule(1, "userId", 1, 10, 5, RateLimitingAlgorithm.TOKENBUCKET));
    }
}
