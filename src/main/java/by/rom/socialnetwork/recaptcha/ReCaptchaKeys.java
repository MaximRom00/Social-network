package by.rom.socialnetwork.recaptcha;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "google.recaptcha.key")
@Data
public class ReCaptchaKeys {

    private String site;
    private String secret;
}
