# Adhese SDK Sample App
## Introduction
This sample app demonstrates the usage of the Adhese Android SDK. The app simply displays some lorem ipsum
text with two ads in-between.

## Developing locally
Download the SDK code and put the folder at the same level of the sample app. Then add the following to your gradle files:

*app/build.gradle*

    implementation project(':sdk')
    
*settings.gradle*
    
    include ':app', 'sdk'
    project(':sdk').projectDir = new File('../adhese-sdk-android/sdk')

``

Finally, re-sync the project via Gradle.