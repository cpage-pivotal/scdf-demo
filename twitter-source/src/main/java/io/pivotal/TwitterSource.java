package io.pivotal;


import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.messaging.Source;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;

@EnableBinding( Source.class )
public class TwitterSource {

    @Autowired
    @Qualifier("output")
    private MessageChannel _output;

    @RabbitListener(bindings = @QueueBinding(value = @Queue,
            exchange = @Exchange(value = "tweets.exch", type = "topic", durable = "true"), key = "tweets.*" ) )
    public void receiveMessage(Message message)
    {
        _output.send( message );
    }

}
