package com.daijia.library;

import android.inputmethodservice.Keyboard;
import android.inputmethodservice.KeyboardView;
import android.text.Editable;
import android.widget.EditText;

/**
 * Created by flb on 16/8/1.
 */
public class SimpleListener implements KeyboardView.OnKeyboardActionListener {

    EditText ed;

    public SimpleListener(EditText editText){
        ed = editText;
    }

    @Override
    public void swipeUp() {
    }

    @Override
    public void swipeRight() {
    }

    @Override
    public void swipeLeft() {
    }

    @Override
    public void swipeDown() {
    }

    @Override
    public void onText(CharSequence text) {
    }

    @Override
    public void onRelease(int primaryCode) {
    }

    @Override
    public void onPress(int primaryCode) {
    }
    //一些特殊操作按键的codes是固定的比如完成、回退等
    @Override
    public void onKey(int primaryCode, int[] keyCodes) {
        Editable editable = ed.getText();
        int start = ed.getSelectionStart();
        if (primaryCode == Keyboard.KEYCODE_DELETE) {// 回退
            if (editable != null && editable.length() > 0) {
                if (start > 0) {
                    editable.delete(start - 1, start);
                }
            }
        }else if (primaryCode == 4896) {// 清空
            editable.clear();
        } else { //将要输入的数字现在编辑框中
            editable.insert(start, Character.toString((char) primaryCode));
        }
    }

}
