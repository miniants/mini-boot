package cn.miniants.toolkit;

import org.springframework.core.SpringProperties;
import org.springframework.core.io.ClassPathResource;

import java.util.*;
import java.io.*;


public class LinkedProperties extends Properties {
    private final LinkedHashMap<Object, Object> properties = new LinkedHashMap<>();

    public Iterable<Object> orderedKeys() {
        return properties.keySet();
    }

    public Object put(Object key, Object value) {
        return properties.put(key, value);
    }

    public Set<Object> keySet() {
        return properties.keySet();
    }

    public Set<String> stringPropertyNames() {
        Set<String> set = new LinkedHashSet<>();
        for (Object key : properties.keySet()) {
            set.add((String) key);
        }
        return set;
    }

    private static final String XML_FILE_EXTENSION = ".xml";
    private static final boolean shouldIgnoreXml = SpringProperties.getFlag("spring.xml.ignore");

    public static LinkedProperties loadProperties(ClassPathResource resource) throws IOException {
        LinkedProperties props = new LinkedProperties();
        try (InputStream is = resource.getInputStream()) {
            String filename = resource.getFilename();
            if (filename != null && filename.endsWith(XML_FILE_EXTENSION)) {
                if (shouldIgnoreXml) {
                    throw new UnsupportedOperationException("XML support disabled");
                }
                props.loadFromXML(is);
            }
            else {
                props.load(is);
            }
        }

        return props;
    }

}
