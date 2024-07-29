plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.jetbrains.kotlin.android) apply false
    id("com.google.dagger.hilt.android") version "2.46" apply false
    id ("androidx.navigation.safeargs") version "2.7.6" apply false
}