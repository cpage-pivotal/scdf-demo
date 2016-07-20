package io.pivotal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.remoting.httpinvoker.HttpInvokerServiceExporter;

@Configuration
public class NlpConfiguration {

    @Autowired NlpService _nlpService;

    @Bean( name = "/NlpService" )
    HttpInvokerServiceExporter exportedService()
    {
        HttpInvokerServiceExporter result = new HttpInvokerServiceExporter();
        result.setService( _nlpService );
        result.setServiceInterface( NlpService.class );
        return result;
    }
}
