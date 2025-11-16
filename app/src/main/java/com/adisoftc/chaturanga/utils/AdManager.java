package com.adisoftc.chaturanga.utils;

import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.OnUserEarnedRewardListener;
import com.google.android.gms.ads.appopen.AppOpenAd;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.google.android.gms.ads.nativead.NativeAd;
import com.google.android.gms.ads.nativead.NativeAdOptions;
import com.google.android.gms.ads.nativead.NativeAdView;
import com.google.android.gms.ads.rewarded.RewardedAd;
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback;

public class AdManager {
    
    private static final String TAG = "AdManager";
    private static InterstitialAd interstitialAd;
    private static RewardedAd rewardedAd;
    private static AppOpenAd appOpenAd;
    private static boolean isShowingAd = false;
    
    public static void initializeAds(Activity activity) {
        MobileAds.initialize(activity, initializationStatus -> {
            Log.d(TAG, "AdMob initialized");
        });
    }
    
    public static AdView createBannerAd(Activity activity) {
        AdView bannerAd = new AdView(activity);
        bannerAd.setAdUnitId(Constants.BANNER_AD_UNIT_ID);
        bannerAd.setAdSize(AdSize.BANNER);
        
        AdRequest adRequest = new AdRequest.Builder().build();
        bannerAd.loadAd(adRequest);
        
        bannerAd.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                Log.d(TAG, "Banner ad loaded");
            }
            
            @Override
            public void onAdFailedToLoad(LoadAdError adError) {
                Log.e(TAG, "Banner ad failed to load: " + adError.getMessage());
            }
        });
        
        return bannerAd;
    }
    
    public static void loadBannerAd(Activity activity, LinearLayout container) {
        AdView bannerAd = createBannerAd(activity);
        
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        );
        bannerAd.setLayoutParams(params);
        
        container.addView(bannerAd);
    }
    
    public static void loadInterstitialAd(Activity activity) {
        AdRequest adRequest = new AdRequest.Builder().build();
        
        InterstitialAd.load(activity, Constants.INTERSTITIAL_AD_UNIT_ID, adRequest,
            new InterstitialAdLoadCallback() {
                @Override
                public void onAdLoaded(InterstitialAd ad) {
                    interstitialAd = ad;
                    Log.d(TAG, "Interstitial ad loaded");
                    
                    ad.setFullScreenContentCallback(new FullScreenContentCallback() {
                        @Override
                        public void onAdDismissedFullScreenContent() {
                            interstitialAd = null;
                            loadInterstitialAd(activity);
                        }
                        
                        @Override
                        public void onAdFailedToShowFullScreenContent(AdError adError) {
                            Log.e(TAG, "Interstitial ad failed to show: " + adError.getMessage());
                            interstitialAd = null;
                        }
                        
                        @Override
                        public void onAdShowedFullScreenContent() {
                            Log.d(TAG, "Interstitial ad showed");
                        }
                    });
                }
                
                @Override
                public void onAdFailedToLoad(LoadAdError loadAdError) {
                    Log.e(TAG, "Interstitial ad failed to load: " + loadAdError.getMessage());
                    interstitialAd = null;
                }
            });
    }
    
    public static void showInterstitialAd(Activity activity) {
        if (interstitialAd != null) {
            interstitialAd.show(activity);
        } else {
            Log.d(TAG, "Interstitial ad not ready");
            loadInterstitialAd(activity);
        }
    }
    
    public static void loadRewardedAd(Activity activity) {
        AdRequest adRequest = new AdRequest.Builder().build();
        
        RewardedAd.load(activity, Constants.REWARDED_AD_UNIT_ID, adRequest,
            new RewardedAdLoadCallback() {
                @Override
                public void onAdLoaded(RewardedAd ad) {
                    rewardedAd = ad;
                    Log.d(TAG, "Rewarded ad loaded");
                    
                    ad.setFullScreenContentCallback(new FullScreenContentCallback() {
                        @Override
                        public void onAdDismissedFullScreenContent() {
                            rewardedAd = null;
                            loadRewardedAd(activity);
                        }
                        
                        @Override
                        public void onAdFailedToShowFullScreenContent(AdError adError) {
                            Log.e(TAG, "Rewarded ad failed to show: " + adError.getMessage());
                            rewardedAd = null;
                        }
                        
                        @Override
                        public void onAdShowedFullScreenContent() {
                            Log.d(TAG, "Rewarded ad showed");
                        }
                    });
                }
                
                @Override
                public void onAdFailedToLoad(LoadAdError loadAdError) {
                    Log.e(TAG, "Rewarded ad failed to load: " + loadAdError.getMessage());
                    rewardedAd = null;
                }
            });
    }
    
    public static void showRewardedAd(Activity activity, RewardCallback callback) {
        if (rewardedAd != null) {
            rewardedAd.show(activity, rewardItem -> {
                Log.d(TAG, "User earned reward: " + rewardItem.getAmount());
                if (callback != null) {
                    callback.onRewarded();
                }
            });
        } else {
            Log.d(TAG, "Rewarded ad not ready");
            if (callback != null) {
                callback.onFailed();
            }
            loadRewardedAd(activity);
        }
    }
    
    public static void loadAppOpenAd(Activity activity) {
        AdRequest adRequest = new AdRequest.Builder().build();
        
        AppOpenAd.load(activity, Constants.APP_OPEN_AD_UNIT_ID, adRequest,
            AppOpenAd.APP_OPEN_AD_ORIENTATION_PORTRAIT,
            new AppOpenAd.AppOpenAdLoadCallback() {
                @Override
                public void onAdLoaded(AppOpenAd ad) {
                    appOpenAd = ad;
                    Log.d(TAG, "App open ad loaded");
                    
                    ad.setFullScreenContentCallback(new FullScreenContentCallback() {
                        @Override
                        public void onAdDismissedFullScreenContent() {
                            appOpenAd = null;
                            isShowingAd = false;
                            loadAppOpenAd(activity);
                        }
                        
                        @Override
                        public void onAdFailedToShowFullScreenContent(AdError adError) {
                            Log.e(TAG, "App open ad failed to show: " + adError.getMessage());
                            appOpenAd = null;
                            isShowingAd = false;
                        }
                        
                        @Override
                        public void onAdShowedFullScreenContent() {
                            Log.d(TAG, "App open ad showed");
                            isShowingAd = true;
                        }
                    });
                }
                
                @Override
                public void onAdFailedToLoad(LoadAdError loadAdError) {
                    Log.e(TAG, "App open ad failed to load: " + loadAdError.getMessage());
                    appOpenAd = null;
                }
            });
    }
    
    public static void showAppOpenAd(Activity activity) {
        if (!isShowingAd && appOpenAd != null) {
            appOpenAd.show(activity);
        } else {
            Log.d(TAG, "App open ad not ready or already showing");
            loadAppOpenAd(activity);
        }
    }
    
    public static NativeAdView loadNativeAd(Activity activity, FrameLayout container) {
        AdRequest adRequest = new AdRequest.Builder().build();
        
        com.google.android.gms.ads.AdLoader adLoader = new com.google.android.gms.ads.AdLoader.Builder(
            activity, Constants.NATIVE_AD_UNIT_ID)
            .forNativeAd(nativeAd -> {
                NativeAdView adView = (NativeAdView) activity.getLayoutInflater()
                    .inflate(R.layout.native_ad_layout, null);
                populateNativeAdView(nativeAd, adView);
                container.removeAllViews();
                container.addView(adView);
            })
            .withNativeAdOptions(new NativeAdOptions.Builder().build())
            .withAdListener(new AdListener() {
                @Override
                public void onAdFailedToLoad(LoadAdError adError) {
                    Log.e(TAG, "Native ad failed to load: " + adError.getMessage());
                }
            })
            .build();
        
        adLoader.loadAd(adRequest);
        return null;
    }
    
    private static void populateNativeAdView(NativeAd nativeAd, NativeAdView adView) {
        adView.setNativeAd(nativeAd);
    }
    
    public interface RewardCallback {
        void onRewarded();
        void onFailed();
    }
}
