package gc.myapplication;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import java.util.Iterator;
import java.util.List;

/**
 * Created by gaochao on 2018/1/20.
 */

public class Tools {
    // px 转 dip
    public static int dip2px(Context context, float dipValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }
    /**
     * 切换Fragment
     */
    public static Fragment swichFragment(Class<? extends Fragment> clazz,
                                         int contentLayoutId, Bundle bundle, FragmentActivity fActivity) {
        String className = clazz.getName();
        FragmentManager fm = fActivity.getSupportFragmentManager();

        // 构建目标fragment
        Fragment tagFragment = fm.findFragmentByTag(className);
        FragmentTransaction ft = fm.beginTransaction();

        if (tagFragment == null) {
            try {
                tagFragment = (Fragment) clazz.newInstance();
                if (bundle != null) {
                    tagFragment.setArguments(bundle);
                }
                ft.add(contentLayoutId, tagFragment, className);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InstantiationException e) {
                e.printStackTrace();
            }
        }

        if (tagFragment.isVisible())// 避免重复显示隐藏
            return tagFragment;
        ft.attach(tagFragment);
        ft.show(tagFragment);
        // 隐藏和显示相应的fragment
        List<Fragment> fragments = fm.getFragments();
        if (fragments != null) {
            Iterator<Fragment> it = fragments.iterator();
            Fragment fragment = null;
            while (it.hasNext()) {
                fragment = it.next();
                if (!className.equals(fragment.getTag())) {
                    ft.hide(fragment);
                    ft.detach(fragment);
                }
            }
        }
        ft.commitAllowingStateLoss();
        return tagFragment;
    }

}
