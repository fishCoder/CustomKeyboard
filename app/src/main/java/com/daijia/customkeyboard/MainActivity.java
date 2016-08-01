package com.daijia.customkeyboard;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.inputmethodservice.KeyboardView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.PopupWindow;

import com.daijia.library.CustomKeyboard;

import java.lang.reflect.Field;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final EditText editText = (EditText) findViewById(R.id.edit_text);

        CustomKeyboard.showKeyboardNumberX(this,editText);

//        editText.setOnTouchListener(new View.OnTouchListener() {
//
//            PopupWindow popupWindow ;
//            @Override
//            public boolean onTouch(View view, MotionEvent motionEvent) {
//
//
//                if(popupWindow == null){
//                    popupWindow = new PopupWindow(MainActivity.this);
//                    KeyboardView keyboardView = (KeyboardView) getLayoutInflater().inflate(R.layout.keyborad,null);
//                    new KeyboardUtil(MainActivity.this,keyboardView,editText).showKeyboard();
//                    popupWindow.setContentView(keyboardView);
//                    popupWindow.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
//                    popupWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//                    popupWindow.setTouchable(true);
//                    popupWindow.setFocusable(true);
//                    popupWindow.setOutsideTouchable(true);
//                    popupWindow.setAnimationStyle(android.R.style.Animation_InputMethod);
//                }
//
//                if(!popupWindow.isShowing()){
//                    int inputType = editText.getInputType();
//                    editText.setInputType(InputType.TYPE_NULL);
//                    popupWindow.showAtLocation(findViewById(android.R.id.content), Gravity.BOTTOM,0,0);
//                    editText.setInputType(inputType);
//
//
//                    try {
//                        Field field = PopupWindow.class.getDeclaredField("mDecorView");
//                        field.setAccessible(true);
//                        final View decorView = (View) field.get(popupWindow);
//                        decorView.setOnTouchListener(new View.OnTouchListener() {
//                            @Override
//                            public boolean onTouch(View v, MotionEvent event) {
//
//                                int[] decorLoc = new int[2];
//                                v.getLocationOnScreen(decorLoc);
//
//                                final int x = (int) event.getX();
//                                final int y = decorLoc[1] + (int) event.getY();
//
//                                Log.d("event","x:"+x+"   y:"+y);
//
//                                if ((event.getAction() == MotionEvent.ACTION_DOWN)
//                                     && (y<decorLoc[1])) {
//
//                                    int editHeight = editText.getMeasuredHeight();
//                                    int editWidth = editText.getMeasuredWidth();
//
//                                    int[] loc = new int[2];
//                                    editText.getLocationOnScreen(loc);
//
//
//                                    if(x>loc[0]&&x<loc[0]+editWidth
//                                            &&y>loc[0]&&y<loc[1]+editHeight){
//                                        int touchPosition = editText.getOffsetForPosition(x-loc[0], y-loc[0]);
//                                        if (touchPosition  > 0){
//                                            editText.setSelection(touchPosition);
//                                        }
//                                    }else {
//                                        popupWindow.dismiss();
//                                    }
//
//
//                                    return true;
//                                } else if (event.getAction() == MotionEvent.ACTION_OUTSIDE) {
//                                    popupWindow.dismiss();
//                                    return true;
//                                } else {
//                                    return false;
//                                }
//                            }
//                        });
//                    } catch (NoSuchFieldException e) {
//                        e.printStackTrace();
//                    } catch (IllegalAccessException e) {
//                        e.printStackTrace();
//                    }
//                }
//
//                return true;
//            }
//        });
    }
}
