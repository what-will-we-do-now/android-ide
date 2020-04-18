package uk.ac.tees.v8036651.mode.GUI;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.View;

import androidx.appcompat.widget.AppCompatEditText;

import java.io.File;

import uk.ac.tees.v8036651.mode.plugins.PluginManager;

public class NumberedTextView extends AppCompatEditText {

    private Paint paint;
    private boolean autoHighlight;
    private boolean autoClose;
    private File fileEdited;

    private AutoFormatter autoHighlightWatcher;
    private AutoClose autoCloseWatcher;


    public NumberedTextView(Context context) {
        super(context);
        paint = new Paint();
        paint.setStyle(Paint.Style.FILL);
        paint.setTextSize(getTextSize());
        setWillNotDraw(false);

        setTypeface(Typeface.MONOSPACE);

        TypedValue typedValue = new TypedValue();

        context.getTheme().resolveAttribute(android.R.attr.textColor, typedValue, true);
        int color = typedValue.data;

        paint.setColor(color);



        postInit();
    }

    public NumberedTextView(Context context, AttributeSet attrs) {
        super(context, attrs, android.R.attr.editTextStyle);
        paint = new Paint();
        paint.setStyle(Paint.Style.FILL);
        paint.setTextSize(getTextSize());
        setWillNotDraw(false);

        setTypeface(Typeface.MONOSPACE);

        TypedValue typedValue = new TypedValue();

        context.getTheme().resolveAttribute(android.R.attr.textColor, typedValue, true);
        int color = typedValue.data;

        paint.setColor(color);
        postInit();
    }

    public NumberedTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, android.R.attr.editTextStyle);
        paint = new Paint();
        paint.setStyle(Paint.Style.FILL);
        paint.setTextSize(getTextSize());
        setWillNotDraw(false);

        setTypeface(Typeface.MONOSPACE);

        TypedValue typedValue = new TypedValue();

        context.getTheme().resolveAttribute(android.R.attr.textColor, typedValue, true);
        int color = typedValue.data;

        paint.setColor(color);
        postInit();
    }

    private final void postInit(){
        autoHighlight = true;
        autoHighlightWatcher = new AutoFormatter(this);
        addTextChangedListener(autoHighlightWatcher);
        autoClose = true;
        autoCloseWatcher = new AutoClose(this);
        addTextChangedListener(autoCloseWatcher);

        //compatibility with physical keyboard
        setOnKeyListener(new HardwareKeyboardListener());
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

    public final void setAutoHighlight(boolean autoHighlight){
        if(autoHighlight == this.autoHighlight){
            return;
        }
        if(autoHighlight){
            addTextChangedListener(autoHighlightWatcher);
        }else{
            removeTextChangedListener(autoHighlightWatcher);
        }
        this.autoHighlight = autoHighlight;
    }

    public final boolean getAutoHighlight(){
        return autoHighlight;
    }

    public final void setAutoClose(boolean autoClose){
        if(this.autoClose == autoClose){
            return;
        }
        if(autoClose){
            addTextChangedListener(autoCloseWatcher);
        }else{
            removeTextChangedListener(autoCloseWatcher);
        }
        this.autoClose = autoClose;
    }

    public final boolean getAutoClose(){
        return autoClose;
    }

    public void setFileEdited(File file){
        this.fileEdited = file;
    }

    public File getFileEdited(){
        return this.fileEdited;
    }

    public class AutoFormatter implements TextWatcher {

        private NumberedTextView edit;
        private boolean ignore;

        public AutoFormatter(NumberedTextView edit){
            this.edit = edit;
            this.ignore = false;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {}

        @Override
        public void afterTextChanged(Editable s) {
            if(!ignore) {
                ignore = true;
                if(getFileEdited() != null) {
                    PluginManager.formatText(edit, getFileEdited());
                }
                ignore = false;
            }
        }
    }

    public class AutoClose implements TextWatcher {

        private NumberedTextView edit;
        private boolean ignore;

        public AutoClose(NumberedTextView edit){
            this.edit = edit;
            ignore = false;
        }


        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if(!ignore) {
                ignore = true;
                if (count == 1 && s.charAt(start) == '(') {
                    if(edit.getText().toString().replaceAll("([^(|^)])", "").length() % 2 != 0) {
                        edit.getText().insert(start + count, ")");
                        edit.setSelection(start + count);
                    }
                }else if (count == 1 && s.charAt(start) == '{') {
                    if(edit.getText().toString().replaceAll("([^{|^}])", "").length() % 2 != 0) {
                        edit.getText().insert(start + count, "}");
                        edit.setSelection(start + count);
                    }
                }else if (count == 1 && s.charAt(start) == '[') {
                    if(edit.getText().toString().replaceAll("([^\\[|^\\]])", "").length() % 2 != 0) {
                        edit.getText().insert(start + count, "]");
                        edit.setSelection(start + count);
                    }
                }else if (count == 1 && s.charAt(start) == '"') {
                    if(s.length() > start + 1 && s.charAt(start + 1) == '\"'){
                        String pre = edit.getText().toString().substring(0, start) + edit.getText().toString().substring(start + count);
                        //check if the brackets were balanced before
                        if(pre.replaceAll("[^\"]", "").length() % 2 == 0){
                            edit.setText(pre);
                            edit.setSelection(start + count);
                        }
                    }else if(edit.getText().toString().replaceAll("[^\"]", "").length() % 2 != 0) {
                        edit.getText().insert(start + count, "\"");
                        edit.setSelection(start + count);
                    }
                }else if (count == 1 && s.charAt(start) == '\'') {
                    if(s.length() > start + 1 && s.charAt(start + 1) == '\''){
                        String pre = edit.getText().toString().substring(0, start) + edit.getText().toString().substring(start + count);
                        //check if the brackets were balanced before
                        if(pre.replaceAll("[^\']", "").length() % 2 == 0){
                            edit.setText(pre);
                            edit.setSelection(start + count);
                        }
                    }else if(edit.getText().toString().replaceAll("[^\']", "").length() % 2 != 0) {
                        edit.getText().insert(start + count, "\'");
                        edit.setSelection(start + count);
                    }
                }else if(count == 1 && s.charAt(start) == ')'){
                    if(s.length() > start + 1 && s.charAt(start + 1) == ')'){
                        String pre = edit.getText().toString().substring(0, start) + edit.getText().toString().substring(start + count);
                        //check if the brackets were balanced before
                        if(pre.replaceAll("([^(|^)])", "").length() % 2 == 0){
                            edit.setText(pre);
                            edit.setSelection(start + count);
                        }
                    }
                }else if(count == 1 && s.charAt(start) == '}'){
                    if(s.length() > start + 1 && s.charAt(start + 1) == '}'){
                        String pre = edit.getText().toString().substring(0, start) + edit.getText().toString().substring(start + count);
                        //check if the brackets were balanced before
                        if(pre.replaceAll("([^{|^}])", "").length() % 2 == 0){
                            edit.setText(pre);
                            edit.setSelection(start + count);
                        }
                    }
                }else if(count == 1 && s.charAt(start) == ']'){
                    if(s.length() > start + 1 && s.charAt(start + 1) == ']'){
                        String pre = edit.getText().toString().substring(0, start) + edit.getText().toString().substring(start + count);
                        //check if the brackets were balanced before
                        if(pre.replaceAll("([^\\[|^\\]])", "").length() % 2 == 0){
                            edit.setText(pre);
                            edit.setSelection(start + count);
                        }
                    }
                }
                ignore = false;
            }
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    }

    public class HardwareKeyboardListener implements OnKeyListener{

        @Override
        public boolean onKey(View v, int keyCode, KeyEvent event) {
            if(!event.isCanceled() && keyCode == KeyEvent.KEYCODE_TAB && event.getAction() == KeyEvent.ACTION_UP){
                NumberedTextView txtCode = ((NumberedTextView) v);
                txtCode.getText().insert(txtCode.getSelectionStart(), "  ");
                return true;
            }
            return false;
        }
    }
}