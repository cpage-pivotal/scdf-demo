package io.pivotal;

import org.cloudfoundry.operations.CloudFoundryOperations;
import org.cloudfoundry.operations.applications.ApplicationSummary;
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
import reactor.core.publisher.Flux;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Properties;
import java.util.function.Consumer;
import java.util.stream.Stream;

@Component
public class RegisterApps {

    public static final String SERVER_APPLICATION_NAME = "dataflow-server";
    public static final String NLP_PROCESSOR_URI = "https://s3.amazonaws.com/scdf-apps/nlp-proxy-processor-0.0.1-SNAPSHOT.jar";
    public static final String REDIS_SINK_URI = "https://s3.amazonaws.com/scdf-apps/redis-sink-0.0.1-SNAPSHOT.jar";

    private final RestTemplate _restTemplate;
    private final CloudFoundryOperations _cloudFoundryOperations;

    @Autowired
    public RegisterApps(RestTemplate restTemplate, CloudFoundryOperations cloudFoundryOperations) {
        this._restTemplate = restTemplate;
        this._cloudFoundryOperations = cloudFoundryOperations;
    }

    @Bean
    public CommandLineRunner run() {
        return (String... args) -> {

            Stream<ApplicationSummary> appStream = _cloudFoundryOperations.applications().list().toStream();
            appStream.forEach(applicationSummary -> {
                if (applicationSummary.getName().equals(SERVER_APPLICATION_NAME))
                {
                    URI serverUri;
                    String hostname = applicationSummary.getUrls().get(0);
                    System.out.println( "Server host: " + hostname );
                    try {
                        serverUri = new URI( "https://" + hostname);

                        try {
                            registerApps(serverUri);
                        } catch (IOException e) {
                            throw new UncheckedIOException( e );
                        }
                    }
                    catch ( URISyntaxException e ) {
                        System.out.println( "Could not parse URI: " + hostname );
                    }
                }
            });
        };
    }

    private void registerApps(URI serverUri) throws URISyntaxException, IOException {
        DataFlowTemplate dataFlowTemplate = new DataFlowTemplate(serverUri, _restTemplate);

        AppRegistryOperations appRegistryOperations = dataFlowTemplate.appRegistryOperations();
        appRegistryOperations.register("nlp", ApplicationType.processor,
                NLP_PROCESSOR_URI, null, true);
        appRegistryOperations.register("redis", ApplicationType.sink,
                REDIS_SINK_URI, null, true);

        Resource resource = new ClassPathResource("appStarters.properties");
        Properties properties = PropertiesLoaderUtils.loadProperties(resource);
        appRegistryOperations.registerAll(properties, true);

        System.out.println(appRegistryOperations.list().getContent().size() + " starter apps registered");
    }
}
