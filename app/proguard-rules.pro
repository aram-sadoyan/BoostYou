# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile


-keepattributes *Annotation*

# Okio
-keep class sun.misc.Unsafe { *; }
-dontwarn java.nio.file.*
-dontwarn org.codehaus.mojo.animal_sniffer.IgnoreJRERequirement
-keep class okio.** { *; }
-dontwarn okio.**
-keep public class * extends android.app.Activity
-keep public class * extends android.app.Application
-keep class androidx.core.app.** { *; }
-keep class androidx.core.content.** { *; }
-keep interface androidx.core.app.** { *; }
-keep class androidx.core.content.** { *; }
-keep class androidx.appcompat.app.** { *; }
-keep class androidx.preference.internal.** { *; }
-keep class androidx.vectordrawable.graphics.** { *; }
-keep class androidx.cardview.** { *; }
-keep class androidx.appcompat.** { *; }
-keep interface androidx.appcompat.app.** { *; }
-keep class androidx.appcompat.widget.** { *; }
-keep class androidx.preference.internal.* { *; }
-keep class androidx.recyclerview.widget.* { *; }
-keep public class * extends android.preference.Preference

-keep public class com.socialin.android.lib.AmazonPurchaseActivity
-keep public class com.socialin.android_samsung_billing_lib.SamsungPurchaseActivity

-keep class androidx.legacy.app.** { *; }
-keep interface androidx.legacy.app.** { *; }
-keep class androidx.multidex.** { *; }
-keep class com.google.android.material.** { *; }

#for amazon purchase and ads
-dontwarn com.amazon.**
-keep class com.amazon.** {*;}
-keepattributes *Annotation*

-keep class * implements android.os.Parcelable {
  public static final android.os.Parcelable$Creator *;
}

-keep public class com.picsart.studio.activity.** {
	*;
}

-keepclassmembers class * extends android.app.Activity {
   public void *(android.view.View);
}

-keepclasseswithmembernames class * {
    native <methods>;
}

#------------------------ Retrofit ------------------------
# Platform calls Class.forName on types which do not exist on Android to determine platform.
-dontnote retrofit2.Platform
-dontwarn retrofit2.**
-keep class retrofit2.** { *; }

#-libraryjars ../android_getjar_billing_lib/libs/GetJarSDK-3.4.4.20130503.06.jar

