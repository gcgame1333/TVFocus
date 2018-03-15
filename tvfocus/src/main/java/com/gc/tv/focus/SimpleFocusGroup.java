package com.gc.tv.focus;

import android.content.res.TypedArray;
import android.graphics.Rect;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import com.gc.tv.focus.views.FocusWindow;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by gaochao on 2018/1/22.
 * 焦点组控件
 */

public class SimpleFocusGroup implements FocusGroup {

    public interface RequestFocus {
        boolean superRequestFocus(int direction);
    }

    private Relation relation = Relation.ONE;

    private SparseArray<View> relationMap = new SparseArray<>();

    private List<View> viewGroup = new ArrayList<>();
    private ViewGroup curLayout = null;


    public SimpleFocusGroup(ViewGroup viewGroup, AttributeSet attrs) {
        curLayout = viewGroup;
        viewGroup.setOnHierarchyChangeListener(onHierarchyChangeListener);
        if (attrs != null) {
            TypedArray attributes = viewGroup.getContext().getTheme().obtainStyledAttributes(attrs, R.styleable.FocusGroup, 0, 0);
            int r = attributes.getInt(R.styleable.FocusGroup_focus_relation, 1);
            if (r == 2) {
                relation = Relation.MANY;
            }
            attributes.recycle();
        }
    }

    public ViewGroup.OnHierarchyChangeListener onHierarchyChangeListener = new ViewGroup.OnHierarchyChangeListener() {
        @Override
        public void onChildViewAdded(View parent, View child) {
            addFocusView(child);
        }

        @Override
        public void onChildViewRemoved(View parent, View child) {
            viewGroup.remove(child);
            if (curFocus == child)
                curFocus = null;
            if (relation == Relation.MANY) {
                int size = relationMap.size();
                for (int i = 0; i < size; i++) {
                    View v = relationMap.get(relationMap.keyAt(i));
                    if (v == child) {
                        relationMap.removeAt(i);
                        break;
                    }
                }
            }

        }
    };

    /**
     * 处理特殊组件的焦点事件
     *
     * @param v
     */
    private void handleSpecialView(View v) {
        if (v instanceof ViewPager) {
            final ViewPager viewPager = (ViewPager) v;

            ViewGroup parent = (ViewGroup) v.getParent();

            if (parent instanceof FocusGroup) {

                //重写viewPage左右焦点事件，告诉FocusGroup如何处理边界焦点
                ((FocusGroup) (parent)).setOnFocusGroupIntercept(new OnFocusDirectionIntercept() {
                    @Override
                    public boolean interceptDirection(int direction) {
                        if (viewPager.getAdapter() != null) {
                            if (direction == View.FOCUS_LEFT) {
                                return viewPager.getCurrentItem() != 0;
                            } else if (direction == View.FOCUS_RIGHT) {
                                return viewPager.getCurrentItem() + 1 != viewPager.getAdapter().getCount();
                            }
                        }
                        return false;
                    }
                });
            }
        } else if (v instanceof RecyclerView) {
            final RecyclerView recyclerView = (RecyclerView) v;
            ViewGroup parent = (ViewGroup) v.getParent();
            if (parent instanceof FocusGroup) {

                ((FocusGroup) (parent)).setOnFocusGroupIntercept(new OnFocusDirectionIntercept() {
                    LinearLayoutManager linearLayoutManager = null;

                    @Override
                    public boolean interceptDirection(int direction) {

                        if (null == linearLayoutManager) {
                            RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
                            linearLayoutManager = layoutManager instanceof LinearLayoutManager ? (LinearLayoutManager) layoutManager : null;
                        }

                        if (recyclerView.getAdapter() != null && null != linearLayoutManager) {
                            if (direction == View.FOCUS_UP) {
                                return linearLayoutManager.findFirstVisibleItemPosition() != 0;
                            } else if (direction == View.FOCUS_DOWN) {
                                return linearLayoutManager.findLastVisibleItemPosition() != recyclerView.getAdapter().getItemCount() - 1;
                            }
                        }

                        return false;
                    }
                });
            }

        }
    }


    //添加可获得焦点布局控件
    private void addFocusView(View v) {
        if (viewGroup.contains(v))
            return;
        if (v.isFocusable()) {
            viewGroup.add(v);
            handleSpecialView(v);
        }
        if (!(v instanceof FocusGroup) && (v instanceof ViewGroup)) {
            //  Log.i("keyFocus", "addViewGroup:" + v);
            ViewGroup group = (ViewGroup) v;
            group.setOnHierarchyChangeListener(onHierarchyChangeListener);
            int size = group.getChildCount();
            for (int i = 0; i < size; i++) {
                addFocusView(group.getChildAt(i));
            }
        }
    }

    private View curFocus = null;
    private Rect childVisible = new Rect();
    private Rect parentVisible = new Rect();

    //探测是否在有效区域
    private boolean checkViewVisible(View view) {
        ViewParent viewParent = view.getParent();
        if (viewParent == null) {
            return false;
        }
        if (viewParent instanceof View) {
            view.getGlobalVisibleRect(childVisible);
            ((View) viewParent).getGlobalVisibleRect(parentVisible);
            return parentVisible.contains(childVisible);
        }
        return true;
    }

    @Override
    public void enterFocus(FocusGroup fromFocus, View focus) {
        if (relation == Relation.MANY) {
            View f = fromFocus != null ? relationMap.get(fromFocus.hashCode()) : null;
            if (f != null && checkViewVisible(f)) {
                f.requestFocus();
            } else if (curLayout instanceof RequestFocus) {
                if (f != null) {
                    relationMap.remove(fromFocus.hashCode());
                }
                ((RequestFocus) curLayout).superRequestFocus(View.FOCUS_DOWN);
            }
        } else if (relation == Relation.ONE) {
            if (curFocus != null) {
                curFocus.requestFocus();
            } else if (focus != null) {
                focus.requestFocus();
            } else if (curLayout instanceof RequestFocus) {
                ((RequestFocus) curLayout).superRequestFocus(View.FOCUS_DOWN);
            }
        }
    }

    @Override
    public void toFocus(FocusGroup toFocus) {
        //内部组件变化不需要关心
        if (toFocus == curLayout || toFocus == null)
            return;
        if (relation == Relation.MANY) {
            put(toFocus, curFocus);
        }
    }

    private void put(FocusGroup key, View value) {
        if (key != null) {
            relationMap.put(key.hashCode(), value);
        }
    }


    public boolean requestFocus(int direction, Rect previouslyFocusedRect) {
//        Log.i("keyFocus","requestFocus");
        FocusWindow focusWindow = FocusWindow.findFocusWindow(curLayout);
        FocusGroup fg = null;
        if (focusWindow != null) {
            fg = focusWindow.getLastFocusGroup();
        }
        enterFocus(fg, curFocus);
        return true;
    }

    @Override
    public boolean interceptFocus(View nextFocus) {
        //contains 效率高
        if (nextFocus == null || viewGroup.contains(nextFocus)) {
            curFocus = nextFocus;
            return false;
        }
        return true;
    }

    private OnFocusDirectionIntercept onFocusGroupIntercept;

    @Override
    public boolean directionIntercept(int direction) {
        if (onFocusGroupIntercept != null) {
            return onFocusGroupIntercept.interceptDirection(direction);
        }
        return false;
    }

    @Override
    public void verifyFocus(View newFocus) {
        verifyFocus(null, newFocus);
    }

    @Override
    public void verifyFocus(FocusGroup fromFocusGroup, View newFocus) {
        if (newFocus != curFocus) {
            curFocus = newFocus;
            if (relation == Relation.MANY) {
                if (fromFocusGroup != null && fromFocusGroup != curLayout) {
                    put(fromFocusGroup, newFocus);
                }
            }
        }
    }

    @Override
    public void setOnFocusGroupIntercept(OnFocusDirectionIntercept listener) {
        onFocusGroupIntercept = listener;
    }
}
