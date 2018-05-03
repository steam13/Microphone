package nuance.com;

import android.os.Handler;
import android.os.Message;
import nuance.com.MicView.MicState;

class DragonHandler extends Handler {
    private static int mThreadWaitTimeout_ms = 5000;
    private static final String sThis = ":DragonHandler:";
    private AudioReader mAudioReader;
    private Thread mAudioReaderThread;
    private AudioWriter mAudioWriter;
    private Thread mAudioWriterThread;
    private DragonClient mClient;
    private MicState mMicState = MicState.DISCONNECTED;
    private int mSampleRate;
    private String mSessionId = "";
    private Speakers mSpeakers;
    private final Handler mUi;

    private void onMicDisconnect() {
/*        if (mMicState == MicView.MicState.DISCONNECTED)
            break MISSING_BLOCK_LABEL_74;
        if (mSessionId != "")
            mClient.Disconnect(mSessionId);
        stopAudioThreads();
        mSessionId = "";
        mMicState = MicView.MicState.DISCONNECTED;
        postToUI(mMicState);
        if (mClient != null)
        {
            mClient.close();
            mClient = null;
        }
        _L2:
        return;
        DragonHttpException dragonhttpexception;
        dragonhttpexception;
        stopAudioThreads();
        mSessionId = "";
        mMicState = MicView.MicState.DISCONNECTED;
        postToUI(mMicState);
        if (mClient == null) goto _L2; else goto _L1
        _L1:
        mClient.close();
        mClient = null;
        return;
        Exception exception;
        stopAudioThreads();
        mSessionId = "";
        mMicState = MicView.MicState.DISCONNECTED;
        postToUI(mMicState);
        if (mClient != null)
        {
            mClient.close();
            mClient = null;
        }
        throw exception;*/
    }

    private void onMicQuit() {
        /* JADX: method processing error */
/*
Error: java.util.NoSuchElementException
	at java.util.HashMap$HashIterator.nextNode(HashMap.java:1431)
	at java.util.HashMap$KeyIterator.next(HashMap.java:1453)
	at jadx.core.dex.visitors.blocksmaker.BlockFinallyExtract.applyRemove(BlockFinallyExtract.java:535)
	at jadx.core.dex.visitors.blocksmaker.BlockFinallyExtract.extractFinally(BlockFinallyExtract.java:175)
	at jadx.core.dex.visitors.blocksmaker.BlockFinallyExtract.processExceptionHandler(BlockFinallyExtract.java:79)
	at jadx.core.dex.visitors.blocksmaker.BlockFinallyExtract.visit(BlockFinallyExtract.java:51)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:31)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:17)
	at jadx.core.ProcessClass.process(ProcessClass.java:37)
	at jadx.core.ProcessClass.processDependencies(ProcessClass.java:59)
	at jadx.core.ProcessClass.process(ProcessClass.java:42)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:306)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
	at jadx.api.JadxDecompiler$1.run(JadxDecompiler.java:199)
*/
        /*
        r4 = this;
        r3 = 0;
        r0 = r4.mMicState;
        r1 = nuance.com.MicView.MicState.QUIT;
        if (r0 == r1) goto L_0x003d;
    L_0x0007:
        r0 = r4.mSessionId;	 Catch:{ DragonHttpException -> 0x003e, all -> 0x0060 }
        r1 = "";	 Catch:{ DragonHttpException -> 0x003e, all -> 0x0060 }
        if (r0 == r1) goto L_0x001d;	 Catch:{ DragonHttpException -> 0x003e, all -> 0x0060 }
    L_0x000d:
        r0 = r4.mClient;	 Catch:{ DragonHttpException -> 0x003e, all -> 0x0060 }
        r1 = r4.mSessionId;	 Catch:{ DragonHttpException -> 0x003e, all -> 0x0060 }
        r2 = nuance.com.MicView.MicState.OFF;	 Catch:{ DragonHttpException -> 0x003e, all -> 0x0060 }
        r0.SetMicState(r1, r2);	 Catch:{ DragonHttpException -> 0x003e, all -> 0x0060 }
        r0 = r4.mClient;	 Catch:{ DragonHttpException -> 0x003e, all -> 0x0060 }
        r1 = r4.mSessionId;	 Catch:{ DragonHttpException -> 0x003e, all -> 0x0060 }
        r0.Disconnect(r1);	 Catch:{ DragonHttpException -> 0x003e, all -> 0x0060 }
    L_0x001d:
        r4.stopAudioThreads();
        r0 = "";
        r4.mSessionId = r0;
        r0 = nuance.com.MicView.MicState.QUIT;
        r4.mMicState = r0;
        r0 = r4.mMicState;
        r4.postToUI(r0);
        r0 = nuance.com.MicView.MicStatus.DISCONNECTED;
        r4.postToUI(r0);
        r0 = r4.mClient;
        if (r0 == 0) goto L_0x003d;
    L_0x0036:
        r0 = r4.mClient;
        r0.close();
        r4.mClient = r3;
    L_0x003d:
        return;
    L_0x003e:
        r0 = move-exception;
        r4.stopAudioThreads();
        r0 = "";
        r4.mSessionId = r0;
        r0 = nuance.com.MicView.MicState.QUIT;
        r4.mMicState = r0;
        r0 = r4.mMicState;
        r4.postToUI(r0);
        r0 = nuance.com.MicView.MicStatus.DISCONNECTED;
        r4.postToUI(r0);
        r0 = r4.mClient;
        if (r0 == 0) goto L_0x003d;
    L_0x0058:
        r0 = r4.mClient;
        r0.close();
        r4.mClient = r3;
        goto L_0x003d;
    L_0x0060:
        r0 = move-exception;
        r4.stopAudioThreads();
        r1 = "";
        r4.mSessionId = r1;
        r1 = nuance.com.MicView.MicState.QUIT;
        r4.mMicState = r1;
        r1 = r4.mMicState;
        r4.postToUI(r1);
        r1 = nuance.com.MicView.MicStatus.DISCONNECTED;
        r4.postToUI(r1);
        r1 = r4.mClient;
        if (r1 == 0) goto L_0x0081;
    L_0x007a:
        r1 = r4.mClient;
        r1.close();
        r4.mClient = r3;
    L_0x0081:
        throw r0;
        */
        throw new UnsupportedOperationException("Method not decompiled: nuance.com.DragonHandler.onMicQuit():void");
    }

    DragonHandler(Handler handler)
    {
        mMicState = MicView.MicState.DISCONNECTED;
        mSessionId = "";
        mUi = handler;
    }

    protected boolean verifySpeaker() throws DragonHttpException {
        String serverSpeaker = this.mClient.Speaker();
        String clientSpeaker = this.mClient.getClientSpec().getSpeaker();
        this.mSpeakers = new Speakers(clientSpeaker, serverSpeaker);
        return serverSpeaker.toLowerCase().equals(clientSpeaker.toLowerCase());
    }

    protected boolean verifyConnection() throws DragonHttpException {
        if (this.mClient != null && this.mSessionId.equals("")) {
            this.mSessionId = this.mClient.ConnectDevice();
            this.mSampleRate = this.mClient.SampleRate();
        }
        return !this.mSessionId.equals("");
    }

    protected void onMessageClientSpec(DragonClientSpec newClientSpec) {
        boolean createClient = false;
        if (this.mClient == null) {
            createClient = true;
        } else if (!this.mClient.getClientSpec().equals(newClientSpec)) {
            try {
                this.mClient.SetMicState(this.mSessionId, MicState.OFF);
                this.mClient.Disconnect(this.mSessionId);
                this.mClient.close();
            } catch (DragonHttpException e) {
            } finally {
                this.mSessionId = "";
                createClient = true;
            }
        }
        if (createClient) {
            this.mClient = new DragonClient(newClientSpec);
        }
    }

    protected void onMessageMicState(MicState reqMicState) {
        if (this.mClient == null) {
            if (reqMicState == MicState.QUIT) {
                postToUI(MicView.MicStatus.DISCONNECTED);
            }
        } else if (reqMicState == MicState.ON) {
            if (this.mMicState != MicState.ON) {
                onMicOn();
            }
        } else if (reqMicState == MicState.OFF) {
            onMicOff();
        } else if (reqMicState == MicState.DISCONNECTED) {
            onMicDisconnect();
        } else if (reqMicState == MicState.QUIT) {
            onMicQuit();
        } else if (reqMicState == MicState.ONPAUSE) {
            stopAudioThreads();
        } else if (reqMicState == MicState.RESUME) {
            onMicOn();
        } else {
            this.mMicState = reqMicState;
        }
    }

    private void onMicOn() {
        try {
            if (!verifySpeaker()) {
                postToUI(this.mSpeakers);
            } else if (verifyConnection()) {
                this.mMicState = this.mClient.SetMicState(this.mSessionId, MicState.ON);
                if (this.mMicState == MicState.PAUSE) {
                    this.mMicState = MicState.ON;
                    postToUI(MicState.ON);
                } else {
                    postToUI(this.mMicState);
                }
                startAudioThreads();
            } else {
                postToUI(MicView.MicStatus.ERROR_CONNECT);
            }
        } catch (DragonHttpException e) {
            Logger.logException(e, sThis);
            if (e.getCode() != 409 || this.mSessionId == "") {
                postToUI(new Integer(e.getCode()));
                return;
            }
            this.mSessionId = "";
            onMicOn();
        }
    }

    private void onMicOff() {
        if (this.mMicState != MicState.OFF) {
            try {
                stopAudioThreads();
                this.mMicState = this.mClient.SetMicState(this.mSessionId, MicState.OFF);
            } catch (DragonHttpException e) {
                postToUI(new Integer(e.getCode()));
            } finally {
                this.mMicState = MicState.OFF;
                postToUI(this.mMicState);
            }
        }
    }

    protected void startAudioThreads() {
        if (this.mAudioReader == null && this.mAudioWriter == null) {
            this.mAudioReader = new AudioReader(this, this.mSampleRate);
            this.mAudioWriter = new AudioWriter(this, this.mAudioReader, this.mSessionId, this.mClient);
            this.mAudioReaderThread = new Thread(this.mAudioReader);
            this.mAudioReaderThread.start();
            this.mAudioWriterThread = new Thread(this.mAudioWriter);
            this.mAudioWriterThread.start();
        }
    }

    protected void stopAudioThreads() {
        if (this.mAudioReader != null) {
            this.mAudioReader.stop();
            try {
                this.mAudioReaderThread.join((long) mThreadWaitTimeout_ms);
            } catch (InterruptedException e) {
            }
            this.mAudioReader = null;
        }
        if (this.mAudioWriter != null) {
            this.mAudioWriter.stop();
            try {
                this.mAudioWriterThread.join((long) mThreadWaitTimeout_ms);
            } catch (InterruptedException e2) {
            }
            this.mAudioWriter = null;
        }
    }

    protected void onMessageMicTask(MicView.MicTask micTask) {
        if (micTask == MicView.MicTask.SHUTDOWN) {
            stopAudioThreads();
            if (this.mClient != null) {
                this.mClient.close();
                this.mClient = null;
            }
        }
    }

    protected void postToUI(Object obj) {
        this.mUi.sendMessage(this.mUi.obtainMessage(0, obj));
        if (obj instanceof MicState) {
            onMessageMicState((MicState) obj);
        }
    }

    public void handleMessage(Message msg) {
        if (msg.obj instanceof DragonClientSpec) {
            onMessageClientSpec((DragonClientSpec) msg.obj);
        } else if (msg.obj instanceof MicState) {
            onMessageMicState((MicState) msg.obj);
        } else if (msg.obj instanceof MicView.MicTask) {
            onMessageMicTask((MicView.MicTask) msg.obj);
        }
    }
}
