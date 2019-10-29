package util;

public interface BufferSizePredictor {
    int nextBufferSize();

    void previousReceivedBufferSize(int previousReceivedBufferSize);
}
