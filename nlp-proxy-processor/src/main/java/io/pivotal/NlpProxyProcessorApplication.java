package io.pivotal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.remoting.httpinvoker.HttpInvokerProxyFactoryBean;

import java.util.List;
import java.util.Map;

@SpringBootApplication
public class NlpProxyProcessorApplication {

	public static void main(String[] args) {
		SpringApplication.run(NlpProxyProcessorApplication.class, args);
	}

	@Autowired
	ApplicationContext _applicationContext;

	@Bean
	CommandLineRunner run()
	{
		return args -> {
			String sentence = "Everybody in the party throw up your hands and say yeah";

			NlpService nlpService = (NlpService) _applicationContext.getBean("nlpServiceProxy");

			Map<String, List<String>> stringListMap = nlpService.analyzeSentence(sentence);

			System.out.println( stringListMap.size() );
		};
	}
}
