rootProject.name = "ComposeMediaPlayer"
enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")


include(":composeApp")
include(":mediaPlayer")

pluginManagement {
    repositories {
        google()
        gradlePluginPortal()
        mavenCentral()
    }
}

dependencyResolutionManagement {
    repositories {
        google()
        mavenCentral()
        mavenLocal()
    }
}
