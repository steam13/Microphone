package nuance.com;

import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;

class MicAudioRecorder {
    private static final int mRecorderBuffers = 15;
    private AudioRecord mAudio;
    private final int mAudioFormat = 2;
    private final int mChannelConfig = 16;
    private final int mDefaultRecorderBufferSize;
    private final DragonHandler mHandler;
    private final int mSampleRate;

    MicAudioRecorder(DragonHandler handler, int sampleRate) {
        this.mHandler = handler;
        this.mSampleRate = sampleRate;
        if (sampleRate == 11025) {
            this.mDefaultRecorderBufferSize = this.mSampleRate * 14;
        } else {
            this.mDefaultRecorderBufferSize = this.mSampleRate * mRecorderBuffers;
        }
    }

    boolean initialize() {
        int recorderBufferSize = getRecorderBufferSize();
        if (recorderBufferSize <= 0) {
            return false;
        }
        try {
            this.mAudio = new AudioRecord(1, this.mSampleRate, 16, 2, recorderBufferSize);
       /*     this.mAudio =  new AudioRecord(MediaRecorder.AudioSource.MIC,
                    mSampleRate,
                    AudioFormat.CHANNEL_IN_MONO,
                    AudioFormat.ENCODING_PCM_16BIT,
                    recorderBufferSize);*/
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    void start() throws IllegalStateException {
        if (this.mAudio != null) {
            this.mAudio.startRecording();
            return;
        }
        throw new IllegalStateException();
    }

    int read(byte[] buffer) {
        if (this.mAudio == null || this.mAudio.getState() != 1) {
            return 0;
        }
        int bytesRead = this.mAudio.read(buffer, 0, buffer.length);
        if (bytesRead == -3 || bytesRead == -2) {
            return 0;
        }
        return bytesRead;
    }

    void stop() {
        try {
            if (this.mAudio != null) {
                this.mAudio.stop();
                this.mAudio.release();
            }
        } catch (IllegalStateException e) {
        }
    }

    int getReadBufferSize() {
        return this.mSampleRate;
    }

    private int getRecorderBufferSize() {
        int minBufSize = AudioRecord.getMinBufferSize(this.mSampleRate, 16, 2);
        if (minBufSize == -2) {
            return 0;
        }
        return minBufSize <= this.mDefaultRecorderBufferSize ? this.mDefaultRecorderBufferSize : minBufSize;
    }
}
