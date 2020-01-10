package common;

public abstract class AbstractOutputEntry<T> implements OutputEntry<T>{

    private final T data;
    public AbstractOutputEntry(T data){
        this.data = data;
    }
    @Override
    public T getData() {
        return data;
    }
}
