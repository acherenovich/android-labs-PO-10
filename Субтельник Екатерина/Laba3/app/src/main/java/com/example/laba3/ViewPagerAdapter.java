package com.example.laba3;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class ViewPagerAdapter extends FragmentStateAdapter {

    public ViewPagerAdapter(@NonNull FragmentActivity activity) {
        super(activity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0: return new FragmentShow();
            case 1: return new FragmentAdd();
            case 2: return new FragmentDelete();
            case 3: return new FragmentUpdate();
            default: return new FragmentShow();
        }
    }

    @Override
    public int getItemCount() {
        return 4;
    }
}

