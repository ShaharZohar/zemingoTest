package com.zemingo.rsssimulation.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.zemingo.rsssimulation.R;
import com.zemingo.rsssimulation.models.RssCategory;
import com.zemingo.rsssimulation.models.RssCategoryIdMapper;
import com.zemingo.rsssimulation.models.RssIdMapper;

public class RssActivity extends AppCompatActivity {

    private RssIdMapper mRssIdMapper = new RssCategoryIdMapper();

    public static Intent newIntent(@NonNull final Context context) {
        return new Intent(context, RssActivity.class);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rss);
        initBottomNavigation();
    }

    private void initBottomNavigation() {
        final BottomNavigationView navView = findViewById(R.id.nav_view);
        navView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {

            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                return setRssFragmentFromMenuId(menuItem.getItemId());
            }
        });

        setRssFragmentFromMenuId(navView.getSelectedItemId());
    }

    private boolean setRssFragmentFromMenuId(int id) {
        switch (id) {
            case R.id.navigation_cars:
                replaceFragment(RssFragment.getInstance(mRssIdMapper.getRssId(RssCategory.CARS)));
                return true;
            case R.id.navigation_culture_and_sport:
                Toast.makeText(RssActivity.this, "TODO", Toast.LENGTH_SHORT).show();
                return false;
        }

        return false;
    }

    private void replaceFragment(@NonNull final Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .commit();
    }
}
