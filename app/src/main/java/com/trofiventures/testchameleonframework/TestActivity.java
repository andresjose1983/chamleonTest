package com.trofiventures.testchameleonframework;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.facebook.stetho.Stetho;
import com.trofiventures.testchameleonframework.viewmodel.TestViewModel;

import java.util.HashMap;

public class TestActivity extends AppCompatActivity {

    private TestViewModel testViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // only for test
        Stetho.initializeWithDefaults(this);
        final Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //create a viewmodel references
        testViewModel = ViewModelProviders.of(this).get(TestViewModel.class);
        // set context to viewmodel
        testViewModel.setContext(this);
        //create chameleon observer
        testViewModel.getChameleon().observe(this, new Observer<HashMap<String, String>>() {
            @Override
            public void onChanged(@Nullable HashMap<String, String> theme) {
                Log.d("chameleon", "" + theme.size());

                toolbar.setBackgroundColor(Color.parseColor(theme.get("navigationBar.backgroundColor")));

                toolbar.setTitleTextColor(Color.parseColor(theme.get("navigationBar.textColor")));

                findViewById(R.id.main_view).setBackgroundColor(Color.parseColor(theme.get("common.backgroundColor")));
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        // start chameleon service
        testViewModel.onResume();
    }

    @Override
    protected void onStop() {
        super.onStop();
        // stop chamelon service
        testViewModel.onPause();
    }

    @Override
    protected void onDestroy() {
        testViewModel.getChameleon().removeObservers(this);
        super.onDestroy();
    }
}
