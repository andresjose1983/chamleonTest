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
import com.trofiventures.chameleon.Theme;
import com.trofiventures.testchameleonframework.viewmodel.TestViewModel;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

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
                Iterator it = theme.entrySet().iterator();
                while (it.hasNext()) {
                    Map.Entry pair = (Map.Entry) it.next();
                    switch (pair.getKey().toString()) {
                        //Theme.BOTTOM_NAVIGATION_ITEM_NORMAL_COLOR
                        //Theme.TOOLBAR_TITLE_COLOR
                        //Theme.TOOLBAR_ICON_COLOR
                        /**
                         * In this class "Theme" we have all the options for change color and images components
                         * - Toolbar (icon tint and title text color)
                         * - TabLayout (backgroundColor and textColor)
                         * - FloatingActionButton (background and tint color)
                         * - BottomNavigationView (background color, unselected and select item color)
                         * - NavigationView (background color, icon and text color)
                         * - EditText text color
                         * - TextInputLayout text color and focus color
                         * - ImageView
                         * - Change border color for a view VIEW_BORDER_COLOR
                         * - Change backgroundColor for a view VIEW_BACKGROUND_COLOR
                         */
                        case Theme.TOOLBAR_BACKGROUND_COLOR:
                            // change background color for toolbar object
                            toolbar.setBackgroundColor(Color.parseColor(pair.getValue().toString()));
                            break;
                    }
                }
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
