// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.google.services) apply false
    alias(libs.plugins.detekt)
}

val detektFormating = libs.detekt.formatting
val detektCompose = libs.detekt.compose
val detektPlugin = libs.plugins.detekt.get().pluginId

subprojects{

   apply{
       plugin(detektPlugin)
       from("${rootProject.projectDir}/quality/quality.gradle")
   }

   detekt{
       config.from(rootProject.files("quality/detekt.yml"))
   }

   dependencies{
       detektPlugins(detektFormating)
       detektPlugins(detektCompose)
   }
}