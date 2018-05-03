package nuance.com;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.Button;

import nuance.com.MicView.MicState;


public class NuanceMicButton extends android.support.v7.widget.AppCompatButton {
    private static int[] $SWITCH_TABLE$nuance$com$MicView$MicState;
    private static final int[] DISCONNECTED = new int[]{R.attr.state_DISCONNECTED};
    private static final int[] OFF = new int[]{R.attr.state_OFF};
    private static final int[] ON = new int[]{R.attr.state_ON};
    private static final int[] PAUSE = new int[]{R.attr.state_PAUSE};
    private static final int[] PRESSED = new int[]{R.attr.state_PRESSED};
    private static final int[] QUIT = new int[]{R.attr.state_QUIT};
    private static final int[] SLEEPING = new int[]{R.attr.state_SLEEPING};
    private boolean mIsDisconnected = false;
    private boolean mIsOff = false;
    private boolean mIsOn = false;
    private boolean mIsPause = false;
    private boolean mIsPressed = false;
    private boolean mIsQuit = false;
    private boolean mIsSleeping = false;

    static int[] $SWITCH_TABLE$nuance$com$MicView$MicState() {
        int[] iArr = $SWITCH_TABLE$nuance$com$MicView$MicState;
        if (iArr == null) {
            iArr = new int[MicState.values().length];
            try {
                iArr[MicState.DISCONNECTED.ordinal()] = 1;
            } catch (NoSuchFieldError e) {
            }
            try {
                iArr[MicState.OFF.ordinal()] = 3;
            } catch (NoSuchFieldError e2) {
            }
            try {
                iArr[MicState.ON.ordinal()] = 2;
            } catch (NoSuchFieldError e3) {
            }
            try {
                iArr[MicState.ONPAUSE.ordinal()] = 7;
            } catch (NoSuchFieldError e4) {
            }
            try {
                iArr[MicState.PAUSE.ordinal()] = 4;
            } catch (NoSuchFieldError e5) {
            }
            try {
                iArr[MicState.QUIT.ordinal()] = 6;
            } catch (NoSuchFieldError e6) {
            }
            try {
                iArr[MicState.RESUME.ordinal()] = 8;
            } catch (NoSuchFieldError e7) {
            }
            try {
                iArr[MicState.SLEEPING.ordinal()] = 5;
            } catch (NoSuchFieldError e8) {
            }
            $SWITCH_TABLE$nuance$com$MicView$MicState = iArr;
        }
        return iArr;
    }

    public NuanceMicButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public NuanceMicButton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public NuanceMicButton(Context context) {
        super(context);
    }

    public void setState(MicState state) {
        this.mIsDisconnected = false;
        this.mIsOn = false;
        this.mIsOff = false;
        this.mIsPause = false;
        this.mIsQuit = false;
        this.mIsSleeping = false;
        switch ($SWITCH_TABLE$nuance$com$MicView$MicState()[state.ordinal()]) {
 /*           case R.styleable.States_state_OFF:
                this.mIsDisconnected = true;
                break;
            case R.styleable.States_state_PAUSE:
                this.mIsOn = true;
                break;
            case R.styleable.States_state_ON:
                this.mIsOff = true;
                break;
            case R.styleable.States_state_SLEEPING:
                this.mIsPause = true;
                break;
            case R.styleable.States_state_QUIT*//*5*//*:
                this.mIsSleeping = true;
                break;
            case R.styleable.States_state_PRESSED
                    :
                this.mIsQuit = true;
                break;*/

            case 6: // '\006'
                mIsQuit = true;
                break;

            case 5: // '\005'
                mIsSleeping = true;
                break;

            case 4: // '\004'
                mIsPause = true;
                break;

            case 3: // '\003'
                mIsOff = true;
                break;

            case 2: // '\002'
                mIsOn = true;
                break;

            case 1: // '\001'
                mIsDisconnected = true;
                break;
        }
        refreshDrawableState();
    }

    public void setPressed(boolean isPressed) {
        this.mIsPressed = isPressed;
    }

    protected int[] onCreateDrawableState(int extraSpace) {
        int[] drawableState = super.onCreateDrawableState(extraSpace + 7);
        if (this.mIsPressed) {
            mergeDrawableStates(drawableState, PRESSED);
        }
        if (this.mIsDisconnected) {
            mergeDrawableStates(drawableState, DISCONNECTED);
        }
        if (this.mIsOff) {
            mergeDrawableStates(drawableState, OFF);
        }
        if (this.mIsPause) {
            mergeDrawableStates(drawableState, PAUSE);
        }
        if (this.mIsOn) {
            mergeDrawableStates(drawableState, ON);
        }
        if (this.mIsSleeping) {
            mergeDrawableStates(drawableState, SLEEPING);
        }
        if (this.mIsQuit) {
            mergeDrawableStates(drawableState, QUIT);
        }
        return drawableState;
    }
}


/*public class NuanceMicButton extends Button
{

    private static int $SWITCH_TABLE$nuance$com$MicView$MicState[];
    private static final int DISCONNECTED[] = {
            0x7f010000
    };
    private static final int OFF[] = {
            0x7f010001
    };
    private static final int ON[] = {
            0x7f010003
    };
    private static final int PAUSE[] = {
            0x7f010002
    };
    private static final int PRESSED[] = {
            0x7f010006
    };
    private static final int QUIT[] = {
            0x7f010005
    };
    private static final int SLEEPING[] = {
            0x7f010004
    };
    private boolean mIsDisconnected;
    private boolean mIsOff;
    private boolean mIsOn;
    private boolean mIsPause;
    private boolean mIsPressed;
    private boolean mIsQuit;
    private boolean mIsSleeping;

    static int[] $SWITCH_TABLE$nuance$com$MicView$MicState()
    {
        int ai[] = $SWITCH_TABLE$nuance$com$MicView$MicState;
        if (ai != null)
            return ai;
        int ai1[] = new int[MicView.MicState.values().length];
        try
        {
            ai1[MicView.MicState.DISCONNECTED.ordinal()] = 1;
        }
        catch (NoSuchFieldError nosuchfielderror) { }
        try
        {
            ai1[MicView.MicState.OFF.ordinal()] = 3;
        }
        catch (NoSuchFieldError nosuchfielderror1) { }
        try
        {
            ai1[MicView.MicState.ON.ordinal()] = 2;
        }
        catch (NoSuchFieldError nosuchfielderror2) { }
        try
        {
            ai1[MicView.MicState.ONPAUSE.ordinal()] = 7;
        }
        catch (NoSuchFieldError nosuchfielderror3) { }
        try
        {
            ai1[MicView.MicState.PAUSE.ordinal()] = 4;
        }
        catch (NoSuchFieldError nosuchfielderror4) { }
        try
        {
            ai1[MicView.MicState.QUIT.ordinal()] = 6;
        }
        catch (NoSuchFieldError nosuchfielderror5) { }
        try
        {
            ai1[MicView.MicState.RESUME.ordinal()] = 8;
        }
        catch (NoSuchFieldError nosuchfielderror6) { }
        try
        {
            ai1[MicView.MicState.SLEEPING.ordinal()] = 5;
        }
        catch (NoSuchFieldError nosuchfielderror7) { }
        $SWITCH_TABLE$nuance$com$MicView$MicState = ai1;
        return ai1;
    }

    public NuanceMicButton(Context context)
    {
        super(context);
        mIsDisconnected = false;
        mIsOn = false;
        mIsOff = false;
        mIsPause = false;
        mIsSleeping = false;
        mIsQuit = false;
        mIsPressed = false;
    }

    public NuanceMicButton(Context context, AttributeSet attributeset)
    {
        super(context, attributeset);
        mIsDisconnected = false;
        mIsOn = false;
        mIsOff = false;
        mIsPause = false;
        mIsSleeping = false;
        mIsQuit = false;
        mIsPressed = false;
    }

    public NuanceMicButton(Context context, AttributeSet attributeset, int i)
    {
        super(context, attributeset, i);
        mIsDisconnected = false;
        mIsOn = false;
        mIsOff = false;
        mIsPause = false;
        mIsSleeping = false;
        mIsQuit = false;
        mIsPressed = false;
    }

    protected int[] onCreateDrawableState(int i)
    {
        int ai[] = super.onCreateDrawableState(i + 7);
        if (mIsPressed)
            mergeDrawableStates(ai, PRESSED);
        if (mIsDisconnected)
            mergeDrawableStates(ai, DISCONNECTED);
        if (mIsOff)
            mergeDrawableStates(ai, OFF);
        if (mIsPause)
            mergeDrawableStates(ai, PAUSE);
        if (mIsOn)
            mergeDrawableStates(ai, ON);
        if (mIsSleeping)
            mergeDrawableStates(ai, SLEEPING);
        if (mIsQuit)
            mergeDrawableStates(ai, QUIT);
        return ai;
    }

    public void setPressed(boolean flag)
    {
        mIsPressed = flag;
    }

    public void setState(MicView.MicState micstate)
    {
        mIsDisconnected = false;
        mIsOn = false;
        mIsOff = false;
        mIsPause = false;
        mIsQuit = false;
        mIsSleeping = false;
        switch ($SWITCH_TABLE$nuance$com$MicView$MicState()[micstate.ordinal()])
        {
            case 6: // '\006'
                mIsQuit = true;
                break;

            case 5: // '\005'
                mIsSleeping = true;
                break;

            case 4: // '\004'
                mIsPause = true;
                break;

            case 3: // '\003'
                mIsOff = true;
                break;

            case 2: // '\002'
                mIsOn = true;
                break;

            case 1: // '\001'
                mIsDisconnected = true;
                break;
        }
        refreshDrawableState();
    }

}*/
