package io.pivotal;

import org.cloudfoundry.client.CloudFoundryClient;
import org.cloudfoundry.operations.DefaultCloudFoundryOperations;
import org.cloudfoundry.reactor.ConnectionContext;
import org.cloudfoundry.reactor.DefaultConnectionContext;
import org.cloudfoundry.reactor.TokenProvider;
import org.cloudfoundry.reactor.client.ReactorCloudFoundryClient;
import org.cloudfoundry.reactor.tokenprovider.PasswordGrantTokenProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RegisterAppsConfiguration {

    @Bean
    ConnectionContext connectionContext(@Value("${CF_API}") String apiHost) {
        String hostName = apiHost.substring( apiHost.indexOf( "://" ) + 3 );
        System.out.println( "Configuring Host: " + hostName );

        return DefaultConnectionContext.builder()
                .apiHost(hostName)
                .build();
    }

    @Bean
    TokenProvider tokenProvider(@Value("${CF_USER}") String username,
                                @Value("${CF_PASSWORD}") String password) {
        return PasswordGrantTokenProvider.builder()
                .password(password)
                .username(username)
                .build();
    }

    @Bean
    CloudFoundryClient cloudFoundryClient(ConnectionContext connectionContext, TokenProvider tokenProvider) {
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
