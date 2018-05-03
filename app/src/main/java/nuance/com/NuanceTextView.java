package nuance.com;

import android.content.Context;
import android.text.Layout;
import android.text.TextUtils.TruncateAt;
import android.util.AttributeSet;
import android.widget.TextView;

public class NuanceTextView extends TextView {
    final float MIN_TEXT_SIZE_PX = 14.0f;
    float mDefaultTextSizePx;

    public NuanceTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initDefaultSettings();
    }

    public NuanceTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initDefaultSettings();
    }

    public NuanceTextView(Context context) {
        super(context);
        initDefaultSettings();
    }

    protected void initDefaultSettings() {
        setSingleLine();
        setEllipsize(TruncateAt.END);
    }

    protected boolean changeTextSize() {
        Layout layout = getLayout();
        if (layout != null && layout.getLineCount() == 1 && layout.getEllipsisCount(0) > 0) {
            float textSizePx = getTextSize();
            if (textSizePx > 14.0f) {
                setTextSize(0, textSizePx - 1.0f);
                return true;
            }
        }
        return false;
    }

    protected void onTextChanged(CharSequence text, int start, int lengthBefore, int lengthAfter) {
        super.onTextChanged(text, start, lengthBefore, lengthAfter);
        float textSizePx = getTextSize();
        if (textSizePx > this.mDefaultTextSizePx) {
            this.mDefaultTextSizePx = textSizePx;
        } else if (textSizePx < this.mDefaultTextSizePx) {
            setTextSize(0, this.mDefaultTextSizePx);
        }
        requestLayout();
    }

    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        if (changeTextSize()) {
            requestLayout();
        }
    }

    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (changeTextSize()) {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        }
    }
}
