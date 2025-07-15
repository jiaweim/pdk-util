package pdk.util.io;

import org.junit.jupiter.api.Test;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author Jiawei Mao
 * @version 1.0.0
 * @since 15 Jul 2025, 5:21 PM
 */
class XMLUtilsTest {

    @Test
    void toNextCharacters() throws XMLStreamException {
        String text = """
                <binaryDataArray encodedLength="164">
                  <cvParam cvRef="MS" accession="MS:1000523" name="64-bit float" value=""/>
                  <cvParam cvRef="MS" accession="MS:1000574" name="zlib compression" value=""/>
                  <cvParam cvRef="MS" accession="MS:1000515" name="intensity array" value="" unitCvRef="MS" unitAccession="MS:1000131" unitName="number of detector counts"/>
                  <binary>eJxjYGBIKEhb7cAABMeqwXSC5v71ILphyvUNINrBznsTWJxp+XEQ/WBb/H4wv0dqJ1hdz9O9YP4h3R1g+sxbMK1QJ3QYrD+J6SVYn1MamP+gK/AZ2L7lPWBzFmx2Bqs/MP/kSbD8BddDYP1yn/eBzVfduMsBAAI3Mrc=</binary>
                </binaryDataArray>
                """;
        InputStream stream = new ByteArrayInputStream(text.getBytes(StandardCharsets.UTF_8));
        XMLStreamReader reader = XMLUtils.createReader(stream);
        while (reader.hasNext()) {
            reader.next();

            if (reader.isStartElement()) {
                String localName = reader.getLocalName();
                if (localName.equals("binary")) {
                    XMLUtils.toNextCharacters(reader);
                    String binary = reader.getText();
                    assertEquals("eJxjYGBIKEhb7cAABMeqwXSC5v71ILphyvUNINrBznsTWJxp+XEQ/WBb/H4wv0dqJ1hdz9O9YP4h3R1g+sxbMK1QJ3QYrD+J6SVYn1MamP+gK/AZ2L7lPWBzFmx2Bqs/MP/kSbD8BddDYP1yn/eBzVfduMsBAAI3Mrc=", binary);
                }
            }
        }
        reader.close();
    }
}