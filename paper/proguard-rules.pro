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

# Preserve reflection-based class loading (for DexClassLoader)
-keep class com.yash.paper.** { *; }

# Keep any dynamically loaded or reflective classes
-keepclassmembers class * {
    @androidx.annotation.Keep *;
}

# Keep your dynamic loader code (to avoid optimization issues)
-keep class com.yash.dev.DexLoaderKt { *; }

# Keep dalvik and system classloaders
-keep class dalvik.system.** { *; }

# (Optional but recommended) preserve annotations and line info for debugging
-keepattributes SourceFile,LineNumberTable,RuntimeVisibleAnnotations,RuntimeVisibleParameterAnnotations
