package io.anymobi.common;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "app-security")
@Getter
@Setter
public class AppSecurityProperties {

    private String defaultClientId;

    private String defaultClientSecret;

    private String adminUsername;

    private String adminPassword;

    private String userUsername;

    private String userPassword;

}
