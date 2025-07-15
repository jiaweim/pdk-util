package pdk.util.io;

import javax.xml.namespace.NamespaceContext;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import java.io.*;
import java.util.Stack;

/**
 * XMLStreamWriter with indent.
 *
 * @author Jiawei Mao
 * @version 1.0.0
 * @since 21 Jun 2024, 10:47 AM
 */
public class PrettyXMLStreamWriter implements XMLStreamWriter {

    private static final Object SEEN_NOTHING = new Object();
    private static final Object SEEN_ELEMENT = new Object();
    private static final Object SEEN_DATA = new Object();

    private final XMLStreamWriter writer;
    private Object state;
    private final Stack<Object> stateStack;
    private String indentStep;
    private int depth;

    /**
     * Create an {@link PrettyXMLStreamWriter}
     *
     * @param file output {@link File}
     * @throws IOException for creating {@link XMLStreamWriter} error.
     */
    public PrettyXMLStreamWriter(File file) throws IOException {
        this(new FileWriter(file));
    }

    /**
     * Create an {@link PrettyXMLStreamWriter}
     *
     * @param writer {@link Writer} to output
     * @throws IOException for creating {@link XMLStreamWriter} error.
     */
    public PrettyXMLStreamWriter(Writer writer) throws IOException {
        try {
            XMLOutputFactory xof = XMLOutputFactory.newFactory();
            this.writer = xof.createXMLStreamWriter(writer);
        } catch (XMLStreamException e) {
            throw new IOException("Cannot create XMLStreamWriter for " + writer);
        }
        this.state = SEEN_NOTHING;
        this.stateStack = new Stack<>();
        this.indentStep = "  ";
        this.depth = 0;
    }

    /**
     * Create an {@link PrettyXMLStreamWriter} with given {@link OutputStream}
     *
     * @param outputStream an {@link OutputStream} instance.
     * @throws IOException for creating writer error
     */
    public PrettyXMLStreamWriter(OutputStream outputStream) throws IOException {
        try {
            XMLOutputFactory xof = XMLOutputFactory.newFactory();
            this.writer = xof.createXMLStreamWriter(outputStream, "UTF-8");
        } catch (XMLStreamException e) {
            throw new IOException("Cannot create XMLStreamWriter for " + outputStream);
        }
        this.state = SEEN_NOTHING;
        this.stateStack = new Stack<>();
        this.indentStep = "  ";
        this.depth = 0;
    }

    public PrettyXMLStreamWriter(XMLStreamWriter writer) {
        this.writer = writer;
        this.state = SEEN_NOTHING;
        this.stateStack = new Stack<>();
        this.indentStep = "  ";
        this.depth = 0;
    }

    public void setIndentStep(String s) {
        this.indentStep = s;
    }

    private void onStartElement() throws XMLStreamException {
        this.stateStack.push(SEEN_ELEMENT);
        this.state = SEEN_NOTHING;
        if (this.depth > 0) {
            writer.writeCharacters("\n");
        }

        this.doIndent();
        ++this.depth;
    }

    private void onEndElement() throws XMLStreamException {
        --this.depth;
        if (this.state == SEEN_ELEMENT) {
            writer.writeCharacters("\n");
            this.doIndent();
        }

        this.state = this.stateStack.pop();
    }

    private void onEmptyElement() throws XMLStreamException {
        this.state = SEEN_ELEMENT;
        if (this.depth > 0) {
            writer.writeCharacters("\n");
        }

        this.doIndent();
    }

    private void doIndent() throws XMLStreamException {
        if (this.depth > 0) {
            for (int i = 0; i < this.depth; ++i) {
                writer.writeCharacters(this.indentStep);
            }
        }
    }

    public void writeStartElement(String localName) throws XMLStreamException {
        this.onStartElement();
        writer.writeStartElement(localName);
    }

    public void writeStartElement(String namespaceURI, String localName) throws XMLStreamException {
        this.onStartElement();
        writer.writeStartElement(namespaceURI, localName);
    }

    public void writeStartElement(String prefix, String localName, String namespaceURI) throws XMLStreamException {
        this.onStartElement();
        writer.writeStartElement(prefix, localName, namespaceURI);
    }

    public void writeEmptyElement(String namespaceURI, String localName) throws XMLStreamException {
        this.onEmptyElement();
        writer.writeEmptyElement(namespaceURI, localName);
    }

    public void writeEmptyElement(String prefix, String localName, String namespaceURI) throws XMLStreamException {
        this.onEmptyElement();
        writer.writeEmptyElement(prefix, localName, namespaceURI);
    }

    public void writeEmptyElement(String localName) throws XMLStreamException {
        this.onEmptyElement();
        writer.writeEmptyElement(localName);
    }

    public void writeEndElement() throws XMLStreamException {
        this.onEndElement();
        writer.writeEndElement();
    }

    public void writeEndDocument() throws XMLStreamException {
        this.writer.writeEndDocument();
    }

    public void close() throws XMLStreamException {
        this.writer.close();
    }

    public void flush() throws XMLStreamException {
        this.writer.flush();
    }

    public void writeAttribute(String localName, String value) throws XMLStreamException {
        this.writer.writeAttribute(localName, value);
    }

    public void writeAttribute(String prefix, String namespaceURI, String localName,
            String value) throws XMLStreamException {
        this.writer.writeAttribute(prefix, namespaceURI, localName, value);
    }

    public void writeAttribute(String namespaceURI, String localName, String value) throws XMLStreamException {
        this.writer.writeAttribute(namespaceURI, localName, value);
    }

    public void writeNamespace(String prefix, String namespaceURI) throws XMLStreamException {
        this.writer.writeNamespace(prefix, namespaceURI);
    }

    public void writeDefaultNamespace(String namespaceURI) throws XMLStreamException {
        this.writer.writeDefaultNamespace(namespaceURI);
    }

    public void writeComment(String data) throws XMLStreamException {
        this.writer.writeComment(data);
    }

    public void writeProcessingInstruction(String target) throws XMLStreamException {
        this.writer.writeProcessingInstruction(target);
    }

    public void writeProcessingInstruction(String target, String data) throws XMLStreamException {
        this.writer.writeProcessingInstruction(target, data);
    }

    public void writeCData(String data) throws XMLStreamException {
        this.state = SEEN_DATA;
        writer.writeCData(data);
    }

    public void writeDTD(String dtd) throws XMLStreamException {
        this.writer.writeDTD(dtd);
    }

    public void writeEntityRef(String name) throws XMLStreamException {
        this.writer.writeEntityRef(name);
    }

    public void writeStartDocument() throws XMLStreamException {
        writer.writeStartDocument();
        writer.writeCharacters("\n");
    }

    public void writeStartDocument(String version) throws XMLStreamException {
        writer.writeStartDocument(version);
        writer.writeCharacters("\n");
    }

    public void writeStartDocument(String encoding, String version) throws XMLStreamException {
        writer.writeStartDocument(encoding, version);
        writer.writeCharacters("\n");
    }

    public void writeCharacters(String text) throws XMLStreamException {
        this.state = SEEN_DATA;
        writer.writeCharacters(text);
    }

    public void writeCharacters(char[] text, int start, int len) throws XMLStreamException {
        this.state = SEEN_DATA;
        writer.writeCharacters(text, start, len);
    }

    public String getPrefix(String uri) throws XMLStreamException {
        return this.writer.getPrefix(uri);
    }

    public void setPrefix(String prefix, String uri) throws XMLStreamException {
        this.writer.setPrefix(prefix, uri);
    }

    public void setDefaultNamespace(String uri) throws XMLStreamException {
        this.writer.setDefaultNamespace(uri);
    }

    public NamespaceContext getNamespaceContext() {
        return this.writer.getNamespaceContext();
    }

    public void setNamespaceContext(NamespaceContext context) throws XMLStreamException {
        this.writer.setNamespaceContext(context);
    }

    public Object getProperty(String name) throws IllegalArgumentException {
        return this.writer.getProperty(name);
    }
}
