package pdk.util.io;

import org.jspecify.annotations.Nullable;

import javax.xml.stream.*;
import javax.xml.stream.events.XMLEvent;
import java.io.*;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static pdk.util.ArgUtils.checkArgument;

/**
 * XML Utilities.
 *
 * @author Jiawei Mao
 * @version 1.0.0
 * @since 28 Jun 2024, 2:44 PM
 */
public class XMLUtils {

    private static final Pattern xmlHeader = Pattern.compile(".*<\\?xml.+\\?>.*", Pattern.DOTALL);
    private static final Pattern xmlEnc = Pattern.compile(".*encoding\\s*=\\s*[\"']([A-Za-z]([A-Za-z0-9._]|[-])*)" +
            "[\"'](.*)", Pattern.DOTALL);

    /**
     * Create a {@link XMLStreamWriter}.
     *
     * @param targetFile target xml file path.
     * @return a {@link XMLStreamWriter} instance.
     */
    public static XMLStreamWriter createXMLStreamWriter(String targetFile) {
        XMLOutputFactory xof = XMLOutputFactory.newFactory();
        XMLStreamWriter writer = null;
        try {
            writer = xof.createXMLStreamWriter(new BufferedWriter(new FileWriter(targetFile)));
        } catch (XMLStreamException | IOException e) {
            e.printStackTrace();
        }
        return writer;
    }

    /**
     * Create an {@link PrettyXMLStreamWriter} to output formatted xml writer.
     *
     * @param targetFile target file path.
     * @return a {@link XMLStreamWriter} instance.
     */
    public static XMLStreamWriter createPrettyWriter(File targetFile) {
        PrettyXMLStreamWriter writer = null;
        try {
            writer = new PrettyXMLStreamWriter(targetFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return writer;
    }

    /**
     * Create an {@link PrettyXMLStreamWriter} to output formatted xml writer.
     *
     * @param outputStream target stream.
     * @return a {@link XMLStreamWriter} instance.
     */
    public static XMLStreamWriter createPrettyWriter(OutputStream outputStream) {
        PrettyXMLStreamWriter writer = null;
        try {
            writer = new PrettyXMLStreamWriter(outputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return writer;
    }

    /**
     * Create a {@link XMLStreamReader} of given file
     *
     * @param file file path
     * @return {@link XMLStreamReader}
     */
    public static XMLStreamReader createReader(File file) throws FileNotFoundException, XMLStreamException {
        XMLInputFactory factory = XMLInputFactory.newFactory();
        factory.setProperty(XMLInputFactory.IS_COALESCING, Boolean.TRUE);

        return factory.createXMLStreamReader(new BufferedReader(new FileReader(file)));
    }

    /**
     * Create a {@link XMLStreamReader} of given file
     *
     * @param file file path
     * @return {@link XMLStreamReader}
     */
    public static XMLStreamReader createReader(URL file) throws XMLStreamException, IOException {
        return createReader(file.openStream());
    }

    /**
     * Create a {@link XMLStreamReader} of given {@link InputStream}
     *
     * @param inputStream an {@link InputStream}
     * @return {@link XMLStreamReader} for this {@link InputStream}
     */
    public static XMLStreamReader createReader(InputStream inputStream) {
        try {
            XMLStreamReader reader = createReader(inputStream, 8192);
            return reader;
        } catch (XMLStreamException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Create a {@link XMLStreamReader} of given {@link InputStream}
     *
     * @param inputStream an {@link InputStream}
     * @param size        the buffer size.
     * @return {@link XMLStreamReader} for this {@link InputStream}
     */
    public static XMLStreamReader createReader(InputStream inputStream, int size) throws XMLStreamException {
        XMLInputFactory factory = XMLInputFactory.newFactory();
        factory.setProperty(XMLInputFactory.IS_COALESCING, Boolean.TRUE);

        XMLStreamReader reader;
        if (inputStream instanceof BufferedInputStream)
            reader = factory.createXMLStreamReader(inputStream);
        else
            reader = factory.createXMLStreamReader(new BufferedInputStream(inputStream, size));

        return reader;
    }

    /**
     * detect the xml file encoding
     *
     * @param aFile         a file
     * @param maxReadLength max number of byte to read to detect the encoding
     * @return String representation of the encoding
     */
    public static String detectFileEncoding(File aFile, int maxReadLength) throws IOException {

        checkArgument(aFile.exists(), "The file is not present: " + aFile.getName());

        // read a bit of the input file and check if it contains a XML header
        InputStream in = new FileInputStream(aFile);
        int length = in.available();

        // read a maximum of maxReadLength bytes
        byte[] bytes;
        if (length > maxReadLength) {
            bytes = new byte[maxReadLength];
        } else {
            bytes = new byte[length];
        }
        // fill the byte buffer
        in.read(bytes);
        in.close();
        // convert the bytes to String using ASCII
        String fileStart = new String(bytes, "ASCII");

        // first check if there is a XML header
        Matcher mHead = xmlHeader.matcher(fileStart);
        if (!mHead.matches()) {
            return null;
        }
        Matcher mEnc = xmlEnc.matcher(fileStart);
        if (!mEnc.matches()) {
            return null;
        }
        if (mEnc.groupCount() < 1) {
            return null;
        }
        return mEnc.group(1);
    }

    /**
     * This method reads up to maxReadLength bytes from the file specified by fileLocation and will try to detect the
     * character encoding. This simple detection is assuming a file according to XML 1.1 specs, where the encoding
     * should be provided in the XML header/prolog. It will only try to parse this information from the read bytes.
     *
     * @param fileLocation  The location of the file to check.
     * @param maxReadLength The maximum number of bytes to read from the file.
     * @return A String representing the Charset detected for the provided file or null if no character encoding could
     * be determined.
     * @throws IOException If the specified location could not be opened for reading.
     */
    public static String detectFileEncoding(URL fileLocation, int maxReadLength) throws IOException {
        // read a bit of the input file and check if it contains a XML header
        InputStream in = fileLocation.openStream();
        int length = in.available();

        // read a maximum of maxReadLength bytes
        byte[] bytes;
        if (length > maxReadLength) {
            bytes = new byte[maxReadLength];
        } else {
            bytes = new byte[length];
        }
        // fill the byte buffer
        in.read(bytes);
        in.close();
        // convert the bytes to String using ASCII
        String fileStart = new String(bytes, "ASCII");

        // first check if there is a XML header
        Matcher mHead = xmlHeader.matcher(fileStart);
        if (!mHead.matches()) {
            return null;
        }
        Matcher mEnc = xmlEnc.matcher(fileStart);
        if (!mEnc.matches()) {
            return null;
        }
        if (mEnc.groupCount() < 1) {
            return null;
        }
        return mEnc.group(1);
    }

    /**
     * return true if the <code>reader</code> is pointing to the start of the element <code>tag</code>
     *
     * @param reader {@link XMLStreamReader}
     * @param tag    element tag
     */
    public static boolean isStartElement(XMLStreamReader reader, String tag) {
        return reader.getEventType() == XMLStreamConstants.START_ELEMENT && reader.getLocalName().equals(tag);
    }

    /**
     * return true if the <code>reader</code> currently point to the end of the Element with given <code>tag</code>
     *
     * @param reader {@link XMLStreamReader}
     * @param tag    element tag
     */
    public static boolean isEndElement(XMLStreamReader reader, String tag) {
        return reader.getEventType() == XMLStreamConstants.END_ELEMENT
                && reader.getLocalName().equals(tag);
    }

    /**
     * Seek to the next start tag.
     *
     * @param reader  xml reader.
     * @param tagName the start element tag.
     * @return true if find the start element.
     */
    public static boolean toStartElement(XMLStreamReader reader, String tagName) throws XMLStreamException {
        while (reader.hasNext()) {
            int event = reader.next();
            if (event == XMLStreamConstants.START_ELEMENT && reader.getLocalName().equals(tagName))
                return true;
        }
        return false;
    }

    /**
     * seek to the next start tag.
     *
     * @param tagName    element tag name
     * @param altTagName alternative tag name
     */
    public static boolean toStartElement(XMLStreamReader reader, String tagName,
            String altTagName) throws XMLStreamException {
        while (reader.hasNext()) {
            reader.next();
            if (reader.isStartElement()) {
                String localName = reader.getLocalName();
                if (localName.equals(tagName) || localName.equals(altTagName))
                    return true;
            }
        }

        return false;
    }

    /**
     * Return the value of given attribute name.
     *
     * @param xmlSR {@link XMLStreamReader}
     * @param name  attribute name
     * @return attribute name, null for absent.
     */
    public static String attr(XMLStreamReader xmlSR, String name) {
        return xmlSR.getAttributeValue(null, name);
    }

    /**
     * Return the value of given attribute name.
     *
     * @param xmlSR {@link XMLStreamReader}
     * @param name  attribute name
     * @return attribute value
     * @throws XMLStreamException if attribute is absent
     */
    public static String requiredAttr(XMLStreamReader xmlSR, String name) throws XMLStreamException {
        String value = xmlSR.getAttributeValue(null, name);
        if (value == null)
            throw new XMLStreamException(String.format("%s miss attr %s", xmlSR.getLocalName(), name));
        return value;
    }

    /**
     * Return the value of given attribute.
     *
     * @param xmlSR a {@link XMLStreamReader}
     * @param name  attribute name
     * @return attribute boolean value, null for absent.
     */
    public static Boolean booleanAttr(XMLStreamReader xmlSR, String name) {
        Boolean result = null;
        String attributeValue = xmlSR.getAttributeValue(null, name);
        if (attributeValue != null)
            result = Boolean.parseBoolean(attributeValue);

        return result;
    }

    /**
     * Return the value of given attribute.
     *
     * @param xmlSR a {@link XMLStreamReader}
     * @param name  attribute name
     * @return attribute boolean value.
     */
    public static Boolean requiredBooleanAttr(XMLStreamReader xmlSR, String name) throws XMLStreamException {
        String attributeValue = xmlSR.getAttributeValue(null, name);
        if (attributeValue == null)
            throw new XMLStreamException(String.format("%s miss attr %s", xmlSR.getLocalName(), name));

        return Boolean.parseBoolean(attributeValue);
    }

    /**
     * Return the value of given attribute.
     *
     * @param xmlSR a {@link XMLStreamReader}
     * @param name  attribute name
     * @return attribute double value, null for absent.
     */
    public static @Nullable Double doubleAttr(XMLStreamReader xmlSR, String name) {
        Double value = null;
        String attributeValue = xmlSR.getAttributeValue(null, name);
        if (attributeValue != null)
            value = Double.parseDouble(attributeValue);
        return value;
    }

    /**
     * Return the value of given attribute name.
     *
     * @param xmlSR {@link XMLStreamReader}
     * @param name  attribute name
     * @return attribute value
     * @throws XMLStreamException for attribute absent.
     */
    public static Double requiredDoubleAttr(XMLStreamReader xmlSR, String name) throws XMLStreamException {
        String attributeValue = xmlSR.getAttributeValue(null, name);
        if (attributeValue == null) {
            throw new XMLStreamException(String.format("%s miss attr %s", xmlSR.getLocalName(), name));
        }
        return Double.parseDouble(attributeValue);
    }

    /**
     * Return the value of given attribute that must exist.
     *
     * @return integer attribute value, null for absent.
     */
    public static Integer intAttr(XMLStreamReader xmlSR, String name) {
        Integer value = null;
        String attributeValue = xmlSR.getAttributeValue(null, name);
        if (attributeValue != null)
            value = Integer.parseInt(attributeValue);
        return value;
    }

    /**
     * Return the value of given attribute that must exist.
     *
     * @return integer attribute value, null for absent.
     */
    public static int requiredIntAttr(XMLStreamReader xmlSR, String name) throws XMLStreamException {
        String attributeValue = xmlSR.getAttributeValue(null, name);
        if (attributeValue == null)
            throw new XMLStreamException(String.format("%s miss attr %s", xmlSR.getLocalName(), name));
        return Integer.parseInt(attributeValue);
    }

    /**
     * Return the value of given attributes.
     *
     * @return Float attribute value, null for absent
     */
    public static Float floatAttr(XMLStreamReader xmlSR, String name) {
        Float value = null;
        String attributeValue = xmlSR.getAttributeValue(null, name);
        if (attributeValue != null) {
            value = Float.parseFloat(attributeValue);
        }
        return value;
    }

    /**
     * Return the value of given attributes.
     *
     * @return Float attribute value, null for absent
     */
    public static Float requiredFloatAttr(XMLStreamReader xmlSR, String name) throws XMLStreamException {
        String attributeValue = xmlSR.getAttributeValue(null, name);
        if (attributeValue == null) {
            throw new XMLStreamException(String.format("%s miss attr %s", xmlSR.getLocalName(), name));
        }
        return Float.parseFloat(attributeValue);
    }

    /**
     * Return the value of given attributes.
     *
     * @return Long attribute value, null for absent
     */
    public static @Nullable Long longAttr(XMLStreamReader xmlSR, String name) {
        Long value = null;
        String attributeValue = xmlSR.getAttributeValue(null, name);
        if (attributeValue != null) {
            value = Long.parseLong(attributeValue);
        }
        return value;
    }


    /**
     * Return the value of given attributes.
     *
     * @return Long attribute value
     */
    public static Long requiredLongAttr(XMLStreamReader xmlSR, String name) throws XMLStreamException {
        String attributeValue = xmlSR.getAttributeValue(null, name);
        if (attributeValue == null) {
            throw new XMLStreamException(String.format("%s miss attr %s", xmlSR.getLocalName(), name));
        }
        return Long.parseLong(attributeValue);
    }


    /**
     * Seek to the next start element.
     *
     * @param reader xml reader
     */
    public static void nextStartElement(XMLStreamReader reader) throws XMLStreamException {
        while (reader.hasNext()) {
            int event = reader.next();
            if (event == XMLEvent.START_ELEMENT)
                return;
        }
    }

    /**
     * Move the cursor to the next {@link XMLEvent#CHARACTERS} event
     *
     * @param reader {@link XMLStreamReader}
     * @throws XMLStreamException
     */
    public static void toNextCharacters(XMLStreamReader reader) throws XMLStreamException {
        while (reader.hasNext()) {
            int event = reader.next();
            if (event == XMLEvent.CHARACTERS)
                return;
        }
    }
}
