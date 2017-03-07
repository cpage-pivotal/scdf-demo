package io.pivotal;

import org.springframework.batch.core.*;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.dataflow.rest.client.VndErrorResponseErrorHandler;
import org.springframework.cloud.dataflow.rest.client.support.*;
import org.springframework.cloud.dataflow.rest.job.StepExecutionHistory;
import org.springframework.context.annotation.Bean;
import org.springframework.hateoas.hal.Jackson2HalModule;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
public class RegisterAppsApplication {

    public static void main(String[] args) {
        SpringApplication.run(RegisterAppsApplication.class, args);
    }

    @Bean
    public static RestTemplate restTemplate() {
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.setErrorHandler(new VndErrorResponseErrorHandler(restTemplate.getMessageConverters()));

        for (HttpMessageConverter<?> converter : restTemplate.getMessageConverters()) {
            if (converter instanceof MappingJackson2HttpMessageConverter) {
                final MappingJackson2HttpMessageConverter jacksonConverter =
                        (MappingJackson2HttpMessageConverter) converter;

                jacksonConverter.getObjectMapper()
                        .registerModule(new Jackson2HalModule())
                        .addMixIn(JobExecution.class, JobExecutionJacksonMixIn.class)
                        .addMixIn(JobParameters.class, JobParametersJacksonMixIn.class)
                        .addMixIn(JobParameter.class, JobParameterJacksonMixIn.class)
                        .addMixIn(JobInstance.class, JobInstanceJacksonMixIn.class)
                        .addMixIn(ExitStatus.class, ExitStatusJacksonMixIn.class)
                        .addMixIn(StepExecution.class, StepExecutionJacksonMixIn.class)
                        .addMixIn(ExecutionContext.class, ExecutionContextJacksonMixIn.class)
                        .addMixIn(StepExecutionHistory.class, StepExecutionHistoryJacksonMixIn.class);
            }
        }
        return restTemplate;
    }
}
