package io.pivotal;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.CommandLineRunner;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.cloud.stream.messaging.Processor;
import org.springframework.cloud.stream.messaging.Sink;
import org.springframework.cloud.stream.messaging.Source;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.messaging.handler.annotation.SendTo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;

@EnableBinding( Processor.class )
public class TransformProcessor {

    @StreamListener( Sink.INPUT )
    @SendTo( Source.OUTPUT )
    public String transform( String message ) throws IOException {
        String result = "";
        ObjectMapper objectMapper = new ObjectMapper();
        TypeReference<HashMap<String,Object>> typeRef
                = new TypeReference<HashMap<String,Object>>() {};

        try {
            HashMap<String, Object> map = objectMapper.readValue(message, typeRef);
            result = (String)map.get( "text" );
        }
        catch ( Exception ex )
        {
            System.out.println( "Error parsing input: " + message );
        }
        return result;
    }
}
