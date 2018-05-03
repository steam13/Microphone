package nuance.com;

import android.Manifest;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.os.StrictMode;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.InflateException;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.LayoutInflater.Factory;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.io.IOException;

import nuance.com.db.NuanceDatabaseAdapter;

public class MicView extends NuanceActivity implements OnTouchListener {
    private static final String sThis = "MicView:";
    private final int AUDIO_ISSUES = 502;
    private final int CONFLICT = 409;
    private final int GONE = 410;
    private final int INTERNAL_SERVER_ERROR = 500;
    private final int NOT_IMPLEMENTED = 501;
    private final int NOT_MODIFIED = 304;
    private AlertDialog connectionErrorDialog;
    private Context context;
    private ProfileData currentProfile;
    private boolean isInFront;
    private AlertDialog mDisplayOnceDialog;
    private DragonHandlerThread mDragonHandlerThread;
    private boolean mIsPaused = false;
    final Handler mMessageHandler = new C00111();
    private NuanceMicButton mMicButton;
    private MicState mMicState = MicState.DISCONNECTED;
    private DragonClientSpec mPauseSpec = null;
    private NuanceDatabaseAdapter nuanceDatabaseAdapter;
    WakeLock wakeLock;

    class C00111 extends Handler {
        C00111() {
        }

        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            MicView.this.onHandleMessage(msg);
        }
    }

    class C00122 implements OnClickListener {
        C00122() {
        }

        public void onClick(View v) {
            MicView.this.connectionErrorDialog.dismiss();
            if (MicView.this.mMicState == MicState.OFF || MicView.this.mMicState == MicState.DISCONNECTED) {
                MicView.this.postToThread(new DragonClientSpec(MicView.this.currentProfile.getDnsNames(), MicView.this.currentProfile.getPort(), MicView.this.currentProfile.getProfileName()));
                MicView.this.postToThread(MicState.ON);
            } else if (MicView.this.mMicState == MicState.SLEEPING) {
                MicView.this.postToThread(MicState.ON);
            } else if (MicView.this.mMicState == MicState.ON) {
                MicView.this.postToThread(MicState.OFF);
            }
        }
    }

    class C00133 implements OnClickListener {
        C00133() {
        }

        public void onClick(View v) {
            MicView.this.connectionErrorDialog.dismiss();
            MicView.this.mMicButton.setPressed(false);
            MicView.this.mMicButton.refreshDrawableState();
            Intent addprofileIntent = new Intent();
            addprofileIntent.setClassName("nuance.com", "nuance.com.AddProfileScreen");
            Bundle b1 = new Bundle();
            b1.putParcelable(NuanceKeys.MICEDITPROFILE, MicView.this.currentProfile);
            addprofileIntent.putExtras(b1);
            MicView.this.startActivity(addprofileIntent);
        }
    }

    class C00144 implements OnClickListener {
        C00144() {
        }

        public void onClick(View v) {
            MicView.this.connectionErrorDialog.dismiss();
            MicView.this.mMicButton.setPressed(false);
            MicView.this.mMicButton.refreshDrawableState();
        }
    }

    class C00165 implements Factory {
        C00165() {
        }

        public View onCreateView(String name, Context context, AttributeSet attrs) {
            if (name.equalsIgnoreCase("com.android.internal.view.menu.IconMenuItemView")) {
                try {
                    final View view = MicView.this.getLayoutInflater().createView(name, null, attrs);
                    new Handler().post(new Runnable() {
                        public void run() {
                            view.setBackgroundResource(R.color.Black);
                            ((TextView) view).setTextColor(-1);
                        }
                    });
                    return view;
                } catch (InflateException e) {
                } catch (ClassNotFoundException e2) {
                }
            }
            return null;
        }
    }

    public enum MicState {
        DISCONNECTED,
        ON,
        OFF,
        PAUSE,
        SLEEPING,
        QUIT,
        ONPAUSE,
        RESUME
    }

    enum MicStatus {
        OK,
        ERROR_CONNECT,
        DISCONNECTED
    }

    enum MicTask {
        SHUTDOWN
    }

    void onHandleMessage(Message msg) {
        if (msg.obj instanceof MicState) {
            setMicState((MicState) msg.obj);
        } else if (msg.obj instanceof MicStatus) {
            setMicStatus((MicStatus) msg.obj);
        } else if (msg.obj instanceof Integer) {
            try {
                switch (((Integer) msg.obj).intValue()) {
                    case 304:
                        MessageBox.showOK((Context) this, (int) R.string.error_not_modified);
                        return;
                    case 409:
                        MessageBox.showOK(this, getString(R.string.Someone_Else_Connected), getString(R.string.Someone_Else_Connected_Title));
                        return;
                    case 410:
                        if (this.mDisplayOnceDialog == null || !this.mDisplayOnceDialog.isShowing()) {
                            this.mDisplayOnceDialog = MessageBox.showOKGetInstance(this, getString(R.string.error_gone));
                            return;
                        }
                        return;
                    case 500:
                        if (this.isInFront) {
                            this.connectionErrorDialog.show();
                            return;
                        }
                        return;
                    case 501:
                        MessageBox.showOK((Context) this, (int) R.string.error_not_implemented);
                        return;
                    case 502:
                        postToThread(MicState.OFF);
                        MessageBox.showOK((Context) this, (int) R.string.Error_Sending_Data_Message);
                        return;
                    default:
                        MessageBox.showOK((Context) this, (int) R.string.error_connect);
                        return;
                }
            } catch (NumberFormatException e) {
            }
        } else if (msg.obj instanceof String) {
            MessageBox.showOK((Context) this, (String) msg.obj);
        } else if (msg.obj instanceof Speakers) {
            Speakers speakers = (Speakers) msg.obj;
            if (speakers.getDragonSpSpeaker().equals("")) {
                MessageBox.showOK(this, getString(R.string.no_user_message), getString(R.string.No_user_loaded_Title));
            } else {
                MessageBox.showOK(this, speakers.formSpeakerError(getString(R.string.wrong_user_message)), getString(R.string.Wrong_user_loaded_Title));
            }
        } else {
            MessageBox.showOK((Context) this, "Class: " + msg.obj.getClass());
        }
    }

    void setMicState(MicState micState) {
        this.mMicState = micState;
        this.mMicButton.setState(this.mMicState);
        this.mMicButton.setPressed(false);
        this.mMicButton.refreshDrawableState();
    }

    void setMicStatus(MicStatus micStatus) {
        if (micStatus == MicStatus.ERROR_CONNECT) {
            MessageBox.showOK((Context) this, (int) R.string.error_connect);
        } else if (micStatus == MicStatus.DISCONNECTED) {
            finish();
        }
    }

    void postToThread(Object obj) {
        if (this.mDragonHandlerThread != null) {
            Handler handler = this.mDragonHandlerThread.getHandler();
            if (handler != null) {
                handler.sendMessage(handler.obtainMessage(0, obj));
            }
        }
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        setContentView(R.layout.miclayout);
        this.context = this;
        this.nuanceDatabaseAdapter = new NuanceDatabaseAdapter(this);
        this.nuanceDatabaseAdapter.open();
        this.currentProfile = this.nuanceDatabaseAdapter.selectCurrentProfile();
        buildErrorDialog();
        this.mMicButton = (NuanceMicButton) findViewById(R.id.imageButtonMic);
        this.mMicButton.setState(MicState.DISCONNECTED);
        this.mMicButton.setOnTouchListener(this);
        if (!checkWifi()) {
            MessageBox.showOK(this, getString(R.string.No_WiFi_Message), getString(R.string.No_WiFi));
        }
        this.mDragonHandlerThread = new DragonHandlerThread(this.mMessageHandler);
        this.mDragonHandlerThread.start();
        do {
        } while (this.mDragonHandlerThread.getLooper() == null);
        requestRecordAudioPermission();
    }

    private void buildErrorDialog() {
        LayoutInflater inflater = getLayoutInflater();
        View layout = inflater.inflate(R.layout.internal_server_error_dialog, (ViewGroup) findViewById(R.id.internal_error_layout_root));
        ((Button) layout.findViewById(R.id.buttonTryAgain)).setOnClickListener(new C00122());
        ((Button) layout.findViewById(R.id.buttonEditComputerInfo)).setOnClickListener(new C00133());
        ((Button) layout.findViewById(R.id.buttonCancel)).setOnClickListener(new C00144());
        TextView title = (TextView) inflater.inflate(R.layout.dialog_title, (ViewGroup) findViewById(R.id.dialog_title));
        title.setText(R.string.Connection_Problem_Detected);
        Builder builder = new Builder(this.context);
        builder.setView(layout);
        builder.setCustomTitle(title);
        builder.setCancelable(true);
        this.connectionErrorDialog = builder.create();
    }

    public boolean onTouch(View v, MotionEvent event) {
        boolean isHandled = false;
        if (event.getAction() != 0) {
            return false;
        }
        this.mMicButton.setPressed(true);
        this.mMicButton.refreshDrawableState();
        if (v.getId() == R.id.imageButtonMic || v.getId() == R.id.buttonTryAgain) {
            isHandled = true;
            if (((TextView) findViewById(R.id.textViewMicuser)).getText().toString().equals(getResources().getString(R.string.Select_Menu_to_begin))) {
                Intent settingIntent = new Intent();
                settingIntent.setClassName("nuance.com", "nuance.com.SettingScreen");
                startActivity(settingIntent);
            } else if (!checkWifi()) {
                MessageBox.showOK(this, getString(R.string.No_WiFi_Message), getString(R.string.No_WiFi));
            } else if (this.mMicState == MicState.OFF || this.mMicState == MicState.DISCONNECTED) {
                postToThread(new DragonClientSpec(this.currentProfile.getDnsNames(), this.currentProfile.getPort(), this.currentProfile.getProfileName()));
                postToThread(MicState.ON);
            } else if (this.mMicState == MicState.SLEEPING) {
                postToThread(MicState.ON);
            } else if (this.mMicState == MicState.ON) {
                postToThread(MicState.OFF);
            }
        }
        return isHandled;
    }

    public void onStart() {
        super.onStart();
        this.nuanceDatabaseAdapter.open();
        this.currentProfile = this.nuanceDatabaseAdapter.selectCurrentProfile();
        if (this.currentProfile != null) {
            ((TextView) findViewById(R.id.textViewMicuser)).setText(this.currentProfile.getProfileName());
        } else {
            ((TextView) findViewById(R.id.textViewMicuser)).setText(R.string.Select_Menu_to_begin);
        }

        try {
            int pid = android.os.Process.myPid();
            String whiteList = "logcat -P '" + pid + "'";
            Runtime.getRuntime().exec(whiteList).waitFor();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void onResume() {
        this.isInFront = true;
        this.wakeLock = ((PowerManager) getSystemService(Context.POWER_SERVICE)).newWakeLock(6, sThis);
        this.wakeLock.acquire();
        super.onResume();
        if (this.mIsPaused && this.currentProfile != null && new DragonClientSpec(this.currentProfile.getDnsNames(), this.currentProfile.getPort(), this.currentProfile.getProfileName()).equals(this.mPauseSpec)) {
            postToThread(MicState.RESUME);
        }
        this.mIsPaused = false;
    }

    public void onPause() {
        this.isInFront = false;
        this.wakeLock.release();
        if (this.connectionErrorDialog.isShowing()) {
            this.connectionErrorDialog.dismiss();
        }
        if (this.mMicState == MicState.ON || this.mMicState == MicState.SLEEPING) {
            this.mIsPaused = true;
            if (this.currentProfile != null) {
                this.mPauseSpec = new DragonClientSpec(this.currentProfile.getDnsNames(), this.currentProfile.getPort(), this.currentProfile.getProfileName());
            }
            postToThread(MicState.ONPAUSE);
        }
        super.onPause();
    }

    public void onStop() {
        super.onStop();
    }

    public void onDestroy() {
        this.mDragonHandlerThread.quit();
        this.nuanceDatabaseAdapter.close();
        super.onDestroy();
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode != 4) {
            return super.onKeyDown(keyCode, event);
        }
        postToThread(MicState.QUIT);
        return true;
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.options_menu, menu);
        getLayoutInflater().setFactory(new C00165());
        return super.onCreateOptionsMenu(menu);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.settings_menu_item:
                startActivity(new Intent(this, SettingScreen.class));
                return true;
            case R.id.information_menu_item:
                startActivity(new Intent(this, InformationScreen.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void requestRecordAudioPermission() {
        //check API version, do nothing if API version < 23!
        int currentapiVersion = android.os.Build.VERSION.SDK_INT;
        if (currentapiVersion > android.os.Build.VERSION_CODES.LOLLIPOP){

            if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {

                // Should we show an explanation?
                if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.RECORD_AUDIO)) {

                    // Show an expanation to the user *asynchronously* -- don't block
                    // this thread waiting for the user's response! After the user
                    // sees the explanation, try again to request the permission.

                } else {

                    // No explanation needed, we can request the permission.

                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECORD_AUDIO}, 1);
                }
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                    Log.d("Activity", "Granted!");

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Log.d("Activity", "Denied!");
                    finish();
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }
}
