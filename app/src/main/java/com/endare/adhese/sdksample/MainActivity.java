package com.endare.adhese.sdksample;

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
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity implements AdView.OnAdLoadedListener, AdView.OnViewImpressionNotifiedListener, AdView.OnTrackerNotifiedListener {

    private SnackbarManager snackbarManager;
    private TextView firstArticle;
    private TextView secondArticle;
    private AdView billboardAdView;
    private AdView halfPageAdView;
    private AdheseOptions options;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Create the options
        options = new AdheseOptions.Builder()
                .withAccount("_demo_ster_a_")
                .forLocation("demo")
                .addSlot("billboard")
                .addSlot("halfpage")
                .withCookieMode(CookieMode.ALL)
                .enableDebugMode()
                .build();

        // Init the SDK
        Adhese.initialise(this, options);

        View rootLayout = findViewById(android.R.id.content);
        firstArticle = findViewById(R.id.firstArticle);
        secondArticle = findViewById(R.id.secondArticle);
        billboardAdView = findViewById(R.id.billboardAd);
        halfPageAdView = findViewById(R.id.halfPageAd);

        firstArticle.setText(LoremIpsum.getInstance().getParagraphs(7, 7));
        secondArticle.setText(LoremIpsum.getInstance().getParagraphs(3, 3));

        snackbarManager = new SnackbarManager(this, rootLayout);

        billboardAdView.setAdLoadedListener(this);
        billboardAdView.setTrackingNotifiedListener(this);
        billboardAdView.setViewImpressionNotifiedListener(this);
        halfPageAdView.setAdLoadedListener(this);
        halfPageAdView.setTrackingNotifiedListener(this);
        halfPageAdView.setViewImpressionNotifiedListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();

        Adhese.loadAds(options, new APICallback<List<Ad>>() {
            @Override
            public void onResponse(List<Ad> ads, Exception error) {

                if (error != null) {
                    return;
                }

                Ad billboard = findByType(ads, "billboard");
                Ad halfPage = findByType(ads, "halfpage");

                if (billboard != null) {
                    billboardAdView.setAd(billboard);
                } else {
                    billboardAdView.setVisibility(View.GONE);
                }

                if (halfPage != null) {
                    halfPageAdView.setAd(halfPage);
                } else {
                    halfPageAdView.setVisibility(View.GONE);
                }
            }
        });
    }

    private @Nullable Ad findByType(List<Ad> ads, String adType) {
        for (Ad ad : ads) {
            if (ad.getAdType().equals(adType)) {
                return ad;
            }
        }
        return null;
    }

    @Override
    public void onAdLoaded(@NonNull AdView adView) {
        snackbarManager.showInfo(String.format("Slot %s was loaded", adView.getAd().getSlotName()));
    }

    @Override
    public void onViewImpressionNotified(@NonNull AdView adView) {
        snackbarManager.showInfo(String.format("Slot %s view impression was called.", adView.getAd().getSlotName()));
    }

    @Override
    public void onTrackerNotified(@NonNull AdView adView) {
        snackbarManager.showInfo(String.format("Slot %s tracker was called.", adView.getAd().getSlotName()));
    }
}
