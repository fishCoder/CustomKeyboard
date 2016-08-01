package com.daijia.library;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.text.InputType;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.PopupWindow;

import java.lang.reflect.Field;

/**
 * Created by flb on 16/8/1.
 */
public class KeyboardHelper {

    private EditText mTargetEditText;
    private PopupWindow mKeyboardPopupWindow;
    private View rootView;
    public KeyboardHelper(Activity activity,EditText targetEdit){
        mTargetEditText = targetEdit;
        rootView = activity.findViewById(android.R.id.content);
    }

    public void setContentView(View contentView){
        mKeyboardPopupWindow = createPopupWindow(contentView);

        mTargetEditText.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                showKeyboard();

                return false;
            }
        });
    }

    private PopupWindow createPopupWindow(View contentView){
        PopupWindow popupWindow = new PopupWindow(mTargetEditText.getContext());
        popupWindow.setContentView(contentView);
        popupWindow.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        popupWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        popupWindow.setTouchable(true);
        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setAnimationStyle(android.R.style.Animation_InputMethod);
        return popupWindow;
    }


    public void showKeyboard(){
        if(mKeyboardPopupWindow == null || mKeyboardPopupWindow.isShowing()){
           return;
        }

        int inputType = mTargetEditText.getInputType();
        mTargetEditText.setInputType(InputType.TYPE_NULL);
        mKeyboardPopupWindow.showAtLocation(rootView, Gravity.BOTTOM,0,0);
        mTargetEditText.setInputType(inputType);

        hookPopupOnTouchEvent();
    }

    private void hookPopupOnTouchEvent(){
        try {
            Field field = PopupWindow.class.getDeclaredField("mDecorView");
            field.setAccessible(true);
            View decorView = (View) field.get(mKeyboardPopupWindow);
            decorView.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    int[] decorLoc = new int[2];
                    v.getLocationOnScreen(decorLoc);

                    final int x = (int) event.getX();
                    final int y = decorLoc[1] + (int) event.getY();

                    if ((event.getAction() == MotionEvent.ACTION_DOWN)
                            && y<decorLoc[1] ) {

                        int editHeight = getTargetEditTextHeight();
                        int editWidth = getTargetEditTextWidth();

                        int[] loc = getTargetEditTextLocation();

                        if(x>loc[0]&&x<loc[0]+editWidth
                                &&y>loc[0]&&y<loc[1]+editHeight){
                            setTargetEditTextSelection(x-loc[0], y-loc[0]);
                        }else {
                            dismiss();

                        }

                        return true;
                    } else if (event.getAction() == MotionEvent.ACTION_OUTSIDE) {
                        dismiss();
                        return true;
                    } else {
                        return false;
                    }
                }
            });
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }


    public void dismiss(){
        if(mKeyboardPopupWindow != null){
            mKeyboardPopupWindow.dismiss();
        }
    }

    protected int  getTargetEditTextHeight(){
        return mTargetEditText.getMeasuredHeight();
    }

    protected int getTargetEditTextWidth(){
        return mTargetEditText.getMeasuredWidth();
    }

    protected int[] getTargetEditTextLocation(){
        int[] loc = new int[2];
        mTargetEditText.getLocationOnScreen(loc);
        return loc;
    }


    protected void setTargetEditTextSelection(int x,int y){
        int touchPosition = mTargetEditText.getOffsetForPosition(x, y);
        if (touchPosition  > 0){
            mTargetEditText.setSelection(touchPosition);
        }
    }
}
