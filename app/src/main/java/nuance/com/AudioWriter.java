package nuance.com;

import nuance.com.MicView.MicState;

class AudioWriter implements Runnable {
    private static final int audioDataTimeout_ms = 5000;
    private final DragonClient mClient;
    private final DragonHandler mHandler;
    private final AudioReader mReader;
    private final String mSessionId;
    private boolean mStopped = false;

    AudioWriter(DragonHandler handler, AudioReader reader, String sessionId, DragonClient client) {
        this.mHandler = handler;
        this.mReader = reader;
        this.mSessionId = sessionId;
        this.mClient = client;
    }

    void stop() {
        this.mStopped = true;
    }

    public void run() {
        MicState lastMicState = MicState.DISCONNECTED;
        while (!this.mStopped) {
            try {
                MicState micState = this.mClient.MicAudio(this.mSessionId, this.mReader.getBuffer(5000));
                if (micState != lastMicState) {
                    this.mHandler.postToUI(micState);
                    lastMicState = micState;
                }
            } catch (InterruptedException e) {
            } catch (DragonHttpException de) {
                if (de.getCode() == 410) {
                    this.mHandler.postToUI(new Integer(de.getCode()));
                }
            }
        }
    }
}
