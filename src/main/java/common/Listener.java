package common;

public interface Listener {
    void sessionOpened(Session session) throws Throwable;

    void sessionClosed(Session session) throws Throwable;

    void messageReceived(Session session, Object message) throws Throwable;

    default void failedOpeningSession(Integer sessionId, Throwable t) throws Throwable {

    }

    default void failedAcceptingSession(Integer sessionId, Throwable t) throws Throwable {

    }
}
