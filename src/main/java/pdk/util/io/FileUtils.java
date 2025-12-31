package pdk.util.io;

import java.io.File;
import java.nio.file.Path;

/**
 * File related methods not available in commons-io.
 *
 * @author Jiawei Mao
 * @version 1.1.0
 * @since 22 Nov 2024, 8:09 PM
 */
public final class FileUtils {

    private FileUtils() {}

    private static final int NOT_FOUND = -1;

    /**
     * The system separator character.
     */
    public static final char SYSTEM_NAME_SEPARATOR = File.separatorChar;

    /**
     * The extension separator character.
     */
    public static final char EXTENSION_SEPARATOR = '.';

    /**
     * The Windows separator character.
     */
    private static final char WINDOWS_NAME_SEPARATOR = '\\';

    /**
     * The Unix separator character.
     */
    private static final char UNIX_NAME_SEPARATOR = '/';

    /**
     * The separator character that is the opposite of the system separator.
     */
    private static final char OTHER_SEPARATOR = flipSeparator(SYSTEM_NAME_SEPARATOR);

    /**
     * Flips the Windows name separator to Linux and vice-versa.
     *
     * @param ch The Windows or Linux name separator.
     * @return The Windows or Linux name separator.
     */
    static char flipSeparator(final char ch) {
        if (ch == UNIX_NAME_SEPARATOR) {
            return WINDOWS_NAME_SEPARATOR;
        }
        if (ch == WINDOWS_NAME_SEPARATOR) {
            return UNIX_NAME_SEPARATOR;
        }
        throw new IllegalArgumentException(String.valueOf(ch));
    }

    /**
     * Determines if Windows file system is in use.
     *
     * @return true if the system is Windows
     */
    static boolean isSystemWindows() {
        return SYSTEM_NAME_SEPARATOR == WINDOWS_NAME_SEPARATOR;
    }

    /**
     * Returns the index of the last extension separator character, which is a period.
     * <p>
     * This method also checks that there is no directory separator after the last period. To do this it uses
     * {@link #indexOfLastSeparator(String)} which will handle a file in either Unix or Windows format.
     * </p>
     * <p>
     * The output will be the same irrespective of the machine that the code is running on, with the
     * exception of a possible {@link IllegalArgumentException} on Windows (see below).
     * </p>
     * <strong>Note:</strong> This method used to have a hidden problem for names like "foo.exe:bar.txt".
     * In this case, the name wouldn't be the name of a file, but the identifier of an
     * alternate data stream (bar.txt) on the file foo.exe. The method used to return
     * ".txt" here, which would be misleading. Commons IO 2.7, and later versions, are throwing
     * an {@link IllegalArgumentException} for names like this.
     *
     * @param fileName the file name to find the last extension separator in, null returns -1
     * @return the index of the last extension separator character, or -1 if there is no such character
     * @throws IllegalArgumentException <strong>Windows only:</strong> the file name parameter is, in fact,
     *                                  the identifier of an Alternate Data Stream, for example "foo.exe:bar.txt".
     */
    public static int indexOfExtension(final String fileName) throws IllegalArgumentException {
        if (fileName == null) {
            return NOT_FOUND;
        }
        if (isSystemWindows()) {
            // Special handling for NTFS ADS: Don't accept colon in the file name.
            final int offset = fileName.indexOf(':', getAdsCriticalOffset(fileName));
            if (offset != -1) {
                throw new IllegalArgumentException("NTFS ADS separator (':') in file name is forbidden.");
            }
        }
        final int extensionPos = fileName.lastIndexOf(EXTENSION_SEPARATOR);
        final int lastSeparator = indexOfLastSeparator(fileName);
        return lastSeparator > extensionPos ? NOT_FOUND : extensionPos;
    }

    /**
     * Checks the input for null characters ({@code U+0000}), a sign of unsanitized data being passed to file level functions.
     * <p>
     * This may be used to defend against poison byte attacks.
     *
     * @param path the path to check
     * @return The input
     * @throws IllegalArgumentException if path contains the null character ({@code U+0000})
     */
    private static String requireNonNullChars(final String path) {
        if (path.indexOf(0) >= 0) {
            throw new IllegalArgumentException(
                    "Null character present in file/path name. There are no known legitimate use cases for such data, but several injection attacks may use it");
        }
        return path;
    }

    /**
     * Removes the extension from a fileName.
     * <p>
     * This method returns the textual part of the file name before the last period.
     * There must be no directory separator after the period.
     * <pre>
     * foo.txt    --&gt; foo
     * .txt       --&gt; "" (empty string)
     * a\b\c.jpg  --&gt; a\b\c
     * /a/b/c.jpg --&gt; /a/b/c
     * a\b\c      --&gt; a\b\c
     * a.b\c      --&gt; a.b\c
     * </pre>
     * <p>
     * The output will be the same irrespective of the machine that the code is running on.
     *
     * @param fileName the file name, null returns null
     * @return the file name minus the extension
     * @throws IllegalArgumentException if the file name contains the null character ({@code U+0000})
     */
    public static String removeExtension(final String fileName) {
        if (fileName == null) {
            return null;
        }
        requireNonNullChars(fileName);

        final int index = indexOfExtension(fileName);
        if (index == NOT_FOUND) {
            return fileName;
        }
        return fileName.substring(0, index);
    }

    /**
     * Returns the index of the last directory separator character.
     * <p>
     * This method will handle a file in either Unix or Windows format.
     * The position of the last forward or backslash is returned.
     * <p>
     * The output will be the same irrespective of the machine that the code is running on.
     *
     * @param fileName the file name to find the last path separator in, null returns -1
     * @return the index of the last separator character, or -1 if there
     * is no such character
     */
    public static int indexOfLastSeparator(final String fileName) {
        if (fileName == null) {
            return NOT_FOUND;
        }
        final int lastUnixPos = fileName.lastIndexOf(UNIX_NAME_SEPARATOR);
        final int lastWindowsPos = fileName.lastIndexOf(WINDOWS_NAME_SEPARATOR);
        return Math.max(lastUnixPos, lastWindowsPos);
    }

    /**
     * Append a suffix to a filename to generate a new file name.
     *
     * @param filename the filename to append a suffix.
     * @param suffix   suffix to added
     * @return a new filename with a suffix added to original filename.
     */
    public static String appendSuffix(String filename, String suffix) {
        StringBuilder sBuffer = new StringBuilder(filename);
        int extIndex = indexOfExtension(filename);
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
        String newName = removeExtension(file.getAbsolutePath()) + newExt;
        return new File(newName);
    }

    /**
     * Special handling for NTFS ADS: Don't accept colon in the file name.
     *
     * @param fileName a file name
     * @return ADS offsets.
     */
    private static int getAdsCriticalOffset(final String fileName) {
        // Step 1: Remove leading path segments.
        final int offset1 = fileName.lastIndexOf(SYSTEM_NAME_SEPARATOR);
        final int offset2 = fileName.lastIndexOf(OTHER_SEPARATOR);
        if (offset1 == -1) {
            if (offset2 == -1) {
                return 0;
            }
            return offset2 + 1;
        }
        if (offset2 == -1) {
            return offset1 + 1;
        }
        return Math.max(offset1, offset2) + 1;
    }
}
