package com.myhindlab.abkat.activities.regularcampcreation;

import android.content.Context;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.myhindlab.abkat.R;


/**
 * A [FragmentPagerAdapter] that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
public class SectionsPagerAdapter extends FragmentStatePagerAdapter {

    @StringRes
    private static final int[] TAB_TITLES = new int[]{R.string.site_selection, R.string.test_mapping, R.string.device_allocation, R.string.consumable_allocation, R.string.resource_allocation};
    private final Context mContext;
    private FragmentChange fragmentChange;

    public SectionsPagerAdapter(Context context, FragmentManager fm, FragmentChange fragmentChange) {
        super(fm);
        mContext = context;
        this.fragmentChange = fragmentChange;
    }

    @Override
    public Fragment getItem(int position) {
        // getItem is called to instantiate the fragment for the given page.
        // Return a PlaceholderFragment (defined as a static inner class below).
        switch (position) {
            case 0:
                return DeviceAllocationFragment.newInstance("", "", fragmentChange);
            case 1:
                return ConsumableAllocationFragment.newInstance("", "", fragmentChange);
            case 2:
                return ResourceAllocationFragment.newInstance("", "");
            default:

                return DeviceAllocationFragment.newInstance("", "", fragmentChange);

        //    return PlaceholderFragment.newInstance(position + 1);

        }
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return mContext.getResources().getString(TAB_TITLES[position]);
    }

    @Override
    public int getCount() {
        // Show 2 total pages.
        return TAB_TITLES.length;
    }


}