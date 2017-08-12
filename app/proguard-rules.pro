# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in D:\ANDROID\sdk/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Add any project specific keep options here:

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

-keepclasseswithmembernames class * {
    native <methods>;
}

-dontwarn org.apache.http.annotation.**

-keep class com.sinch.** { *; }
-keep interface com.sinch.** { *; }
-keep class org.webrtc.** { *; }

-keep class com.google.android.gms.** { *; }

-dontwarn com.google.android.gms.**
-dontwarn android.media.**

-ignorewarnings
-keep class * {
    public private *;
}

-keep public class * implements com.bumptech.glide.module.GlideModule
-keep public enum com.bumptech.glide.load.resource.bitmap.ImageHeaderParser$** {
  **[] $VALUES;
  public *;
}

# for DexGuard only
#-keepresourcexmlelements manifest/application/meta-data@value=GlideModule

#-keep public class com.paypal.android.sdk.**{ *;}
#-keep public class OkHttpClient
#-keep public class okio
#-keep public class okhttp3
#-keep public class Lokhttp3
