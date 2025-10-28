package pdk.util.io;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 *
 *
 * @author Jiawei Mao
 * @version 1.0.0
 * @since 28 Oct 2025, 11:23 AM
 */
class FileUtilsTest {

    @Test
    void appendSuffix() {
        String path = "G:\\dataset\\a.fasta";
        String s = FileUtils.appendSuffix(path, ".decoy");
        assertEquals("G:\\dataset\\a.decoy.fasta", s);
    }
}