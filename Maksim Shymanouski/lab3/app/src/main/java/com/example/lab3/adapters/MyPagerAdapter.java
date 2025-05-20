package com.example.lab3.adapters;


import com.example.lab3.fragments.FragmentAdd;
import com.example.lab3.fragments.FragmentDel;
import com.example.lab3.fragments.FragmentShow;
import com.example.lab3.fragments.FragmentUpdate;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;


public class MyPagerAdapter extends FragmentPagerAdapter {
    public MyPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new FragmentShow();
            case 1:
                return new FragmentAdd();
            case 2:
                return new FragmentDel();
            case 3:
                return new FragmentUpdate();
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return 4;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return "Show (Shymanouski)";
            case 1:
                return "Add (Shymanouski)";
            case 2:
                return "Del (Shymanouski)";
            case 3:
                return "Update (Shymanouski)";
            default:
                return null;
        }
    }
}
