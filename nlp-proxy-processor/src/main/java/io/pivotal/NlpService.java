package io.pivotal;

import java.util.List;
import java.util.Map;

public interface NlpService {
    Map<String,List<String>> analyzeSentence(String string);
}
