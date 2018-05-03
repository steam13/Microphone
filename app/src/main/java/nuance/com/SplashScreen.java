package nuance.com;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.LinearLayout;

public class SplashScreen extends Activity implements OnTouchListener, OnClickListener {
    private static final long SPLASHTIME = 3000;
    private static final int STOPSPLASH = 1;
    private Button btnAboutDragonRemoteMicrophone;
    private Button btnAboutDragonSpeechRecognition;
    private Context context;
    private boolean isInFront;
    private final Handler splashHandler = new C00251();

    class C00251 extends Handler {
        C00251() {
        }

        public void handleMessage(Message msg) {
            if (SplashScreen.this.isInFront) {
                switch (msg.what) {
                    case 1:
                        Intent micViewIntent = new Intent(SplashScreen.this.context, MicView.class);
                        micViewIntent.addFlags(536870912);
                        SplashScreen.this.startActivity(micViewIntent);
                        break;
                }
            }
            SplashScreen.this.finish();
            super.handleMessage(msg);
        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.context = this;
        requestWindowFeature(1);
        Preferences mPrefs = new Preferences(this);
        if (mPrefs.isIntialLaunch()) {
            setContentView(R.layout.first_splash);
            LinearLayout splashLayout = (LinearLayout) findViewById(R.id.splashLayout);
            this.btnAboutDragonRemoteMicrophone = (Button) findViewById(R.id.btnAboutDragonRemoteMic);
            this.btnAboutDragonSpeechRecognition = (Button) findViewById(R.id.btnAboutDragonSpeech);
            this.btnAboutDragonRemoteMicrophone.setOnClickListener(this);
            mPrefs.updateLaunched();
            this.btnAboutDragonSpeechRecognition.setOnClickListener(this);
            splashLayout.setOnTouchListener(this);
            return;
        }
        setContentView(R.layout.splash);
        Message msg = new Message();
        msg.what = 1;
        this.splashHandler.sendMessageDelayed(msg, SPLASHTIME);
    }

    public void onResume() {
        this.isInFront = true;
        super.onResume();
    }

    public void onPause() {
        this.isInFront = false;
        super.onPause();
    }

    public final boolean onTouch(View v, MotionEvent event) {
        Intent micViewIntent = new Intent(this, MicView.class);
        micViewIntent.addFlags(536870912);
        startActivity(micViewIntent);
        finish();
        return false;
    }

    public void onClick(View v) {
        if (v.equals(this.btnAboutDragonRemoteMicrophone)) {
            startActivity(new Intent("android.intent.action.VIEW", Uri.parse(getResources().getString(R.string.remote_microphone_link))));
        }
        if (v.equals(this.btnAboutDragonSpeechRecognition)) {
            startActivity(new Intent("android.intent.action.VIEW", Uri.parse(getResources().getString(R.string.about_dragon_link))));
        }
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode != 4) {
            return super.onKeyDown(keyCode, event);
        }
        if (this.splashHandler.hasMessages(1)) {
            this.splashHandler.removeMessages(1);
            finish();
            return true;
        }
        onTouch(null, null);
        return true;
    }
}
