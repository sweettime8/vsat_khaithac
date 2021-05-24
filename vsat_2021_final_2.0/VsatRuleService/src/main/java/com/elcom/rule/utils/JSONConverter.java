package com.elcom.rule.utils;

import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JSONConverter {

    private static final Logger LOGGER = LoggerFactory.getLogger(JSONConverter.class);
    
    private static final Gson GSON = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();

    public static String toJSON(Object obj) {
        return GSON.toJson(obj);
    }

    private static String getClassName(String input) {
        if (input == null) {
            return "";
        }
        int index = input.lastIndexOf(".");
        if (index != -1) {
            input = input.substring(index);
        }
        if (input.lastIndexOf(".") != -1) {
            input = input.replace(".", "");
        }
        return input;
    }

    public static <T> T toObject(String value, Class<T> actualObject) {
        try {
            return GSON.fromJson(value, actualObject);
        } catch (JsonSyntaxException ex) {
            LOGGER.error(StringUtil.printException(ex));
            return null;
        }
    }
    
    public static < T> T toObject(Class< T> classInput, String content) {
        @SuppressWarnings("unchecked")
        Map< String, Object> r = GSON.fromJson(content, Map.class);
        String className = getClassName(classInput.getName());
        String innerJson = GSON.toJson(r.get(className));
        T _r = GSON.fromJson(innerJson, classInput);
        return _r;
    }
}
