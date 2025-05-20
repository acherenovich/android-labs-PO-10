package com.example.lab3;

import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class ViewPagerAdapter extends FragmentStateAdapter {

    public ViewPagerAdapter(MainActivity mainActivity) {
        super(mainActivity);
    }

    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return new FragmentShow();  // Фрагмент для показа списка заметок
            case 1:
                return new FragmentAdd();   // Фрагмент для добавления заметки
            case 2:
                return new FragmentDel();   // Фрагмент для удаления заметки
            case 3:
                return new FragmentUpdate(); // Фрагмент для обновления заметки
            default:
                return null;
        }
    }

    @Override
    public int getItemCount() {
        return 4; // Всего 4 фрагмента
    }
}
