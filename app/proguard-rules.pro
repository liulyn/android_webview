# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in D:\Android\sdk/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html
# Add any project specific keep options here:
# 设置混淆的压缩比率 0 ~ 7
-optimizationpasses 5
# Aa aA
-dontusemixedcaseclassnames
# 如果应用程序引入的有jar包,并且想混淆jar包里面的class
-dontskipnonpubliclibraryclasses
-dontpreverify
# 混淆后生产映射文件 map 类名->转化后类名的映射
-verbose                    # 混淆时是否记录日志
# 混淆采用的算法.
-optimizations !code/simplification/arithmetic,!field/*,!class/merging/*
# 所有activity的子类不要去混淆
-keep public class * extends android.app.Activity
-keep public class * extends android.app.Application
-keep public class * extends android.app.Service
-keep public class * extends android.content.BroadcastReceiver
-keep public class * extends android.content.ContentProvider
-keep public class * extends android.app.backup.BackupAgentHelper
-keep public class * extends android.preference.Preference
-keep public class com.android.vending.licensing.ILicensingService
-keep class android.support.v4.**{*;}
-keepattributes *Annotation*
-dontwarn android.support.v4.**
-dontwarn android.annotation
-dontwarn android.webkit.**
#内部类及Annotation相关类不混淆
-keepattributes Signature
-keepattributes *Annotation*
-keepattributes InnerClasses
-keepattributes InnerClasses,EnclosingMethod
# 所有native的方法不能去混淆.
-keepclasseswithmembernames class * {
    native <methods>;
}
# 某些构造方法不能去混淆
-keepclasseswithmembers class * {
    public <init>(android.content.Context, android.util.AttributeSet);
}
-keepclasseswithmembers class * {
    public <init>(android.content.Context, android.util.AttributeSet, int);
}
-keepclassmembers class * extends android.app.Activity {
   public void *(android.view.View);
}
#枚举类不能去混淆
-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}
# ============================================
# 对于引用第三方包的情况，可以采用下面方式避免打包出错：
-keepclassmembers class * {
   public <init>(org.json.JSONObject);
}
# ==================eventbusbus start=================
-keep class org.greenrobot.** {*;}
-keep class de.greenrobot.** {*;}
-keepclassmembers class ** {
    public void onEvent*(**);
    void onEvent*(**);
}
# ==================eventbusbus end=================
-keep class com.baoyachi.stepview.**{*;}
-keep class com.baoyachi.stepview.bean.**{*;}
# ==================okhttp start=================
-dontwarn com.squareup.okhttp.**
-keep class com.zhy.http.okhttp.**{*;}
-keep class com.squareup.okhttp.** { *;}
-dontwarn okio.**
# ==================okhttp end=================
# ==================zxing start=================
-dontwarn com.google.zxing.**
-keep  class com.google.zxing.**{*;}
# ==================zxing end=================
-keep class com.google.**{*;}
-keepclassmembers class * implements java.io.Serializable {
    static final long serialVersionUID;
    private static final java.io.ObjectStreamField[] serialPersistentFields;
    private void writeObject(java.io.ObjectOutputStream);
    private void readObject(java.io.ObjectInputStream);
    java.lang.Object writeReplace();
    java.lang.Object readResolve();
}
-keep class * implements android.os.Parcelable {
  public static final android.os.Parcelable$Creator *;
}
#上面是导入的model。同样的其他的也是如此
-keep class com.github.** { *; }
#上面这个是避免混淆你导入的maven类库之类的
-dontwarn#不用输出警告
#-ignorewarning-dump class_files.txt
-printseeds seeds.txt
-printusage unused.txt
-printmapping mapping.txt
#生成对应的混淆信息
#-libraryjars libs/alipaySdk-20161009.jar
#-libraryjars libs/libammsdk
-keep class com.cloud.tao.net.** {*;}
# ==================wxapi start=================
-keep class com.tencent.mm.sdk.** {*;}
-dontwarn com.tencent.mm.**
# ==================wxapi end=================
# ==================http start=================
-dontwarn org.apache.**
-dontwarn android.net.SSLCertificateSocketFactory
# ==================http end=================
#---------------------------------实体类---------------------------------
-keep class com.cloud.tao.bean.** { *; }
-keep class com.cloud.tao.bean.etc.** { *; }
-keep class com.cloud.tao.bean.etc.event.** { *; }
-keep class com.cloud.tao.bean.etc.upgrade.** { *; }
#-keep public class com.cloud.tao.util.** { *;}