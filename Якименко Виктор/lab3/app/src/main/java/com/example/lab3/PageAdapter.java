package com.example.lab3;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class PageAdapter extends FragmentStateAdapter {

    public static final int SHOW_PAGE = 0;
    public static final int ADD_PAGE = 1;
    public static final int DEL_PAGE = 2;
    public static final int UPDATE_PAGE = 3;

    //private DatabaseAdapter databaseAdapter;
    public PageAdapter(FragmentActivity fragmentActivity) {
        super(fragmentActivity);
        //this.databaseAdapter = adapter;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case SHOW_PAGE:
                return new ShowFragment();
            case ADD_PAGE:
                return new AddFragment();
            case DEL_PAGE:
                return new DelFragment();
            case UPDATE_PAGE:
                return new UpdateFragment();
            default:
                throw new IllegalArgumentException("Invalid position: " + position);
        }
    }

    @Override
    public int getItemCount() {
        return 4;
    }
}
