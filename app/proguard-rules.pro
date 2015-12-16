# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in /home/bkosarzycki/Android/Sdk/tools/proguard/proguard-android.txt
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

# retrofit
-dontwarn retrofit.**
-keep class retrofit.** { *; }
-keepattributes Signature
-keepattributes Exceptions
-dontwarn okio.**

# keep model
-keep class com.bkosarzycki.example.autocompleteexample.model.** { *; }

# guava
-injars path/to/myapplication.jar
-injars lib/guava-r07.jar
-libraryjars lib/jsr305.jar
-outjars myapplication-dist.jar

-dontoptimize
-dontobfuscate
-dontwarn sun.misc.Unsafe
-dontwarn com.google.common.collect.MinMaxPriorityQueue

-keepclasseswithmembers public class * {
    public static void main(java.lang.String[]);
}

# glide lib
-keep public class * implements com.bumptech.glide.module.GlideModule
-keep public enum com.bumptech.glide.load.resource.bitmap.ImageHeaderParser$** {
    **[] $VALUES;
    public *;
}

# dagger 2.0.2 temp fix
-keep class dagger.** { *; }
-keepattributes Signature
-keepattributes Exceptions
-dontwarn dagger.**
