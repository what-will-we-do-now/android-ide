package uk.ac.tees.v8036651.mode.GUI;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatEditText;

public class NumberedTextView extends AppCompatEditText {

    private Paint paint;

    public NumberedTextView(Context context) {
        super(context);
        paint = new Paint();
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.WHITE);
        paint.setTextSize(getTextSize());
        setWillNotDraw(false);
    }

    public NumberedTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        paint = new Paint();
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.WHITE);
        paint.setTextSize(getTextSize());
        setWillNotDraw(false);
    }

    public NumberedTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        paint = new Paint();
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.WHITE);
        paint.setTextSize(getTextSize());
        setWillNotDraw(false);
    }

    @Override
    protected void onDraw(Canvas canvas){
        int top = getBaseline();

        for(int x = 0; x < getLineCount(); x++){
            canvas.drawText(String.valueOf(x + 1), getScrollX() + 2, top, paint);
            top += getLineHeight();
        }
        //set padding to always adjust to the length of line count
        setPadding((int) (getTextSize() * (String.valueOf(getLineCount()).length())), 0, 0, 0);
        super.onDraw(canvas);
    }
}