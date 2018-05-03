package nuance.com;

import android.util.Log;

class Logger {
    public static final String LTAG = "nuance.mic.log";

    Logger() {
    }

    public static void logException(Exception e, String sWho) {
        Log.e(LTAG, new StringBuilder(String.valueOf(sWho)).append(" Exception: ").append(e.getMessage()).toString());
        Log.d(LTAG, new StringBuilder(String.valueOf(sWho)).append(" Exception: ").append(e.getStackTrace()).toString());
    }

    public static void logMessage(String messge) {
        Log.d(LTAG, messge);
    }
}
