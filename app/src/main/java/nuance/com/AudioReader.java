package nuance.com;

class AudioReader implements Runnable {
    private static final int audioDataTimeout_ms = 5000;
    private static final int maxBytes = 8388608;
    private final int AUDIO_ISSUES = 502;
    private final int mBufferBytes;
    private final DragonHandler mHandler;
    private final MicAudioRecorder mRecorder;
    private final RingBuffer<byte[]> mRingBuf;
    private boolean mStopped = false;

    AudioReader(DragonHandler handler, int sampleRate) {
        this.mHandler = handler;
        this.mRecorder = new MicAudioRecorder(handler, sampleRate);
        if (sampleRate == 11025) {
            this.mBufferBytes = 11010;
        } else {
            this.mBufferBytes = this.mRecorder.getReadBufferSize();
        }
        this.mRingBuf = new RingBuffer(maxBytes / this.mBufferBytes);
    }

    void stop() {
        this.mStopped = true;
    }

    public void run() {
        try {
            if (this.mRecorder.initialize()) {
                this.mRecorder.start();
                while (!this.mStopped) {
                    byte[] buffer = new byte[this.mBufferBytes];
                    if (this.mRecorder.read(buffer) > 0) {
                        putBuffer(buffer);
                    }
                }
                if (this.mStopped) {
                    this.mRecorder.stop();
                    return;
                }
                return;
            }
            throw new RuntimeException("Failed to initialize MicAudioRecorder");
        } catch (IllegalStateException e) {
        } catch (Exception e2) {
        }
    }

    synchronized void putBuffer(byte[] buffer) throws InterruptedException {
        try {
            this.mRingBuf.enqueue(buffer);
            notify();
        } catch (RuntimeException e) {
            this.mHandler.postToUI(Integer.valueOf(502));
            wait(5000);
        }
    }

    synchronized byte[] getBuffer(long timeout) throws InterruptedException {
        byte[] retBuf;
        retBuf = null;
        do {
            if (this.mRingBuf.isEmpty()) {
                wait(timeout);
                continue;
            } else {
                try {
                    retBuf = (byte[]) this.mRingBuf.dequeue();
                    continue;
                } catch (RuntimeException e) {
                    continue;
                }
            }
        } while (retBuf == null);
        return retBuf;
    }
}
