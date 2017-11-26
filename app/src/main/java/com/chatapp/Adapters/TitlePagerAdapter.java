package com.chatapp.Adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;

/**
 * Created by shaymaa on 8/21/2017.
 */

public class TitlePagerAdapter extends FragmentPagerAdapter {

    ArrayList<Fragment> fragments = new ArrayList<>() ;
    ArrayList<String>  titles = new ArrayList<>() ;
    public TitlePagerAdapter(FragmentManager fm) {
        super(fm);
    }

    public void addFragment(Fragment income_fragment , String income_title){
        fragments.add(income_fragment) ;
        titles.add(income_title) ;
    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return titles.get(position);
    }
}
