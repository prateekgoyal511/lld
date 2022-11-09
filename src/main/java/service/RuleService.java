package service;

import model.ThrottlingRule;

import java.util.List;

public interface RuleService {

    public List<ThrottlingRule> getRulesByClientId(int clientId);
}
