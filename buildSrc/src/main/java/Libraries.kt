import Version.AndroidXTestVersion
import Version.AppCompat
import Version.CoreKtx
import Version.EspressoCore
import Version.GsonVersion
import Version.HiltAndroidVersion
import Version.HiltNavigationCompose
import Version.JunitExtKtx
import Version.LifecycleRunTimeKtx
import Version.NavigationCompose
import Version.SplashScreenApi
import Version.TestRunnerVersion
import Version.TruthExt

object Version {
    const val CoreKtx = "1.9.0"
    const val AppCompat = "1.6.1"
    const val ComposeBom = "2023.05.01"
    const val LifecycleRunTimeKtx = "2.3.1"
    const val NavigationCompose = "2.5.3"
    const val AndroidXTestVersion = "1.5.0"
    const val EspressoCore = "3.5.1"
    const val TestRunnerVersion = "1.5.2"
    const val JunitExtKtx = "1.1.5"
    const val TruthExt = "1.5.0"
    const val HiltNavigationCompose = "1.0.0"
    const val HiltAndroidVersion = "2.44"
    const val Accompanist = "0.28.0"
    const val SplashScreenApi = "1.1.0-alpha01"
    const val GsonVersion = "2.10.1"
    const val RetrofitVersion = "2.9.0"
    const val OkHttpVersion = "4.10.0"
    const val DataStoreVersion = "1.0.0"
}


object Libraries {
    object AndroidX {
        const val coreKtx = "androidx.core:core-ktx:$CoreKtx"
        const val appCompat = "androidx.appcompat:appcompat:$AppCompat"
        const val lifecycleRunTimeKtx =
            "androidx.lifecycle:lifecycle-runtime-ktx:$LifecycleRunTimeKtx"
        const val splashScreen = "androidx.core:core-splashscreen:$SplashScreenApi"
    }

    object Compose {
        const val composeBom = "androidx.compose:compose-bom:${Version.ComposeBom}"
        const val composeUi = "androidx.compose.ui:ui"
        const val composeUiToolingPreview = "androidx.compose.ui:ui-tooling-preview"
        const val composeMaterial3 = "androidx.compose.material3:material3"
        const val composeFoundation = "androidx.compose.foundation:foundation"
        const val composeRuntime = "androidx.compose.runtime:runtime"
        const val composeActivity = "androidx.activity:activity-compose:1.6.1"
        const val composeUiUtil = "androidx.compose.ui:ui-util"
    }

    object Google {
        const val gson = "com.google.code.gson:gson:$GsonVersion"
    }

    object Accompanist {
        const val systemuicontroller =
            "com.google.accompanist:accompanist-systemuicontroller:${Version.Accompanist}"
    }

    object Naviagtion {
        const val navigationCompose = "androidx.navigation:navigation-compose:$NavigationCompose"
    }

    object Test {
        const val testCoreKtx = "androidx.test:core-ktx:$AndroidXTestVersion"
        const val espressorCore = "androidx.test.espresso:espresso-core:$EspressoCore"
        const val junitExtKtx = "androidx.test.ext:junit-ktx:$JunitExtKtx"
        const val truthExt = "androidx.test.ext:truth:$TruthExt"
        const val runner = "androidx.test:runner:$TestRunnerVersion"
    }


    object Hilt {
        const val hiltAndroid = "com.google.dagger:hilt-android:$HiltAndroidVersion"
        const val hiltCompiler = "com.google.dagger:hilt-android-compiler:$HiltAndroidVersion"

        //hilt compose
        const val hiltNavigationCompse =
            "androidx.hilt:hilt-navigation-compose:$HiltNavigationCompose"
    }

    object Network {
        const val retrofit = "com.squareup.retrofit2:retrofit:${Version.RetrofitVersion}"
        const val retrofitGsonConverter =
            "com.squareup.retrofit2:converter-gson:${Version.RetrofitVersion}"
        const val okhttpLogging =
            "com.squareup.okhttp3:logging-interceptor:${Version.OkHttpVersion}"
    }

    object Storage {
        const val dataStorePreferences =
            "androidx.datastore:datastore-preferences:${Version.DataStoreVersion}"
    }
}
