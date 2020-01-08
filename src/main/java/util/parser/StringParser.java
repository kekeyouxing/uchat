package util.parser;

import java.nio.ByteBuffer;
import java.util.function.Consumer;

/**
 * StringParser: complete(accept)
 *              ||
 * DelimiterParser: complete(accept)
 *
 * CharParser: complete(delimiterParser::receive)
 *
 * StringParser: receive()
 *
 * DelimiterParser: receive(String obj)
 *
 * CharParser: receive(byteBuffer)
 *
 * StringParser.receive(ByteBuffer obj)-->StringParser.parse(ByteBuffer obj)-->
 * CharParser.receive(ByteBuffer obj)-->CharParser.parse(ByteBuffer obj)-->CharParser.action.accept()
 * -->DelimiterParser.receive(String obj)-->DelimiterParser.parse(String obj)-->DelimiterParser.action.accept()
 * @author keyouxing
 */
public class StringParser extends AbstractMessageHandler<ByteBuffer, String> {

    CharParser charParser = new CharParser();

    private final DelimiterParser delimiterParser;

    public StringParser(){
        this("\n");
    }

    public StringParser(String delimiter){
        delimiterParser = new DelimiterParser(delimiter);
        charParser.complete(delimiterParser::receive);
    }

    @Override
    protected void parse(ByteBuffer byteBuffer) {
        charParser.receive(byteBuffer);
    }

    @Override
    public StringParser complete(Consumer<String> accept){
        super.complete(accept);
        delimiterParser.complete(accept);
        return this;
    }
}
