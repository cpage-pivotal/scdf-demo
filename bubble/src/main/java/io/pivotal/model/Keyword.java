package io.pivotal.model;

import io.pivotal.service.KeywordStore;

public class Keyword {

    public static final int NOUN_CODE = 2008;
    public static final int VERB_CODE = 2009;

    private String _word;
    private String _partOfSpeech;
    private int _count;
    private String _group;

    public Keyword() {
    }

    public Keyword(String word, String partOfSpeech, int count) {
        _word = word;
        _partOfSpeech = partOfSpeech;
        _count = count;
    }

    public String getWord() {
        return _word;
    }

    public String getPartOfSpeech() {
        return _partOfSpeech;
    }

    public int getPosCode() {
        int result = 2008;
        if ( _partOfSpeech.equals(KeywordStore.NOUNS) )
            result = 2009;
        if ( _partOfSpeech.equals(KeywordStore.HASHTAGS) )
            result = 2010;
        return result;
    }

    public int getCount() {
        return _count;
    }

    public String getGroup() {
        return _group;
    }

    public void setGroup(String group) {
        _group = group;
    }
}
