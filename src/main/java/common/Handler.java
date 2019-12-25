package common;

/**
 * @author keyouxing
 */
public interface Handler {
    /**
     * This method is called back when the client connects to the server successfully.
     * @param context Session between clients connecting servers
     */
	public void connectionOpenSuccess(Context context, TcpConnection connection);

}
