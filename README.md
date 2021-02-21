# FLP-Current-Location:globe_with_meridians:
The demo app shows the implementation of the latest API - [`getCurrentLocation()`][getCuurentLocationAPIUrl] - of FusedLocationProvider to get the current location. This is the recommended way when you want to get a single location.

## Background

Getting current location of the user is one of the most useful features when you want to give location-based experiences in the app. Many of the times, we just want to get the latest location of the phone and do the action using that location i.e. Search nearby places, restaurants, save address, etc.  

To get the current location, we have the Google's [FusedLocationProvider][FLPTrainingUrl] APIs. 
The **FLP (FusedLocationProvider)** provides two different APIs to get the location: 
1. Using `getLastLocation()`
2. Using `requestLocationUpdates()`

To get the single location, some people use `getLastLocation()` API. But do you know it gives the cached location instead of giving the exact location at the time we requested? So, this API doesn't sound much useful when you want to trigger actions using the latest location.

The only other option we are left with is `requestLocationUpdates()` API. Although this API gives you the latest location, it comes with a drawback. The `requestLocationUpdates()` API meant for getting the stream of locations. Which means it is useful when you want to continously get the location updates. Ofcourse this is useful when you have use cases like location tracking. But for the purpose of getting only one location, this API is not suitable. 

We have been using some hacks with this API to get the single location. For example, we stop the location updates as soon as we get the first location callback. Or we use [`setNumUpdates()`][setNumUpdatesUrl] in the `LocationRequest` to only get one location. When using `setNumUpdates()`, you need to set a timeout, if you miss to do that, you might end up consuming the location services indefinitely when a location cannot be computed due to unfortunate reasons.

## [`getCurrentLocation()`][getCuurentLocationAPIUrl] API :new:

To provide a more developer-friendly experience to get a single location, FLP (`com.google.android.gms:play-services-location:17.1.0`) has introduced getCurrentLocation() API. This API gives you the straightforward implementation to get the latest-single location. Just call FLP.getCurrentLocation() and register the success and failure listeners. This API automatically releases the location services as soon as you get the location. Also it takes care of setting a default timeout so that the location services are not consumed indefinitely. 

To know how to use it in action, checkout the code!

## Bonus :eight_pointed_black_star:

The code uses the latest recommended [Activity Result APIs][ActivityResultAPIsUrl] to request location permissions! :cool:

[getCuurentLocationAPIUrl]: https://developers.google.com/android/reference/com/google/android/gms/location/FusedLocationProviderClient#getCurrentLocation(int,%20com.google.android.gms.tasks.CancellationToken)
[FLPTrainingUrl]:https://developer.android.com/training/location/request-updates
[setNumUpdatesUrl]:https://developers.google.com/android/reference/com/google/android/gms/location/LocationRequest#public-locationrequest-setnumupdates-int-numupdates
[ActivityResultAPIsUrl]:https://developer.android.com/training/basics/intents/result
