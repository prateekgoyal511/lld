package dao;

import model.TokenBucket;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class TokenBucketDaoImpl implements TokenBucketDao {

    private Map<String, TokenBucket> bucketsDb;

    public TokenBucketDaoImpl() {
        this.bucketsDb = new ConcurrentHashMap<>();
    }

    @Override
    public TokenBucket getBucketByKey(String key) {
        return bucketsDb.get(key);
    }

    @Override
    public void updateBucket(String key, TokenBucket bucket) {
        bucketsDb.put(key, bucket);
    }
}
