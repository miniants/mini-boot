package cn.miniants.toolkit;

import org.springframework.core.SpringProperties;
import org.springframework.core.io.ClassPathResource;

import java.util.*;
import java.io.*;
import java.util.function.BiConsumer;


public class OrderedProperties extends Properties {
    private final LinkedHashMap<Object, Object> linkedProperties = new LinkedHashMap<>();

    @Override
    public synchronized Object put(Object key, Object value) {
        return linkedProperties.put(key, value);
    }

    @Override
    public synchronized Object remove(Object key) {
        return linkedProperties.remove(key);
    }

    @Override
    public synchronized void clear() {
        linkedProperties.clear();
    }

    @Override
    public synchronized boolean contains(Object value) {
        return linkedProperties.containsValue(value);
    }

    @Override
    public synchronized boolean containsKey(Object key) {
        return linkedProperties.containsKey(key);
    }

    @Override
    public synchronized boolean containsValue(Object value) {
        return linkedProperties.containsValue(value);
    }

    @Override
    public synchronized Enumeration<Object> elements() {
        return Collections.enumeration(linkedProperties.values());
    }

    @Override
    public Set<Map.Entry<Object, Object>> entrySet() {
        return linkedProperties.entrySet();
    }

    @Override
    public synchronized void forEach(BiConsumer<? super Object, ? super Object> action) {
        linkedProperties.forEach(action);
    }

    @Override
    public synchronized Object get(Object key) {
        return linkedProperties.get(key);
    }

    @Override
    public synchronized boolean isEmpty() {
        return linkedProperties.isEmpty();
    }

    @Override
    public Enumeration<Object> keys() {
        return Collections.enumeration(linkedProperties.keySet());
    }

    @Override
    public Set<Object> keySet() {
        return linkedProperties.keySet();
    }

    @Override
    public synchronized Enumeration<Object> propertyNames() {
        return Collections.enumeration(linkedProperties.keySet());
    }

    @Override
    public synchronized Object setProperty(String key, String value) {
        return linkedProperties.put(key, value);
    }

    @Override
    public synchronized int size() {
        return linkedProperties.size();
    }

    @Override
    public synchronized Collection<Object> values() {
        return linkedProperties.values();
    }

    @Override
    public Set<String> stringPropertyNames() {
        Set<String> set = new HashSet<>();
        for (Object key : linkedProperties.keySet()) {
            set.add((String) key);
        }
        return set;
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
