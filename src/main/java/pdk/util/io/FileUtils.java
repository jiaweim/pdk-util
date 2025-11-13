package pdk.util.io;

import org.apache.commons.io.FilenameUtils;

import java.io.File;
import java.nio.file.Path;

/**
 * File related methods not available in commons-io {@link FilenameUtils}
 *
 * @author Jiawei Mao
 * @version 1.1.0
 * @since 22 Nov 2024, 8:09 PM
 */
public final class FileUtils {

    private FileUtils() {}

    /**
     * Append a suffix to a filename to generate a new file name.
     *
     * @param filename the filename to append a suffix.
     * @param suffix   suffix to added
     * @return a new filename with a suffix added to original filename.
     */
    public static String appendSuffix(String filename, String suffix) {
        StringBuilder sBuffer = new StringBuilder(filename);
        int extIndex = FilenameUtils.indexOfExtension(filename);
        sBuffer.insert(extIndex, suffix);

        return sBuffer.toString();
    }

    /**
     * Remove illegal characters from the path, and replace it with the "_.
     *
     * @param path a file path
     * @return path without illegal characters.
     */
    public static String legalPath(String path) {
        return legalPath(path, "_");
    }

    /**
     * Remove illegal characters from the path, and replace it with the {@code replaceChar}.
     *
     * @param path        a file path
     * @param replaceChar character used to replace illegal characters
     * @return path without illegal characters.
     */
    public static String legalPath(String path, String replaceChar) {
        return path.replaceAll("[^a-zA-Z0-9_\\-.]", replaceChar);
    }

    /**
     * Return the file of new extension, such as <code>newExtension("hello.txt", ".java") == "hello.java"</code>
     *
     * @param file   a {@link Path}
     * @param newExt new extension of the file
     * @return {@link Path} with different extension.
     */
    public static File newExtension(File file, String newExt) {
        String newName = FilenameUtils.removeExtension(file.getAbsolutePath()) + newExt;
        return new File(newName);
    }
}
