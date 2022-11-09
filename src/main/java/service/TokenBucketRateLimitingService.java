package service;

import dao.TokenBucketDao;
import model.TokenBucket;
import pojo.CheckRateLimitRequest;
import pojo.CheckRateLimitResponse;

import java.util.concurrent.locks.ReentrantLock;

public class TokenBucketRateLimitingService implements RateLimitingService {
    private int burstRate;
    private int averageRate;
    private TokenBucketDao tokenBucketDao;
    public TokenBucketRateLimitingService(int burstRate, int averageRate, TokenBucketDao tokenBucketDao) {
        this.burstRate = burstRate;
        this.averageRate = averageRate;
        this.tokenBucketDao = tokenBucketDao;
    }

    private static String JOINER = "_";

    //We have to add a lock obtaining and releasing here. Because, if 2 threads enter here without lock
    //Then, even if there is a concurrenthashmap in tokenbucketdao, both will be able to read same state
    //Then, both will modify according to them and try to update. While, their updates may be serialized,
    //they are still not aware of each other and hence their update will be wrong.
    @Override
    public CheckRateLimitResponse allowRequest(final CheckRateLimitRequest checkRateLimitRequest) {
        final String key = buildKeyForTokenBucket(checkRateLimitRequest);
        ReentrantLock lock = new ReentrantLock();
        try {
            lock.tryLock();
            TokenBucket existingBucket = tokenBucketDao.getBucketByKey(key);
            if(existingBucket != null) {
                existingBucket.refill();
            } else {
                existingBucket = new TokenBucket(burstRate, burstRate, averageRate, System.currentTimeMillis());
            }

            if(existingBucket.getCurrentTokens() > 0) {
                existingBucket.consumeToken();
                tokenBucketDao.updateBucket(key, existingBucket);
                return new CheckRateLimitResponse(true, null);
            } else {
                tokenBucketDao.updateBucket(key, existingBucket);
                return new CheckRateLimitResponse(false, "Not allowed");
            }
        } finally {
            lock.unlock();
        }
    }

    private String buildKeyForTokenBucket(final CheckRateLimitRequest checkRateLimitRequest) {
        return String.join(JOINER, String.valueOf(checkRateLimitRequest.getClientId()),
                String.valueOf(checkRateLimitRequest.getParameterId()),
                String.valueOf(checkRateLimitRequest.getApiId()));
    }
}
