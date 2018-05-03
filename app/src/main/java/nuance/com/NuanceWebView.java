package nuance.com;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.webkit.WebView;

public class NuanceWebView extends WebView {
    public NuanceWebView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public NuanceWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public NuanceWebView(Context context) {
        super(context);
    }

    public boolean onTouchEvent(MotionEvent ev) {
        if (ev.getAction() == 0 || ev.getAction() == 5 || ev.getAction() == 5 || ev.getAction() == 261 || ev.getAction() == 517) {
            if (ev.getPointerCount() > 1) {
                getSettings().setBuiltInZoomControls(true);
                getSettings().setSupportZoom(true);
            } else {
                getSettings().setBuiltInZoomControls(false);
                getSettings().setSupportZoom(false);
            }
        }
        return super.onTouchEvent(ev);
    }
}
