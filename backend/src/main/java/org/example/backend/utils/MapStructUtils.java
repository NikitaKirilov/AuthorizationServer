package org.example.backend.utils;

import lombok.experimental.UtilityClass;
import org.mapstruct.Named;
import org.springframework.util.StringUtils;

import java.util.Collection;
import java.util.Set;

@UtilityClass
public class MapStructUtils {

    @Named("stringToStringSet")
    public static Set<String> stringToStringSet(String str) {
        return StringUtils.commaDelimitedListToSet(str);
    }

    @Named("collectionToString")
    public static String collectionToString(Collection<String> collection) {
        return StringUtils.collectionToCommaDelimitedString(collection);
    }
}
