package dao;

import model.TokenBucket;

public interface TokenBucketDao {
    public TokenBucket getBucketByKey(String key);

    void updateBucket(String key, TokenBucket existingBucket);
}
