package com.example.anitracker.activities;

import android.os.Bundle;
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

import com.example.anitracker.R;
import com.example.anitracker.adapters.VPAdapter;
import com.example.anitracker.fragments.CharacterOverviewFragment;
import com.example.anitracker.fragments.RelationsFragment;
import com.example.anitracker.mediaObjects.CharacterDetails;
import com.example.anitracker.type.MediaType;
import com.example.anitracker.uiObjects.Image;
import com.example.anitracker.viewModels.EntityViewModel;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.Objects;

public class EntityDetails extends AppCompatActivity {
    private final String[] fragmentTitles = {"Overview"};
    EntityViewModel viewModel;
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

        // set up back button
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setNavigationOnClickListener(e -> finish());

        // set up view model which holds all info used by all fragments
        this.viewModel = new ViewModelProvider(this).get(EntityViewModel.class);
        this.viewModel.setId(Objects.requireNonNull(getIntent().getExtras()).getString("ID"));
        this.viewModel.setMediaType(MediaType.safeValueOf(Objects.requireNonNull(Objects.requireNonNull(getIntent().getExtras()).getString("Type"))));


        // observe any errors from repository
        this.viewModel.observeErrorMsg().observe(this, errorMsg -> Toast.makeText(this, errorMsg, Toast.LENGTH_SHORT).show());

        // get and observe details from repository
        if (this.viewModel.getMediaType() != MediaType.VISUAL_NOVEL) {
            this.viewModel.observeCharacterDetail().observe(this, res -> {
                viewModel.setLastFetchedDetail(res);
                this.populateActivity(viewModel.getLastFetchedDetail());
            });
        } else {
            this.viewModel.observeVNCharDetail().observe(this, res-> {
                viewModel.setLastFetchedDetail(res.getVNCharList(viewModel.getId()).get(0));
                this.populateActivity(viewModel.getLastFetchedDetail());
            });
        }

        this.viewModel.getCharacterDetail();

        // hide ui while data is loading
        this.appBarLayout = findViewById(R.id.appBarLayout);
        this.appBarLayout.setVisibility(View.INVISIBLE);
        
        // set up tab layout and view pager
        TabLayout tabLayout = findViewById(R.id.tabs);
        ViewPager2 viewPager = findViewById(R.id.viewpager);

        VPAdapter viewPagerAdapter = new VPAdapter(this, this);
        
        // creating fragments
        CharacterOverviewFragment overviewFragment = new CharacterOverviewFragment();
        //RelationsFragment relationsFragment = new RelationsFragment();
        
        // adding fragments to view pager
        viewPagerAdapter.addFragment(overviewFragment);
        //viewPagerAdapter.addFragment(relationsFragment);

        viewPager.setAdapter(viewPagerAdapter);
        new TabLayoutMediator(tabLayout, viewPager, (tab, position) -> tab.setText(fragmentTitles[position])).attach();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        this.viewModel.clearComposite();
    }

    private void populateActivity(CharacterDetails details) {
            //insert cover image
            ImageView cover = this.findViewById(R.id.cover);
            Image.loadImage(this, details.getImage(), cover);

            // insert name
            TextView title = this.findViewById(R.id.title);
            title.setText(details.getName().getUserPref());

            this.appBarLayout.setVisibility(View.VISIBLE);
    }
}