# AdMob Mediation Integration Guide

This guide instructs you step-by-step on how to set GrowsTarry live as an Ad Network on the Admob Mediation platform.

Currently we support Rewarded Video, Banner and Interstitial for mediation.

### Before You Start

- Make sure you have creat an application and ad slotIds on the our Platform.
- [Download the latest GrowsTarry SDK](https://github.com/GrowsTarry/GrowsTarry/blob/master/AndroidSDK.zip)
- [Download the GrowsTarry Adapter for Admob](https://github.com/GrowsTarry/GrowsTarry/blob/master/GrowsTarrySDK_Adapter-For-Admob.zip)


### Step 1: Integrate GrowsTarry SDK and Adapter

* Detail of GrowsTarry SDK

    | jar name | jar function |
    | --- | --- |
    | growstarry_base_xx.jar        | for banner\interstitial |
    | growstarry_video_xx.jar       | RewardedVideo function |
    | growstarry_imageloader_xx.jar | Imageloader function |

* Add the needed jars to your module's libs/
* Update the module's build.gradle as follows

 ```
    dependencies {
       implementation files('libs/growstarry_base_xx.jar')
       implementation files('libs/growstarry_video_xx.jar')
       implementation files('libs/growstarry_imageloader_xx.jar')
    }
 ```

* Update the AndroidManifest.xml

 ```
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

    <!-- for growstarry rewardvideo -->
    <activity
        android:name="com.growstarry.video.view.RewardedVideoActivity"
        android:configChanges="keyboard|keyboardHidden|orientation|screenLayout|uiMode|screenSize|smallestScreenSize" />

    <!-- for growstarry interstitial ads -->
    <activity android:name="com.growstarry.kern.view.InterstitialActivity" />
 ```


* Copy the needed adapter files into your project source folder, like follows:

    ![image](https://user-images.githubusercontent.com/15087458/50626675-898a4100-0f6a-11e9-815f-830ff0d72a55.png)

### Step 2: Create Mediation Group

![image](https://user-images.githubusercontent.com/20314643/34598723-6a3249e2-f229-11e7-96b8-0ba05840c957.png)

1.1 Choose the ad format and platform

Currently we support rewarded video and interstitial mediation for Android

![image](https://user-images.githubusercontent.com/20314643/34598771-abf82482-f229-11e7-8f5b-849a295461fa.png)

1.2 Select your targeting location, turn the status on and "add ad units"

![image](https://user-images.githubusercontent.com/20314643/34598876-34953cd0-f22a-11e7-9d76-f2610179dc99.png)

1.3 Select your application and ad units that you plan to integrate with Cloudmobi

![image](https://user-images.githubusercontent.com/20314643/34598969-da664082-f22a-11e7-9441-c6aca7cd93ba.png)

### step 3: Define Custom Events

”eCPM“ determines the order of the ad network to serve ads.

![image](https://user-images.githubusercontent.com/20314643/34599932-fab8e6c8-f22f-11e7-93df-37119420eb58.png)

![image](https://user-images.githubusercontent.com/20314643/34600140-f0e26a74-f230-11e7-9451-baaaf675b2ce.png)

### Step 4: Add GrowStarry custom event in Admob console

![image](https://user-images.githubusercontent.com/20314643/34600301-c64459c0-f231-11e7-8ab5-67a61423e5ea.png)

(1) Class Names should match the ad formats in GrowStarry -- for example, if you are integrating rewarded video ads, the full class name should be: 

 ```
com.growstarry.mediation.admob.GTCustomEventRewardedVideo
 ```

(2) Parameter is the slot ID, you can get it from our operational staff

### Step 5. Add ProGuard Rules

```
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
**Done!** You are now all set to deliver GrowsTarry Ads within your application!

For more details information about Custom Events in Admob Mediation, please click [here](https://developers.google.com/admob/android/custom-events)

