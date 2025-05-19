package com.example.lab33.adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.lab33.fragments.FragmentAdd;
import com.example.lab33.fragments.FragmentDel;
import com.example.lab33.fragments.FragmentShow;
import com.example.lab33.fragments.FragmentUpdate;

public class NotesPagesAdapter extends FragmentPagerAdapter {

    private final String[] tabTitles = {"Show", "Add", "Del", "Update"};

    public NotesPagesAdapter(@NonNull FragmentManager fm) {
        super(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
    }

    @NonNull
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
                return new FragmentShow();
        }
    }

    @Override
    public int getCount() {
        return 4;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return tabTitles[position];
    }
}
