package apx.inc.design_web_services_backend.iam.application.internal.commandservices;

import apx.inc.design_web_services_backend.iam.interfaces.rest.resources.RecaptchaResponse;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Service
public class RecaptchaService {

    @Value("${recaptcha.secret-key}")
    private String recaptchaSecret;

    @Value("${recaptcha.verify-url}")
    private String recaptchaVerifyUrl;

    public boolean verifyRecaptcha(String recaptchaResponse) {
        try {
            RestTemplate restTemplate = new RestTemplate();

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

            MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
            map.add("secret", recaptchaSecret);
            map.add("response", recaptchaResponse);

            HttpEntity<MultiValueMap<String, String>> request =
                    new HttpEntity<>(map, headers);

            ResponseEntity<String> response = restTemplate.postForEntity(
                    recaptchaVerifyUrl, request, String.class);

            // Parsear la respuesta
            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(response.getBody());
            return root.path("success").asBoolean();

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}