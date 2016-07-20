package io.pivotal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.cloud.stream.messaging.Processor;
import org.springframework.cloud.stream.messaging.Sink;
import org.springframework.cloud.stream.messaging.Source;
import org.springframework.context.ApplicationContext;
import org.springframework.messaging.Message;
import org.springframework.messaging.handler.annotation.SendTo;

import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Map;

@EnableBinding(Processor.class)
public class NlpProxyProcessor {

    @Autowired
    ApplicationContext _applicationContext;

    @StreamListener(Sink.INPUT)
    @SendTo(Source.OUTPUT)
    public Map<String, List<String>> processMessage( Message message ) throws UnsupportedEncodingException {
        String sentence = message.getPayload().toString();

        NlpService nlpService = (NlpService) _applicationContext.getBean("nlpServiceProxy");

        return nlpService.analyzeSentence(sentence);
    }
}
