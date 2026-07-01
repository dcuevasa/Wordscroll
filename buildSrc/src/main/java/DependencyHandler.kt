import org.gradle.api.artifacts.Dependency
import org.gradle.api.artifacts.dsl.DependencyHandler

fun DependencyHandler.implementation(dependencyNotation: Any): Dependency? =
    add("implementation", dependencyNotation)

fun DependencyHandler.testImplementation(dependencyNotation: Any): Dependency? =
    add("testImplementation", dependencyNotation)

fun DependencyHandler.androidTestImplementation(dependencyNotation: Any): Dependency? =
    add("androidTestImplementation", dependencyNotation)

fun DependencyHandler.kapt(dependencyNotation: Any): Dependency? =
    add("kapt", dependencyNotation)

fun DependencyHandler.composeDependencies() {
    implementation(platform(Libraries.Compose.composeBom))
    implementation(Libraries.Compose.composeActivity)
    implementation(Libraries.Compose.composeUi)
    implementation(Libraries.Compose.composeUiToolingPreview)
    implementation(Libraries.Compose.composeUiUtil)
    implementation(Libraries.Compose.composeFoundation)
    implementation(Libraries.Compose.composeRuntime)
    implementation(Libraries.Compose.composeMaterial3)

    //navgation
    implementation(Libraries.Naviagtion.navigationCompose)

    //hilt navigation
    implementation(Libraries.Hilt.hiltNavigationCompse)

    //accompanist
    accompanistDependencies()
}

fun DependencyHandler.baseDependencies() {
    implementation(Libraries.AndroidX.appCompat)
    implementation(Libraries.AndroidX.coreKtx)
    implementation(Libraries.AndroidX.lifecycleRunTimeKtx)
    implementation(Libraries.AndroidX.splashScreen)
    implementation(Libraries.Google.gson)
    implementation(Libraries.Hilt.hiltAndroid)
    kapt(Libraries.Hilt.hiltCompiler)
}

fun DependencyHandler.accompanistDependencies() {
    implementation(Libraries.Accompanist.systemuicontroller)
}

fun DependencyHandler.testDependencies() {
    androidTestImplementation(Libraries.Test.testCoreKtx)
    androidTestImplementation(Libraries.Test.espressorCore)
    androidTestImplementation(Libraries.Test.runner)
    androidTestImplementation(Libraries.Test.junitExtKtx)
    androidTestImplementation(Libraries.Test.truthExt)
}

fun DependencyHandler.networkDependencies() {
    implementation(Libraries.Network.retrofit)
    implementation(Libraries.Network.retrofitGsonConverter)
    implementation(Libraries.Network.okhttpLogging)
    implementation(Libraries.Storage.dataStorePreferences)
}

fun DependencyHandler.moduleDependencies() {
    DOMAIN
    DATA
    CORE
    COMMON_THEME
    COMMON_COMPOSABLE
    FEATURE_HOME
    FEATURE_CREATOR_PROFILE
}


val DependencyHandler.DOMAIN
    get() = implementation(project(mapOf("path" to ":domain")))

val DependencyHandler.DATA
    get() = implementation(project(mapOf("path" to ":data")))

val DependencyHandler.CORE
    get() = implementation(project(mapOf("path" to ":core")))

val DependencyHandler.COMMON_COMPOSABLE
    get() = implementation(project(mapOf("path" to ":common:composable")))

val DependencyHandler.COMMON_THEME
    get() = implementation(project(mapOf("path" to ":common:theme")))

val DependencyHandler.FEATURE_HOME
    get() = implementation(project(mapOf("path" to ":feature:home")))

val DependencyHandler.FEATURE_CREATOR_PROFILE
    get() = implementation(project(mapOf("path" to ":feature:creatorprofile")))
