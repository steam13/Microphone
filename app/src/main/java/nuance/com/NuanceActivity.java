package nuance.com;


import android.net.ConnectivityManager;
import android.support.v7.app.AppCompatActivity;

public class NuanceActivity extends AppCompatActivity {
    protected void onRestart() {
        super.onRestart();
        if (!checkWifi()) {
            MessageBox.showOK(this, getString(R.string.No_WiFi_Message), getString(R.string.No_WiFi));
        }
    }

    protected boolean checkWifi() {
        return ((ConnectivityManager) getSystemService("connectivity")).getNetworkInfo(1).isConnected();
    }
}
