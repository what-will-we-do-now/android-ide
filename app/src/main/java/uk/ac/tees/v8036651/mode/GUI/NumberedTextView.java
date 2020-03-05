package uk.ac.tees.v8036651.mode.GUI;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.inputmethod.InputConnection;
import android.view.inputmethod.InputConnectionWrapper;

import androidx.appcompat.widget.AppCompatEditText;

public class NumberedTextView extends AppCompatEditText {

    private Paint paint;
    private String language;


    public NumberedTextView(Context context) {
        super(context);
        paint = new Paint();
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.WHITE);
        paint.setTextSize(getTextSize());
        setWillNotDraw(false);
    }

    public NumberedTextView(Context context, AttributeSet attrs) {
        super(context, attrs, android.R.attr.editTextStyle);
        paint = new Paint();
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.WHITE);
        paint.setTextSize(getTextSize());
        setWillNotDraw(false);
    }

    public NumberedTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, android.R.attr.editTextStyle);
        paint = new Paint();
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.WHITE);
        paint.setTextSize(getTextSize());
        setWillNotDraw(false);
    }

    @Override
    protected void onDraw(Canvas canvas){
        super.onDraw(canvas);
        int top = getBaseline();

        for(int x = 0; x < getLineCount(); x++){
            canvas.drawText(String.valueOf(x + 1), getScrollX() + 2, top, paint);
            top += getLineHeight();
        }
        //set padding to always adjust to the length of line count
        setPadding((int) (getTextSize() * (String.valueOf(getLineCount()).length())), 0, 0, 0);
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getLanguage() {
        return language;
    }


    /**
     * Dear Google
     *
     * Keyboard is a keyboard
     * no matter whatever it is on screen or a physical one.
     * If you want to see a world where in future
     * some websites work only if you use hardware keyboard
     * and others only work if you use on screen keyboard
     * then maybe you should retire.
     *
     * Kind Regards
     * Dominik Sysojew - Osinski @ MoDE
     *
     * /
    private class KeyboardConnection extends InputConnectionWrapper {

        /**
         * Initializes a wrapper.
         *
         * <p><b>Caveat:</b> Although the system can accept {@code (InputConnection) null} in some
         * places, you cannot emulate such a behavior by non-null {@link InputConnectionWrapper} that
         * has {@code null} in {@code target}.</p>
         *
         * @param target  the {@link InputConnection} to be proxied.
         * @param mutable set {@code true} to protect this object from being reconfigured to target
         *                another {@link InputConnection}.  Note that this is ignored while the target is {@code null}.
         * /
        public KeyboardConnection(InputConnection target, boolean mutable) {
            super(target, mutable);
        }

        @Override
        public boolean sendKeyEvent(KeyEvent event){



            return super.sendKeyEvent(event);
        }
    }*/
}