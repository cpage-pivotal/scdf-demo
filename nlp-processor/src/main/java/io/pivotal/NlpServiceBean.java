package io.pivotal;

import edu.stanford.nlp.simple.Sentence;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class NlpServiceBean implements NlpService {

    private static final String VERBS = "verbs";
    private static final String NOUNS = "nouns";
    private static final String HASHTAGS = "hashtags";

    @Override
    public Map<String,List<String>> analyzeSentence(String string)
    {
        Sentence sentence = new Sentence(string);

        Map<String,List<String>> result = new HashMap<>();

//        addPersonsAndLocations(sentence, result);

        addPartsOfSpeech( sentence, result );

        return result;
    }

    private void addPartsOfSpeech(Sentence sentence, Map<String, List<String>> result) {
        List<String> nounList = new ArrayList<>();
        List<String> verbList = new ArrayList<>();
        List<String> hashtagList = new ArrayList<>();

        for (int index = 0; index < sentence.posTags().size(); index++) {
            String pos = sentence.posTags().get(index);
            String word = sentence.lemma(index);

            if ( pos.startsWith( "NN" ) && !isHashtag( word ) && !word.equals( "rt" ) )
                nounList.add( word );

            if ( pos.startsWith( "VB" ) && !isHashtag( word ) )
                verbList.add( word );

            if ( isHashtag( word ) && !word.equals( "#" ) )
                hashtagList.add( word );
        }

        result.put( NOUNS, nounList );
        result.put( VERBS, verbList );
        result.put( HASHTAGS, hashtagList );
    }

    private boolean isHashtag( String lemma )
    {
        return lemma.startsWith( "#" );
    }

    private void addPersonsAndLocations(Sentence sentence, Map<String, List<String>> result) {
        List<String> personList = new ArrayList<>();
        List<String> locationList = new ArrayList<>();

        for (int index = 0; index < sentence.nerTags().size(); index++) {
            String entity = sentence.nerTags().get(index);
            String word = sentence.word(index);

            if ( entity.equals( "PERSON") )
                personList.add(word);

            if ( entity.equals( "LOCATION" ) )
                locationList.add(word);
        }

        result.put( "PERSON", personList );
        result.put( "LOCATION", locationList );
    }
}
