package com.endare.adhese.sdksample;

import android.graphics.Rect;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.endare.adhese.sdk.Ad;
import com.endare.adhese.sdk.Adhese;
import com.endare.adhese.sdk.AdheseOptions;
import com.endare.adhese.sdk.api.APICallback;
import com.endare.adhese.sdk.parameters.CookieMode;
import com.endare.adhese.sdk.views.AdView;
import com.thedeanda.lorem.LoremIpsum;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private SnackbarManager snackbarManager;
    private TextView firstArticle;
    private TextView secondArticle;
    private AdView firstAdView;
    private AdView secondAdView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Create the options
        AdheseOptions options = new AdheseOptions.Builder()
                .withAccount("_demo_ster_a_")
                .forLocation("demo")
                .addSlot("billboard")
                .addSlot("imu")
                .withCookieMode(CookieMode.ALL)
                .enableDebugMode()
                .build();

        // Init the SDK
        Adhese.initialise(this, options);

        View rootLayout = findViewById(android.R.id.content);
        firstArticle = findViewById(R.id.firstArticle);
        secondArticle = findViewById(R.id.secondArticle);
        firstAdView = findViewById(R.id.firstAd);
        secondAdView = findViewById(R.id.secondAd);

        firstArticle.setText(LoremIpsum.getInstance().getParagraphs(7, 7));
        secondArticle.setText(LoremIpsum.getInstance().getParagraphs(3, 3));

        snackbarManager = new SnackbarManager(this, rootLayout);

        firstAdView.setAdLoadedListener(new AdView.OnAdLoadedListener() {
            @Override
            public void onAdLoaded(@NonNull AdView adView) {
                snackbarManager.showInfo(String.format("Slot %s was loaded", firstAdView.getAd().getSlotName()));
            }
        });

        firstAdView.setTrackingNotifiedListener(new AdView.OnTrackerNotifiedListener() {
            @Override
            public void onTrackerNotified(@NonNull AdView adView) {
                snackbarManager.showInfo(String.format("Slot %s tracker was called.", firstAdView.getAd().getSlotName()));
            }
        });

        firstAdView.setTrackingNotifiedListener(new AdView.OnTrackerNotifiedListener() {
            @Override
            public void onTrackerNotified(@NonNull AdView adView) {
                snackbarManager.showInfo(String.format("Slot %s view impression was called.", firstAdView.getAd().getSlotName()));
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        Adhese.loadAds(new APICallback<List<Ad>>() {
            @Override
            public void onResponse(List<Ad> ads, Exception error) {

                if (error != null) {
                    return;
                }

                if (ads.size() == 1) {
                    firstAdView.setAd(ads.get(0));
                }

                if (ads.size() == 2) {
                    secondAdView.setAd(ads.get(1));
                }
            }
        });
    }



}
