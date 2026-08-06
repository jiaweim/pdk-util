package pdk.util;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.List;
import java.util.Objects;

/**
 * A lightweight task information holder for background operations
 * <p>
 * This class provides observable properties commonly required by long-running
 * tasks, including title, message, progress, result value, exception, and
 * lifecycle state.
 * <p>
 * The task lifecycle is represented by {@link State}:
 * <pre>
 * READY
 *   |
 * start()
 *   |
 * RUNNING
 *   |
 * +-------------+-------------+
 * |             |             |
 * succeed()    fail()      cancel()
 * |             |             |
 * v             v             v
 * SUCCEEDED    FAILED     CANCELLED
 * </pre>
 *
 * <p>
 * Subclasses should call {@link #start()} before performing work and finish
 * with one of {@link #succeed(Object)}, {@link #fail(Throwable)}, or
 * {@link #cancel()}.
 *
 * <b>Threading note:</b> Property change listeners are called synchronously
 * on the thread that invokes the update methods. If you need to update UI
 * components, wrap the listener code with {@code SwingUtilities.invokeLater()}
 * or equivalent.
 *
 * <p>
 * <b>Typical usage in a background thread:</b>
 * <pre>{@code
 * class MyTask extends InfoTask<String>{
 *
 *     public void run(){
 *         start();
 *         updateTitle("Processing");
 *
 *         try {
 *             // work
 *             succeed("Done");
 *         } catch(Exception e){
 *             fail(e);
 *         }
 *     }
 * }
 *   }</pre>
 *
 * @param <V> the result type produced by this task
 * @author Jiawei Mao
 * @version 2.1.0
 * @since 21 Jun 2024
 */
public class InfoTask<V> {

    //<editor-fold desc="Property names">
    private static final String TITLE = "title";
    private static final String MESSAGE = "message";
    private static final String PROGRESS = "progress";
    private static final String VALUE = "value";
    private static final String EXCEPTION = "exception";
    private static final String STATE = "state";
    //</editor-fold>

    private static final List<String> PROPERTY_NAMES =
            List.of(
                    TITLE, MESSAGE, PROGRESS, VALUE, EXCEPTION, STATE
            );

    public enum State {

        /**
         * Task has been created but not started.
         * <p>
         * A task in this state can transition only to {@link #RUNNING}
         * through {@link InfoTask#start()}.
         */
        READY,

        /**
         * Task is currently running.
         * <p>
         * From this state the task may transition to
         * {@link #SUCCEEDED}, {@link #FAILED}, or {@link #CANCELLED}.
         */
        RUNNING,

        /**
         * The task completed successfully and produced its final result.
         * <p>
         * The result can be obtained through {@link InfoTask#getValue()}.
         */
        SUCCEEDED,

        /**
         * The task terminated because an exception occurred.
         * <p>
         * The failure cause is available through {@link InfoTask#getException()}.
         */
        FAILED,

        /**
         * The task was cancelled before normal completion.
         * <p>
         * Cancellation is requested through {@link InfoTask#cancel()}.
         */
        CANCELLED;

        public boolean isTerminal() {
            return switch (this) {
                case SUCCEEDED, FAILED, CANCELLED -> true;
                default -> false;
            };
        }
    }

    private volatile String title_ = null;
    private volatile String message_ = null;
    private volatile double progress_ = -1;
    private volatile Throwable exception_ = null;
    private volatile V value_ = null;
    private volatile State state_ = State.READY;

    private final PropertyChangeSupport pcs_ = new PropertyChangeSupport(this);

    /**
     * Registers a general property change listener that will be notified
     * when any bound property changes.
     *
     * @param listener the listener to add
     */
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        pcs_.addPropertyChangeListener(listener);
    }

    /**
     * Removes a general property change listener previously registered
     * via {@link #addPropertyChangeListener}.
     *
     * @param listener the listener to remove
     */
    public void removePropertyChangeListener(PropertyChangeListener listener) {
        pcs_.removePropertyChangeListener(listener);
    }

    /**
     * Returns the current lifecycle state of this task.
     *
     * @return the current {@link State}
     */
    public final State getState() {
        return state_;
    }

    /**
     * Updates the task state and notifies registered listeners.
     *
     * @param state the new task state
     */
    private void updateState(State state) {
        State oldState = this.state_;
        if (oldState == state) {
            return;
        }

        this.state_ = state;
        pcs_.firePropertyChange(STATE, oldState, state);
    }

    /**
     * Registers a {@link PropertyChangeListener} that will be notified when the
     * {@code state} property changes.
     *
     * <p>
     * The listener receives {@link java.beans.PropertyChangeEvent} instances where
     * the old and new values are {@link State} objects representing the previous
     * and current lifecycle states of this task.
     *
     * @param listener the listener to add
     */
    public final void addStateListener(
            PropertyChangeListener listener) {
        pcs_.addPropertyChangeListener(STATE, listener);
    }

    /**
     * Removes a previously registered {@link PropertyChangeListener} for the
     * {@code state} property.
     *
     * <p>
     * If the specified listener was not registered, this method has no effect.
     *
     * @param listener the listener to remove
     */
    public final void removeStateListener(
            PropertyChangeListener listener) {
        pcs_.removePropertyChangeListener(STATE, listener);
    }

    /**
     * Starts this task.
     *
     * <p>
     * A task can only be started once. This method changes the state from
     * {@link State#READY} to {@link State#RUNNING}.
     *
     * @throws IllegalStateException if this task has already been started
     */
    public final synchronized void start() {
        if (state_ != State.READY) {
            throw new IllegalStateException("Task already started");
        }
        updateState(State.RUNNING);
    }

    /**
     * An optional title that should be associated with this task.
     *
     * @return the current title
     */
    public final String getTitle() {
        return title_;
    }

    /**
     * Updates the {@code title} property.
     *
     * @param title the new title
     */
    public final void updateTitle(String title) {
        if (Objects.equals(title, title_)) {
            return;
        }
        String oldTitle = title_;
        title_ = title;
        pcs_.firePropertyChange(TITLE, oldTitle, this.title_);
    }

    /**
     * Updates the title without notifying registered listeners.
     *
     * <p>
     * This method should be used only when event notification is intentionally
     * suppressed.
     *
     * @param title the new title
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
     * Gets a message associated with the current state of this task. This may
     * be something such as "Processing image 1 of 3", for example.
     *
     * @return the current message
     */
    public final String getMessage() {
        return message_;
    }

    /**
     * Updates the {@code message} property.
     *
     * @param message the new message
     */
    public final void updateMessage(String message) {
        if (Objects.equals(this.message_, message)) {
            return;
        }
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
     * Indicates the current progress of this task in terms of percent complete.
     * <p>
     * A value between zero and one indicates progress toward completion. A value
     * of -1 means that the current progress cannot be determined (that is, it is
     * indeterminate). This property may or may not change from its default value
     * of -1 depending on the specific task implementation.
     *
     * @return the current progress
     */
    public final double getProgress() {
        return progress_;
    }

    /**
     * Sets the {@code progress} property.
     *
     * @param progress the new progress value; must be between -1 (indeterminate)
     *                 and 1 (complete). Values outside this range are rejected.
     * @throws IllegalArgumentException if progress is not in [-1, 1]
     */
    public final void updateProgress(double progress) {
        if (Double.isNaN(progress) || progress < -1 || progress > 1) {
            throw new IllegalArgumentException("Progress must be in range [-1,1]");
        }
        if (Double.compare(this.progress_, progress) == 0) {
            return;
        }
        double oldProgress = this.progress_;
        progress_ = progress;
        pcs_.firePropertyChange(PROGRESS, oldProgress, progress);
    }

    /**
     * Convenience method to set progress based on work done vs total work.
     * <p>
     * If {@code totalWork} is less than or equal to zero, the progress is
     * set to -1, indicating an indeterminate state.
     *
     * @param workDone  the amount of work already completed
     * @param totalWork the total amount of work; must be > 0 for determinate progress
     */
    public final void updateProgress(int workDone, int totalWork) {
        if (totalWork <= 0) {
            updateProgress(-1);
            return;
        }
        double progress = Math.clamp(workDone / (double) totalWork, 0.0, 1.0);
        updateProgress(progress);
    }

    /**
     * Set the {@code progress} property.
     *
     * @param progress new progress value
     */
    public final void updateProgressQuietly(double progress) {
        if (Double.isNaN(progress) || progress < -1 || progress > 1) {
            throw new IllegalArgumentException("Progress must be in range [-1,1]");
        }
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
     * Cancels this task.
     * <p>
     * Cancellation is only effective while the task is in the
     * {@link State#RUNNING} state. If the task is not in {@link State#RUNNING},
     * this method does nothing. Otherwise, the state changes to {@link State#CANCELLED}.
     *
     * @return {@code true} if the task was successfully cancelled;
     * {@code false} if the task was not in {@link State#RUNNING}.
     */
    public final synchronized boolean cancel() {
        if (state_ != State.RUNNING) {
            return false;
        }

        updateState(State.CANCELLED);
        return true;
    }

    /**
     * Returns whether this task has entered the {@link State#CANCELLED}
     * state.
     *
     * @return {@code true} if this task was cancelled
     */
    public final boolean isCancelled() {
        return state_ == State.CANCELLED;
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
     * @param throwable {@link Throwable} instance.
     */
    public final void updateException(Throwable throwable) {
        if (Objects.equals(throwable, exception_)) {
            return;
        }
        Throwable oldException = this.exception_;
        this.exception_ = throwable;
        pcs_.firePropertyChange(EXCEPTION, oldException, exception_);
    }

    /**
     * Marks this task as failed.
     *
     * <p>
     * The supplied exception is stored as the failure cause and the task state
     * changes from {@link State#RUNNING} to {@link State#FAILED}.
     *
     * @param throwable the exception that caused the failure
     * @throws IllegalStateException if the task is not running
     */
    public final synchronized void fail(Throwable throwable) {
        if (state_ != State.RUNNING) {
            throw new IllegalStateException("Task is not running");
        }
        updateException(throwable);
        updateState(State.FAILED);
    }

    /**
     * Sets the {@code exception} property without firing an event.
     *
     * @param throwable the new exception, may be {@code null}
     */
    public final void updateExceptionQuietly(Throwable throwable) {
        this.exception_ = throwable;
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
     * Gets the result value associated with this task.
     *
     * <p>
     * The value is normally assigned when the task enters
     * {@link State#SUCCEEDED}.
     */
    public final V getValue() {
        return value_;
    }

    /**
     * Updates the {@code value} property.
     *
     * @param value the new value
     */
    public final void updateValue(V value) {
        if (Objects.equals(value, value_)) {
            return;
        }
        V oldValue = this.value_;
        this.value_ = value;
        pcs_.firePropertyChange(VALUE, oldValue, value);
    }

    /**
     * Completes this task successfully.
     *
     * <p>
     * This method stores the result value, sets progress to {@code 1.0}, and
     * changes the state from {@link State#RUNNING} to {@link State#SUCCEEDED}.
     *
     * @param value the final result value
     * @throws IllegalStateException if the task is not running
     */
    public final synchronized void succeed(V value) {
        if (state_ != State.RUNNING) {
            throw new IllegalStateException("Task is not running");
        }

        updateValue(value);
        updateProgress(1.0);
        updateState(State.SUCCEEDED);
    }

    /**
     * Completes this task successfully without a result value.
     *
     * <p>
     * This method is intended for tasks whose completion does not produce
     * a meaningful result. It changes the state from {@link State#RUNNING}
     * to {@link State#SUCCEEDED} and sets progress to {@code 1.0}.
     *
     * @throws IllegalStateException if the task is not running
     */
    public final synchronized void succeed() {
        succeed(null);
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

    /**
     * Resets this task to its initial state.
     *
     * <p>
     * All task properties are cleared and the lifecycle state is changed to
     * {@link State#READY}.
     *
     * <p>
     * This method is intended for reusable task instances.
     */
    public final synchronized void reset() {
        if (state_ == State.RUNNING) {
            throw new IllegalStateException("Cannot reset running task");
        }
        updateTitle(null);
        updateMessage(null);
        updateProgress(-1);
        updateException(null);
        updateValue(null);
        updateState(State.READY);
    }

    /**
     * Returns whether this task is currently running.
     *
     * @return {@code true} if state is {@link State#RUNNING}
     */
    public final boolean isRunning() {
        return state_ == State.RUNNING;
    }

    /**
     * Returns whether this task has reached a terminal state.
     *
     * @return {@code true} if the task is completed, failed, or cancelled
     */
    public final boolean isDone() {
        return state_.isTerminal();
    }

    /**
     * Removes all property change listeners that have been registered
     * on this task, both general and property-specific ones.
     * <p>
     * Call this when the task is no longer needed (e.g., after completion
     * or when the owning component is disposed) to prevent memory leaks.
     */
    public final void clearListeners() {
        // 1. 移除所有无属性名的通用监听器
        for (PropertyChangeListener l : pcs_.getPropertyChangeListeners()) {
            pcs_.removePropertyChangeListener(l);
        }
        // 2. 移除所有注册在特定属性上的监听器
        //    属性名与类中常量保持一致
        for (String prop : PROPERTY_NAMES) {
            for (PropertyChangeListener l : pcs_.getPropertyChangeListeners(prop)) {
                pcs_.removePropertyChangeListener(prop, l);
            }
        }
    }
}
