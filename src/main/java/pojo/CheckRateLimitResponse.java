package pojo;

import lombok.AllArgsConstructor;
import lombok.ToString;

@AllArgsConstructor
@ToString
public class CheckRateLimitResponse {
    private boolean isAllowed;
    private String message;
}
