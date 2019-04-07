package com.claude.justmove.util;

import com.fasterxml.jackson.core.type.TypeReference;
import com.google.gson.Gson;

import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;

public class CollectionsUtils {

    public static <T> Predicate<T> distinctByKey(Function<? super T, ?> keyExtractor) {
        Set<Object> seen = ConcurrentHashMap.newKeySet();
        return t -> seen.add(keyExtractor.apply(t));
    }

    public static <T> T mapToObject(Object o, TypeReference<T> typeReference) {
        Gson gson = new Gson();
        String json = gson.toJson(o);
        return mapToObject(json, typeReference);
    }

    public static <T> T mapToObject(String o, TypeReference<T> typeReference) {
        return new Gson().fromJson(o, typeReference.getType());
    }
}
