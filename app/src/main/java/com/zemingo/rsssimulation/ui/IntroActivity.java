package com.zemingo.rsssimulation.ui;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatTextView;

import com.zemingo.rsssimulation.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class IntroActivity extends AppCompatActivity {

    private AppCompatTextView time, rss_title;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);
        initViews();
    }

    private void initViews() {
        findViewById(R.id.rss_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(RssActivity.newIntent(IntroActivity.this));
            }
        });

        time = (AppCompatTextView) findViewById(R.id.time);
        rss_title = (AppCompatTextView) findViewById(R.id.rss_title);
    }

    @Override
    protected void onResume() {
        super.onResume();

        updateIntroTime();
        updateIntroRssTitle();
    }

    private void updateIntroRssTitle() {
        // get latest rss title from shared preference
        SharedPreferences prefs = getSharedPreferences("RSS_SP", MODE_PRIVATE);
        String title = prefs.getString("rss_title", ""); // empty string is the default value.

        rss_title.setText(title);
    }

    private void updateIntroTime() {
        // update on-resume activity time
        String currentTime = new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date());
        time.setText(currentTime);
    }
}
