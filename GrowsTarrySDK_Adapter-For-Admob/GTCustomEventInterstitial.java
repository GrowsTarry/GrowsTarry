package com.growstarry.mediation.admob;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.mediation.MediationAdRequest;
import com.google.android.gms.ads.mediation.customevent.CustomEventInterstitial;
import com.google.android.gms.ads.mediation.customevent.CustomEventInterstitialListener;
import com.growstarry.kern.callback.AdEventListener;
import com.growstarry.kern.core.GrowsTarrySDK;
import com.growstarry.kern.core.GTNative;
import com.growstarry.kern.vo.AdsNativeVO;

public class GTCustomEventInterstitial implements CustomEventInterstitial {
    private GTNative result;
    private static final String TAG = "ZCEventInterstitial";
    private CustomEventInterstitialListener interstitialListener;


    @Override
    public void requestInterstitialAd(Context context, CustomEventInterstitialListener customEventInterstitialListener, String serverParameter, MediationAdRequest mediationAdRequest, Bundle bundle) {
        interstitialListener = customEventInterstitialListener;
        Log.e(TAG, "requestInterstitialAd: Admob -> " + serverParameter);
        if (!(context instanceof Activity)) {
            Log.w(TAG, "Context must be of type Activity.");
            if (interstitialListener != null) {
                interstitialListener.onAdFailedToLoad(AdRequest.ERROR_CODE_NO_FILL);
            }
            return;
        }
        GrowsTarrySDK.initialize(context, serverParameter);
        GrowsTarrySDK.preloadInterstitialAd(context, serverParameter, ctAdEventListener);
    }


    @Override
    public void showInterstitial() {
        if (!GrowsTarrySDK.isInterstitialAvailable(result)) {
            if (interstitialListener != null) {
                interstitialListener.onAdFailedToLoad(AdRequest.ERROR_CODE_INTERNAL_ERROR);
            }
            return;
        }
        GrowsTarrySDK.showInterstitialAd(result);
    }


    @Override
    public void onDestroy() {

    }


    @Override
    public void onPause() {

    }


    @Override
    public void onResume() {

    }


    private AdEventListener ctAdEventListener = new AdEventListener() {
        @Override
        public void onReceiveAdSucceed(GTNative result) {
            if (interstitialListener != null) {
                interstitialListener.onAdLoaded();
            }
            GTCustomEventInterstitial.this.result = result;
        }


        @Override
        public void onReceiveAdVoSucceed(AdsNativeVO result) {

        }


        @Override
        public void onInterstitialLoadSucceed(GTNative result) {
            if (interstitialListener != null) {
                interstitialListener.onAdOpened();
            }
        }


        @Override
        public void onReceiveAdFailed(GTNative result) {
            if (interstitialListener != null) {
                interstitialListener.onAdFailedToLoad(AdRequest.ERROR_CODE_NO_FILL);
            }
        }


        @Override
        public void onLandpageShown(GTNative result) {

        }


        @Override
        public void onAdClicked(GTNative result) {
            if (interstitialListener != null) {
                interstitialListener.onAdClicked();
            }
        }


        @Override
        public void onAdClosed(GTNative result) {
            if (interstitialListener != null) {
                interstitialListener.onAdClosed();
            }
        }

    };
}
