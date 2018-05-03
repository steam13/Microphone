package nuance.com;

import android.os.Handler;
import android.os.HandlerThread;

class DragonHandlerThread extends HandlerThread {
    private DragonHandler mHandler;
    private final Handler mUiHandler;

    DragonHandlerThread(Handler uiHandler) {
        super("DragonClientThread");
        this.mUiHandler = uiHandler;
    }

    Handler getHandler() {
        return this.mHandler;
    }

    protected void onLooperPrepared() {
        this.mHandler = new DragonHandler(this.mUiHandler);
    }
}
