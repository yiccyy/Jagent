package jc.chuan.jagent.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author cy
 * @date 2026/5/12
 */
@Data
@Component
@ConfigurationProperties(prefix = "app")
public class AppProperties {

    private String apiKey;
    private String modelName;

}
