
<p align="center">
    <a>
    <img src="QR_COde.png" width="200" height="200"/>
    </a>
    <h1 align="center">QR Code <br> (Generator & Scanner)</h1>
</p>

## 🌟 About
This My first Android application! Written in <b>Kotlin</b> <br>
This last 4 weeks have been amazing , starting from learning KOTLIN CODELABS to making an app :) 

## 🚀 Features
<h5>
	✔ 1. Genrate an QR code. </br>
	✔ 2. Save QR result for future use. </br>
	✔ 3. Share your QR Code to other app. </br>
	✔ 4. Copy as a plain text to use.</br>
	✔ 5. Scan Barcode and QR code using Camera.</br>
</h5>

## 📃 Libraries used
[ZXing][1] <br>
I have Used [ZXing][1] library for Scanning and Generating QR code with Android.



### Adding aar dependency with Gradle 
In term of using ZXing Libraries you need to qdd the following to your `build.gradle` file:

```groovy
dependencies {
    implementation 'me.dm7.barcodescanner:zxing:1.9.8'
    implementation 'com.google.zxing:core:3.3.0'
    implementation 'com.journeyapps:zxing-android-embedded:3.2.0@aar'
}
```

You'll also need this in your Android manifest:

```xml
<uses-sdk tools:overrideLibrary="com.google.zxing.client.android" />
```


<p align="center">
    <a>
    <img src="Project.gif" width="300" height="600"/>
    </a>
    <h2 align="center">Preview of App</h2>
</p>

## Developed By:

**Sahil Sheikh**
iamsahilsk99@gmail.com

[Download apk][2]

[1]: https://github.com/zxing/zxing/
[2]:https://drive.google.com/file/d/1MecJfpB5_ENMLMJIGBcSCdjk2R31fZG-/view?usp=sharing
