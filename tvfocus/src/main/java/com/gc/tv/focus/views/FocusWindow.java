package com.gc.tv.focus.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.FocusFinder;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.ViewTreeObserver;
import android.widget.RelativeLayout;

import com.gc.tv.focus.FocusGroup;

/**
 * Created by gaochao on 2018/1/19.
 */

public class FocusWindow extends RelativeLayout {

    public FocusWindow(Context context) {
        super(context);
        init();
    }

    public FocusWindow(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public FocusWindow(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        //校验焦点
        getViewTreeObserver().addOnGlobalFocusChangeListener(new ViewTreeObserver.OnGlobalFocusChangeListener() {
            @Override
            public void onGlobalFocusChanged(View oldFocus, View newFocus) {
                // Log.i("keyFocus_global", "oldFocus:" + oldFocus + " newFocus:" + newFocus);
                FocusGroup mNewFocusGroup = findFocusGroup(newFocus);
                //校验一次
                FocusGroup mOldFocusGroup = lastFocusGroup;
                if (mOldFocusGroup != null) {
                    mOldFocusGroup.toFocus(mNewFocusGroup);
                }
                if (mNewFocusGroup != null) {
                    mNewFocusGroup.verifyFocus(mOldFocusGroup, newFocus);
                }
            }
        });
    }

    //获取当前的组
    public static FocusWindow findFocusWindow(View view) {
        if (view == null)
            return null;
        ViewParent parent = view.getParent();
        if (parent instanceof ViewGroup) {
            ViewGroup mViewGroup = (ViewGroup) parent;
            if (mViewGroup instanceof FocusWindow) {
                return (FocusWindow) mViewGroup;
            } else {
                return findFocusWindow(mViewGroup);
            }
        }
        return null;
    }

    //递归父组件
    public static FocusGroup findFocusGroup(View view) {
        if (view == null)
            return null;
        ViewParent parent = view.getParent();
        if (parent instanceof ViewGroup) {
            ViewGroup mViewGroup = (ViewGroup) parent;
            if (mViewGroup instanceof FocusGroup) {
                return (FocusGroup) mViewGroup;
            } else {
                return findFocusGroup(mViewGroup);
            }
        }
        return null;
    }

    /***
     * 派发方向事件
     * */
    private boolean dispatchDirection(FocusGroup mFocusGroup, int orientation) {
        if (mFocusGroup.directionIntercept(orientation)) {
            return true;
        }
        if (mFocusGroup instanceof View) {
            FocusGroup p = findFocusGroup((View) mFocusGroup);
            if (p != null) {
                return dispatchDirection(p, orientation);
            }
        }
        return false;
    }

    /**
     * 派发焦点事件
     * */
    public boolean dispatchFocus(FocusGroup mFocusGroup, View nextFocus, int orientation) {
        lastFocusGroup = mFocusGroup;
        if (mFocusGroup != null && nextFocus != null && mFocusGroup.interceptFocus(nextFocus)) {
            //判断父级是否拦截
            if (dispatchDirection(mFocusGroup, orientation)) {
                return false;
            }
            FocusGroup mNextFocusGroup = findFocusGroup(nextFocus);
            mFocusGroup.toFocus(mNextFocusGroup);
            if (mNextFocusGroup != null) {
                mNextFocusGroup.enterFocus(mFocusGroup, nextFocus);
                return true;
            }
        }
        return false;
    }

    private FocusGroup lastFocusGroup;

    public FocusGroup getLastFocusGroup() {
        return lastFocusGroup;
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_DOWN) {
            int orientation = 0;
            switch (event.getKeyCode()) {
                case KeyEvent.KEYCODE_DPAD_RIGHT:
                    orientation = View.FOCUS_RIGHT;
                    break;
                case KeyEvent.KEYCODE_DPAD_LEFT:
                    orientation = View.FOCUS_LEFT;
                    break;
                case KeyEvent.KEYCODE_DPAD_DOWN:
                    orientation = View.FOCUS_DOWN;
                    break;
                case KeyEvent.KEYCODE_DPAD_UP:
                    orientation = View.FOCUS_UP;
                    break;
            }
            if (orientation != 0) {
                View curFocus = findFocus();
                View nextFocus = FocusFinder.getInstance().findNextFocus(this, curFocus, orientation);
            //    Log.i("keyFocus", "cur:" + curFocus + "  next:" + nextFocus);
                if (dispatchFocus(findFocusGroup(curFocus), nextFocus, orientation)) {
                    return true;
                }
            }
        }
        return super.dispatchKeyEvent(event);
    }
}
