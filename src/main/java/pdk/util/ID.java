package pdk.util;

/**
 * ID gives object a unique name within a scope.
 *
 * @author Jiawei Mao
 * @version 1.0.0
 * @since 21 Jun 2024, 4:53 PM
 */
public interface ID {

    /**
     * An id is an unambiguous string that is unique within the scope (i.e. a document, a set of related
     * documents, or a repository) of its use, the uniqueness of the id is responsible by user.
     *
     * @return a unique identifier of this object.
     */
    String getID();
}
