package pdk.util;

import java.lang.annotation.*;

/**
 * Marks a program element (type, method, constructor, or field) as part of the public API and provides metadata about
 * its stability and version of introduction.
 *
 * <p>This annotation is intended to be used by library developers to clearly communicate
 * the lifecycle and support level of API elements. It is retained at runtime for reflective access and included in
 * generated documentation.</p>
 *
 * <p>The {@link #status()} element indicates the current stability level of the element,
 * while {@link #since()} specifies the version in which the element was first introduced.</p>
 *
 * @see Status
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD, ElementType.CONSTRUCTOR, ElementType.FIELD})
public @interface API {

    /**
     * Returns the stability status of the annotated API element.
     *
     * @return the {@link Status} value indicating whether the element is stable, experimental, or deprecated
     */
    Status status();

    /**
     * Returns the version string since which the annotated API element has been available.
     *
     * <p>The format of the version string is not strictly enforced, but it is recommended
     * to use a consistent versioning scheme such as semantic versioning (e.g., "1.2.0").</p>
     *
     * @return the version string, never {@code null}
     */
    String since();

    /**
     * Enumerates the possible lifecycle states of an API element.
     */
    enum Status {
        /**
         * Indicates that the API element is stable and intended for general use. Breaking changes to this element
         * should be avoided without prior deprecation.
         */
        STABLE,
        /**
         * Indicates that the API element is experimental and may undergo significant changes or be removed in future
         * releases without prior deprecation.
         */
        EXPERIMENTAL,
        /**
         * Indicates that the API element is experimental and may undergo significant changes or be removed in future
         * releases without prior deprecation.
         */
        DEPRECATED
    }
}
