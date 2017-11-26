package com.chatapp.Adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;

/**
 * Created by shaymaa on 8/21/2017.
 */

public class TileViewPagerAdapter  extends FragmentPagerAdapter {

    ArrayList<Fragment> frgaments  = new ArrayList<>() ;
    ArrayList<String> titls  = new ArrayList<>() ;

    public TileViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    void addFragment(Fragment fragment , String title ){
        frgaments.add(fragment) ;
        titls.add(title) ;
    }

    @Override
    public Fragment getItem(int position) {
        return frgaments.get(position);
    }

    @Override
    public int getCount() {
        return frgaments.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return titls.get(position);
    }

}
