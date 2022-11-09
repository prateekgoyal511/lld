package model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class TokenBucket {
    private int capacity;
    private int currentTokens;
    private int refillRate;
    private long lastRefillTimestamp;

    //We are keeping it synchronized. Because, just in case 2 threads hold the same object, then only
    //one should be able to modify at a time. Though, we have avoided the possibility of 2 threads holding
    //the same object by putting locks before reading the object.
    public synchronized void refill() {
        long now = System.currentTimeMillis();
        int tokensToAdd  = (int) ((now-lastRefillTimestamp)*refillRate/1e3);
        System.out.println("Tokens to add: " + tokensToAdd + " current thread: " + Thread.currentThread().getName());
        System.out.print("before refilled : " + currentTokens+ "  ");
        currentTokens = Math.min(currentTokens+tokensToAdd, capacity);
        System.out.print("After refilled : " + currentTokens);
        if(tokensToAdd > 0) lastRefillTimestamp = now;
    }

    public synchronized void consumeToken() {
        currentTokens = currentTokens - 1;
    }
}
