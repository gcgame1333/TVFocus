package gc.myapplication;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.Button;

import com.gc.tv.focus.views.FocusWindow;


public class MainActivity extends FragmentActivity {

    private ViewPager viewPager;
    private Fragment pager[] = {new ChildFragment("列表1"), new ChildFragment("列表2"), new ChildFragment("列表3"), new ChildFragment("列表4")};
    private Button tab[] = new Button[4];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        viewPager = findViewById(R.id.viewPager);
        tab[0] = findViewById(R.id.tab1);
        tab[1] = findViewById(R.id.tab2);
        tab[2] = findViewById(R.id.tab3);
        tab[3] = findViewById(R.id.tab4);
        tab[0].setOnFocusChangeListener(changeListener);
        tab[0].setTag(0);
        tab[1].setOnFocusChangeListener(changeListener);
        tab[1].setTag(1);
        tab[2].setOnFocusChangeListener(changeListener);
        tab[2].setTag(2);
        tab[3].setOnFocusChangeListener(changeListener);
        tab[3].setTag(3);
        viewPager.setAdapter(new PageFragmentAdapter(getSupportFragmentManager(), pager));
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

                FocusWindow.findFocusGroup(tab[0]).verifyFocus(tab[position]);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        Tools.swichFragment(RightLayout.class, R.id.rightLayout, null, this);
//        FocusWindow.findFocusGroup(viewPager).setOnFocusGroupIntercept(new OnFocusDirectionIntercept() {
//            @Override
//            public boolean interceptDirection(int direction) {
//                if (direction == View.FOCUS_LEFT) {
//                    return viewPager.getCurrentItem() != 0;
//                } else if (direction == View.FOCUS_RIGHT) {
//                    return viewPager.getCurrentItem() + 1 != viewPager.getAdapter().getCount();
//                }
//                return false;
//            }
//        });
    }

    private View.OnFocusChangeListener changeListener = new View.OnFocusChangeListener() {
        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            viewPager.setCurrentItem((Integer) v.getTag());
        }
    };

    @Override
    public void onBackPressed() {
        finish();
    }
}
