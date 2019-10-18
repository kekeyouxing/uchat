package common;

import java.util.function.Consumer;

/**
 * @author keyouxing
 */
public class StringParser {


    private Consumer<String> accept;

    public void complete(Consumer<String> accept) {
        this.accept = accept;
    }
    public void receive(Object obj){
    }
}
