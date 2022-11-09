package model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@AllArgsConstructor
@Getter
@NoArgsConstructor
@ToString
public class ThrottlingRule {
    private int ruleId;
    private String parameterType;
    private int apiId;
    private int burstRate;
    private int averageRate;
    private RateLimitingAlgorithm rateLimitingAlgorithm;
}
