package test;

public abstract class AbstractLifecycle implements Lifecycle{
    protected volatile boolean started;
    protected int value;
    @Override
    public void start(){
        if(!started){
            synchronized (this){
                if (!started){
                    started = true;
                    init();
                    System.out.println("Parent: " + value);
                }
            }
        }
    }

    public abstract void init();
}
