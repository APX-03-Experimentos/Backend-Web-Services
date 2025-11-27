package apx.inc.design_web_services_backend.iam.interfaces.rest.resources;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RecaptchaResponse {
    private boolean success;
    private String challengeTs;
    private String hostname;
    private Double score;
    private String action;

    @JsonProperty("error-codes")
    private String[] errorCodes;


}