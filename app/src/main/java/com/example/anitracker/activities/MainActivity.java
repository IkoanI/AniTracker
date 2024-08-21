package com.example.anitracker.activities;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.widget.ViewPager2;

import com.example.anitracker.R;
import com.example.anitracker.adapters.VPAdapter;
import com.example.anitracker.fragments.AnimeSearchFragment;
import com.example.anitracker.fragments.MangaSearchFragment;
import com.example.anitracker.fragments.VNSearchFragment;
import com.example.anitracker.viewModels.MainViewModel;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEvent;
import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEventListener;

public class MainActivity extends AppCompatActivity {
    MainViewModel viewModel;
    private final String[] fragmentTitles = {"Anime", "Manga", "Visual Novel"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        Handler handler = new Handler();

        // initialize view model
        viewModel = new ViewModelProvider(this).get(MainViewModel.class);



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
        android.widget.SearchView searchBar = findViewById(R.id.searchBar);
        searchBar.setOnQueryTextListener(new android.widget.SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // clear focus from search bar after submitting
                searchBar.clearFocus();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                // delay search until user stops typing
                handler.removeCallbacksAndMessages(null);
                handler.postDelayed(() -> viewModel.setUserSearch(newText), 500);

                return true;
            }
        });

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

        VPAdapter viewPagerAdapter = new VPAdapter(this);

        // creating fragments
        AnimeSearchFragment animeSearchFragment = new AnimeSearchFragment();
        MangaSearchFragment mangaSearchFragment = new MangaSearchFragment();
        VNSearchFragment vnSearchFragment = new VNSearchFragment();

        // adding fragments to view pager
        viewPagerAdapter.addFragment(animeSearchFragment);
        viewPagerAdapter.addFragment(mangaSearchFragment);
        viewPagerAdapter.addFragment(vnSearchFragment);

        // since number of tabs is known, load all at once instead of loading every time user switches
        viewPager.setOffscreenPageLimit(2);

        viewPager.setAdapter(viewPagerAdapter);
        new TabLayoutMediator(tabLayout, viewPager, (tab, position) -> tab.setText(fragmentTitles[position])).attach();


    }
}