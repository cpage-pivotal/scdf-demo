package io.pivotal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.cloud.stream.messaging.Sink;
import org.springframework.data.redis.core.BoundZSetOperations;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.List;
import java.util.Map;

@EnableBinding( Sink.class )
public class RedisSink {

    private static final String VERBS = "verbs";
    private static final String NOUNS = "nouns";
    private static final String HASHTAGS = "hashtags";

    @Autowired RedisTemplate<String,String> _redisTemplate;

    @StreamListener( Sink.INPUT )
    public void writeToRedis(Map<String,List<String>> partsOfSpeech)
    {
        BoundZSetOperations<String, String> verbOps = _redisTemplate.boundZSetOps(VERBS);
        BoundZSetOperations<String, String> nounOps = _redisTemplate.boundZSetOps(NOUNS);
        BoundZSetOperations<String, String> hashtagOps = _redisTemplate.boundZSetOps(HASHTAGS);

        List<String> verbs = partsOfSpeech.get(VERBS);
        for (String verb : verbs) {
            verbOps.incrementScore( verb, 1 );
        }

        List<String> nouns = partsOfSpeech.get(NOUNS);
        for (String noun : nouns) {
            nounOps.incrementScore( noun, 1 );
        }

        List<String> hashtags = partsOfSpeech.get(HASHTAGS);
        for (String hashtag : hashtags) {
            hashtagOps.incrementScore( hashtag, 1 );
        }
    }
}
