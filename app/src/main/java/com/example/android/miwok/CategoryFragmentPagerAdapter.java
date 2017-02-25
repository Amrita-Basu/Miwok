package com.example.android.miwok;

import android.support.design.widget.TabItem;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentManager;
/**
 * Created by DELL on 25-02-2017.
 */

public class CategoryFragmentPagerAdapter extends FragmentPagerAdapter {


    public CategoryFragmentPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    private String tabTitles[] = new String[] { "NUMBERS", "FAMILY", "COLORS", "PHRASES" };
    @Override
    public Fragment getItem(int position) {



        if(position == 0) {


            return new NumbersFragment();
        }
        else if(position == 1)
            return new FamilyFragment();
        else if(position == 2)
            return new ColoursFragment();
        else

           return new PhrasesFragment();





    }

    @Override
    public int getCount() {
        return 4;
    } // viewpager asks for the count upfront, so that it can be prepared for those many fragments and create those many tabs if tabs are present

    @Override

    public CharSequence getPageTitle(int position) { // when the viewpager interacts with the fragment adapter, it asks upfront for the title to be displayed for each fragment/page

        // Generate title based on item position   // the corresponding values stored in our titles string array are returned and these are then set as the text for the corresponding tabs
        return tabTitles[position];
    }



}

