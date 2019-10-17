package common;

/**
 * @author keyouxing
 */
public interface Handler {
    /**
     * This method is called back when the client connects to the server successfully.
     * @param session Session between clients connecting servers
     */
	public void sessionOpen(Session session);
}
