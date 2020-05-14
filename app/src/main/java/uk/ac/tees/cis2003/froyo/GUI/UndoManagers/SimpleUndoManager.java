package uk.ac.tees.cis2003.froyo.GUI.UndoManagers;

import android.text.Editable;

import java.util.ArrayList;

import uk.ac.tees.cis2003.froyo.GUI.NumberedTextView;
import uk.ac.tees.cis2003.froyo.Utils.StringUtils;

public class SimpleUndoManager implements NumberedTextView.UndoManager {

    private ArrayList<String> undos;
    private ArrayList<String> redos;
    private boolean recordChanges;

    private NumberedTextView editor;
    private int lines;

    public SimpleUndoManager(NumberedTextView editor){
        undos = new ArrayList<>();
        redos = new ArrayList<>();
        recordChanges = true;
        this.editor = editor;
    }

    @Override
    public void undo() {
        recordChanges = false;
        redos.add(editor.getText().toString());

        int newCur = StringUtils.indexOfDifference(editor.getText().toString(), undos.get(undos.size() - 1));
        editor.setText(undos.remove(undos.size() - 1));
        editor.setSelection(newCur);

        recordChanges = true;
    }

    @Override
    public void redo() {
        recordChanges = false;
        undos.add(editor.getText().toString());

        int newCur = StringUtils.indexOfDifference(editor.getText().toString(), redos.get(redos.size() - 1));
        editor.setText(redos.remove(redos.size() - 1));
        editor.setSelection(newCur);
        recordChanges = true;
    }

    @Override
    public void clearHistory() {
        undos.clear();
        redos.clear();
        lines = 0;
    }

    @Override
    public boolean canUndo() {
        return undos.size() != 0;
    }

    @Override
    public boolean canRedo() {
        return redos.size() != 0;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        if(recordChanges){
            if(undos.size() == 0 || !undos.get(undos.size() - 1).equals(s.toString())) {
                if(lines != StringUtils.numberOfLines(s.toString())) {
                    lines = StringUtils.numberOfLines(s.toString());
                    undos.add(s.toString());
                }
            }
            redos.clear();
        }
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {}

    @Override
    public void afterTextChanged(Editable s) {}
}