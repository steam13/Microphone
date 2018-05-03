package nuance.com;

import android.app.Activity;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

class Preferences {
    private static final String INTIAL = "INTIAL";
    private static final String STANDARD = "STANDARD";
    private static final String sKey_Install = "nuance_installed";
    private String mLaunch = INTIAL;
    private final SharedPreferences mPrefs;

    Preferences(Activity activity) {
        this.mPrefs = activity.getSharedPreferences("Nuance.DragonMic.Prefs", 0);
        restore();
    }

    private void restore() {
        this.mLaunch = this.mPrefs.getString(sKey_Install, INTIAL);
    }

    public void updateLaunched() {
        this.mLaunch = STANDARD;
        save();
    }

    public boolean isIntialLaunch() {
        return this.mLaunch.equals(INTIAL);
    }

    private void save() {
        Editor editor = this.mPrefs.edit();
        editor.putString(sKey_Install, this.mLaunch);
        editor.commit();
    }
}
