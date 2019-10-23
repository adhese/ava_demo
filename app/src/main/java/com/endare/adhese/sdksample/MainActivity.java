package com.endare.adhese.sdksample;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.endare.adhese.sdk.Ad;
import com.endare.adhese.sdk.Adhese;
import com.endare.adhese.sdk.AdheseOptions;
import com.endare.adhese.sdk.api.APICallback;
import com.endare.adhese.sdk.api.APIError;
import com.endare.adhese.sdk.parameters.CookieMode;
import com.endare.adhese.sdk.views.AdView;
import com.thedeanda.lorem.LoremIpsum;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentTransaction;

public class MainActivity extends AppCompatActivity implements
        AdView.OnAdLoadedListener,
        AdView.OnViewImpressionNotifiedListener,
        AdView.OnTrackerNotifiedListener,
        AdView.OnErrorListener,
        AdView.OnAdClickListener {

    private SnackbarManager snackbarManager;
    private TextView firstArticle;
    private TextView secondArticle;
    private AdView billboardAdView;
    private AdView halfPageAdView;
    private AdheseOptions options;

    private ArrayList<String> events;

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
                .build();

        // Init the SDK
        Adhese.initialise(this, true);

        View rootLayout = findViewById(android.R.id.content);
        firstArticle = findViewById(R.id.firstArticle);
        secondArticle = findViewById(R.id.secondArticle);
        billboardAdView = findViewById(R.id.billboardAd);
        halfPageAdView = findViewById(R.id.halfPageAd);

        firstArticle.setText(LoremIpsum.getInstance().getParagraphs(7, 7));
        secondArticle.setText(LoremIpsum.getInstance().getParagraphs(3, 3));

        snackbarManager = new SnackbarManager(this, rootLayout);
        events = new ArrayList<>();

        billboardAdView.setAdLoadedListener(this);
        billboardAdView.setTrackingNotifiedListener(this);
        billboardAdView.setViewImpressionNotifiedListener(this);
        billboardAdView.setOnAdClickListener(this);
        billboardAdView.setErrorListener(this);
        halfPageAdView.setAdLoadedListener(this);
        halfPageAdView.setTrackingNotifiedListener(this);
        halfPageAdView.setViewImpressionNotifiedListener(this);
        halfPageAdView.setErrorListener(this);
        halfPageAdView.setOnAdClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();

        Adhese.loadAds(options, new APICallback<List<Ad>>() {
            @Override
            public void onResponse(List<Ad> ads, APIError error) {
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_show_events:
                showDialog();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Nullable
    private Ad findByType(List<Ad> ads, String adType) {
        for (Ad ad : ads) {
            if (ad.getAdType().equals(adType)) {
                return ad;
            }
        }
        return null;
    }

    private void showDialog() {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.addToBackStack(null);

        DialogFragment fragment = EventViewerDialogFragment.newInstance(events);
        fragment.show(ft, "dialog");
    }

    @Override
    public void onAdLoaded(@NonNull AdView adView) {
        String event = String.format("Slot %s was loaded", adView.getAd().getSlotName());
        events.add(event);
    }

    @Override
    public void onViewImpressionNotified(@NonNull AdView adView) {
        String event = String.format("Slot %s view impression was called.", adView.getAd().getSlotName());
        events.add(event);
        snackbarManager.showSuccess(event);
    }

    @Override
    public void onTrackerNotified(@NonNull AdView adView) {
        String event = String.format("Slot %s tracker was called.", adView.getAd().getSlotName());
        events.add(event);
    }

    @Override
    public void onError(@NonNull AdView adView, APIError error) {
        String event = String.format("Slot %s had an error", adView.getAd().getSlotName());
        events.add(event);
        snackbarManager.showError(event);
    }

    @Override
    public void onAdClicked(@NonNull AdView adView) {
        events.add(String.format("Slot %s was clicked.", adView.getAd().getSlotName()));
    }
}
