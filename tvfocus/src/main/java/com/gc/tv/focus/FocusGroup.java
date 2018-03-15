package com.gc.tv.focus;

import android.graphics.Rect;
import android.view.View;

/**
 * Created by gaochao on 2018/1/19.
 */

public interface FocusGroup {
    public static enum Relation {
        ONE,
        MANY
    }
    //焦点进入
    void enterFocus(FocusGroup fromFocus, View focus);

    void toFocus(FocusGroup toFocus);
    //判断是否包含该焦点
    boolean interceptFocus(View nextFocus);
    //是否拦截方向
    boolean directionIntercept(int direction);
    //校验焦点
    void verifyFocus(View newFocus);

    void verifyFocus(FocusGroup fromFocusGroup, View newFocus);
    //设置方向拦截器
    void setOnFocusGroupIntercept(OnFocusDirectionIntercept listener);

    boolean requestFocus(int direction, Rect previouslyFocusedRect);
}
