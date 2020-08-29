package com.example.countrytriviaapp.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.widget.Button;
import android.widget.FrameLayout;

import com.example.countrytriviaapp.Fragments.LoginFragment;
import com.example.countrytriviaapp.R;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.dynamic.SupportFragmentWrapper;


public class MainActivity extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });

        FrameLayout container = findViewById(R.id.mainActivity_MainFrame_FrameLayout);

        // Check that the activity is using the layout version with
        // the fragment_container FrameLayout
        if (findViewById(container.getId()) != null){
            // However, if we're being restored from a previous state,
            // then we don't need to do anything and should return or else
            // we could end up with overlapping fragments.
            if (savedInstanceState != null)
                return;

            LoginFragment loginFragment = new LoginFragment();
            loginFragment.setArguments(getIntent().getExtras());
            getSupportFragmentManager().beginTransaction()
                    .add(container.getId(),loginFragment).commit();
        }
    }

    public void mainFragmentManager(Fragment newFragmentToMoveTo) {

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.mainActivity_MainFrame_FrameLayout, newFragmentToMoveTo);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}