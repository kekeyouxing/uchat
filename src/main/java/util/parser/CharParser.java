package util.parser;

import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CoderResult;

/**
 * @author keyouxing
 */
public class CharParser extends AbstractByteBufferMessageHandler<String> {

    private CharsetDecoder decoder;

    private final static String DEFAULT_CHARSET = "UTF-8";
    public CharParser(){
        this(DEFAULT_CHARSET);
    }

    public CharParser(String charset) {
        decoder = Charset.forName(charset).newDecoder();
    }

    private CharBuffer allocate() {
        int expectedLength = (int) (buffer.remaining() * decoder.averageCharsPerByte()) + 1;
        return CharBuffer.allocate(expectedLength);
    }

    @Override
    void parse() {
        CharBuffer charBuffer = allocate();
        decoder.reset();
        while (buffer.hasRemaining()){
            CoderResult coderResult = decoder.decode(buffer, charBuffer, false);
            charBuffer.flip();
            if (coderResult.isUnderflow()) {
                if (buffer.hasRemaining()){
                    buffer = buffer.slice();
                }
                if(charBuffer.hasRemaining()){
                    action.accept(charBuffer.toString());
                }
                break;
            }else if(coderResult.isOverflow()){
                action.accept(charBuffer.toString());
                charBuffer = allocate();
            }
        }

    }
}
