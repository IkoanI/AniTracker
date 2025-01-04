package com.example.anitracker.activities;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.SearchView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.widget.ViewPager2;

import com.example.anitracker.R;
import com.example.anitracker.adapters.VPAdapter;
import com.example.anitracker.fragments.SearchFragment;
import com.example.anitracker.type.MediaType;
import com.example.anitracker.uiObjects.SearchQueryListener;
import com.example.anitracker.viewModels.SearchViewModel;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEvent;

public class Search extends AppCompatActivity {
    private final String[] fragmentTitles = {"Anime", "Manga", "Visual Novel"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_search);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // initialize view model
        SearchViewModel viewModel = new ViewModelProvider(this).get(SearchViewModel.class);

        // set up error alert dialogue
        AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
        builder1.setCancelable(true);
        builder1.setPositiveButton(
                "Refresh",
                (dialog, id) -> {
                    Intent intent = new Intent(this, this.getClass());
                    dialog.cancel();
                    finish();
                    overridePendingTransition(0, 0);
                    startActivity(intent);
                    overridePendingTransition(0, 0);
                });

        // observe error message
        viewModel.getErrorMsg().observe(this, error -> {
            builder1.setMessage(error);
            AlertDialog alert11 = builder1.create();
            alert11.show();
        });

        // set up search bar
        SearchView searchBar = findViewById(R.id.searchBar);
        searchBar.setOnQueryTextListener(new SearchQueryListener(searchBar, viewModel.getUserSearch()));

        // clear focus from searchbar when keyboard hidden
        KeyboardVisibilityEvent.setEventListener(this,
                b -> {
                    if(!b){
                        searchBar.clearFocus();
                    }
                });


        // set up tab layout and view pager
        TabLayout tabLayout = findViewById(R.id.tabs);
        ViewPager2 viewPager = findViewById(R.id.viewpager);

        VPAdapter viewPagerAdapter = new VPAdapter(this, this);

        // adding fragments to view pager
        viewPagerAdapter.addFragment(SearchFragment.newInstance(MediaType.ANIME));
        viewPagerAdapter.addFragment(SearchFragment.newInstance(MediaType.MANGA));
        viewPagerAdapter.addFragment(SearchFragment.newInstance(MediaType.VISUAL_NOVEL));

        viewPager.setAdapter(viewPagerAdapter);
        viewPager.setOffscreenPageLimit(2);

        new TabLayoutMediator(tabLayout, viewPager, (tab, position) -> tab.setText(fragmentTitles[position])).attach();
    }
}