package com.daijia.library;

import android.app.Activity;
import android.inputmethodservice.Keyboard;
import android.inputmethodservice.KeyboardView;
import android.view.View;
import android.widget.EditText;

/**
 * Created by flb on 16/8/1.
 */
public class CustomKeyboard {

    public static void showKeyboardNumberX(Activity activity,EditText targetView){

        KeyboardView contentView = (KeyboardView) activity.getLayoutInflater().inflate(R.layout.keyborad,null);
        contentView.setKeyboard(new Keyboard(activity,R.xml.symbols_x));
        contentView.setEnabled(true);
        contentView.setPreviewEnabled(false);
        contentView.setVisibility(View.VISIBLE);
        contentView.setOnKeyboardActionListener(new SimpleListener(targetView));

        KeyboardHelper helper = new KeyboardHelper(activity,targetView);
        helper.setContentView(contentView);

    }
}
