package test;

public class B extends AbstractLifecycle{
    @Override
    public void init() {
        System.out.println("this is B");
        System.out.println(this.value);
    }
}
