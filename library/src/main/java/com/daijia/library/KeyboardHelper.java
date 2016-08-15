package com.daijia.library;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.support.v4.view.ViewCompat;
import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.PopupWindow;

import java.lang.ref.WeakReference;
import java.lang.reflect.Field;

/**
 * Created by flb on 16/8/1.
 */
public class KeyboardHelper {

    private EditText mTargetEditText;
    private PopupWindow mKeyboardPopupWindow;
    private ViewGroup rootView;
    private int[] mPopupWindowLocation = new int[2];
    private int mInputType;
    private WeakReference<Activity> weakReference;

    public KeyboardHelper(Activity activity,EditText targetEdit){
        mTargetEditText = targetEdit;
        rootView = (ViewGroup) activity.findViewById(android.R.id.content);
        mInputType = targetEdit.getInputType();
        mTargetEditText.setInputType(InputType.TYPE_NULL);
        mTargetEditText.setTextIsSelectable(true);
        weakReference = new WeakReference<>(activity);
    }

    public void setContentView(View contentView){
        mKeyboardPopupWindow = createPopupWindow(contentView);

        mTargetEditText.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                Activity activity = weakReference.get();

                if(activity!=null){
                    View focus = activity.getCurrentFocus();

                    if(focus!=null){
                        InputMethodManager imm = (InputMethodManager)activity.getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(focus.getWindowToken(), 0);

                        focus.clearFocus();
                    }

                }




                showKeyboard();

                return false;
            }
        });
    }

    private PopupWindow createPopupWindow(View contentView){
        PopupWindow popupWindow = new PopupWindow(mTargetEditText.getContext()){
            @Override
            public void dismiss() {
                super.dismiss();
                translateAnimation(0,0);
            }
        };
        popupWindow.setContentView(contentView);
        popupWindow.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        popupWindow.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
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

        mKeyboardPopupWindow.showAtLocation(rootView, Gravity.BOTTOM,0,0);

        hookPopupOnTouchEvent();
    }

    void translateAnimation(int x,int y){
        ViewCompat.animate(rootView.getChildAt(0)).x(x).y(y).setDuration(200).start();
    }

    private void hookPopupOnTouchEvent(){
        try {
            Field field;
            if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M){
                field = PopupWindow.class.getDeclaredField("mDecorView");
            }else {
                field = PopupWindow.class.getDeclaredField("mPopupView");
            }

            field.setAccessible(true);
            final View decorView = (View) field.get(mKeyboardPopupWindow);

            decorView.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
                @Override
                public boolean onPreDraw() {

                    decorView.getLocationOnScreen(mPopupWindowLocation);
                    int[] editLoc = new int[2];
                    mTargetEditText.getLocationOnScreen(editLoc);

                    if(editLoc[1]+mTargetEditText.getMeasuredHeight()>mPopupWindowLocation[1]){
                        translateAnimation(0,mPopupWindowLocation[1]-(editLoc[1]+(int)1.5f*mTargetEditText.getMeasuredHeight()));
                    }

                    mTargetEditText.setInputType(mInputType);
                    decorView.getViewTreeObserver().removeOnPreDrawListener(this);
                    return false;
                }
            });

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

                        if((x>loc[0]&&x<loc[0]+editWidth)
                                &&(y>loc[1]&&y<loc[1]+editHeight)){
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
