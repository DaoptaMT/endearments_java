package mt.endearments.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "async.executor")
@Data
public class AsyncProperties {
    private int corePoolSize = 2;
    private int maxPoolSize = 4;
    private int queueCapacity = 50;
}
