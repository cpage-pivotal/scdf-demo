package io.pivotal;

import io.pivotal.model.Keyword;
import io.pivotal.service.KeywordStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Controller
public class BubbleController {

    @Autowired private KeywordStore _keywordStore;

    @Autowired
    RedisTemplate<String,String> _redisTemplate;

    @RequestMapping("/")
    public String home()
    {
        return "index";
    }

    @RequestMapping("/old")
    public String homeOld()
    {
        return "index-old";
    }

    @RequestMapping("/keywords")
    public @ResponseBody List<Keyword> allKeywords()
    {
        List<Keyword> result = new ArrayList<>();
        result.addAll( _keywordStore.getVerbs() );
        result.addAll( _keywordStore.getNouns() );
        result.addAll( _keywordStore.getHashtags() );

        classifyKeywords( result );
        return result;
    }

    private void classifyKeywords(List<Keyword> keywords) {
        Collections.sort( keywords, (Keyword a, Keyword b) -> b.getCount() - a.getCount());

        int firstBreak = keywords.size() / 3;
        int secondBreak = keywords.size() / 3 * 2;

        for (int index = 0; index < keywords.size(); index++) {
            Keyword keyword = keywords.get(index);
            if ( index < firstBreak )
                keyword.setGroup( "high" );
            else if ( index < secondBreak )
                keyword.setGroup( "medium" );
            else keyword.setGroup( "low" );
        }
    }

    @RequestMapping("/flush")
    public String flush()
    {
        _redisTemplate.getConnectionFactory().getConnection().flushAll();
        return "redirect:/";
    }
}
