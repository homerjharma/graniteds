package org.granite.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import org.w3c.dom.Element;
import org.xml.sax.EntityResolver;
import org.xml.sax.SAXException;

public class XMap implements Serializable {
    private static final long serialVersionUID = 1L;
    private volatile Map<String, String> bypassParsingXmlMap = new Hashtable<String, String>();
    private volatile OriginalXMap originalXMap = null;

    
    /**
     * An empty and unmodifiable XMap instance.
     */
    public static final XMap EMPTY_XMAP = new XMap(null, null, false) {

        private static final long serialVersionUID = 1L;

        @Override
        public String put(String key, String value) {
            throw new RuntimeException("Immutable XMap");
        }
        
        @Override
        public String remove(String key) {
            throw new RuntimeException("Immutable XMap");
        }
    };

    public XMap() {
        this.originalXMap = new OriginalXMap();
    }

    public XMap(DOM dom, Element root, boolean clone) {
        this.originalXMap = new OriginalXMap(dom, root, clone);
    }

    public XMap(Element root) {
        this.originalXMap = new OriginalXMap(root);
    }

    public XMap(InputStream input, EntityResolver resolver) throws IOException, SAXException {
        this.originalXMap = new OriginalXMap(input, resolver);
    }

    public XMap(InputStream input) throws IOException, SAXException {
        this.originalXMap = new OriginalXMap(input);
    }

    public XMap(XMap map) {
        this.originalXMap = new OriginalXMap(map != null ? map.originalXMap : null);
    }

    public XMap(OriginalXMap originalXMap) {
        this.originalXMap = new OriginalXMap(originalXMap);
    }

    public XMap(String root) {
        this.originalXMap = new OriginalXMap(root);
    }

    public synchronized String get(String key) {
        String value = bypassParsingXmlMap.get(key);
        if (value == null) {
            value = originalXMap.get(key);
            if (value != null) {
                bypassParsingXmlMap.put(key, value);
            }
        }
        return value;
    
    }

    public synchronized boolean containsKey(String key) {
        return originalXMap.containsKey(key);
    }

    public synchronized Element getRoot() {
        return originalXMap.getRoot();
    }

    public synchronized <T> T get(String key, Class<T> clazz, T defaultValue) {
        return originalXMap.get(key, clazz, defaultValue);
    }

    public synchronized <T> T get(String key, Class<T> clazz, T defaultValue, boolean required, boolean warn) {
        return originalXMap.get(key, clazz, defaultValue, required, warn);
    }

    public synchronized XMap getOne(String key) {
        XMap retVal = null;
        OriginalXMap one = originalXMap.getOne(key);
        if (one != null) {
            retVal = new XMap(one);
        }
        return retVal;
    }

    public synchronized String put(String key, String value) {
        clearCachedData();
        return originalXMap.put(key, value);
    }

    public synchronized String put(String key, String value, boolean append) {
        clearCachedData();
        return originalXMap.put(key, value, append);
    }

    public synchronized String remove(String key) {
        clearCachedData();
        return originalXMap.remove(key);
    }

    protected void clearCachedData() {
        bypassParsingXmlMap.clear();
    }

    public synchronized List<XMap> getAll(String key) {
        List<OriginalXMap> all = originalXMap.getAll(key);
        List<XMap> retVal = null;
        if (all != null) {
            retVal = new ArrayList<XMap>(all.size());
            for (OriginalXMap originalXMap : all){
                retVal.add(new XMap(originalXMap));
            }
        }
        return retVal;
    }

    @Override
    public synchronized boolean equals(Object arg0) {
        if (!(arg0 instanceof XMap)) {
            return false;
        }
        XMap other = (XMap) arg0;
        return originalXMap.equals(other.originalXMap);
    }

    @Override
    public synchronized int hashCode() {
        return originalXMap.hashCode();
    }

    @Override
    public synchronized String toString() {
        return originalXMap.toString();
    }
}
