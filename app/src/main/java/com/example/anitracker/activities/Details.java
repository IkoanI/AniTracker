package com.example.anitracker.activities;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.widget.ViewPager2;

import com.example.anitracker.adapters.VPAdapter;
import com.example.anitracker.R;
import com.example.anitracker.fragments.CharactersFragment;
import com.example.anitracker.fragments.RelationsFragment;
import com.example.anitracker.mediaObjects.Entity;
import com.example.anitracker.mediaObjects.MediaDetails;
import com.example.anitracker.type.MediaType;
import com.example.anitracker.uiObjects.Image;
import com.example.anitracker.viewModels.DetailsViewModel;
import com.example.anitracker.fragments.OverviewFragment;
import com.example.anitracker.fragments.StaffFragment;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.Objects;

public class Details extends AppCompatActivity {
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

        // get extras;
        String mediaType = Objects.requireNonNull(getIntent().getExtras()).getString("Type", null);
        String entityType = Objects.requireNonNull(getIntent().getExtras()).getString("Entity", null);
        String id = Objects.requireNonNull(getIntent().getExtras()).getString("ID", null);

        // set up back button
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setNavigationOnClickListener(e -> finish());

        // set up view model which holds all info used by all fragments
        this.detailsViewModel = new ViewModelProvider(this).get(DetailsViewModel.class);
        if (mediaType != null) {
            this.detailsViewModel.setMediaType(MediaType.safeValueOf(mediaType));
        }

        if (entityType != null) {
            this.detailsViewModel.setEntityType(entityType);
        }

        if (id != null) {
            this.detailsViewModel.setId(id);
        }

        // observe any errors from repository
        this.detailsViewModel.observeErrorMsg().observe(this, errorMsg -> Toast.makeText(this, errorMsg, Toast.LENGTH_SHORT).show());

        // get and observe details from repository
        if (Objects.equals(this.detailsViewModel.getEntityType(), "Char")) {
            this.detailsViewModel.observeCharDetail().observe(this, res -> {
                this.detailsViewModel.setLastFetchedCharDetail(res);
                this.populateActivity(res);
            });
            this.detailsViewModel.getCharDetail();
        } else if (Objects.equals(this.detailsViewModel.getEntityType(), "Staff")){
            this.detailsViewModel.observeStaffDetail().observe(this, res -> {
                detailsViewModel.setLastFetchedStaffDetail(res);
                this.populateActivity(res);
            });
            this.detailsViewModel.getStaffDetail();
        } else {
            this.detailsViewModel.observeMediaDetails().observe(this, this::populateActivity);
            this.detailsViewModel.getDetails();
        }

        // hide ui while data is loading
        this.appBarLayout = findViewById(R.id.appBarLayout);
        this.appBarLayout.setVisibility(View.INVISIBLE);
        
        // set up tab layout and view pager
        TabLayout tabLayout = findViewById(R.id.tabs);
        ViewPager2 viewPager = findViewById(R.id.viewpager);

        viewPager.setAdapter(this.viewPagerSetup());
        String[] fragmentTitles;
        if (Objects.equals(this.detailsViewModel.getEntityType(), "Char")) {
            fragmentTitles = new String[]{"Overview", "Roles"};
        } else if (Objects.equals(this.detailsViewModel.getEntityType(), "Staff")) {
            fragmentTitles = new String[]{"Overview", "Roles", "Characters"};
        } else {
            fragmentTitles = new String[]{"Overview", "Characters", "Staff", "Relations"};
        }

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
                Image.loadImage(this, details.getBanner(), banner);
            }

            //insert cover image
            ImageView cover = this.findViewById(R.id.cover);
            Image.loadImage(this, details.getImage(), cover);

            // insert title
            TextView title = this.findViewById(R.id.title);
            title.setText(details.getTitles().getUserPref());

            this.appBarLayout.setVisibility(View.VISIBLE);
    }

    private void populateActivity(Entity details) {
        //insert cover image
        ImageView cover = this.findViewById(R.id.cover);
        Image.loadImage(this, details.getImage(), cover);

        // insert name
        TextView title = this.findViewById(R.id.title);
        title.setText(details.getName().getUserPref());

        this.appBarLayout.setVisibility(View.VISIBLE);
    }

    private VPAdapter viewPagerSetup() {
        VPAdapter viewPagerAdapter = new VPAdapter(this, this);

        // creating fragments
        OverviewFragment overviewFragment = new OverviewFragment();
        CharactersFragment charactersFragment = new CharactersFragment();
        StaffFragment staffFragment = new StaffFragment();
        RelationsFragment relationsFragment = new RelationsFragment();

        // adding fragments to view pager
        if (Objects.equals(this.detailsViewModel.getEntityType(), "Char")) {
            viewPagerAdapter.addFragment(overviewFragment);
            viewPagerAdapter.addFragment(relationsFragment);
        } else if (Objects.equals(this.detailsViewModel.getEntityType(), "Staff")) {
            viewPagerAdapter.addFragment(overviewFragment);
            viewPagerAdapter.addFragment(relationsFragment);
            viewPagerAdapter.addFragment(charactersFragment);
        } else {
            viewPagerAdapter.addFragment(overviewFragment);
            viewPagerAdapter.addFragment(charactersFragment);
            viewPagerAdapter.addFragment(staffFragment);
            viewPagerAdapter.addFragment(relationsFragment);
        }

        return viewPagerAdapter;
    }
}