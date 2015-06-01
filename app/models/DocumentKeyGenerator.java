package models;

import java.lang.reflect.Method;

import org.springframework.cache.interceptor.KeyGenerator;

public class DocumentKeyGenerator implements KeyGenerator {

    @Override
    public Object generate(Object arg0, Method arg1, Object... arg2) {
        return ((Document)arg2[0]).id;
    }

}
