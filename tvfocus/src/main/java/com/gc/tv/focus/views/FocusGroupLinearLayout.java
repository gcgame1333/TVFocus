package com.gc.tv.focus.views;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

import com.gc.tv.focus.FocusGroup;
import com.gc.tv.focus.OnFocusDirectionIntercept;
import com.gc.tv.focus.SimpleFocusGroup;

/**
 * Created by gaochao on 2018/1/21.
 */

public class FocusGroupLinearLayout extends LinearLayout implements FocusGroup, SimpleFocusGroup.RequestFocus {

    private FocusGroup myFocusGroup = null;

    public FocusGroupLinearLayout(Context context) {
        super(context);
        init(null);
    }

    public FocusGroupLinearLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public FocusGroupLinearLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        myFocusGroup = new SimpleFocusGroup(this,attrs);
    }

    @Override
    public void enterFocus(FocusGroup fromFocus,View nextFocus) {
        myFocusGroup.enterFocus(fromFocus,nextFocus);
    }

    @Override
    public void toFocus(FocusGroup toFocus) {
        myFocusGroup.toFocus(toFocus);
    }

    @Override
    public boolean interceptFocus(View nextFocus) {
        return myFocusGroup.interceptFocus(nextFocus);
    }

    @Override
    public boolean directionIntercept(int direction) {
        return myFocusGroup.directionIntercept(direction);
    }

    @Override
    public void verifyFocus(View newFocus) {
        myFocusGroup.verifyFocus(newFocus);
    }

    @Override
    public void verifyFocus(FocusGroup fromFocusGroup, View newFocus) {
        myFocusGroup.verifyFocus(fromFocusGroup,newFocus);
    }

    public boolean requestFocus(int direction, Rect previouslyFocusedRect) {
        return myFocusGroup.requestFocus(direction, previouslyFocusedRect);
    }

    @Override
    public void setOnFocusGroupIntercept(OnFocusDirectionIntercept listener) {
        myFocusGroup.setOnFocusGroupIntercept(listener);
    }

    @Override
    public boolean superRequestFocus(int direction) {
        return super.requestFocus(direction, null);
    }
}
