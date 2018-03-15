package gc.myapplication;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * viewpager嵌套的fragment的adapter
 * @author luoxiandong
 *
 */
public class PageFragmentAdapter extends FragmentPagerAdapter {
	
	private Fragment[] mFragments = null;

	public PageFragmentAdapter(FragmentManager fm, Fragment[] fragments) {
		super(fm);
		this.mFragments = fragments;
	}

	@Override
	public Fragment getItem(int arg0) {
		return mFragments[arg0];
	}

	@Override
	public int getCount() {
		if(mFragments != null)
			return mFragments.length;
		return 0;
	}

}
