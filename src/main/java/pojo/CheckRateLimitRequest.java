package pojo;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class CheckRateLimitRequest {
    private int clientId;
    private int apiId;
    private int parameterId;
}
