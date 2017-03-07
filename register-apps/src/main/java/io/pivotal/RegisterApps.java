package io.pivotal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.cloud.dataflow.core.ApplicationType;
import org.springframework.cloud.dataflow.rest.client.AppRegistryOperations;
import org.springframework.cloud.dataflow.rest.client.DataFlowTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PropertiesLoaderUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.Properties;

@Component
public class RegisterApps {

    @Autowired
    private RestTemplate _restTemplate;

    @Bean
    public CommandLineRunner run() {
        return args -> {

            DataFlowTemplate dataFlowTemplate = new DataFlowTemplate(
                    new URI("https://scdf-demo15.cfapps.io/"), _restTemplate);

            AppRegistryOperations appRegistryOperations = dataFlowTemplate.appRegistryOperations();
            appRegistryOperations.register( "nlp", ApplicationType.processor,
                    "https://s3.amazonaws.com/scdf-apps/nlp-proxy-processor-0.0.1-SNAPSHOT.jar", true );
            appRegistryOperations.register( "redis", ApplicationType.sink,
                    "https://s3.amazonaws.com/scdf-apps/redis-sink-0.0.1-SNAPSHOT.jar", true );

            Resource resource = new ClassPathResource( "appStarters.properties" );
            Properties properties = PropertiesLoaderUtils.loadProperties(resource);
            appRegistryOperations.registerAll( properties, true );

            System.out.println( appRegistryOperations.list().getContent().size() + " apps registered" );
        };
    }
}
