# Flutter wrapper
-keep class io.flutter.** { *; }
-keep class io.flutter.plugins.** { *; }

# Suppress all missing Play Core classes (Flutter deferred components - not used in this app)
-dontwarn com.google.android.play.core.**
-dontwarn javax.xml.stream.XMLStreamException
-dontwarn retrofit2.Call
-dontwarn retrofit2.Callback
-dontwarn retrofit2.Converter$Factory
-dontwarn retrofit2.Response
-dontwarn retrofit2.Retrofit$Builder
-dontwarn retrofit2.Retrofit
-dontwarn retrofit2.converter.gson.GsonConverterFactory
-dontwarn retrofit2.http.GET
-dontwarn retrofit2.http.Query