package util;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class KeywordParser {
    public static List<String> parseHashTags(String str){
        List<String> result = new ArrayList<String>();
        
        Pattern p = Pattern.compile("(#[\\S]+)");
        Matcher m = p.matcher(str);
        while(m.find()){
            String keyword = m.group().substring(1);
            result.add(keyword);
        }
        
        return result;
    }
}
