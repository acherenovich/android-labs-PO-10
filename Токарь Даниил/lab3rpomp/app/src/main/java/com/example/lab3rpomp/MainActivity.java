package com.example.lab3rpomp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.PagerTabStrip;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ViewPager viewPager;
    private PagerTabStrip pagerTabStrip;
    private MyFragmentPagerAdapter pagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        viewPager = findViewById(R.id.view_pager);
        pagerTabStrip = findViewById(R.id.pager_tab_strip);

        // Создаем список фрагментов для ViewPager
        List<Fragment> fragmentList = new ArrayList<>();
        fragmentList.add(new FragmentShow());
        fragmentList.add(new FragmentAdd());
        fragmentList.add(new FragmentDel());
        fragmentList.add(new FragmentUpdate());

        // Создаем адаптер для ViewPager
        pagerAdapter = new MyFragmentPagerAdapter(getSupportFragmentManager(), fragmentList);

        // Устанавливаем адаптер для ViewPager
        viewPager.setAdapter(pagerAdapter);
    }

    // Внутренний класс FragmentPagerAdapter
    private class MyFragmentPagerAdapter extends FragmentPagerAdapter {

        private final List<Fragment> fragments;
        private final String[] tabTitles = new String[]{"Show", "Add", "Del", "Update"}; // Заголовки вкладок

        public MyFragmentPagerAdapter(FragmentManager fm, List<Fragment> fragments) {
            super(fm);
            this.fragments = fragments;
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
            return tabTitles[position]; // Возвращаем заголовок вкладки для PagerTabStrip
        }
    }
}