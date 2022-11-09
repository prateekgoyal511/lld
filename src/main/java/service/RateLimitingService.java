package service;

import pojo.CheckRateLimitRequest;
import pojo.CheckRateLimitResponse;

public interface RateLimitingService {
    CheckRateLimitResponse allowRequest(CheckRateLimitRequest checkRateLimitRequest);
}
