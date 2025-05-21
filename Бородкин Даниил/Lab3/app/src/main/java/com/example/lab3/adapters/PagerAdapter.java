package com.example.lab3.adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;


import com.example.lab3.fragments.FragmentAdd;
import com.example.lab3.fragments.FragmentDel;
import com.example.lab3.fragments.FragmentShow;
import com.example.lab3.fragments.FragmentUpdate;

import java.util.ArrayList;
import java.util.List;

public class PagerAdapter extends FragmentStateAdapter {

    private List<Fragment> fragment_list;
    public PagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
        fragment_list = new ArrayList<>();
        fragment_list.add(new FragmentShow());
        fragment_list.add(new FragmentAdd());
        fragment_list.add(new FragmentDel());
        fragment_list.add(new FragmentUpdate());
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        return fragment_list.get(position);
    }

    @Override
    public int getItemCount() {
        return fragment_list.size();
    }
}
