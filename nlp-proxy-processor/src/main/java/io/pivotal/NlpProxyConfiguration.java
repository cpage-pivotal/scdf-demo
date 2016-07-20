package io.pivotal;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.remoting.httpinvoker.HttpInvokerProxyFactoryBean;

@Configuration
public class NlpProxyConfiguration {

    @Value("${nlp-domain}")
    private String _domain;

    @Bean(name = "nlpServiceProxy")
    HttpInvokerProxyFactoryBean proxy()
    {
        HttpInvokerProxyFactoryBean result = new HttpInvokerProxyFactoryBean();
        result.setServiceUrl( "http://" + _domain + "/NlpService" );
        result.setServiceInterface( NlpService.class );
        return result;
    }
}
