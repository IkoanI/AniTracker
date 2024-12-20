package com.example.anitracker.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.widget.ViewPager2;

import com.bumptech.glide.Glide;
import com.example.anitracker.adapters.VPAdapter;
import com.example.anitracker.R;
import com.example.anitracker.fragments.CharactersFragment;
import com.example.anitracker.fragments.RelationsFragment;
import com.example.anitracker.mediaObjects.MediaDetails;
import com.example.anitracker.type.MediaType;
import com.example.anitracker.uiObjects.LoadingCircleDrawable;
import com.example.anitracker.viewModels.DetailsViewModel;
import com.example.anitracker.fragments.OverviewFragment;
import com.example.anitracker.fragments.StaffFragment;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.Objects;

public class Details extends AppCompatActivity {
    private final String[] fragmentTitles = {"Overview", "Characters", "Staff", "Relations"};
    DetailsViewModel detailsViewModel;
    AppBarLayout appBarLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_details);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // set up view model which holds all info used by all fragments
        this.detailsViewModel = new ViewModelProvider(this).get(DetailsViewModel.class);
        this.detailsViewModel.setType(MediaType.safeValueOf(Objects.requireNonNull(Objects.requireNonNull(getIntent().getExtras()).getString("Type"))));
        this.detailsViewModel.setId(Objects.requireNonNull(getIntent().getExtras()).getString("ID"));

        // observe any errors from repository
        this.detailsViewModel.observeErrorMsg().observe(this, errorMsg -> Toast.makeText(this, errorMsg, Toast.LENGTH_SHORT).show());

        // get and observe details from repository
        this.detailsViewModel.getDetails();
        this.detailsViewModel.observeMediaDetails().observe(this, this::populateActivity);

        // hide ui while data is loading
        this.appBarLayout = findViewById(R.id.appBarLayout);
        this.appBarLayout.setVisibility(View.INVISIBLE);
        
        // set up tab layout and view pager
        TabLayout tabLayout = findViewById(R.id.tabs);
        ViewPager2 viewPager = findViewById(R.id.viewpager);

        VPAdapter viewPagerAdapter = new VPAdapter(this, this);
        
        // creating fragments
        OverviewFragment overviewFragment = new OverviewFragment();
        CharactersFragment charactersFragment = new CharactersFragment();
        StaffFragment staffFragment = new StaffFragment();
        RelationsFragment relationsFragment = new RelationsFragment();
        
        // adding fragments to view pager
        viewPagerAdapter.addFragment(overviewFragment);
        viewPagerAdapter.addFragment(charactersFragment);
        viewPagerAdapter.addFragment(staffFragment);
        viewPagerAdapter.addFragment(relationsFragment);

        viewPager.setAdapter(viewPagerAdapter);
        new TabLayoutMediator(tabLayout, viewPager, (tab, position) -> tab.setText(fragmentTitles[position])).attach();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        this.detailsViewModel.clearComposite();
    }

    private void populateActivity(MediaDetails details) {
            // insert banner image
            ImageView banner = this.findViewById(R.id.banner);
            if (details.getBanner() != null) {
                Glide.with(this)
                        .load(details.getBanner())
                        .placeholder(LoadingCircleDrawable.getLoadingCircle(this))
                        .into(banner);
            }

            //insert cover image
            ImageView cover = this.findViewById(R.id.cover);
            Glide.with(this)
                    .load(details.getCoverImg())
                    .placeholder(LoadingCircleDrawable.getLoadingCircle(this))
                    .into(cover);

            // insert title
            TextView title = this.findViewById(R.id.title);
            title.setText(details.getTitles().getUserPref());

            this.appBarLayout.setVisibility(View.VISIBLE);
    }
}