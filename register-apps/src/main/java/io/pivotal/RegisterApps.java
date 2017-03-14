package io.pivotal;

import org.cloudfoundry.client.CloudFoundryClient;
import org.cloudfoundry.client.v3.applications.Application;
import org.cloudfoundry.doppler.DopplerClient;
import org.cloudfoundry.operations.CloudFoundryOperations;
import org.cloudfoundry.operations.DefaultCloudFoundryOperations;
import org.cloudfoundry.operations.applications.ApplicationSummary;
import org.cloudfoundry.reactor.ConnectionContext;
import org.cloudfoundry.reactor.DefaultConnectionContext;
import org.cloudfoundry.reactor.TokenProvider;
import org.cloudfoundry.reactor.client.ReactorCloudFoundryClient;
import org.cloudfoundry.reactor.tokenprovider.PasswordGrantTokenProvider;
import org.cloudfoundry.uaa.UaaClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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

    @Autowired
    private CloudFoundryOperations _cloudFoundryOperations;

    @Bean
    public CommandLineRunner run() {
        return args -> {

//            String api = System.getenv("CF_API");
//            String user = System.getenv("CF_USER");
//            String password = System.getenv("CF_PASSWORD");
//            String org = System.getenv("CF_ORG");
//            String space = System.getenv("CF_SPACE");

            _cloudFoundryOperations.applications().list().map(ApplicationSummary::getName).subscribe(System.out::println);

            DataFlowTemplate dataFlowTemplate = new DataFlowTemplate(
                    new URI("https://dataflow-server-lythraceous-chrysalid.cfapps.io/"), _restTemplate);

            AppRegistryOperations appRegistryOperations = dataFlowTemplate.appRegistryOperations();
            appRegistryOperations.register("nlp", ApplicationType.processor,
                    "https://s3.amazonaws.com/scdf-apps/nlp-proxy-processor-0.0.1-SNAPSHOT.jar", true);
            appRegistryOperations.register("redis", ApplicationType.sink,
                    "https://s3.amazonaws.com/scdf-apps/redis-sink-0.0.1-SNAPSHOT.jar", true);

            Resource resource = new ClassPathResource("appStarters.properties");
            Properties properties = PropertiesLoaderUtils.loadProperties(resource);
            appRegistryOperations.registerAll(properties, true);

            System.out.println(appRegistryOperations.list().getContent().size() + " apps registered");
        };
    }

    @Bean
    DefaultConnectionContext connectionContext(@Value("${CF_API}") String apiHost) {
        return DefaultConnectionContext.builder()
                .apiHost(apiHost)
                .build();
    }

    @Bean
    PasswordGrantTokenProvider tokenProvider(@Value("${CF_USER}") String username,
                                             @Value("${CF_PASSWORD}") String password) {
        return PasswordGrantTokenProvider.builder()
                .password(password)
                .username(username)
                .build();
    }

    @Bean
    ReactorCloudFoundryClient cloudFoundryClient(ConnectionContext connectionContext, TokenProvider tokenProvider) {
        return ReactorCloudFoundryClient.builder()
                .connectionContext(connectionContext)
                .tokenProvider(tokenProvider)
                .build();
    }

    @Bean
    DefaultCloudFoundryOperations cloudFoundryOperations(CloudFoundryClient cloudFoundryClient,
                                                         @Value("${CF_ORG}") String organization,
                                                         @Value("${CF_SPACE}") String space) {
        return DefaultCloudFoundryOperations.builder()
                .cloudFoundryClient(cloudFoundryClient)
                .organization(organization)
                .space(space)
                .build();
    }
}
