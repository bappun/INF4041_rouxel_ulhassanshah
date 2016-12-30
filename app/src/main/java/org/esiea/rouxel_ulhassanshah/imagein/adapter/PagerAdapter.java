package org.esiea.rouxel_ulhassanshah.imagein.adapter;

/**
 * Created by bachi on 30/12/16.
 */


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import org.esiea.rouxel_ulhassanshah.imagein.fragment.ExtrasFragment;
import org.esiea.rouxel_ulhassanshah.imagein.fragment.UploadFragment;

public class PagerAdapter extends FragmentStatePagerAdapter {
    int mNumOfTabs;

    public PagerAdapter(FragmentManager fm, int NumOfTabs) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                UploadFragment tab1 = new UploadFragment();
                return tab1;
            case 1:
                ExtrasFragment tab2 = new ExtrasFragment();
                return tab2;

            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }
}
