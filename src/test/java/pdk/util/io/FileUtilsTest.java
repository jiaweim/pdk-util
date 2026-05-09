package pdk.util.io;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 *
 *
 * @author Jiawei Mao
 * @version 1.0.0
 * @since 28 Oct 2025, 11:23 AM
 */
class FileUtilsTest {

    @Test
    void newExtensionPath() {
        // 1. normal
        Path p1 = Path.of("data/sequence.fasta");
        Path p2 = FileUtils.newExtension(p1, "fa");
        assertEquals("data\\sequence.fa", p2.toString());

        // 2. no ext
        p1 = Path.of("README");
        p2 = FileUtils.newExtension(p1, "md");
        assertEquals("README.md", p2.toString());

        // 3. multiple exts
        p1 = Path.of("project.tar.gz");
        p2 = FileUtils.newExtension(p1, "xz");
        assertEquals("project.tar.xz", p2.toString());

        // 4. ignore
        p1 = Path.of(".gitignore");
        p2 = FileUtils.newExtension(p1, "bak");
        assertEquals(".gitignore.bak", p2.toString());
    }

    @Test
    void appendSuffixPath() {
        // normal
        Path path = Path.of("data/test.fasta");
        Path processed = FileUtils.appendSuffix(path, "_processed");
        assertEquals("data\\test_processed.fasta", processed.toString());

        // without ext
        Path p1 = Path.of("README");
        Path p2 = FileUtils.appendSuffix(p1, ".bak");
        assertEquals("README.bak", p2.toString());

        // multiple exts
        p1 = Path.of("archive.tar.gz");
        p2 = FileUtils.appendSuffix(p1, "_temp");
        assertEquals("archive.tar_temp.gz", p2.toString());
    }

    @Test
    void appendSuffix() {
        String path = "G:\\dataset\\a.fasta";
        String s = FileUtils.appendSuffix(path, ".decoy");
        assertEquals("G:\\dataset\\a.decoy.fasta", s);
    }

    @Test
    void appendSuffix2() {
        String path = "E:\\dataset\\test.psm.tsv";
        String s = FileUtils.appendSuffix(path, ".decoy");
        System.out.println(s);
    }

    @Test
    void endWith() {
        Path path = Path.of("G:\\dataset\\a.fasta");
        assertTrue(FileUtils.endWith(path, ".fasta"));

        Path path1 = Path.of("C:\\repositories\\pdk-seq\\src\\test\\resources\\pdk\\seq\\io");
        assertTrue(FileUtils.endWith(path1, "io"));
    }

    @Test
    void toPath() throws MalformedURLException {
        URL fileUrl = new URL("file:///C:/Users/example/file.txt");
        Path path = FileUtils.toPath(fileUrl);
        assertEquals(Path.of("C:/Users/example/file.txt"), path);
    }

    @Test
    void createDirectory() throws IOException {
        Path abc = FileUtils.createDirectory(Path.of("G:\\dataset\\_test"), "abc");
        System.out.println(abc);
    }
}