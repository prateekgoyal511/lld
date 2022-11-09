import com.fasterxml.jackson.databind.ObjectMapper;
import model.ThrottlingRule;
import pojo.CheckRateLimitRequest;
import pojo.CheckRateLimitResponse;
import service.RateLimitingServiceFactory;
import service.RuleService;
import service.RuleServiceImpl;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class Client {
    public static void main(String[] args) throws IOException {
        System.out.println("Hello world");
        //Reading a json file
        ObjectMapper mapper = new ObjectMapper();
        ThrottlingRule throttlingRule = mapper.readValue(new File("/Users/Prateek/Desktop/All User Stuff/LLD/src/main/resources/throttlingRule.json"), ThrottlingRule.class);
        System.out.println("Read from Json file: " + throttlingRule);

        CheckRateLimitRequest checkRateLimitRequest = new CheckRateLimitRequest(1, 1, 1);

        RateLimitingServiceFactory rateLimitingServiceFactory = new RateLimitingServiceFactory();
        RuleService ruleService = new RuleServiceImpl();
        //Should be singleton.
        RateLimiterApplication rateLimiterApplication = new RateLimiterApplication(rateLimitingServiceFactory, ruleService);
//        for(int i=0; i<50; i++) {
//            CheckRateLimitResponse checkRateLimitResponse = rateLimiterApplication.checkRateLimit(checkRateLimitRequest);
//            System.out.println("Response is: " + checkRateLimitResponse);
//            try {
//                Thread.sleep(100);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//        }

        //Schedule these requests using a threadpool
        ExecutorService executorService = Executors.newFixedThreadPool(10);
        List<Future<CheckRateLimitResponse>> futures = new ArrayList<>();
        for(int j=0; j<50; j++) {
            futures.add(executorService.submit(new Callable<CheckRateLimitResponse>() {
                @Override
                public CheckRateLimitResponse call() {
                    return rateLimiterApplication.checkRateLimit(checkRateLimitRequest);
                }
            }));
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        for (Future<CheckRateLimitResponse> f : futures) {
            try {
                System.out.println(f.get());
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }
        executorService.shutdown();

    }
}
