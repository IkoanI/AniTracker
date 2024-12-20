package com.example.anitracker.adapters;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import java.util.ArrayList;
import java.util.List;

public class VPAdapter extends FragmentStateAdapter {
    private final List<Fragment> fragmentList = new ArrayList<>();
    AppCompatActivity context;

    public VPAdapter(@NonNull FragmentActivity fragmentActivity, Context context) {
        super(fragmentActivity);
        this.context = (AppCompatActivity) context;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        return this.fragmentList.get(position);
    }

    @Override
    public int getItemCount() {
        return this.fragmentList.size();
    }

    public void addFragment(Fragment fragment) {
        this.fragmentList.add(fragment);
    }
}
