package org.granite.test.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.granite.util.XMap;
import org.junit.Before;
import org.junit.Test;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;


public class XMapTest {

    @Before
    public void setUp() throws Exception {
    }

    @Test
    public void testUninitialized() {
        XMap xmap = new XMap();
        assertNull(xmap.get("A"));
        assertNull(xmap.get("B"));
        assertNull(xmap.get("C"));
        assertNull(xmap.getRoot());
        assertFalse(xmap.containsKey("A"));
        assertFalse(xmap.containsKey("B"));
        assertFalse(xmap.containsKey("C"));
        assertNull(xmap.getRoot());
    }

    @Test
    public void testRootOnly() {
        XMap xmap = new XMap("rootElement");
        assertNull(xmap.get("A"));
        assertNull(xmap.get("B"));
        assertNull(xmap.get("C"));
        assertFalse(xmap.containsKey("A"));
        assertFalse(xmap.containsKey("B"));
        assertFalse(xmap.containsKey("C"));
        assertEquals("", xmap.getRoot().getTextContent());
    }

    @Test
    public void testRootNull() {
        XMap xmap = new XMap((String)null);
        assertNull(xmap.get("A"));
        assertNull(xmap.get("B"));
        assertNull(xmap.get("C"));
        assertFalse(xmap.containsKey("A"));
        assertFalse(xmap.containsKey("B"));
        assertFalse(xmap.containsKey("C"));
        assertNull(xmap.getRoot());
    }

    @Test
    public void testInitializedViaPut() {
        XMap xmap = new XMap("rootElement");
        
        xmap.put("A", "Value of A");
        xmap.put("B", "123");
        xmap.put("C", null);
        xmap.put("D", "");
        xmap.put("F", "  \n \r \t    ");

        assertContentsWithAppendDisabled(xmap);

        XMap copyXmap = new XMap(xmap);

        assertContentsWithAppendDisabled(copyXmap);
    }

    private void assertContentsWithAppendDisabled(XMap xmap) {
        assertEquals("Value of A", xmap.get("A"));
        assertEquals("123", xmap.get("B"));
        assertEquals("", xmap.get("C"));
        assertEquals("", xmap.get("D"));
        assertNull(xmap.get("E"));
        assertEquals("", xmap.get("F"));
        assertEquals("Value of A123  \n \r \t    ", xmap.getRoot().getTextContent());
        
        assertTrue(xmap.containsKey("A"));
        assertTrue(xmap.containsKey("B"));
        assertTrue(xmap.containsKey("C"));
        assertTrue(xmap.containsKey("D"));
        assertFalse(xmap.containsKey("E"));
        assertTrue(xmap.containsKey("F"));
        assertFalse(xmap.containsKey("G"));
    }

    @Test
    public void testInitializedViaPutWithAppend() {
        XMap xmap = new XMap("rootElement");
        
        xmap.put("A", "Value of A1", true);
        xmap.put("A", "Value of A2", true);
        xmap.put("A", "Value of A3", true);
        
        xmap.put("B", "Z", true);
        xmap.put("B", "Y", true);
        xmap.put("B", "X", true);
        
        xmap.put("C", "A", true);
        xmap.put("C", null, true);
        xmap.put("C", "B", true);
        
        xmap.put("D", null, true);
        xmap.put("D", null, true);
        xmap.put("D", null, true);
        
        xmap.put("E", "same", true);
        xmap.put("E", "same", true);
        xmap.put("E", "same", true);

        assertEquals("Value of A1", xmap.get("A"));
        assertEquals("Z", xmap.get("B"));
        assertEquals("A", xmap.get("C"));
        assertEquals("", xmap.get("D"));
        assertEquals("same", xmap.get("E"));
        assertEquals("Value of A1Value of A2Value of A3ZYXABsamesamesame", xmap.getRoot().getTextContent());

        assertContentsWithAppendEnabled(xmap);

        XMap copyXmap = new XMap(xmap);

        assertContentsWithAppendEnabled(copyXmap);

    }

    private void assertContentsWithAppendEnabled(XMap xmap) {
        XMap firstA = xmap.getOne("A");
        assertTrue(xmap.containsKey("A"));
        assertTrue(xmap.containsKey("B"));
        assertTrue(xmap.containsKey("C"));
        assertTrue(xmap.containsKey("D"));
        assertTrue(xmap.containsKey("E"));
        assertFalse(xmap.containsKey("F"));

        List<XMap> allA = xmap.getAll("A");
        assertEquals(3, allA.size());
        assertEquals("Value of A1", firstA.getRoot().getTextContent());
        assertEquals("Value of A1", allA.get(0).getRoot().getTextContent());
        assertEquals("Value of A2", allA.get(1).getRoot().getTextContent());
        assertEquals("Value of A3", allA.get(2).getRoot().getTextContent());
        
        XMap firstB = xmap.getOne("B");
        List<XMap> allB = xmap.getAll("B");
        assertEquals(3, allB.size());
        assertEquals("Z", firstB.getRoot().getTextContent());
        assertEquals("Z", allB.get(0).getRoot().getTextContent());
        assertEquals("Y", allB.get(1).getRoot().getTextContent());
        assertEquals("X", allB.get(2).getRoot().getTextContent());
        
        XMap firstC = xmap.getOne("C");
        List<XMap> allC = xmap.getAll("C");
        assertEquals(3, allC.size());
        assertEquals("A", firstC.getRoot().getTextContent());
        assertEquals("A", allC.get(0).getRoot().getTextContent());
        assertEquals("", allC.get(1).getRoot().getTextContent());
        assertEquals("B", allC.get(2).getRoot().getTextContent());
        
        XMap firstD = xmap.getOne("D");
        List<XMap> allD = xmap.getAll("D");
        assertEquals(3, allD.size());
        assertEquals("", firstD.getRoot().getTextContent());
        assertEquals("", allD.get(0).getRoot().getTextContent());
        assertEquals("", allD.get(1).getRoot().getTextContent());
        assertEquals("", allD.get(2).getRoot().getTextContent());
        
        XMap firstE = xmap.getOne("E");
        List<XMap> allE = xmap.getAll("E");
        assertEquals(3, allE.size());
        assertEquals("same", firstE.getRoot().getTextContent());
        assertEquals("same", allE.get(0).getRoot().getTextContent());
        assertEquals("same", allE.get(1).getRoot().getTextContent());
        assertEquals("same", allE.get(2).getRoot().getTextContent());
    }

    @Test(expected=RuntimeException.class)
    public void testEmptyXmapConstantPut() {
        XMap.EMPTY_XMAP.put("anyKey", "anyValue");
    }

    @Test(expected=RuntimeException.class)
    public void testEmptyXmapConstantRemove() {
        XMap.EMPTY_XMAP.remove("anyKey");
    }

    public void testEmptyXmapConstantContents() {
        assertNotNull(XMap.EMPTY_XMAP);
        assertEquals(0, XMap.EMPTY_XMAP.getRoot().getTextContent());
    }

    @Test(expected=RuntimeException.class)
    public void testContainsKeyBadRequest() {
        XMap xmap = new XMap("rootElement");
        xmap.put("A", "Value");
        xmap.containsKey("<A>");
    }

    @Test(expected=RuntimeException.class)
    public void testGetsKeyBadRequest() {
        XMap xmap = new XMap("rootElement");
        xmap.put("A", "Value");
        xmap.get("<A>");
    }
    
    @Test
    public void testGetWithDefault() {
        XMap xmap = new XMap("rootElement");
        xmap.put("A", "One");
        xmap.put("B", "Two");
        assertEquals("One", xmap.get("A", String.class, "DefaultValue"));
        assertEquals("Two", xmap.get("B", String.class, "DefaultValue"));
        assertEquals("DefaultValue", xmap.get("C", String.class, "DefaultValue"));
        assertNull(xmap.get("C"));
    }
    
    @Test(expected=SAXParseException.class)
    public void testReadInputStreamInvalid() throws IOException, SAXException {
        InputStream is = new ByteArrayInputStream("TestInvalidXML".getBytes());
        new XMap(is);
    }
    
    @Test
    public void testReadInputStreamRootOnly() throws IOException, SAXException {
        InputStream is = new ByteArrayInputStream("<rootElement></rootElement>".getBytes());
        XMap xMap = new XMap(is);
        assertNotNull(xMap.getRoot());
        assertEquals("", xMap.getRoot().getTextContent());
    }
    
    @Test
    public void testReadInputStreamRootAndChildren() throws IOException, SAXException {
        InputStream is = new ByteArrayInputStream("<rootElement><x>Some Value 1</x><y>2</y><z></z></rootElement>".getBytes());
        XMap xMap = new XMap(is);
        assertNotNull(xMap.getRoot());
        assertEquals("Some Value 12", xMap.getRoot().getTextContent());
        assertNull(xMap.get("a"));
        assertNotNull(xMap.get("x"));
        assertEquals("Some Value 1", xMap.get("x"));
        assertNotNull(xMap.get("y"));
        assertEquals("2", xMap.get("y"));
        assertNotNull(xMap.get("z"));
        assertEquals("", xMap.get("z"));
        assertNull(xMap.get("Z"));
    }
    
    @Test
    public void testReadInputStreamRootAndChildrenNested() throws IOException, SAXException {
        InputStream is = new ByteArrayInputStream("<rootElement><outer><inner><x>Some Value 1</x><y>2\n3\n4</y></inner><z>just in outer value</z></outer></rootElement>".getBytes());
        XMap xMap = new XMap(is);
        assertNotNull(xMap.getRoot());
        assertEquals("Some Value 12\n3\n4just in outer value", xMap.getRoot().getTextContent());
        assertNull(xMap.get("a"));
        assertNotNull(xMap.get("outer/inner/x"));
        assertEquals("Some Value 1", xMap.get("outer/inner/x"));
        assertNotNull(xMap.get("outer/inner/y"));
        assertEquals("2\n3\n4", xMap.get("outer/inner/y"));
        assertNotNull(xMap.get("outer/z"));
        assertEquals("just in outer value", xMap.get("outer/z"));
        assertNotNull(xMap.get("outer"));
        assertEquals("", xMap.get("outer"));
        
        XMap outerXMap = xMap.getOne("outer");

        assertNotNull(outerXMap.getRoot());
        assertEquals("Some Value 12\n3\n4just in outer value", outerXMap.getRoot().getTextContent());
        assertNotNull(outerXMap.get("inner/x"));
        assertEquals("Some Value 1", outerXMap.get("inner/x"));
        assertNotNull(outerXMap.get("inner/y"));
        assertEquals("2\n3\n4", outerXMap.get("inner/y"));
        assertNotNull(outerXMap.get("z"));
        assertEquals("just in outer value", outerXMap.get("z"));
        assertNotNull(outerXMap.get("inner"));
        assertEquals("", outerXMap.get("inner"));
        
        XMap innerXMap = xMap.getOne("outer/inner");
        assertNotNull(innerXMap.getRoot());
        assertEquals("Some Value 12\n3\n4", innerXMap.getRoot().getTextContent());
        assertNotNull(innerXMap.get("x"));
        assertEquals("Some Value 1", innerXMap.get("x"));
        assertNotNull(innerXMap.get("y"));
        assertEquals("2\n3\n4", innerXMap.get("y"));
    }

    
    @Test
    public void testReadInputStreamPutNestedValues() throws IOException, SAXException {
        InputStream is = new ByteArrayInputStream("<rootElement><outer><inner><x>Some Value 1</x></inner></outer></rootElement>".getBytes());
        XMap xMap = new XMap(is);
        assertNotNull(xMap.get("outer/inner/x"));
        assertEquals("Some Value 1", xMap.get("outer/inner/x"));
        assertNull(xMap.get("outer/inner/y"));
        assertNull(xMap.get("outer/z"));
        assertNull(xMap.get("q"));
        
        xMap.put("outer/inner/y", "ValY");
        xMap.put("outer/z", "ValZ");
        xMap.put("q", "ValQ");
        
        assertNotNull(xMap.get("outer/inner/x"));
        assertEquals("Some Value 1", xMap.get("outer/inner/x"));
        assertNotNull(xMap.get("outer/inner/y"));
        assertEquals("ValY", xMap.get("outer/inner/y"));
        assertNotNull(xMap.get("outer/z"));
        assertEquals("ValZ", xMap.get("outer/z"));
        assertNotNull(xMap.get("q"));
        assertEquals("ValQ", xMap.get("q"));
    }

    @Test
    public void testPutValidKeyNullElement() throws IOException, SAXException {
        XMap xMap = new XMap();
        xMap.put("key", "value");
        assertNotNull(xMap.get("key"));
        assertEquals("value", xMap.get("key"));
    }

    @Test(expected=RuntimeException.class)
    public void testPutInvalidKeyNullElement() throws IOException, SAXException {
        XMap xMap = new XMap();
        xMap.put("</bogusKey>", "value");
    }

    @Test(expected=RuntimeException.class)
    public void testPutParentModeDoesNotExist() throws IOException, SAXException {
        XMap xMap = new XMap();
        xMap.put("some/nested/nodes/key", "value");
    }

    @Test
    public void testPutAttribute() throws IOException, SAXException {
        XMap xMap = new XMap();
        xMap.put("@key", "value");
        assertEquals("value", xMap.get("@key"));
    }
    
    @Test
    public void testRemoveChildrenNested() throws IOException, SAXException {
        InputStream is = new ByteArrayInputStream("<rootElement><outer><inner><x>1</x><y>2</y></inner><z>3</z></outer></rootElement>".getBytes());
        XMap xMap = new XMap(is);
        assertNotNull(xMap.getRoot());
        assertEquals("123", xMap.getRoot().getTextContent());
        assertNotNull(xMap.get("outer/inner/x"));
        assertEquals("1", xMap.get("outer/inner/x"));
        assertNotNull(xMap.get("outer/inner/y"));
        assertEquals("2", xMap.get("outer/inner/y"));
        assertNotNull(xMap.get("outer/z"));
        assertEquals("3", xMap.get("outer/z"));
        
        xMap.remove("outer/inner/x");
        
        assertNotNull(xMap.getRoot());
        assertEquals("23", xMap.getRoot().getTextContent());
        assertNull(xMap.get("outer/inner/x"));
        assertNotNull(xMap.get("outer/inner/y"));
        assertEquals("2", xMap.get("outer/inner/y"));
        assertNotNull(xMap.get("outer/z"));
        assertEquals("3", xMap.get("outer/z"));
        
        xMap.remove("outer");

        assertNotNull(xMap.getRoot());
        assertEquals("", xMap.getRoot().getTextContent());
        assertNull(xMap.get("outer/inner/x"));
        assertNull(xMap.get("outer/inner/y"));
        assertNull(xMap.get("outer/z"));
    }

    @Test
    public void testGetTypesChildren() throws IOException, SAXException {
        InputStream is = new ByteArrayInputStream("<rootElement><outer><x attr1=\"SomeAttrVal\">1</x></outer></rootElement>".getBytes());
        XMap xMap = new XMap(is);
        assertNotNull(xMap.getRoot());
        assertEquals("1", xMap.getRoot().getTextContent());
        assertTrue(xMap.containsKey("outer/x"));
        assertNotNull(xMap.get("outer/x"));
        assertEquals("1", xMap.get("outer/x"));
        assertTrue(xMap.containsKey("outer/x[text()]"));
        assertNotNull(xMap.get("outer/x[text()]"));
        assertEquals("1", xMap.get("outer/x[text()]"));
        assertTrue(xMap.containsKey("outer/x/@attr1"));
        assertNotNull(xMap.get("outer/x/@attr1"));
        assertEquals("SomeAttrVal", xMap.get("outer/x/@attr1"));
    }

    @Test
    public void testGetDataTypes() throws IOException, SAXException {
        InputStream is = new ByteArrayInputStream("<rootElement><int>1</int><float>1.23</float><boolean>true</boolean><byte>127</byte></rootElement>".getBytes());
        XMap xMap = new XMap(is);
        assertEquals(new Short("1"), xMap.get("int", Short.class, (short)9, true, false));
        assertEquals(new Integer("1"), xMap.get("int", Integer.class, (int)9, true, false));
        assertEquals(new Long("1"), xMap.get("int", Long.class, (long)9, true, false));
        assertEquals(new Float("1.23"), xMap.get("float", Float.class, (float)9.99, true, false));
        assertEquals(new Double("1.23"), xMap.get("float", Double.class, (double)9.99, true, false));
        assertTrue(xMap.get("boolean", Boolean.class, false, true, false));
        assertEquals(new Byte("127"), xMap.get("byte", Byte.class, (byte)11, true, false));
    }

    @Test
    public void testGetCopyElement() throws IOException, SAXException {
        InputStream is = new ByteArrayInputStream("<rootElement><int>1</int><float>1.23</float><boolean>true</boolean><byte>127</byte></rootElement>".getBytes());
        XMap xMap = new XMap(is);
        
        XMap elementCopyXmap = new XMap(xMap.getRoot());
        
        assertEquals(new Short("1"), elementCopyXmap.get("int", Short.class, (short)9, true, false));
        assertEquals(new Integer("1"), elementCopyXmap.get("int", Integer.class, (int)9, true, false));
        assertEquals(new Long("1"), elementCopyXmap.get("int", Long.class, (long)9, true, false));
        assertEquals(new Float("1.23"), elementCopyXmap.get("float", Float.class, (float)9.99, true, false));
        assertEquals(new Double("1.23"), elementCopyXmap.get("float", Double.class, (double)9.99, true, false));
        assertTrue(elementCopyXmap.get("boolean", Boolean.class, false, true, false));
        assertEquals(new Byte("127"), elementCopyXmap.get("byte", Byte.class, (byte)11, true, false));
    }
}
