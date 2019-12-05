package util.parser;

/**
 * @author keyouxing
 */
class DelimiterParser extends AbstractMessageHandler<String, String> {
    private StringBuilder buffer = new StringBuilder();
    private String delimiter;
    DelimiterParser(String delimiter) {
        this.delimiter = delimiter;
    }

    @Override
    protected void parse(String s) {
        buffer.append(s);
        int cursor = 0;
        int end;
        while ((end = buffer.indexOf(delimiter, cursor)) != -1) {
            action.accept(buffer.substring(cursor, end));
            cursor = end + delimiter.length();
        }
        if (cursor < s.length()) {
            String remaining = buffer.substring(cursor, s.length());
            buffer.delete(0, s.length());
            buffer.append(remaining);
        }else {
            buffer.delete(0, s.length());
        }
    }
}
