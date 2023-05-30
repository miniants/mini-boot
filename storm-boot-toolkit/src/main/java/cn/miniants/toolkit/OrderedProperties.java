package cn.miniants.toolkit;

import org.springframework.core.SpringProperties;
import org.springframework.core.io.ClassPathResource;

import java.util.*;
import java.io.*;


public class OrderedProperties extends Properties {
    private final LinkedHashMap<Object, Object> properties = new LinkedHashMap<>();

    @Override
    public synchronized Object put(Object key, Object value) {
        return properties.put(key, value);
    }

    @Override
    public synchronized Object remove(Object key) {
        return properties.remove(key);
    }

    @Override
    public synchronized void clear() {
        properties.clear();
    }

    @Override
    public Set<Object> keySet() {
        return properties.keySet();
    }

    @Override
    public Set<String> stringPropertyNames() {
        Set<String> set = new HashSet<>();
        for (Object key : this.properties.keySet()) {
            set.add((String) key);
        }
        return set;
    }

    @Override
    public synchronized boolean contains(Object value) {
        return properties.containsValue(value);
    }

    @Override
    public boolean containsValue(Object value) {
        return properties.containsValue(value);
    }

    @Override
    public synchronized boolean containsKey(Object key) {
        return properties.containsKey(key);
    }

    @Override
    public synchronized Object get(Object key) {
        return properties.get(key);
    }

    @Override
    public synchronized int size() {
        return properties.size();
    }

    @Override
    public synchronized boolean isEmpty() {
        return properties.isEmpty();
    }

    @Override
    public synchronized Enumeration<Object> keys() {
        Iterator<Object> it = properties.keySet().iterator();
        return new Enumeration<Object>() {
            public boolean hasMoreElements() {
                return it.hasNext();
            }
            public Object nextElement() {
                return it.next();
            }
        };
    }

    @Override
    public Set<Map.Entry<Object, Object>> entrySet() {
        return properties.entrySet();
    }


    private static final String XML_FILE_EXTENSION = ".xml";
    private static final boolean shouldIgnoreXml = SpringProperties.getFlag("spring.xml.ignore");

    public static OrderedProperties loadProperties(ClassPathResource resource) throws IOException {
        OrderedProperties props = new OrderedProperties();
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
