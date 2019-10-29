package common;

/**
 * @author keyouxing
 */
public abstract class AbstractLifecycle implements Lifecycle{

	private volatile boolean started;
	@Override
	public void start(){
		if(!started){
			synchronized(this){
				if(!started){
					init();
					started = true;
				}
			}
		}
	}

	@Override
	public void stop(){
		if(started){
			synchronized(this){
				if(started){
					destroy();
					started = false;
				}
			}
		}
	}

	/**
	 * This method will invoke when subclass invoke start method
	 */
	public abstract void init();

	/**
	 * This method will invoke when subclass invoke stop method
	 */
	public abstract void destroy();
}