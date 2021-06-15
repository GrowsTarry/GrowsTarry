# GrowsTarry SDK Integration

1. [Introduction](#introduction)

2. [Integration GrowsTarry SDK](#integration)

    [2.1 Integrating the GrowsTarry SDK in to project](#step1)

    [2.2 Initialize the GrowsTarry SDK](#step2)

    [2.3 GDPR](#step3)
    
    [2.4 Child Oriented Settings](#step4)

    [2.5 Android code obfuscation](#step5)
    
3. [Integration Notes](#note)

4. [Request Ad](#request)

    [4.1 Native](#native)
    * [Element-Native](#common)
    * [Element-Native with adCache](#cache)
    * [Element-Native for Multiple](#multi)
    
    [4.2 Banner](#banner)
    
    [4.3 Interstitial](#interstitial)

    [4.4 Splash](#splash)
    
<<<<<<< HEAD
    [4.5 Appwall](#appwall)
    
    [4.6 Rewarded Video](#reward)
    
=======
    [4.4 Splash](#splash)
    
    [4.5 Appwall](#appwall)
    
    [4.6 Rewarded Video](#reward)
    
>>>>>>> ad811aa9014ba76e6124fad398686a1a518953da
    [4.7 Native Video](#native_video)
    
5. [Error Code For SDK](#error)

## <a name="introduction">Introduction</a>

- GrowsTarry SDK supports Banner, Interstitial, Rewarded Video.
- GrowsTarry Android SDK supports Android API 15+.
- Please make sure you have added an app and at least one ad slot in GrowsTarry Platform.

## <a name="integration">2.Integration GrowsTarry SDK</a>  

### <a name="step1">2.1 Integrating the GrowsTarry SDK in to project</a>

* Check SDK in the rar.

* Detail of the different jars：

  | jar name                 | jar function                                    | require(Y/N) |
  | ------------------------ | ----------------------------------------------- | ------------ |
  | growstarry_base_xx.jar        | basic functions(banner\interstitial\native ads) | Y            |
  | growstarry_imageloader_xx.jar | imageloader functions                           | N            |
  | growstarry_appwall_xx.jar     | appwall ads functions                           | N            |
  | growstarry_video_xx.jar       | video ads functions                             | N            |

* Configure the module's build.gradle for basic functions：
* The landing page after our ads click depends on the androidbrowserhelper module, which can load our web pages with the help of chrome, which can effectively improve the efficiency of advertising conversion.
* The androidbrowserhelper module relies on androidx, so it needs your app to support androidx. if you are still using the support-x package, you need to provide compatible support


``` groovy
    dependencies {
        implementation files('libs/growstarry_base_xx.jar')
         //chrome custom tabs
        implementation 'com.google.androidbrowserhelper:androidbrowserhelper:1.3.1'
    }
```

* Configure AndroidManifest.xml

```xml
	<!--Necessary Permissions-->
	<uses-permission android:name="android.permission.INTERNET"/>
	<uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />

	<!-- Necessary -->
	<activity android:name="com.growstarry.kern.view.InnerWebViewActivity" />

	<provider
            android:authorities="${applicationId}.xxprovider"
            android:name="com.growstarry.kern.core.GrowsTarryProvider"
            android:exported="false"/>
	

	<!--If your targetSdkVersion is 28, you need update <application> as follows:-->
  	<application
    	...  	
        android:usesCleartextTraffic="true"
        ...>
        ...
    </application>
```



## <a name="step2">2.2 Initialization</a>  

> Init the SDK in your Application as detailed below: 

**Please make sure to initialize GrowsTarry SDK before doing anything.**

```java
    GrowsTarrySDK.initialize(context, "Your slotID");
```

**Set schema https**

```java
    GrowsTarrySDK.setSchema(true);
```

## <a name="step3">2.3 GDPR</a>  

**Use this interface to upload consent from affected users for GDPR**

```java
    /**
     * @param context       context
     * @param consentValue  whether the user agrees
     * @param consentType   the agreement name signed with users
     * @param listener      callback
     */
     GrowsTarrySDK.uploadConsent(this, true, "GDPR", new HttpRequester.Listener() {
            @Override
            public void onGetDataSucceed(byte[] data) {
            }


            @Override
            public void onGetDataFailed(String error) {
            }
     });
```
Warning:
1. If SDK don't gather the user informatian ,you probably get no fill. 
2. It is recommended that obtaining the user's consent before SDK initialization. 

## <a name="step4">2.4 Child Oriented Settings</a>  
In order to comply with the provisions of the Children's Online Privacy Protection Act (COPPA), we provide the setIsChildDirected interface.

Developers can use this interface to indicate that your content is child-oriented. We will stop personalized advertising and put in advertisements suitable for children，which may result in no filling.

``` java
     //child-oriented
     GrowsTarrySDK.setIsChildDirected(this, true);
```
Warning:
1. It is recommended to call this interface before requesting advertisements.

## <a name="step5">2.5 Obfuscation Configuration</a> 
> If it needs to obfuscate the codes in building the project process, you should add the following codes into the proguard file:

``` java
    #for sdk
    -keep public class com.growstarry.**{*;}
    -dontwarn com.growstarry.**

    #for gaid
    -keep class **.AdvertisingIdClient$** { *; }

    #for js and webview interface
    -keepclassmembers class * {
        @android.webkit.JavascriptInterface <methods>;
    }
    
```


## <a name="note">3.Integration Notes</a>

​	If you live in a country, such as China, which is forbidden google play, two prerequisites to get GrowsTarry ads: 
> * GooglePlay has installed on your device.
> * Your device have connect to VPN.

   

​	We suggest you define a class to implement the AdEventListener yourself , then you can just override the methods you need when you getBanner or getNative. See the following example:

``` java
public class MyCTAdEventListener extends AdEventListener {

     /**
     * Get ad success
     *
     * @param result Non-persistent object GTNative
     */
    @Override
    public void onReceiveAdSucceed(GTNative result) {
    }

    /**
     * Get ad success
     *
     * @param result Persistent object AdsVO
     */
    @Override
    public void onReceiveAdVoSucceed(AdsVO result) {
    }


    /**
     * Ad display success
     */
    @Override
    public void onShowSucceed(GTNative result) {
    }


    /**
     * Get ad failed Got data fail + Json fail + Rendering fail
     */
    @Override
    public void onReceiveAdFailed(GTNative result) {
        Log.i("sdksample", "==error==" + result.getErrorsMsg());
    }

    /**
     * Go to landing page
     */
    @Override
    public void onLandPageShown(GTNative result) {
    }

    /**
     * Ad was clicked
     */
    @Override
    public void onAdClicked(GTNative result) {
    }

    /**
     * Ad is closed
     */
    @Override
    public void onAdClosed(GTNative result) {
    }
}
```
## <a name="request">4.Request Ad</a>

## <a name="native">4.1 Native Ads Integration</a>

### <a name="common">Native ads interface</a>

> The container and the layout for Native ad:

```java
    ViewGroup container = (ViewGroup) view.findViewById(R.id.container);
    ViewGroup adLayout = (ViewGroup)View.inflate(context,R.layout.native_layout, null);
```

> The method to load Native Ads:

``` java
    /**
     * @param slotId     growstarry id
     * @param context    context
     * @param adListener callback listener 
     */
 	GrowsTarrySDK.getNativeAd("Your slotID", context, new MyCTAdEventListener(){
        @Override
        public void onReceiveAdSucceed(GTNative result) {
            if (result == null) {
                return;
            }
            GTAdvanceNative gtAdvanceNative = (GTAdvanceNative) result;
            showAd(gtAdvanceNative);
            Log.e(TAG, "onReceiveAdSucceed");
            super.onReceiveAdSucceed(result);
        }
     });
```

* Show the Native ad

``` java
    private void showAd(GTAdvanceNative gtAdvanceNative) {
        ImageView img = (ImageView) adLayout.findViewById(R.id.iv_img);
        ImageView icon = (ImageView) adLayout.findViewById(R.id.iv_icon);
        TextView title = (TextView)adLayout.findViewById(R.id.tv_title);
        TextView desc = (TextView)adLayout.findViewById(R.id.tv_desc);
        Button click = (Button)adLayout.findViewById(R.id.bt_click);
        ImageView adChoice = (ImageView)adLayout.findViewById(R.id.choice_icon);
        
        //show the image and icon yourself 
        if (gtAdvanceNative.getImageFile() == null) {
            //if no cache, get image from url
            img.setImageURI(Uri.parse(gtAdvanceNative.getImageUrl()));
        } else {
            //loaded from cache
            img.setImageURI(Uri.fromFile(gtAdvanceNative.getImageFile()));
        }

        if (gtAdvanceNative.getIconFile() == null) {
            //if no cache, get image from url
            icon.setImageURI(Uri.parse(gtAdvanceNative.getIconUrl()));
        } else {
            //loaded from cache
            icon.setImageURI(Uri.fromFile(gtAdvanceNative.getIconFile()));
        }
        
        title.setText(gtAdvanceNative.getTitle());
        desc.setText(gtAdvanceNative.getDesc());
        click.setText(gtAdvanceNative.getButtonStr());
        adChoice.setImageURI(gtAdvanceNative.getAdChoiceIconUrl());
        //offerType（1 : download ads; 2 : content ads）
        int offerType = gtAdvanceNative.getOfferType();  
         
        gtAdvanceNative.registeADClickArea(adLayout);
        container.addView(adLayout);
   }

```



### <a name="cache">Native ads interface for AdCache</a>

> Get Ads for cache

```java
    /**
     * @param slotId     growstarry id
     * @param context    context
     * @param adListener callback listener 
     */
    GrowsTarrySDK.getNativeAdForCache("Your slotID",context,new MyCTAdEventListener() {
        @Override
        public void onReceiveAdVoSucceed(AdsNativeVO result) {
            if (result == null) {
                return;
            }
            Log.e(TAG,"onReceiveAdVoSucceed");
            AdHolder.adNativeVO = result;
            super.onReceiveAdVoSucceed(result);
        }
    });

```

> Show ad from cache

```java
      GTAdvanceNative gtAdvanceNative = new GTAdvanceNative(getContext());
      AdsNativeVO nativeVO = AdHolder.adNativeVO;

	  if (nativeVO != null) {
            gtAdvanceNative.setNativeVO(nativeVO);
            gtAdvanceNative.setSecondAdEventListener(new MyCTAdEventListener() {
                @Override
                public void onAdClicked(GTNative result) {
                    Log.e(TAG, "onAdClicked");
                    super.onAdClicked(result);
                }
            });
            showAd(gtAdvanceNative);
       }
```



### <a name="multi">Multi Native ad interface</a>

> The method to load multi Native ad

``` java
    /**
     * @param reqAdNumber request ads num
     * @param slotId      growstarry id
     * @param context     context
     * @param adListener  callback listener 
     */
	GrowsTarrySDK.getNativeAds(10, "Your slotID", getContext(), new MultiAdsEventListener() {
        public void onMultiNativeAdsSuccessful(List<GTAdvanceNative> res) {
        }

        @Override
        public void onReceiveAdFailed(GTNative result) {
        }

        @Override
        public void onAdClicked(GTNative result) {
            super.onAdClicked(result);
        }
    });
```



## <a name="banner">4.2 Banner Ad Integration</a>

> The method to load Banner Ad:

``` java
	ViewGroup container = (ViewGroup) view.findViewById(R.id.container);

    /**
     * @param context           context
     * @param slotId            growstarry id
     * @param adSize			AdSize.AD_SIZE_320X50,
     							AdSize.AD_SIZE_320X100,
     							AdSize.AD_SIZE_300X250;
     * @param adListener        callback listener 
     */
	 GrowsTarrySDK.getBannerAd(context, "Your slotID", adSize,new MyCTAdEventListener() {
         @Override
         public void onReceiveAdSucceed(GTNative result) {
             if (result != null) {
                 container.setVisibility(View.VISIBLE);
                 container.removeAllViews();
                 container.addView(result);   //把广告添加到容器
             }
             super.onReceiveAdSucceed(result);
         }


         @Override
         public void onReceiveAdFailed(GTNative result) {
             super.onReceiveAdFailed(result);
         }


         @Override
         public void onAdClicked(GTNative result) {
             super.onAdClicked(result);
         }


         @Override
         public void onAdClosed(GTNative result) {
             container.removeAllViews();
             container.setVisibility(View.GONE);
             super.onAdClosed(result);
         }
     });
```

> When you successfully integrated the Banner Ad, you will see the ads are like this


![-1](https://user-images.githubusercontent.com/20314643/42366029-b6289f2a-8132-11e8-9c3e-86557d164d85.png)
![320x100](https://user-images.githubusercontent.com/20314643/42370991-c4188812-8140-11e8-80e9-ab6947c12e92.png)
![300x250](https://user-images.githubusercontent.com/20314643/42370999-c74139f8-8140-11e8-91ff-ba0cdb0ae08a.png)



## <a name="interstitial">4.3 Interstitial Ads Integration</a>

> Configure the AndroidManifest.xml for Interstitial

```xml
	<activity android:name="com.growstarry.kern.view.InterstitialActivity" />    
```

> The method to show Interstitial Ads

``` java
    /**
     * @param context           context
     * @param slotId            growstarry id
     * @param adListener        callback listener 
     */
    GrowsTarrySDK.preloadInterstitialAd(context, "Your slotID",new MyCTAdEventListener() {
                    
        @Override
        public void onReceiveAdSucceed(GTNative result) {              
            if (result != null && result.isLoaded()) {
                Log.w(TAG, "onReceiveAdSucceed");
                if (GrowsTarrySDK.isInterstitialAvailable(result)) {
            		GrowsTarrySDK.showInterstitialAd(result);
        		}
            }
            super.onReceiveAdSucceed(result);
        }

        @Override 
        public void onLandpageShown(GTNative result) {
            super.onLandpageShown(result);
            Log.e(TAG, "onLandpageShown:");
        }


        @Override
        public void onReceiveAdFailed(GTNative error) {
            Log.w(TAG, "onReceiveAdFailed: " + error.getErrorsMsg());
            super.onReceiveAdFailed(error);
        }


        @Override
        public void onAdClosed(GTNative result) {
            super.onAdClosed(result);
            Log.e(TAG, "onAdClosed:");
        }
    });
```

> When you successfully integrated the Interstitial Ad, you will see the ads are like this

![image](https://user-images.githubusercontent.com/20314643/41895879-b4536200-7955-11e8-9847-587f175c4a54.png)
![image](https://user-images.githubusercontent.com/20314643/41895941-e0c6ad1a-7955-11e8-9393-ed91e4a4906f.png)

<<<<<<< HEAD
## <a name="splash">4.4 Spalsh integration</a>

``` java
mSplashContainer = findViewById(R.id.splash_container);
GrowsTarrySDK.preloadSplashAd(this, Config.slotIdSplash, new AdEventListener() {

    @Override
    public void onReceiveAdSucceed(GTNative result) {
        GrowsTarrySDK.showSplashAd(Config.slotIdSplash, mSplashContainer);
    }

    @Override
    public void onReceiveAdVoSucceed(AdsVO result) {

    }

    @Override
    public void onReceiveAdFailed(GTNative result) {
        //startActivity(new Intent(SplashAdActivity.this, MainActivity.class));
        finish();
    }

    @Override
    public void onLandPageShown(GTNative result) {

    }

    @Override
    public void onAdClicked(GTNative result) {

    }

    @Override
    public void onAdClosed(GTNative result) {
        //startActivity(new Intent(SplashAdActivity.this, MainActivity.class));
        finish();
    }

    @Override
    public void onAdTimeOver() {
        super.onAdTimeOver();
        //startActivity(new Intent(SplashAdActivity.this, MainActivity.class));
        finish();
    }
});

=======
## <a name="splash">4.4 Splash Ads Integration</a>

> Configure the AndroidManifest.xml for Splash AD

```xml
	<activity android:name="com.growstarry.kern.view.SplashAdActivity" />    
```

> Get Splash AD

``` java
/**
 * @param context    context
 * @param slotId     slotId
 * @param listener callback listener
 * @param timeOut timeOut  timeout time (in milliseconds)
 */

GrowsTarrySDK.getSplashAd(this, Config.slotIdSplash, new MyCTAdEventListener() {


    @Override
    public void onReceiveAdSucceed(GTNative result) {
        Log.d(TAG, "Splash Ad Loaded.");
        if (!isFinishing()) {
            finish();
        }
    }

    @Override
    public void onReceiveAdFailed(GTNative result) {
        if (result != null && result.getErrorsMsg() != null)
            Log.e(TAG, "onReceiveAdFailed errorMsg=" + result.getErrorsMsg());
    }

    @Override
    public void onShowSucceed(GTNative result) {
        Log.d(TAG, "onShowSucceed");
        if (result != null) {
            SplashView splashView = (SplashView) result;

            /*
             * There are two ways to add a custom view
             * inflate SplashView.getCustomParentView() or SplashView.addCustomView(view)
             */
            //1
            //LayoutInflater.from(getContext()).inflate(R.layout.custom_splash_layout, splashView.getCustomParentView(), true);

            //2
            LinearLayout linearLayout = new LinearLayout(result.getContext());
            linearLayout.setGravity(Gravity.CENTER);
            linearLayout.setBackgroundColor(Color.WHITE);
            linearLayout.setLayoutParams(new ViewGroup.LayoutParams(MATCH_PARENT, Utils.dpToPx(100)));
            TextView textView = new TextView(result.getContext());
            textView.setText("custom");
            textView.setTextSize(22);
            linearLayout.addView(textView, new ViewGroup.LayoutParams(WRAP_CONTENT, WRAP_CONTENT));
            linearLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(getBaseContext(), "custom", Toast.LENGTH_SHORT).show();
                }
            });
            splashView.addCustomView(linearLayout);
        }
    }

    @Override
    public void onLandPageShown(GTNative result) {
        Log.d(TAG, "onLandPageShown");
    }

    @Override
    public void onAdClicked(GTNative result) {
        Log.d(TAG, "onAdClicked");
    }

    @Override
    public void onAdClosed(GTNative result) {
        Log.d(TAG, "onAdClosed");
    }

}, TIME_OUT);
>>>>>>> ad811aa9014ba76e6124fad398686a1a518953da
```

## <a name="appwall">4.5 Appwall integration</a>

> Configure the module's build.gradle for Appwall：

``` groovy
	dependencies {
        	compile files('libs/growstarry_base_xx.jar')
        	compile files('libs/growstarry_appwall_xx.jar')       // for appwall        
        	compile files('libs/growstarry_imageloader_xx.jar')   // for imageloader
	}
```

> Configure the AndroidManifest.xml for Appwall

``` xml
    <activity
            android:name="com.growstarry.appwall.AppwallActivity"
            android:screenOrientation="portrait" />  
```

> Preload appwall

> It‘s better to preload ads for Appwall, to ensure they show properly and in a timely fashion. You can have ads preload with the following line of code:

``` java
   GrowsTarryAppwall.preloadAppwall(context, "Your slotID");
```

> Customize the appwall color theme(optional).

``` java
    CustomizeColor custimozeColor = new CustomizeColor();
    custimozeColor.setMainThemeColor(Color.parseColor("#ff0000ff"));
    GrowsTarryAppwall.setThemeColor(custimozeColor);
```

> Show Appwall.

``` java
     GrowsTarryAppwall.showAppwall(context, "Your slotID");
```

> When you successfully integrated the APP Wall Ad, you will see the ads are like this

![image](https://user-images.githubusercontent.com/20314643/42366246-47526c9c-8133-11e8-963c-bd0eb7a3e1a6.png)



## <a name="reward">4.6 Rewarded Video Ad Integration</a>

> **Google Play Services**

1. Google Advertising ID

 The Rewarded Video function requires access to the Google Advertising ID in order to operate properly.
     See this guide on how to integrate [Google Play Services](https://developers.google.com/android/guides/setup).

2. Google Play Services in Your Android Manifest

    Add the following  inside the <application> tag in your AndroidManifest:

    ```
    <meta-data
    	android:name="com.google.android.gms.ads.AD_MANAGER_APP"
        android:value="true" />
    ```

3. If you have integrated the admob-sdk for basic ads, it's not necessary to do this.


> Configure the module's build.gradle for Rewarded Video：

``` groovy
	dependencies {
        	compile files('libs/growstarry_base_xx.jar')
        	compile files('libs/growstarry_video_xx.jar')
        	compile files('libs/growstarry_imageloader_xx.jar')
	}
```

> Configure the AndroidManifest.xml for Rewarded Video:

``` xml    
<activity
	android:name="com.growstarry.video.view.RewardedVideoActivity"  android:configChanges="keyboard|keyboardHidden|orientation|screenLayout|uiMode|screenSize|smallestScreenSize" />
```

> Setup the UserID

> You should set the userID for s2s-postback before preloading the RewardedVideo function call


```java
	GrowsTarryVideo.setUserId("custom_id");
```

> Preload the RewardedVideo

> It‘s best to call this interface when you want to show RewardedVideo ads to the user. This will help reduce latency and ensure effective, prompt delivery of ads


``` java
 	GrowsTarryVideo.preloadRewardedVideo(getContext(), Config.slotIdReward,
                new VideoAdLoadListener() {
                    @Override
                    public void onVideoAdLoadSucceed(GTVideo videoAd) {
                        video = videoAd;
                        Log.w(TAG, "onVideoAdLoadSucceed: ");
                    }


                    @Override
                    public void onVideoAdLoadFailed(GTError error) {
                        Log.w(TAG, "onVideoAdLoadFailed: " + error.getMsg());
                    }
                });

```

> Show Rewarded Video ads to Your Users
> Before showing the video, you can request or query the video status by calling:

```java
    boolean available = GrowsTarryVideo.isRewardedVideoAvailable(video);
```

> Because it takes a while to load the video creatives. You may need to wait until video creatives loads completely. Once you get an available Reward Video, you are ready to show this video ad to your users by calling the showRewardedVideo() method as in the following example:

```java
  GrowsTarryVideo.showRewardedVideo(video, new RewardedVideoAdListener() {
                @Override
                public void videoStart() {
                    Log.e(TAG, "videoStart: ");
                }

                @Override
                public void videoFinish() {
                    Log.e(TAG, "videoFinish: ");
                }

                @Override
                public void videoError(Exception e) {
                    Log.e(TAG, "videoError: ");
                }

                @Override
                public void videoClosed() {
                    Log.e(TAG, "videoClosed: ");
                }

                @Override
                public void videoClicked() {
                    Log.e(TAG, "videoClicked: ");
                }

                @Override
                public void videoRewarded(String rewardName, String rewardAmount) {
                    Log.e(TAG, "videoRewarded: ");
                }
            });

```

> Reward the User

The SDK will fire the videoRewarded event each time the user successfully completes a video. The RewardedVideoListener will be in place to receive this event so you can reward the user successfully.

The Reward object contains both the Reward Name & Reward Amount of the SlotId as defined in your GrowsTarry SSP:

```java

public void videoRewarded(String rewardName, String rewardAmount) {
      //TODO - here you can reward the user according to the given amount
       Log.e(TAG, "videoRewarded: ");
   }
    
```

> When you successfully integrated the Rewarded Video, you will see the ads are like this

![image](https://user-images.githubusercontent.com/20314643/42371626-94e8e6a2-8142-11e8-9598-eb75de753070.png)


## <a name="native_video">4.7 Native Video Ad Integration</a>

> Configure the module's build.gradle for Native Video：

```groovy
	dependencies {
        compile files('libs/growstarry_base_xx.jar')
        compile files('libs/growstarry_video_xx.jar')
	}
```

> Load the NativeVideo

```java
   GrowsTarryVideo.getNativeVideo(context, Config.slotIdNativeVideo, new VideoAdLoadListener(){
            @Override
            public void onVideoAdLoadSucceed(GTVideo video) {
                showNativeVideo((GTNativeVideo) video);
            }


            @Override
            public void onVideoAdLoadFailed(GTError error) {
            }
        });

```

> Show the NativeVideo

```java
    private void showNativeVideo(GTNativeVideo gtNativeVideo) {
        if (video == null) {
            return;
        }
        
        //layout for NativeVideo
        ViewGroup videoLayout = (ViewGroup) View.inflate(context, R.layout.native_video_layout, null);
      
        //whether autoplay for 4G
        gtNativeVideo.setWWANPlayEnabled(false);

        TextView video_title = videoLayout.findViewById(R.id.video_title);
        RelativeLayout video_container = videoLayout.findViewById(R.id.video_container);
        SimpleDraweeView video_choice = videoLayout.findViewById(R.id.video_choice);
        TextView video_desc = videoLayout.findViewById(R.id.video_desc);
        TextView video_button = videoLayout.findViewById(R.id.video_button);

        video_title.setText(gtNativeVideo.getTitle());
        video_container.addView(gtNativeVideo.getNativeVideoAdView());
        video_choice.setImageURI(Uri.parse(gtNativeVideo.getAdChoiceIconUrl()));
        video_desc.setText(Html.fromHtml(gtNativeVideo.getDesc()));
        video_button.setText(gtNativeVideo.getButtonStr());

        //register for tracking， videolayout is the clickarea
        gtNativeVideo.registerForTracking(videoLayout, 
            new NativeVideoAdListener() {
                @Override
                public void videoPlayBegin() {
                }

                @Override
                public void videoPlayFinished() {
                }

                @Override
                public void videoPlayClicked() {
                }

                @Override
                public void videoPlayError(Exception e) {
                }
        });

        //container in your app
        rl_container.removeAllViews();
        rl_container.addView(videoLayout);
    }
```
## <a name="error">Error Code For SDK</a>

| Error Code                        | Description                              |
| --------------------------------- | ---------------------------------------- |
| ERR\_000\_TRACK                   | Track exception                          |
| ERR\_001\_INVALID_INPUT           | Invalid parameter                        |
| ERR\_002\_NETWORK                 | Network exception                        |
| ERR\_003\_REAL_API                | Error from Ad Server                     |
| ERR\_004\_INVALID_DATA            | Invalid advertisement data               |
| ERR\_005\_RENDER_FAIL             | Advertisement render failed              |
| ERR\_006\_LANDING_URL             | Landing URL failed                       |
| ERR\_007\_TO_DEFAULT_MARKET       | Default Landing URL failed               |
| ERR\_008\_DL_URL                  | Deep-Link exception                      |
| ERR\_009\_DL_URL_JUMP             | Deep-Link jump exception                 |
| ERR\_010\_APK_DOWNLOAD_URL        | Application package download failed      |
| ERR\_011\_APK_INSTALL             | Application install failed               |
| ERR\_012\_VIDEO_LOAD              | Load the video exception                 |
| ERR\_013\_PAGE_LOAD               | HTML5 page load failed                   |
| ERR\_014\_JAR_UPDATE_VERSION      | Update jar check failed                  |
| ERR\_015\_GET_GAID                | Cannot get google advertisement id - GAID retrieval failed |
| ERR\_016\_GET_AD_CONFIG           | Cannot get the account configuration or template |
| ERR\_017\_INTERSTITIAL_SHOW_NO_AD | Tried to load the interstitial advertisement, but the advertisement is not ready/loading |
| ERR\_018\_AD_CLOSED               | Ad slotId has been closed                |
| ERR\_999\_OTHERS                  | All other errors                         |


