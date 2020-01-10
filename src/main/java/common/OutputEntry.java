package common;

public interface OutputEntry<T> {
    T getData();

    OutputEntryType getOutPutEntryType();

    long remaining();
}
