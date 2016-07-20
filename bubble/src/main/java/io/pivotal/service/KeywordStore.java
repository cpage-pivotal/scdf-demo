package io.pivotal.service;

import io.pivotal.model.Keyword;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.BoundZSetOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

@Component
public class KeywordStore extends HashMap<String,Keyword>{

    public static final String VERBS = "verbs";
    public static final String NOUNS = "nouns";
    public static final String HASHTAGS = "hashtags";
    private static final int MAX_RESULTS = 80;

    @Autowired
    RedisTemplate<String,String> _redisTemplate;

    public List<Keyword> getVerbs()
    {
        return getKeywords(VERBS);
    }

    public List<Keyword> getNouns()
    {
        return getKeywords(NOUNS);
    }

    public List<Keyword> getHashtags()
    {
        return getKeywords(HASHTAGS);
    }

    private List<Keyword> getKeywords(String partOfSpeech) {
        List<Keyword> result = new ArrayList<>();
        BoundZSetOperations<String, String> zSetOps = _redisTemplate.boundZSetOps(partOfSpeech);

        Set<ZSetOperations.TypedTuple<String>> typedTuples = zSetOps.reverseRangeByScoreWithScores(0, Double.MAX_VALUE);
        int count = 0;
        for (ZSetOperations.TypedTuple<String> typedTuple : typedTuples) {

            Keyword keyword = new Keyword( typedTuple.getValue(), partOfSpeech, typedTuple.getScore().intValue() );
            result.add( keyword );
            if ( count++ > MAX_RESULTS )
                break;
        }

        return result;
    }
}
