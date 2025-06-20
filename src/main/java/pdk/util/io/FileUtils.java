package pdk.util.io;

import java.io.File;
import java.io.FileFilter;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributeView;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.util.Objects.requireNonNull;

/**
 * Many methods copy from commons-io
 *
 * @author Jiawei Mao
 * @version 1.0.0
 * @since 22 Nov 2024, 8:09 PM
 */
public final class FileUtils {

    private static final String PROTOCOL_FILE = "file";

    private static final int NOT_FOUND = -1;
    private static final String EMPTY_STRING = "";
    private static final String[] EMPTY_STRING_ARRAY = {};

    /**
     * The UNIX separator character.
     */
    private static final char UNIX_NAME_SEPARATOR = '/';

    /**
     * The Windows separator character.
     */
    private static final char WINDOWS_NAME_SEPARATOR = '\\';

    /**
     * The extension separator character.
     *
     * @since 1.4
     */
    public static final char EXTENSION_SEPARATOR = '.';

    /**
     * The system separator character.
     */
    private static final char SYSTEM_NAME_SEPARATOR = File.separatorChar;

    /**
     * The separator character that is the opposite of the system separator.
     */
    private static final char OTHER_SEPARATOR = flipSeparator(SYSTEM_NAME_SEPARATOR);

    private static final Pattern IPV4_PATTERN = Pattern.compile("^(\\d{1,3})\\.(\\d{1,3})\\.(\\d{1,3})\\.(\\d{1,3})$");
    private static final int IPV4_MAX_OCTET_VALUE = 255;

    private static final int IPV6_MAX_HEX_GROUPS = 8;

    private static final int IPV6_MAX_HEX_DIGITS_PER_GROUP = 4;

    private static final int MAX_UNSIGNED_SHORT = 0xffff;

    private static final int BASE_16 = 16;

    private static final Pattern REG_NAME_PART_PATTERN = Pattern.compile("^[a-zA-Z0-9][a-zA-Z0-9-]*$");

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
     * Concatenates a fileName to a base path using normal command line style rules.
     * <p>
     * The effect is equivalent to resultant directory after changing
     * directory to the first argument, followed by changing directory to
     * the second argument.
     * </p>
     * <p>
     * The first argument is the base path, the second is the path to concatenate.
     * The returned path is always normalized via {@link #normalize(String)},
     * thus {@code ..} is handled.
     * </p>
     * <p>
     * If {@code pathToAdd} is absolute (has an absolute prefix), then
     * it will be normalized and returned.
     * Otherwise, the paths will be joined, normalized and returned.
     * </p>
     * <p>
     * The output will be the same on both UNIX and Windows except
     * for the separator character.
     * </p>
     * <pre>
     * /foo/      + bar        --&gt;  /foo/bar
     * /foo       + bar        --&gt;  /foo/bar
     * /foo       + /bar       --&gt;  /bar
     * /foo       + C:/bar     --&gt;  C:/bar
     * /foo       + C:bar      --&gt;  C:bar [1]
     * /foo/a/    + ../bar     --&gt;  /foo/bar
     * /foo/      + ../../bar  --&gt;  null
     * /foo/      + /bar       --&gt;  /bar
     * /foo/..    + /bar       --&gt;  /bar
     * /foo       + bar/c.txt  --&gt;  /foo/bar/c.txt
     * /foo/c.txt + bar        --&gt;  /foo/c.txt/bar [2]
     * </pre>
     * <p>
     * [1] Note that the Windows relative drive prefix is unreliable when
     * used with this method.
     * </p>
     * <p>
     * [2] Note that the first parameter must be a path. If it ends with a name, then
     * the name will be built into the concatenated path. If this might be a problem,
     * use {@link #getFullPath(String)} on the base path argument.
     * </p>
     *
     * @param basePath          the base path to attach to, always treated as a path
     * @param fullFileNameToAdd the file name (or path) to attach to the base
     * @return the concatenated path, or null if invalid
     * @throws IllegalArgumentException if the result path contains the null character ({@code U+0000})
     */
    public static String concat(final String basePath, final String fullFileNameToAdd) {
        final int prefix = getPrefixLength(fullFileNameToAdd);
        if (prefix < 0) {
            return null;
        }
        if (prefix > 0) {
            return normalize(fullFileNameToAdd);
        }
        if (basePath == null) {
            return null;
        }
        final int len = basePath.length();
        if (len == 0) {
            return normalize(fullFileNameToAdd);
        }
        final char ch = basePath.charAt(len - 1);
        if (isSeparator(ch)) {
            return normalize(basePath + fullFileNameToAdd);
        }
        return normalize(basePath + '/' + fullFileNameToAdd);
    }

    /**
     * Gets the full path (prefix + path) from a full file name.
     * <p>
     * This method will handle a file in either UNIX or Windows format.
     * The method is entirely text based, and returns the text before and
     * including the last forward or backslash.
     * </p>
     * <pre>
     * C:\a\b\c.txt --&gt; C:\a\b\
     * ~/a/b/c.txt  --&gt; ~/a/b/
     * a.txt        --&gt; ""
     * a/b/c        --&gt; a/b/
     * a/b/c/       --&gt; a/b/c/
     * C:           --&gt; C:
     * C:\          --&gt; C:\
     * ~            --&gt; ~/
     * ~/           --&gt; ~/
     * ~user        --&gt; ~user/
     * ~user/       --&gt; ~user/
     * </pre>
     * <p>
     * The output will be the same irrespective of the machine that the code is running on.
     * </p>
     *
     * @param fileName the file name, null returns null
     * @return the path of the file, an empty string if none exists, null if invalid
     * @throws IllegalArgumentException if the result path contains the null character ({@code U+0000})
     */
    public static String getFullPath(final String fileName) {
        return doGetFullPath(fileName, true);
    }

    /**
     * Does the work of getting the path.
     *
     * @param fileName         the file name
     * @param includeSeparator true to include the end separator
     * @return the path
     * @throws IllegalArgumentException if the result path contains the null character ({@code U+0000})
     */
    private static String doGetFullPath(final String fileName, final boolean includeSeparator) {
        if (fileName == null) {
            return null;
        }
        final int prefix = getPrefixLength(fileName);
        if (prefix < 0) {
            return null;
        }
        if (prefix >= fileName.length()) {
            if (includeSeparator) {
                return getPrefix(fileName);  // add end slash if necessary
            }
            return fileName;
        }
        final int index = indexOfLastSeparator(fileName);
        if (index < 0) {
            return fileName.substring(0, prefix);
        }
        int end = index + (includeSeparator ? 1 : 0);
        if (end == 0) {
            end++;
        }
        return fileName.substring(0, end);
    }

    /**
     * Gets the prefix such as {@code C:/} or {@code ~/} from a full file name,
     * <p>
     * This method will handle a file in either UNIX or Windows format.
     * The prefix includes the first slash in the full file name where applicable.
     * </p>
     * <pre>
     * Windows:
     * a\b\c.txt           --&gt; ""          --&gt; relative
     * \a\b\c.txt          --&gt; "\"         --&gt; current drive absolute
     * C:a\b\c.txt         --&gt; "C:"        --&gt; drive relative
     * C:\a\b\c.txt        --&gt; "C:\"       --&gt; absolute
     * \\server\a\b\c.txt  --&gt; "\\server\" --&gt; UNC
     *
     * Unix:
     * a/b/c.txt           --&gt; ""          --&gt; relative
     * /a/b/c.txt          --&gt; "/"         --&gt; absolute
     * ~/a/b/c.txt         --&gt; "~/"        --&gt; current user
     * ~                   --&gt; "~/"        --&gt; current user (slash added)
     * ~user/a/b/c.txt     --&gt; "~user/"    --&gt; named user
     * ~user               --&gt; "~user/"    --&gt; named user (slash added)
     * </pre>
     * <p>
     * The output will be the same irrespective of the machine that the code is running on.
     * ie. both UNIX and Windows prefixes are matched regardless.
     * </p>
     *
     * @param fileName the file name, null returns null
     * @return the prefix of the file, null if invalid
     * @throws IllegalArgumentException if the result contains the null character ({@code U+0000})
     */
    public static String getPrefix(final String fileName) {
        if (fileName == null) {
            return null;
        }
        final int len = getPrefixLength(fileName);
        if (len < 0) {
            return null;
        }
        if (len > fileName.length()) {
            requireNonNullChars(fileName);
            return fileName + UNIX_NAME_SEPARATOR;
        }
        return requireNonNullChars(fileName.substring(0, len));
    }


    /**
     * Checks if the character is a separator.
     *
     * @param ch the character to check
     * @return true if it is a separator character
     */
    private static boolean isSeparator(final char ch) {
        return ch == UNIX_NAME_SEPARATOR || ch == WINDOWS_NAME_SEPARATOR;
    }

    /**
     * Checks whether a given string is a valid host name according to
     * RFC 3986.
     *
     * <p>Accepted are IP addresses (v4 and v6) as well as what the
     * RFC calls a "reg-name". Percent encoded names don't seem to be
     * valid names in UNC paths.</p>
     *
     * @param name the hostname to validate
     * @return true if the given name is a valid host name
     * @see "https://tools.ietf.org/html/rfc3986#section-3.2.2"
     */
    private static boolean isValidHostName(final String name) {
        return isIPv6Address(name) || isRFC3986HostName(name);
    }

    /**
     * Checks whether a given string is a valid host name according to
     * RFC 3986 - not accepting IP addresses.
     *
     * @param name the hostname to validate
     * @return true if the given name is a valid host name
     * @see "https://tools.ietf.org/html/rfc3986#section-3.2.2"
     */
    private static boolean isRFC3986HostName(final String name) {
        final String[] parts = name.split("\\.", -1);
        for (int i = 0; i < parts.length; i++) {
            if (parts[i].isEmpty()) {
                // trailing dot is legal, otherwise we've hit a .. sequence
                return i == parts.length - 1;
            }
            if (!REG_NAME_PART_PATTERN.matcher(parts[i]).matches()) {
                return false;
            }
        }
        return true;
    }


    /**
     * Normalizes a path, removing double and single dot path steps.
     * <p>
     * This method normalizes a path to a standard format.
     * The input may contain separators in either UNIX or Windows format.
     * The output will contain separators in the format of the system.
     * <p>
     * A trailing slash will be retained.
     * A double slash will be merged to a single slash (but UNC names are handled).
     * A single dot path segment will be removed.
     * A double dot will cause that path segment and the one before to be removed.
     * If the double dot has no parent path segment, {@code null} is returned.
     * <p>
     * The output will be the same on both UNIX and Windows except
     * for the separator character.
     * <pre>
     * /foo//               --&gt;   /foo/
     * /foo/./              --&gt;   /foo/
     * /foo/../bar          --&gt;   /bar
     * /foo/../bar/         --&gt;   /bar/
     * /foo/../bar/../baz   --&gt;   /baz
     * //foo//./bar         --&gt;   //foo/bar
     * /../                 --&gt;   null
     * ../foo               --&gt;   null
     * foo/bar/..           --&gt;   foo/
     * foo/../../bar        --&gt;   null
     * foo/../bar           --&gt;   bar
     * //server/foo/../bar  --&gt;   //server/bar
     * //server/../bar      --&gt;   null
     * C:\foo\..\bar        --&gt;   C:\bar
     * C:\..\bar            --&gt;   null
     * ~/foo/../bar/        --&gt;   ~/bar/
     * ~/../bar             --&gt;   null
     * </pre>
     * (Note the file separator will be correct for Windows/Unix.)
     *
     * @param fileName the file name to normalize, null returns null
     * @return the normalized fileName, or null if invalid
     * @throws IllegalArgumentException if the file name contains the null character ({@code U+0000})
     */
    public static String normalize(final String fileName) {
        return doNormalize(fileName, SYSTEM_NAME_SEPARATOR, true);
    }


    /**
     * Internal method to perform the normalization.
     *
     * @param fileName      the file name
     * @param separator     The separator character to use
     * @param keepSeparator true to keep the final separator
     * @return the normalized fileName
     * @throws IllegalArgumentException if the file name contains the null character ({@code U+0000})
     */
    private static String doNormalize(final String fileName, final char separator, final boolean keepSeparator) {
        if (fileName == null) {
            return null;
        }

        requireNonNullChars(fileName);

        int size = fileName.length();
        if (size == 0) {
            return fileName;
        }
        final int prefix = getPrefixLength(fileName);
        if (prefix < 0) {
            return null;
        }

        final char[] array = new char[size + 2];  // +1 for possible extra slash, +2 for arraycopy
        fileName.getChars(0, fileName.length(), array, 0);

        // fix separators throughout
        final char otherSeparator = flipSeparator(separator);
        for (int i = 0; i < array.length; i++) {
            if (array[i] == otherSeparator) {
                array[i] = separator;
            }
        }

        // add extra separator on the end to simplify code below
        boolean lastIsDirectory = true;
        if (array[size - 1] != separator) {
            array[size++] = separator;
            lastIsDirectory = false;
        }

        // adjoining slashes
        // If we get here, prefix can only be 0 or greater, size 1 or greater
        // If prefix is 0, set loop start to 1 to prevent index errors
        for (int i = prefix != 0 ? prefix : 1; i < size; i++) {
            if (array[i] == separator && array[i - 1] == separator) {
                System.arraycopy(array, i, array, i - 1, size - i);
                size--;
                i--;
            }
        }

        // dot slash
        for (int i = prefix + 1; i < size; i++) {
            if (array[i] == separator && array[i - 1] == '.' &&
                    (i == prefix + 1 || array[i - 2] == separator)) {
                if (i == size - 1) {
                    lastIsDirectory = true;
                }
                System.arraycopy(array, i + 1, array, i - 1, size - i);
                size -= 2;
                i--;
            }
        }

        // double dot slash
        outer:
        for (int i = prefix + 2; i < size; i++) {
            if (array[i] == separator && array[i - 1] == '.' && array[i - 2] == '.' &&
                    (i == prefix + 2 || array[i - 3] == separator)) {
                if (i == prefix + 2) {
                    return null;
                }
                if (i == size - 1) {
                    lastIsDirectory = true;
                }
                int j;
                for (j = i - 4; j >= prefix; j--) {
                    if (array[j] == separator) {
                        // remove b/../ from a/b/../c
                        System.arraycopy(array, i + 1, array, j + 1, size - i);
                        size -= i - j;
                        i = j + 1;
                        continue outer;
                    }
                }
                // remove a/../ from a/../c
                System.arraycopy(array, i + 1, array, prefix, size - i);
                size -= i + 1 - prefix;
                i = prefix + 1;
            }
        }

        if (size <= 0) {  // should never be less than 0
            return EMPTY_STRING;
        }
        if (size <= prefix) {  // should never be less than prefix
            return new String(array, 0, size);
        }
        if (lastIsDirectory && keepSeparator) {
            return new String(array, 0, size);  // keep trailing separator
        }
        return new String(array, 0, size - 1);  // lose trailing separator
    }

    /**
     * Checks whether a given string represents a valid IPv4 address.
     *
     * @param name the name to validate
     * @return true if the given name is a valid IPv4 address
     */
    // mostly copied from org.apache.commons.validator.routines.InetAddressValidator#isValidInet4Address
    private static boolean isIPv4Address(final String name) {
        final Matcher m = IPV4_PATTERN.matcher(name);
        if (!m.matches() || m.groupCount() != 4) {
            return false;
        }

        // verify that address subgroups are legal
        for (int i = 1; i <= 4; i++) {
            final String ipSegment = m.group(i);
            final int iIpSegment = Integer.parseInt(ipSegment);
            if (iIpSegment > IPV4_MAX_OCTET_VALUE) {
                return false;
            }

            if (ipSegment.length() > 1 && ipSegment.startsWith("0")) {
                return false;
            }

        }

        return true;
    }

    // copied from org.apache.commons.validator.routines.InetAddressValidator#isValidInet6Address

    /**
     * Checks whether a given string represents a valid IPv6 address.
     *
     * @param inet6Address the name to validate
     * @return true if the given name is a valid IPv6 address
     */
    private static boolean isIPv6Address(final String inet6Address) {
        final boolean containsCompressedZeroes = inet6Address.contains("::");
        if (containsCompressedZeroes && inet6Address.indexOf("::") != inet6Address.lastIndexOf("::")) {
            return false;
        }
        if (inet6Address.startsWith(":") && !inet6Address.startsWith("::")
                || inet6Address.endsWith(":") && !inet6Address.endsWith("::")) {
            return false;
        }
        String[] octets = inet6Address.split(":");
        if (containsCompressedZeroes) {
            final List<String> octetList = new ArrayList<>(Arrays.asList(octets));
            if (inet6Address.endsWith("::")) {
                // String.split() drops ending empty segments
                octetList.add("");
            } else if (inet6Address.startsWith("::") && !octetList.isEmpty()) {
                octetList.remove(0);
            }
            octets = octetList.toArray(EMPTY_STRING_ARRAY);
        }
        if (octets.length > IPV6_MAX_HEX_GROUPS) {
            return false;
        }
        int validOctets = 0;
        int emptyOctets = 0; // consecutive empty chunks
        for (int index = 0; index < octets.length; index++) {
            final String octet = octets[index];
            if (octet.isEmpty()) {
                emptyOctets++;
                if (emptyOctets > 1) {
                    return false;
                }
            } else {
                emptyOctets = 0;
                // Is last chunk an IPv4 address?
                if (index == octets.length - 1 && octet.contains(".")) {
                    if (!isIPv4Address(octet)) {
                        return false;
                    }
                    validOctets += 2;
                    continue;
                }
                if (octet.length() > IPV6_MAX_HEX_DIGITS_PER_GROUP) {
                    return false;
                }
                final int octetInt;
                try {
                    octetInt = Integer.parseInt(octet, BASE_16);
                } catch (final NumberFormatException e) {
                    return false;
                }
                if (octetInt < 0 || octetInt > MAX_UNSIGNED_SHORT) {
                    return false;
                }
            }
            validOctets++;
        }
        return validOctets <= IPV6_MAX_HEX_GROUPS && (validOctets >= IPV6_MAX_HEX_GROUPS || containsCompressedZeroes);
    }


    /**
     * Returns the length of the file name prefix, such as {@code C:/} or {@code ~/}.
     * <p>
     * This method will handle a file in either UNIX or Windows format.
     * </p>
     * <p>
     * The prefix length includes the first slash in the full file name
     * if applicable. Thus, it is possible that the length returned is greater
     * than the length of the input string.
     * </p>
     * <pre>
     * Windows:
     * a\b\c.txt           --&gt; 0           --&gt; relative
     * \a\b\c.txt          --&gt; 1           --&gt; current drive absolute
     * C:a\b\c.txt         --&gt; 2           --&gt; drive relative
     * C:\a\b\c.txt        --&gt; 3           --&gt; absolute
     * \\server\a\b\c.txt  --&gt; 9           --&gt; UNC
     * \\\a\b\c.txt        --&gt; -1          --&gt; error
     *
     * Unix:
     * a/b/c.txt           --&gt; 0           --&gt; relative
     * /a/b/c.txt          --&gt; 1           --&gt; absolute
     * ~/a/b/c.txt         --&gt; 2           --&gt; current user
     * ~                   --&gt; 2           --&gt; current user (slash added)
     * ~user/a/b/c.txt     --&gt; 6           --&gt; named user
     * ~user               --&gt; 6           --&gt; named user (slash added)
     * //server/a/b/c.txt  --&gt; 9
     * ///a/b/c.txt        --&gt; -1          --&gt; error
     * C:                  --&gt; 0           --&gt; valid file name as only null character and / are reserved characters
     * </pre>
     * <p>
     * The output will be the same irrespective of the machine that the code is running on.
     * ie. both UNIX and Windows prefixes are matched regardless.
     * </p>
     * <p>
     * Note that a leading // (or \\) is used to indicate a UNC name on Windows.
     * These must be followed by a server name, so double-slashes are not collapsed
     * to a single slash at the start of the file name.
     * </p>
     *
     * @param fileName the file name to find the prefix in, null returns -1
     * @return the length of the prefix, -1 if invalid or null
     */
    public static int getPrefixLength(final String fileName) {
        if (fileName == null) {
            return NOT_FOUND;
        }
        final int len = fileName.length();
        if (len == 0) {
            return 0;
        }
        char ch0 = fileName.charAt(0);
        if (ch0 == ':') {
            return NOT_FOUND;
        }
        if (len == 1) {
            if (ch0 == '~') {
                return 2;  // return a length greater than the input
            }
            return isSeparator(ch0) ? 1 : 0;
        }
        if (ch0 == '~') {
            int posUnix = fileName.indexOf(UNIX_NAME_SEPARATOR, 1);
            int posWin = fileName.indexOf(WINDOWS_NAME_SEPARATOR, 1);
            if (posUnix == NOT_FOUND && posWin == NOT_FOUND) {
                return len + 1;  // return a length greater than the input
            }
            posUnix = posUnix == NOT_FOUND ? posWin : posUnix;
            posWin = posWin == NOT_FOUND ? posUnix : posWin;
            return Math.min(posUnix, posWin) + 1;
        }
        final char ch1 = fileName.charAt(1);
        if (ch1 == ':') {
            ch0 = Character.toUpperCase(ch0);
            if (ch0 >= 'A' && ch0 <= 'Z') {
                if (len == 2 && !FileSystem.getCurrent().supportsDriveLetter()) {
                    return 0;
                }
                if (len == 2 || !isSeparator(fileName.charAt(2))) {
                    return 2;
                }
                return 3;
            }
            if (ch0 == UNIX_NAME_SEPARATOR) {
                return 1;
            }
            return NOT_FOUND;

        }
        if (!isSeparator(ch0) || !isSeparator(ch1)) {
            return isSeparator(ch0) ? 1 : 0;
        }
        int posUnix = fileName.indexOf(UNIX_NAME_SEPARATOR, 2);
        int posWin = fileName.indexOf(WINDOWS_NAME_SEPARATOR, 2);
        if (posUnix == NOT_FOUND && posWin == NOT_FOUND || posUnix == 2 || posWin == 2) {
            return NOT_FOUND;
        }
        posUnix = posUnix == NOT_FOUND ? posWin : posUnix;
        posWin = posWin == NOT_FOUND ? posUnix : posWin;
        final int pos = Math.min(posUnix, posWin) + 1;
        final String hostnamePart = fileName.substring(2, pos - 1);
        return isValidHostName(hostnamePart) ? pos : NOT_FOUND;
    }


    /**
     * append a suffix to current filename to generate a new file name.
     *
     * @param filename the filename to append a suffix.
     * @param suffix   suffix to added
     * @return a new filename with a suffix added to original filename.
     */
    public static String appendSuffix(String filename, String suffix) {
        StringBuilder sBuffer = new StringBuilder(filename);
        sBuffer.insert(indexOfExtension(filename), suffix);

        return sBuffer.toString();
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

    /**
     * Returns the index of the last extension separator character, which is a dot.
     * <p>
     * This method also checks that there is no directory separator after the last dot. To do this it uses
     * {@link #indexOfLastSeparator(String)} which will handle a file in either UNIX or Windows format.
     * </p>
     * <p>
     * The output will be the same irrespective of the machine that the code is running on, with the
     * exception of a possible {@link IllegalArgumentException} on Windows (see below).
     * </p>
     * <b>Note:</b> This method used to have a hidden problem for names like "foo.exe:bar.txt".
     * In this case, the name wouldn't be the name of a file, but the identifier of an
     * alternate data stream (bar.txt) on the file foo.exe. The method used to return
     * ".txt" here, which would be misleading. Commons IO 2.7, and later versions, are throwing
     * an {@link IllegalArgumentException} for names like this.
     *
     * @param fileName the file name to find the last extension separator in, null returns -1
     * @return the index of the last extension separator character, or -1 if there is no such character
     * @throws IllegalArgumentException <b>Windows only:</b> the file name parameter is, in fact,
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
     * Determines if Windows file system is in use.
     *
     * @return true if the system is Windows
     */
    static boolean isSystemWindows() {
        return SYSTEM_NAME_SEPARATOR == WINDOWS_NAME_SEPARATOR;
    }


    /**
     * Returns the index of the last directory separator character.
     * <p>
     * This method will handle a file in either UNIX or Windows format.
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
     * @param path a file path
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
     * Gets the extension of a fileName.
     * <p>
     * This method returns the textual part of the file name after the last dot.
     * There must be no directory separator after the dot.
     * </p>
     * <pre>
     * foo.txt      --&gt; "txt"
     * a/b/c.jpg    --&gt; "jpg"
     * a/b.txt/c    --&gt; ""
     * a/b/c        --&gt; ""
     * </pre>
     * <p>
     * The output will be the same irrespective of the machine that the code is running on, with the
     * exception of a possible {@link IllegalArgumentException} on Windows (see below).
     * </p>
     * <p>
     * <b>Note:</b> This method used to have a hidden problem for names like "foo.exe:bar.txt".
     * In this case, the name wouldn't be the name of a file, but the identifier of an
     * alternate data stream (bar.txt) on the file foo.exe. The method used to return
     * ".txt" here, which would be misleading. Commons IO 2.7 and later throw
     * an {@link IllegalArgumentException} for names like this.
     * </p>
     *
     * @param fileName the file name to retrieve the extension of.
     * @return the extension of the file or an empty string if none exists or {@code null}
     * if the file name is {@code null}.
     * @throws IllegalArgumentException <b>Windows only:</b> the file name parameter is, in fact,
     *                                  the identifier of an Alternate Data Stream, for example "foo.exe:bar.txt".
     */
    public static String getExtension(final String fileName) throws IllegalArgumentException {
        if (fileName == null) {
            return null;
        }
        final int index = indexOfExtension(fileName);
        if (index == NOT_FOUND) {
            return EMPTY_STRING;
        }
        return fileName.substring(index + 1);
    }

    /**
     * Removes the extension from a fileName.
     * <p>
     * This method returns the textual part of the file name before the last dot.
     * There must be no directory separator after the dot.
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
     * Gets the base name, minus the full path and extension, from a full file name.
     * <p>
     * This method will handle a path in either UNIX or Windows format.
     * The text after the last forward or backslash and before the last dot is returned.
     * </p>
     * <pre>
     * a/b/c.txt --&gt; c
     * a\b\c.txt --&gt; c
     * a/b/c.foo.txt --&gt; c.foo
     * a.txt     --&gt; a
     * a/b/c     --&gt; c
     * a/b/c/    --&gt; ""
     * </pre>
     * <p>
     * The output will be the same irrespective of the machine that the code is running on.
     * </p>
     *
     * @param fileName the file name, null returns null
     * @return the name of the file without the path, or an empty string if none exists
     * @throws IllegalArgumentException if the file name contains the null character ({@code U+0000})
     */
    public static String getBaseName(final String fileName) {
        return removeExtension(getName(fileName));
    }

    /**
     * Gets the name minus the path from a full file name.
     * <p>
     * This method will handle a file in either UNIX or Windows format.
     * The text after the last forward or backslash is returned.
     * </p>
     * <pre>
     * a/b/c.txt --&gt; c.txt
     * a\b\c.txt --&gt; c.txt
     * a.txt     --&gt; a.txt
     * a/b/c     --&gt; c
     * a/b/c/    --&gt; ""
     * </pre>
     * <p>
     * The output will be the same irrespective of the machine that the code is running on.
     * </p>
     *
     * @param fileName the file name, null returns null
     * @return the name of the file without the path, or an empty string if none exists
     * @throws IllegalArgumentException if the file name contains the null character ({@code U+0000})
     */
    public static String getName(final String fileName) {
        if (fileName == null) {
            return null;
        }
        return requireNonNullChars(fileName).substring(indexOfLastSeparator(fileName) + 1);
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
     * Creates all parent directories for a File object, including any necessary but non-existent parent directories. If a parent directory already exists or
     * is null, nothing happens.
     *
     * @param file the File that may need parents, may be null.
     * @return The parent directory, or {@code null} if the given File does have a parent.
     * @throws IOException       if the directory was not created along with all its parent directories.
     * @throws SecurityException See {@link File#mkdirs()}.
     * @since 2.9.0
     */
    public static File createParentDirectories(final File file) throws IOException {
        return mkdirs(getParentFile(file));
    }

    /**
     * Gets the parent of the given file. The given file may be null. Note that a file's parent may be null as well.
     *
     * @param file The file to query, may be null.
     * @return The parent file or {@code null}. Note that a file's parent may be null as well.
     */
    private static File getParentFile(final File file) {
        return file == null ? null : file.getParentFile();
    }

    /**
     * Calls {@link File#mkdirs()} and throws an {@link IOException} on failure.
     * <p>
     * Creates all directories for a File object, including any necessary but non-existent parent directories. If the {@code directory} already exists or is
     * null, nothing happens.
     * </p>
     *
     * @param directory the receiver for {@code mkdirs()}. If the {@code directory} already exists or is null, nothing happens.
     * @return the given directory.
     * @throws IOException       if the directory was not created along with all its parent directories.
     * @throws IOException       if the given file object is not a directory.
     * @throws SecurityException See {@link File#mkdirs()}.
     * @see File#mkdirs()
     */
    public static File mkdirs(final File directory) throws IOException {
        if (directory != null && !directory.mkdirs() && !directory.isDirectory()) {
            throw new IOException("Cannot create directory '" + directory + "'.");
        }
        return directory;
    }

    /**
     * Deletes the given File but throws an IOException if it cannot, unlike {@link File#delete()} which returns a
     * boolean.
     *
     * @param file The file to delete.
     * @return the given file.
     * @throws NullPointerException if the parameter is {@code null}
     * @throws IOException          if the file cannot be deleted.
     * @see File#delete()
     */
    public static File delete(final File file) throws IOException {
        requireNonNull(file, PROTOCOL_FILE);
        Files.delete(file.toPath());
        return file;
    }

    /**
     * Copies a file to a new location preserving the file date.
     * <p>
     * This method copies the contents of the specified source file to the specified destination file. The directory
     * holding the destination file is created if it does not exist. If the destination file exists, then this method
     * overwrites it. A symbolic link is resolved before copying so the new file is not a link.
     * </p>
     * <p>
     * <strong>Note:</strong> This method tries to preserve the file's last modified date/times using
     * {@link BasicFileAttributeView#setTimes(FileTime, FileTime, FileTime)}. However, it is not guaranteed that the
     * operation will succeed. If the modification operation fails, it falls back to
     * {@link File#setLastModified(long)}, and if that fails, the method throws IOException.
     * </p>
     *
     * @param srcFile  an existing file to copy, must not be {@code null}.
     * @param destFile the new file, must not be {@code null}.
     * @throws NullPointerException if any of the given {@link File}s are {@code null}.
     * @throws IOException          if source or destination is invalid.
     * @throws IOException          if an error occurs or setting the last-modified time didn't succeed.
     * @throws IOException          if the output file length is not the same as the input file length after the copy completes.
     * @see #copyFileToDirectory(File, File)
     * @see #copyFile(File, File, boolean)
     */
    public static void copyFile(final File srcFile, final File destFile) throws IOException {
        copyFile(srcFile, destFile, StandardCopyOption.REPLACE_EXISTING);
    }

    /**
     * Copies an existing file to a new file location.
     * <p>
     * This method copies the contents of the specified source file to the specified destination file. The directory
     * holding the destination file is created if it does not exist. If the destination file exists, then this method
     * overwrites it. A symbolic link is resolved before copying so the new file is not a link.
     * </p>
     * <p>
     * <strong>Note:</strong> Setting {@code preserveFileDate} to {@code true} tries to preserve the file's last
     * modified date/times using {@link BasicFileAttributeView#setTimes(FileTime, FileTime, FileTime)}. However, it is
     * not guaranteed that the operation will succeed. If the modification operation fails, it falls back to
     * {@link File#setLastModified(long)}, and if that fails, the method throws IOException.
     * </p>
     *
     * @param srcFile          an existing file to copy, must not be {@code null}.
     * @param destFile         the new file, must not be {@code null}.
     * @param preserveFileDate true if the file date of the copy should be the same as the original.
     * @throws NullPointerException if any of the given {@link File}s are {@code null}.
     * @throws IOException          if source or destination is invalid.
     * @throws IOException          if an error occurs or setting the last-modified time didn't succeed.
     * @throws IOException          if the output file length is not the same as the input file length after the copy completes
     * @see #copyFile(File, File, boolean, CopyOption...)
     */
    public static void copyFile(final File srcFile, final File destFile, final boolean preserveFileDate) throws IOException {
        copyFile(srcFile, destFile, preserveFileDate, StandardCopyOption.REPLACE_EXISTING);
    }


    /**
     * Copies a file to a new location.
     * <p>
     * This method copies the contents of the specified source file to the specified destination file. The directory
     * holding the destination file is created if it does not exist. If the destination file exists, you can overwrite
     * it if you use {@link StandardCopyOption#REPLACE_EXISTING}.
     * </p>
     *
     * @param srcFile     an existing file to copy, must not be {@code null}.
     * @param destFile    the new file, must not be {@code null}.
     * @param copyOptions options specifying how the copy should be done, for example {@link StandardCopyOption}.
     * @throws NullPointerException     if any of the given {@link File}s are {@code null}.
     * @throws FileNotFoundException    if the source does not exist.
     * @throws IllegalArgumentException if source is not a file.
     * @throws IOException              if an I/O error occurs.
     * @see StandardCopyOption
     * @since 2.9.0
     */
    public static void copyFile(final File srcFile, final File destFile, final CopyOption... copyOptions) throws IOException {
        copyFile(srcFile, destFile, true, copyOptions);
    }

    /**
     * Copies the contents of a file to a new location.
     * <p>
     * This method copies the contents of the specified source file to the specified destination file. The directory
     * holding the destination file is created if it does not exist. If the destination file exists, you can overwrite
     * it with {@link StandardCopyOption#REPLACE_EXISTING}.
     * </p>
     *
     * <p>
     * By default, a symbolic link is resolved before copying so the new file is not a link.
     * To copy symbolic links as links, you can pass {@code LinkOption.NO_FOLLOW_LINKS} as the last argument.
     * </p>
     *
     * <p>
     * <strong>Note:</strong> Setting {@code preserveFileDate} to {@code true} tries to preserve the file's last
     * modified date/times using {@link BasicFileAttributeView#setTimes(FileTime, FileTime, FileTime)}. However, it is
     * not guaranteed that the operation will succeed. If the modification operation fails, it falls back to
     * {@link File#setLastModified(long)}, and if that fails, the method throws IOException.
     * </p>
     *
     * @param srcFile          an existing file to copy, must not be {@code null}.
     * @param destFile         the new file, must not be {@code null}.
     * @param preserveFileDate true if the file date of the copy should be the same as the original.
     * @param copyOptions      options specifying how the copy should be done, for example {@link StandardCopyOption}.
     * @throws NullPointerException     if any of the given {@link File}s are {@code null}.
     * @throws FileNotFoundException    if the source does not exist.
     * @throws IllegalArgumentException if {@code srcFile} or {@code destFile} is not a file
     * @throws IOException              if the output file length is not the same as the input file length after the copy completes.
     * @throws IOException              if an I/O error occurs, setting the last-modified time didn't succeed,
     *                                  or the destination is not writable
     * @see #copyFileToDirectory(File, File, boolean)
     * @since 2.8.0
     */
    public static void copyFile(final File srcFile, final File destFile, final boolean preserveFileDate, final CopyOption... copyOptions) throws IOException {
        requireNonNull(destFile, "destination");
        checkFileExists(srcFile, "srcFile");
        requireCanonicalPathsNotEquals(srcFile, destFile);
        createParentDirectories(destFile);
        if (destFile.exists()) {
            checkFileExists(destFile, "destFile");
        }

        final Path srcPath = srcFile.toPath();

        Files.copy(srcPath, destFile.toPath(), copyOptions);

        // On Windows, the last modified time is copied by default.
        if (preserveFileDate && !Files.isSymbolicLink(srcPath) && !setTimes(srcFile, destFile)) {
            throw new IOException("Cannot set the file time.");
        }
    }


    /**
     * Copies a file to a directory preserving the file date.
     * <p>
     * This method copies the contents of the specified source file to a file of the same name in the specified
     * destination directory. The destination directory is created if it does not exist. If the destination file exists,
     * then this method will overwrite it.
     * </p>
     * <p>
     * <strong>Note:</strong> This method tries to preserve the file's last modified date/times using
     * {@link BasicFileAttributeView#setTimes(FileTime, FileTime, FileTime)}. However, it is not guaranteed that the
     * operation will succeed. If the modification operation fails it falls back to
     * {@link File#setLastModified(long)} and if that fails, the method throws IOException.
     * </p>
     *
     * @param srcFile an existing file to copy, must not be {@code null}.
     * @param destDir the directory to place the copy in, must not be {@code null}.
     * @throws NullPointerException     if any of the given {@link File}s are {@code null}.
     * @throws IllegalArgumentException if source or destination is invalid.
     * @throws IOException              if an error occurs or setting the last-modified time didn't succeed.
     * @see #copyFile(File, File, boolean)
     */
    public static void copyFileToDirectory(final File srcFile, final File destDir) throws IOException {
        copyFileToDirectory(srcFile, destDir, true);
    }

    /**
     * Copies a whole directory to a new location.
     * <p>
     * This method copies the contents of the specified source directory to within the specified destination directory.
     * </p>
     * <p>
     * The destination directory is created if it does not exist. If the destination directory does exist, then this
     * method merges the source with the destination, with the source taking precedence.
     * </p>
     * <p>
     * <strong>Note:</strong> Setting {@code preserveFileDate} to {@code true} tries to preserve the files' last
     * modified date/times using {@link File#setLastModified(long)}. However it is not guaranteed that those operations
     * will succeed. If the modification operation fails, the method throws IOException.
     * </p>
     *
     * @param srcDir           an existing directory to copy, must not be {@code null}.
     * @param destDir          the new directory, must not be {@code null}.
     * @param preserveFileDate true if the file date of the copy should be the same as the original.
     * @throws IllegalArgumentException if {@code srcDir} exists but is not a directory, or
     *                                  the source and the destination directory are the same
     * @throws FileNotFoundException    if the source does not exist.
     * @throws IOException              if an error occurs, the destination is not writable, or setting the last-modified time didn't succeed
     * @since 1.1
     */
    public static void copyDirectory(final File srcDir, final File destDir, final boolean preserveFileDate)
            throws IOException {
        copyDirectory(srcDir, destDir, null, preserveFileDate);
    }


    /**
     * Copies a filtered directory to a new location.
     * <p>
     * This method copies the contents of the specified source directory to within the specified destination directory.
     * </p>
     * <p>
     * The destination directory is created if it does not exist. If the destination directory does exist, then this
     * method merges the source with the destination, with the source taking precedence.
     * </p>
     * <p>
     * <strong>Note:</strong> Setting {@code preserveFileDate} to {@code true} tries to preserve the file's last
     * modified date/times using {@link BasicFileAttributeView#setTimes(FileTime, FileTime, FileTime)}. However, it is
     * not guaranteed that the operation will succeed. If the modification operation fails it falls back to
     * {@link File#setLastModified(long)}. If that fails, the method throws IOException.
     * </p>
     * <b>Example: Copy directories only</b>
     *
     * <pre>
     * // only copy the directory structure
     * FileUtils.copyDirectory(srcDir, destDir, DirectoryFileFilter.DIRECTORY, false);
     * </pre>
     *
     * <b>Example: Copy directories and txt files</b>
     *
     * <pre>
     * // Create a filter for ".txt" files
     * IOFileFilter txtSuffixFilter = FileFilterUtils.suffixFileFilter(".txt");
     * IOFileFilter txtFiles = FileFilterUtils.andFileFilter(FileFileFilter.INSTANCE, txtSuffixFilter);
     *
     * // Create a filter for either directories or ".txt" files
     * FileFilter filter = FileFilterUtils.orFileFilter(DirectoryFileFilter.DIRECTORY, txtFiles);
     *
     * // Copy using the filter
     * FileUtils.copyDirectory(srcDir, destDir, filter, false);
     * </pre>
     *
     * @param srcDir           an existing directory to copy, must not be {@code null}.
     * @param destDir          the new directory, must not be {@code null}.
     * @param filter           the filter to apply, null means copy all directories and files.
     * @param preserveFileDate true if the file date of the copy should be the same as the original.
     * @throws NullPointerException     if any of the given {@link File}s are {@code null}.
     * @throws IllegalArgumentException if {@code srcDir} exists but is not a directory,
     *                                  the source and the destination directory are the same, or the destination is not writable
     * @throws FileNotFoundException    if the source does not exist.
     * @throws IOException              if an error occurs or setting the last-modified time didn't succeed.
     * @since 1.4
     */
    public static void copyDirectory(final File srcDir, final File destDir, final FileFilter filter, final boolean preserveFileDate) throws IOException {
        copyDirectory(srcDir, destDir, filter, preserveFileDate, StandardCopyOption.REPLACE_EXISTING, LinkOption.NOFOLLOW_LINKS);
    }


    /**
     * Copies a filtered directory to a new location.
     * <p>
     * This method copies the contents of the specified source directory to within the specified destination directory.
     * </p>
     * <p>
     * The destination directory is created if it does not exist. If the destination directory does exist, then this
     * method merges the source with the destination, with the source taking precedence.
     * </p>
     * <p>
     * <strong>Note:</strong> Setting {@code preserveFileDate} to {@code true} tries to preserve the file's last
     * modified date/times using {@link BasicFileAttributeView#setTimes(FileTime, FileTime, FileTime)}. However, it is
     * not guaranteed that the operation will succeed. If the modification operation fails it falls back to
     * {@link File#setLastModified(long)}. If that fails, the method throws IOException.
     * </p>
     * <b>Example: Copy directories only</b>
     *
     * <pre>
     * // only copy the directory structure
     * FileUtils.copyDirectory(srcDir, destDir, DirectoryFileFilter.DIRECTORY, false);
     * </pre>
     *
     * <b>Example: Copy directories and txt files</b>
     *
     * <pre>
     * // Create a filter for ".txt" files
     * IOFileFilter txtSuffixFilter = FileFilterUtils.suffixFileFilter(".txt");
     * IOFileFilter txtFiles = FileFilterUtils.andFileFilter(FileFileFilter.INSTANCE, txtSuffixFilter);
     *
     * // Create a filter for either directories or ".txt" files
     * FileFilter filter = FileFilterUtils.orFileFilter(DirectoryFileFilter.DIRECTORY, txtFiles);
     *
     * // Copy using the filter
     * FileUtils.copyDirectory(srcDir, destDir, filter, false);
     * </pre>
     *
     * @param srcDir           an existing directory to copy, must not be {@code null}
     * @param destDir          the new directory, must not be {@code null}
     * @param fileFilter       the filter to apply, null means copy all directories and files
     * @param preserveFileDate true if the file date of the copy should be the same as the original
     * @param copyOptions      options specifying how the copy should be done, for example {@link StandardCopyOption}.
     * @throws NullPointerException     if any of the given {@link File}s are {@code null}.
     * @throws IllegalArgumentException if {@code srcDir} exists but is not a directory, or
     *                                  the source and the destination directory are the same
     * @throws FileNotFoundException    if the source does not exist.
     * @throws IOException              if an error occurs, the destination is not writable, or setting the last-modified time didn't succeed
     */
    public static void copyDirectory(final File srcDir, final File destDir, final FileFilter fileFilter, final boolean preserveFileDate,
            final CopyOption... copyOptions) throws IOException {
        requireNonNull(destDir, "destination");
        requireDirectoryExists(srcDir, "srcDir");
        requireCanonicalPathsNotEquals(srcDir, destDir);

        // Cater for destination being directory within the source directory (see IO-141)
        List<String> exclusionList = null;
        final String srcDirCanonicalPath = srcDir.getCanonicalPath();
        final String destDirCanonicalPath = destDir.getCanonicalPath();
        if (destDirCanonicalPath.startsWith(srcDirCanonicalPath)) {
            final File[] srcFiles = listFiles(srcDir, fileFilter);
            if (srcFiles.length > 0) {
                exclusionList = new ArrayList<>(srcFiles.length);
                for (final File srcFile : srcFiles) {
                    exclusionList.add(new File(destDir, srcFile.getName()).getCanonicalPath());
                }
            }
        }
        doCopyDirectory(srcDir, destDir, fileFilter, exclusionList, preserveFileDate, copyOptions);
    }


    /**
     * Internal copy directory method. Creates all destination parent directories,
     * including any necessary but non-existent parent directories.
     *
     * @param srcDir          the validated source directory, must not be {@code null}.
     * @param destDir         the validated destination directory, must not be {@code null}.
     * @param fileFilter      the filter to apply, null means copy all directories and files.
     * @param exclusionList   List of files and directories to exclude from the copy, may be null.
     * @param preserveDirDate preserve the directories last modified dates.
     * @param copyOptions     options specifying how the copy should be done, see {@link StandardCopyOption}.
     * @throws IOException              if the directory was not created along with all its parent directories.
     * @throws IllegalArgumentException if {@code destDir} is not writable
     * @throws SecurityException        See {@link File#mkdirs()}.
     */
    private static void doCopyDirectory(final File srcDir, final File destDir, final FileFilter fileFilter, final List<String> exclusionList,
            final boolean preserveDirDate, final CopyOption... copyOptions) throws IOException {
        // recurse dirs, copy files.
        final File[] srcFiles = listFiles(srcDir, fileFilter);
        requireDirectoryIfExists(destDir, "destDir");
        mkdirs(destDir);
        for (final File srcFile : srcFiles) {
            final File dstFile = new File(destDir, srcFile.getName());
            if (exclusionList == null || !exclusionList.contains(srcFile.getCanonicalPath())) {
                if (srcFile.isDirectory()) {
                    doCopyDirectory(srcFile, dstFile, fileFilter, exclusionList, preserveDirDate, copyOptions);
                } else {
                    copyFile(srcFile, dstFile, preserveDirDate, copyOptions);
                }
            }
        }
        // Do this last, as the above has probably affected directory metadata
        if (preserveDirDate) {
            setTimes(srcDir, destDir);
        }
    }


    /**
     * Lists files in a directory, asserting that the supplied directory exists and is a directory.
     *
     * @param directory  The directory to list
     * @param fileFilter Optional file filter, may be null.
     * @return The files in the directory, never {@code null}.
     * @throws NullPointerException     if directory is {@code null}.
     * @throws IllegalArgumentException if {@link directory} exists but is not a directory
     * @throws IOException              if an I/O error occurs.
     */
    private static File[] listFiles(final File directory, final FileFilter fileFilter) throws IOException {
        requireDirectoryExists(directory, "directory");
        final File[] files = fileFilter == null ? directory.listFiles() : directory.listFiles(fileFilter);
        if (files == null) {
            // null if the directory does not denote a directory, or if an I/O error occurs.
            throw new IOException("Unknown I/O error listing contents of directory: " + directory);
        }
        return files;
    }

    /**
     * Requires that the given {@link File} exists and is a directory.
     *
     * @param directory The {@link File} to check.
     * @param name      The parameter name to use in the exception message in case of null input or if the file is not a directory.
     * @throws NullPointerException     if the given {@link File} is {@code null}.
     * @throws FileNotFoundException    if the given {@link File} does not exist
     * @throws IllegalArgumentException if the given {@link File} exists but is not a directory.
     */
    private static void requireDirectoryExists(final File directory, final String name) throws FileNotFoundException {
        requireNonNull(directory, name);
        if (!directory.isDirectory()) {
            if (directory.exists()) {
                throw new IllegalArgumentException("Parameter '" + name + "' is not a directory: '" + directory + "'");
            }
            throw new FileNotFoundException("Directory '" + directory + "' does not exist.");
        }
    }

    /**
     * Copies a file to a directory optionally preserving the file date.
     * <p>
     * This method copies the contents of the specified source file to a file of the same name in the specified
     * destination directory. The destination directory is created if it does not exist. If the destination file exists,
     * then this method will overwrite it.
     * </p>
     * <p>
     * <strong>Note:</strong> Setting {@code preserveFileDate} to {@code true} tries to preserve the file's last
     * modified date/times using {@link BasicFileAttributeView#setTimes(FileTime, FileTime, FileTime)}. However, it is
     * not guaranteed that the operation will succeed. If the modification operation fails it falls back to
     * {@link File#setLastModified(long)} and if that fails, the method throws IOException.
     * </p>
     *
     * @param sourceFile       an existing file to copy, must not be {@code null}.
     * @param destinationDir   the directory to place the copy in, must not be {@code null}.
     * @param preserveFileDate true if the file date of the copy should be the same as the original.
     * @throws NullPointerException if any of the given {@link File}s are {@code null}.
     * @throws IOException          if an error occurs or setting the last-modified time didn't succeed.
     * @throws IOException          if the output file length is not the same as the input file length after the copy completes.
     * @see #copyFile(File, File, CopyOption...)
     * @since 1.3
     */
    public static void copyFileToDirectory(final File sourceFile, final File destinationDir, final boolean preserveFileDate) throws IOException {
        requireNonNull(sourceFile, "sourceFile");
        requireDirectoryIfExists(destinationDir, "destinationDir");
        copyFile(sourceFile, new File(destinationDir, sourceFile.getName()), preserveFileDate);
    }

    /**
     * Requires that the given {@link File} is a directory if it exists.
     *
     * @param directory The {@link File} to check.
     * @param name      The parameter name to use in the exception message in case of null input.
     * @throws NullPointerException     if the given {@link File} is {@code null}.
     * @throws IllegalArgumentException if the given {@link File} exists but is not a directory.
     */
    private static void requireDirectoryIfExists(final File directory, final String name) {
        requireNonNull(directory, name);
        if (directory.exists() && !directory.isDirectory()) {
            throw new IllegalArgumentException("Parameter '" + name + "' is not a directory: '" + directory + "'");
        }
    }


    /**
     * Sets file lastModifiedTime, lastAccessTime and creationTime to match source file
     *
     * @param sourceFile The source file to query.
     * @param targetFile The target file or directory to set.
     * @return {@code true} if and only if the operation succeeded;
     * {@code false} otherwise
     * @throws NullPointerException if sourceFile is {@code null}.
     * @throws NullPointerException if targetFile is {@code null}.
     */
    private static boolean setTimes(final File sourceFile, final File targetFile) {
        requireNonNull(sourceFile, "sourceFile");
        requireNonNull(targetFile, "targetFile");
        try {
            // Set creation, modified, last accessed to match source file
            final BasicFileAttributes srcAttr = Files.readAttributes(sourceFile.toPath(), BasicFileAttributes.class);
            final BasicFileAttributeView destAttrView = Files.getFileAttributeView(targetFile.toPath(), BasicFileAttributeView.class);
            // null guards are not needed; BasicFileAttributes.setTimes(...) is null safe
            destAttrView.setTimes(srcAttr.lastModifiedTime(), srcAttr.lastAccessTime(), srcAttr.creationTime());
            return true;
        } catch (final IOException ignored) {
            // Fallback: Only set modified time to match source file
            return targetFile.setLastModified(sourceFile.lastModified());
        }

        // TODO: (Help!) Determine historically why setLastModified(File, File) needed PathUtils.setLastModifiedTime() if
        //  sourceFile.isFile() was true, but needed setLastModifiedTime(File, long) if sourceFile.isFile() was false
    }


    /**
     * Throws IllegalArgumentException if the given files' canonical representations are equal.
     *
     * @param file1 The first file to compare.
     * @param file2 The second file to compare.
     * @throws IOException              if an I/O error occurs.
     * @throws IllegalArgumentException if the given files' canonical representations are equal.
     */
    private static void requireCanonicalPathsNotEquals(final File file1, final File file2) throws IOException {
        final String canonicalPath = file1.getCanonicalPath();
        if (canonicalPath.equals(file2.getCanonicalPath())) {
            throw new IllegalArgumentException(String
                    .format("File canonical paths are equal: '%s' (file1='%s', file2='%s')", canonicalPath, file1, file2));
        }
    }


    /**
     * Requires that the given {@link File} object
     * points to an actual file (not a directory) in the file system,
     * and throws a {@link FileNotFoundException} if it doesn't.
     * It throws an IllegalArgumentException if the object points to a directory.
     *
     * @param file The {@link File} to check.
     * @param name The parameter name to use in the exception message.
     * @throws FileNotFoundException    if the file does not exist
     * @throws NullPointerException     if the given {@link File} is {@code null}.
     * @throws IllegalArgumentException if the given {@link File} is not a file.
     */
    private static void checkFileExists(final File file, final String name) throws FileNotFoundException {
        requireNonNull(file, name);
        if (!file.isFile()) {
            if (file.exists()) {
                throw new IllegalArgumentException("Parameter '" + name + "' is not a file: " + file);
            }
            if (!Files.isSymbolicLink(file.toPath())) {
                throw new FileNotFoundException("Source '" + file + "' does not exist");
            }
        }
    }

    /**
     * Converts from a {@link URL} to a {@link File}.
     * <p>
     * Syntax such as {@code file:///my%20docs/file.txt} will be
     * correctly decoded to {@code /my docs/file.txt}.
     * UTF-8 is used to decode percent-encoded octets to characters.
     * Additionally, malformed percent-encoded octets are handled leniently by
     * passing them through literally.
     * </p>
     *
     * @param url the file URL to convert, {@code null} returns {@code null}
     * @return the equivalent {@link File} object, or {@code null}
     * if the URL's protocol is not {@code file}
     */
    public static File toFile(final URL url) {
        if (url == null || !isFileProtocol(url)) {
            return null;
        }
        final String fileName = url.getFile().replace('/', File.separatorChar);
        return new File(decodeUrl(fileName));
    }

    /**
     * Tests whether the given URL is a file URL.
     *
     * @param url The URL to test.
     * @return Whether the given URL is a file URL.
     */
    private static boolean isFileProtocol(final URL url) {
        return PROTOCOL_FILE.equalsIgnoreCase(url.getProtocol());
    }

    /**
     * Decodes the specified URL as per RFC 3986, transforming
     * percent-encoded octets to characters by decoding with the UTF-8 character
     * set. This function is primarily intended for usage with
     * {@link java.net.URL} which unfortunately does not enforce proper URLs. As
     * such, this method will leniently accept invalid characters or malformed
     * percent-encoded octets and simply pass them literally through to the
     * result string. Except for rare edge cases, this will make unencoded URLs
     * pass through unaltered.
     *
     * @param url The URL to decode, may be {@code null}.
     * @return The decoded URL or {@code null} if the input was
     * {@code null}.
     */
    static String decodeUrl(final String url) {
        String decoded = url;
        if (url != null && url.indexOf('%') >= 0) {
            final int n = url.length();
            final StringBuilder builder = new StringBuilder();
            final ByteBuffer byteBuffer = ByteBuffer.allocate(n);
            for (int i = 0; i < n; ) {
                if (url.charAt(i) == '%') {
                    try {
                        do {
                            final byte octet = (byte) Integer.parseInt(url.substring(i + 1, i + 3), 16);
                            byteBuffer.put(octet);
                            i += 3;
                        } while (i < n && url.charAt(i) == '%');
                        continue;
                    } catch (final IndexOutOfBoundsException | NumberFormatException ignored) {
                        // malformed percent-encoded octet, fall through and
                        // append characters literally
                    } finally {
                        if (byteBuffer.position() > 0) {
                            byteBuffer.flip();
                            builder.append(StandardCharsets.UTF_8.decode(byteBuffer).toString());
                            byteBuffer.clear();
                        }
                    }
                }
                builder.append(url.charAt(i++));
            }
            decoded = builder.toString();
        }
        return decoded;
    }

}
