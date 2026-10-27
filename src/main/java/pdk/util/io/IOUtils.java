package pdk.util.io;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;

/**
 * IO utilities.
 *
 * @author Jiawei Mao
 * @version 1.0.0
 * @since 14 Apr 2025, 1:28 PM
 */
public final class IOUtils {

    /**
     * Read the first line of a file
     *
     * @param file {@link File}
     * @return first line in the file
     * @throws IOException for io exception
     */
    public static String readFirstLine(final File file) throws IOException {
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line = br.readLine();
            br.close();
            return line;
        }
    }

    /**
     * Gets the contents of an {@link InputStream} as a list of Strings,
     * one entry per line, using the specified character encoding.
     * <p>
     * This method buffers the input internally, so there is no need to use a
     * {@link BufferedInputStream}.
     * </p>
     *
     * @param input   the {@link InputStream} to read, not null
     * @param charset the charset to use, null means platform default
     * @return the list of Strings, never null
     * @throws NullPointerException if the input is null
     * @throws UncheckedIOException if an I/O error occurs
     * @since 2.3
     */
    public static List<String> readLines(final InputStream input, final Charset charset) throws UncheckedIOException {
        return readLines(new InputStreamReader(input, charset));
    }

    public static void writeLines(Path file, List<String> lines) {
        try (BufferedWriter writer = Files.newBufferedWriter(file, StandardCharsets.UTF_8)) {
            for (String line : lines) {
                writer.write(line);
                writer.newLine();
            }
            writer.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Gets the contents of a {@link Reader} as a list of Strings,
     * one entry per line.
     * <p>
     * This method buffers the input internally, so there is no need to use a
     * {@link BufferedReader}.
     * </p>
     *
     * @param reader the {@link Reader} to read, not null
     * @return the list of Strings, never null
     * @throws NullPointerException if the input is null
     * @throws UncheckedIOException if an I/O error occurs
     */
    @SuppressWarnings("resource") // reader wraps input and is the responsibility of the caller.
    public static List<String> readLines(final Reader reader) throws UncheckedIOException {
        return toBufferedReader(reader).lines().collect(Collectors.toList());
    }

    /**
     * Returns the given reader if it is a {@link BufferedReader}, otherwise creates a BufferedReader from the given
     * reader.
     *
     * @param reader the reader to wrap or return (not null)
     * @return the given reader or a new {@link BufferedReader} for the given reader
     * @throws NullPointerException if the input parameter is null
     */
    public static BufferedReader toBufferedReader(final Reader reader) {
        return reader instanceof BufferedReader ? (BufferedReader) reader : new BufferedReader(reader);
    }
}
