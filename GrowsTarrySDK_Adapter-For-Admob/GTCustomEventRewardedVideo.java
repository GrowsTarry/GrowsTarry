package com.growstarry.mediation.admob;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import com.growstarry.kern.core.GTError;
import com.growstarry.kern.core.GTVideo;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.mediation.MediationAdRequest;
import com.google.android.gms.ads.reward.RewardItem;
import com.google.android.gms.ads.reward.mediation.MediationRewardedVideoAdAdapter;
import com.google.android.gms.ads.reward.mediation.MediationRewardedVideoAdListener;
import com.growstarry.kern.callback.VideoAdLoadListener;
import com.growstarry.kern.core.GrowsTarrySDK;
import com.growstarry.video.core.RewardedVideoAdListener;
import com.growstarry.video.core.GrowsTarryVideo;

public class GTCustomEventRewardedVideo implements MediationRewardedVideoAdAdapter {

    private static final String TAG = "ZCEventRewardedVideo";

    private static String slotId = "";

    private MediationRewardedVideoAdListener mAdmobListener;
    private Context context;

    private GTVideo zcVideo;

    private boolean isInitialized;


    @Override
    public void initialize(Context context,
                           MediationAdRequest mediationAdRequest,
                           String unUsed,
                           MediationRewardedVideoAdListener listener,
                           Bundle serverParameters,
                           Bundle mediationExtras) {
        Log.e(TAG, "initialize: serverParameters -> " + serverParameters);
        mAdmobListener = listener;
        slotId = serverParameters.getString("parameter");
        GrowsTarrySDK.initialize(context, slotId);
        if (!(context instanceof Activity)) {
            Log.w("  ZcoupAdapter", "Context must be of type Activity.");
            listener.onInitializationFailed(
                GTCustomEventRewardedVideo.this, AdRequest.ERROR_CODE_INVALID_REQUEST);
            return;
        }
        this.context = context;
        mAdmobListener.onInitializationSucceeded(GTCustomEventRewardedVideo.this);
        isInitialized = true;
    }


    @Override
    public void loadAd(MediationAdRequest mediationAdRequest, Bundle bundle, Bundle bundle1) {
        if (mAdmobListener != null) {
            GrowsTarryVideo.preloadRewardedVideo(context, slotId, loadListener);
        }
    }


    @Override
    public void showVideo() {
        GrowsTarryVideo.showRewardedVideo(zcVideo, cloudRVListener);
    }


    @Override
    public boolean isInitialized() {
        return isInitialized;
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


    private VideoAdLoadListener loadListener = new VideoAdLoadListener() {
        @Override
        public void onVideoAdLoadSucceed(GTVideo video) {
            if (video != null) {
                zcVideo = video;
                mAdmobListener.onAdLoaded(GTCustomEventRewardedVideo.this);
                return;
            }
            mAdmobListener.onAdFailedToLoad(GTCustomEventRewardedVideo.this,
                Integer.parseInt(GTError.ERR_CODE_VIDEO));
        }


        @Override
        public void onVideoAdLoadFailed(GTError error) {
            mAdmobListener.onAdFailedToLoad(GTCustomEventRewardedVideo.this,
                Integer.parseInt(GTError.ERR_CODE_VIDEO));
        }

    };

    private RewardedVideoAdListener cloudRVListener = new RewardedVideoAdListener() {

        @Override
        public void videoStart() {
            if (mAdmobListener != null) {
                mAdmobListener.onAdOpened(GTCustomEventRewardedVideo.this);
                mAdmobListener.onVideoStarted(GTCustomEventRewardedVideo.this);
            }
        }


        @Override
        public void videoFinish() {

        }


        @Override
        public void videoError(Exception e) {
            if (mAdmobListener != null) {
                mAdmobListener.onAdFailedToLoad(
                    GTCustomEventRewardedVideo.this, Integer.parseInt(GTError.ERR_CODE_VIDEO));
            }
        }


        @Override
        public void videoClosed() {
            if (mAdmobListener != null) {
                mAdmobListener.onAdClosed(GTCustomEventRewardedVideo.this);
            }
        }


        @Override
        public void videoClicked() {
            if (mAdmobListener != null) {
                mAdmobListener.onAdClicked(GTCustomEventRewardedVideo.this);
            }
        }


        @Override
        public void videoRewarded(final String rewardName, final String rewardAmount) {
            if (mAdmobListener != null) {
                mAdmobListener.onRewarded(GTCustomEventRewardedVideo.this, new RewardItem() {
                    @Override
                    public String getType() {
                        return rewardName;
                    }


                    @Override
                    public int getAmount() {
                        int amount = 0;
                        try {
                            amount = Integer.parseInt(rewardAmount);
                        } catch (NumberFormatException e) {
                            e.printStackTrace();
                        }
                        return amount;
                    }
                });
            }
        }

    };
}
