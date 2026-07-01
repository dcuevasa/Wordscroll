pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}
rootProject.name = "wordscroll"
include(":app")
include(":common:theme")
include(":domain")
include(":data")
include(":core")
include(":common:composable")
include(":feature:home")
include(":feature:creatorprofile")
