package pdk.util;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

/**
 * Class for task provide progress information
 *
 * @author Jiawei Mao
 * @version 2.0.0
 * @since 21 Jun 2024, 5:01 PM
 */
public abstract class InfoTask<V> {

    //<editor-fold desc="Property names">
    private static final String TITLE = "title";
    private static final String MESSAGE = "message";
    private static final String PROGRESS = "progress";
    private static final String STOPPED = "stopped";
    private static final String VALUE = "value";
    private static final String EXCEPTION = "exception";
    //</editor-fold>

    private String title_ = null;
    private String message_ = null;
    private double progress_ = -1;
    private boolean stopped_ = false;
    private Throwable exception_ = null;
    private V value_ = null;

    protected final PropertyChangeSupport pcs_ = new PropertyChangeSupport(this);

    /**
     * An optional title that should be associated with this Worker.
     *
     * @return the current title
     */
    public final String getTitle() {
        return title_;
    }

    /**
     * Updates the <code>title</code> property.
     *
     * @param title the new title
     */
    public final void updateTitle(String title) {
        String oldTitle = title_;
        title_ = title;
        pcs_.firePropertyChange(TITLE, oldTitle, this.title_);
    }

    /**
     * Update title without firing event.
     *
     * @param title new title
     */
    public final void updateTitleQuietly(String title) {
        this.title_ = title;
    }

    /**
     * Add a {@link PropertyChangeListener} for the {@code title} property.
     *
     * @param listener {@link PropertyChangeListener} instance.
     */
    public final void addTitleListener(PropertyChangeListener listener) {
        pcs_.addPropertyChangeListener(TITLE, listener);
    }

    /**
     * Remove a {@link PropertyChangeListener} for the title property.
     *
     * @param listener {@link PropertyChangeListener} instance.
     */
    public final void removeTitleListener(PropertyChangeListener listener) {
        pcs_.removePropertyChangeListener(TITLE, listener);
    }

    /**
     * Gets a message associated with the current state of this Worker. This may
     * be something such as "Processing image 1 of 3", for example.
     *
     * @return the current message
     */
    public final String getMessage() {
        return message_;
    }

    /**
     * Updates the <code>message</code> property.
     *
     * @param message the new message
     */
    public final void updateMessage(String message) {
        String oldMsg = this.message_;
        this.message_ = message;
        pcs_.firePropertyChange(MESSAGE, oldMsg, this.message_);
    }

    /**
     * Set message property without firing event.
     *
     * @param message new message
     */
    public final void updateMessageQuietly(String message) {
        this.message_ = message;
    }

    /**
     * Add a {@link PropertyChangeListener} for the {@code message} property.
     *
     * @param listener {@link PropertyChangeListener} instance.
     */
    public final void addMessageListener(PropertyChangeListener listener) {
        pcs_.addPropertyChangeListener(MESSAGE, listener);
    }

    /**
     * Remove a {@link PropertyChangeListener} for the message property.
     *
     * @param listener {@link PropertyChangeListener}
     */
    public final void removeMessageListener(PropertyChangeListener listener) {
        pcs_.removePropertyChangeListener(MESSAGE, listener);
    }

    /**
     * Indicates the current progress of this Worker in terms of percent complete.
     * <p>
     * A value between zero and one indicates progress toward completion. A value
     * of -1 means that the current progress cannot be determined (that is, it is
     * indeterminate). This property may or may not change from its default value
     * of -1 depending on the specific Worker implementation.
     *
     * @return the current progress
     */
    public final double getProgress() {
        return progress_;
    }

    /**
     * Set the {@code progress} property
     *
     * @param progress new progress
     */
    public final void updateProgress(double progress) {
        double oldProgress = this.progress_;
        progress_ = progress;
        pcs_.firePropertyChange(PROGRESS, oldProgress, progress);
    }

    /**
     * Set the {@code progress} property.
     *
     * @param progress new progress value
     */
    public final void updateProgressQuietly(double progress) {
        this.progress_ = progress;
    }

    /**
     * Add a {@link PropertyChangeListener} for the {@code progress} property.
     *
     * @param listener {@link PropertyChangeListener} instance.
     */
    public final void addProgressListener(PropertyChangeListener listener) {
        pcs_.addPropertyChangeListener(PROGRESS, listener);
    }

    /**
     * Remove a {@link PropertyChangeListener} for the {@code progress} property.
     *
     * @param listener a {@link PropertyChangeListener} instance.
     */
    public final void removeProgressListener(PropertyChangeListener listener) {
        pcs_.removePropertyChangeListener(PROGRESS, listener);
    }

    /**
     * stop the task explicitly
     */
    public final void stop() {
        boolean old = this.stopped_;
        this.stopped_ = true;
        pcs_.firePropertyChange(STOPPED, old, true);
    }

    /**
     * Return the {@code stopped} property value
     *
     * @return true if the task is stopped.
     */
    public final boolean isStopped() {
        return stopped_;
    }

    /**
     * add a {@link PropertyChangeListener} to the stopped state
     *
     * @param listener {@link PropertyChangeListener} instance
     */
    public final void addStoppedListener(PropertyChangeListener listener) {
        pcs_.addPropertyChangeListener(STOPPED, listener);
    }

    /**
     * Remove a {@link PropertyChangeListener} for the {@code stopped} property.
     *
     * @param listener a {@link PropertyChangeListener} instance.
     */
    public final void removeStoppedListener(PropertyChangeListener listener) {
        pcs_.removePropertyChangeListener(STOPPED, listener);
    }

    /**
     * Indicates the exception which occurred while the Task was running, if any.
     * If this property value is {@code null}, there is no known exception, even if
     * the status is FAILED. If this property is not {@code null}, it will most
     * likely contain an exception that describes the cause of failure.
     *
     * @return the exception, if one occurred
     */
    public final Throwable getException() {
        return exception_;
    }

    /**
     * Set the {@code exception} property
     *
     * @param exception {@link Throwable} instance.
     */
    public final void updateException(Throwable exception) {
        Throwable oldException = this.exception_;
        this.exception_ = exception;
        pcs_.firePropertyChange(EXCEPTION, oldException, exception_);
    }

    /**
     * Set the {@code exception} property
     *
     * @param value new exception property
     */
    public final void updateExceptionQuietly(Throwable value) {
        this.exception_ = value;
    }

    /**
     * Add a {@link PropertyChangeListener} to {@code exception} property.
     *
     * @param listener {@link PropertyChangeListener} instance.
     */
    public final void addExceptionListener(PropertyChangeListener listener) {
        pcs_.addPropertyChangeListener(EXCEPTION, listener);
    }

    /**
     * Remove a {@link PropertyChangeListener} for the {@code exception} property
     *
     * @param listener a {@link PropertyChangeListener} instance.
     */
    public final void removeExceptionListener(PropertyChangeListener listener) {
        pcs_.removePropertyChangeListener(EXCEPTION, listener);
    }

    /**
     * Specifies the value, or result, of this Worker. This is set upon entering
     * the SUCCEEDED state, and cleared (set to null) if the Worker is reinitialized
     * (that is, if the Worker is a reusable Worker and is reset or restarted).
     *
     * @return the current value of this Worker
     */
    public final V getValue() {
        return value_;
    }

    /**
     * Updates the <code>value</code> property.
     *
     * @param value the new value
     */
    public final void updateValue(V value) {
        V oldValue = this.value_;
        this.value_ = value;
        pcs_.firePropertyChange(VALUE, oldValue, value);
    }

    /**
     * Set the {@code value} property
     *
     * @param value new value
     */
    public final void updateValueQuietly(V value) {
        this.value_ = value;
    }

    /**
     * Add a {@link PropertyChangeListener} to {@code value} property.
     *
     * @param listener {@link PropertyChangeListener} instance.
     */
    public final void addValueListener(PropertyChangeListener listener) {
        pcs_.addPropertyChangeListener(VALUE, listener);
    }

    /**
     * Remove a {@link PropertyChangeListener} for the {@code value} property
     *
     * @param listener a {@link PropertyChangeListener} instance.
     */
    public final void removeValueListener(PropertyChangeListener listener) {
        pcs_.removePropertyChangeListener(VALUE, listener);
    }
}
