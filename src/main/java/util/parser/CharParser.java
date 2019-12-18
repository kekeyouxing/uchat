package util.parser;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CoderResult;

/**
 * @author Administrator
 */
public class CharParser extends AbstractMessageHandler<ByteBuffer, String>{

    private CharsetDecoder decoder;

    private final static String DEFAULT_CHARSET = "UTF-8";

    private ByteBuffer in;

    public CharParser(){
        this(DEFAULT_CHARSET);
    }

    public CharParser(String charset){
        decoder = Charset.forName(charset).newDecoder();
    }

    @Override
    public void parse(ByteBuffer buffer) {
        assign(buffer);
        parse();
    }

    private void parse() {
        while (in.hasRemaining()) {
            CharBuffer out = CharBuffer.allocate((int) (in.remaining() * decoder.maxCharsPerByte()));

            try {
                CoderResult cr = decoder.decode(in, out, false);
                if(!cr.isUnderflow()){
                    cr.throwException();
                }
            } catch (CharacterCodingException e) {
                e.printStackTrace();
            }
            action.accept(out.flip().toString());
        }
    }

    private void assign(ByteBuffer buffer) {
        if (in != null){
            if (in.hasRemaining()){
                ByteBuffer buf = ByteBuffer.allocate(in.remaining() + buffer.remaining());
                buf.put(in).put(buffer).flip();
                in =  buffer;
            }else {
                in = buffer;
            }
        }else {
            in = buffer;
        }
    }
}
