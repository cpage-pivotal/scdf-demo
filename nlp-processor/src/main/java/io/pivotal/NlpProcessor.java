package io.pivotal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.cloud.stream.messaging.Processor;
import org.springframework.cloud.stream.messaging.Sink;
import org.springframework.cloud.stream.messaging.Source;
import org.springframework.messaging.Message;
import org.springframework.messaging.handler.annotation.SendTo;

import java.util.List;
import java.util.Map;

@EnableBinding(Processor.class )
public class NlpProcessor {

    @Autowired
    NlpService _nlpService;

    @StreamListener( Sink.INPUT )
    @SendTo( Source.OUTPUT )
    public Map<String,List<String>> processMessage( Message message )
    {
        String string = message.getPayload().toString();

        return _nlpService.analyzeSentence( string );
    }

}
