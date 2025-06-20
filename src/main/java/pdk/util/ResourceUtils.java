package pdk.util;

import org.jspecify.annotations.Nullable;
import pdk.util.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import static pdk.util.ArgUtils.*;

/**
 * Helper class for resource.
 *
 * @author Jiawei Mao
 * @version 1.0.0
 * @since 21 Jun 2024, 7:03 PM
 */
public final class ResourceUtils {

    /**
     * Returns a {@code URL} pointing to {@code resourceName} if the resource is found using the
     * {@linkplain Thread#getContextClassLoader() context class loader}. In simple environments, the
     * context class loader will find resources from the class path. In environments where different
     * threads can have different class loaders, for example app servers, the context class loader
     * will typically have been set to an appropriate loader for the current thread.
     *
     * @param resourceName resource name for ClassLoader, should define from package name, excluding the leading `/`
     * @throws NullPointerException if the resource is not found
     */
    public static URL getResourceURL(String resourceName) {
        checkNotNull(resourceName);
        ClassLoader loader = firstNonNull(Thread.currentThread().getContextClassLoader(), ResourceUtils.class.getClassLoader());
        URL url = loader.getResource(resourceName);
        checkNotNull(url, String.format("resource %s not found", resourceName));
        return url;
    }

    /**
     * Returns a {@code URL} pointing to {@code resourceName} if the resource is found using given {@link ClassLoader}
     *
     * @param classLoader  {@link ClassLoader} used to load resource
     * @param resourceName resource name, should define from package name, excluding the leading `/`
     * @throws IllegalArgumentException if the resource is not found
     */
    public static URL getResourceURL(ClassLoader classLoader, String resourceName) {
        checkNotNull(resourceName);
        URL url = classLoader.getResource(resourceName);
        checkNotNull(url, String.format("resource %s not found.", resourceName));
        return url;
    }

    /**
     * Given a {@code resourceName} that is relative to {@code contextClass}, returns a {@code URL}
     * pointing to the named resource.
     *
     * @param resourceName resource name, <b>absolute path</b> should be start with '/', relative path only for resource in the same package
     * @throws IllegalArgumentException if the resource is not found
     */
    public static URL getResourceURL(Class<?> contextClass, String resourceName) {
        checkNotNull(resourceName);

        URL url = contextClass.getResource(resourceName);
        checkNotNull(url, String.format("resource %s relative to %s not found.",
                resourceName, contextClass.getName()));
        return url;
    }

    /**
     * Return resource path
     *
     * @param contextClass context class to define the relative path
     * @param resourceName resource name, <b>absolute path</b> should be start with '/', relative path only for resource in the same package
     */
    public static @Nullable File getResourceFile(Class<?> contextClass, String resourceName) {
        URL url = getResourceURL(contextClass, resourceName);
        return FileUtils.toFile(url);
    }

    /**
     * Returns a {@code URL} pointing to {@code resourceName} if the resource is found using given {@link ClassLoader}
     *
     * @param resourceName resource name, should define from <b>package name</b>, excluding the leading `/`
     * @throws IllegalArgumentException if the resource is not found
     */
    public static @Nullable File getResourceFile(ClassLoader classLoader, String resourceName) {
        URL resourceURL = getResourceURL(classLoader, resourceName);
        return FileUtils.toFile(resourceURL);
    }

    /**
     * Returns an input stream for reading the specified resource.
     *
     * @param name The resource name, should define from <b>package name</b>, excluding the leading `/`
     * @return AAn input stream for reading the resource; {@code null} if an I/O exception occurs
     * @throws NullPointerException If {@code name} is {@code null}
     */
    public static @Nullable InputStream getResourceStream(ClassLoader classLoader, String name) {
        checkNotNull(name);
        URL url = classLoader.getResource(name);
        checkNotNull(url, String.format("resource %s not found.", name));
        try {
            return url.openStream();
        } catch (IOException e) {
            return null;
        }
    }


    /**
     * Returns an input stream for reading the specified resource.
     *
     * @param contextClass context class to define the path
     * @param name         resource name, <b>absolute path</b> should be start with '/', relative path only for resource in the same package
     * @return An input stream for reading the resource; {@code null} if an I/O exception occurs
     */
    public static @Nullable InputStream getResourceStream(Class<?> contextClass, String name) {
        checkNotNull(name);

        URL url = contextClass.getResource(name);
        checkNotNull(url, String.format("resource %s relative to %s not found.",
                name, contextClass.getName()));
        try {
            return url.openStream();
        } catch (IOException e) {
            return null;
        }
    }
}
