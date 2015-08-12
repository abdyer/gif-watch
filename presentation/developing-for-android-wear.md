# Building an Android Wear app

---

# Anatomy of an Android Wear app

- Wearable app
- Mobile app, with wearable app packaged inside
- Communicate via notifications and/or Google Play Services APIs

---

# Never Gonna GIF<br>You Up

---

# Loading a random GIF on the watch

1. UI library for displaying GIFs
1. Request random GIF from Giphy API
1. Make API request, update view

---

# Loading a random GIF on the watch

1. UI library for displaying GIFs
1. Request random GIF from Giphy API
1. ~~Make API request, update view~~

**Most wearables don't have internet...yet**

---

# Wearable app

---

# Wearable app

1. UI library for displaying GIFs
1. Request GIF from mobile app
1. Listen for data change from phone
1. Update view

---

# Request GIF from mobile app

```java
Set<Node> nodes =  Wearable.CapabilityApi
                    .getCapability(googleApiClient, "gif_me",
                          CapabilityApi.FILTER_REACHABLE)
                    .await()
                    .getCapability()
                    .getNodes();
if (!nodes.isEmpty()) {
    for (Node node : nodes) {
        Wearable.MessageApi.sendMessage(googleApiClient, node.getId(),
                "gif/random", null).await();
    }
}
```

---

# Listen for data changes

```java
@Override
public void onDataChanged(DataEventBuffer dataEvents) {
    for (DataEvent event : dataEvents) {
        if (event.getType() == DataEvent.TYPE_CHANGED) {
            DataItem item = event.getDataItem();
            if (item.getUri().getPath().compareTo("/image") == 0) {
                Asset asset =
                        DataMapItem.fromDataItem(item).getDataMap().get("random_gif");
                // Do something with asset
            }
        }
    }
}
```

---

# Mobile app

---

# Mobile app

1. WearableListenerService to listen for GIF requests
1. Request random GIF from Giphy API
1. Push file bytes to wearable via Data API

---

# Registering a WearableListenerService - AndroidManifest.xml

```xml
<service
    android:name=".GifRequestWearableListenerService"
    tools:ignore="ExportedService" >
    <intent-filter>
        <action android:name="com.google.android.gms.wearable.BIND_LISTENER" />
    </intent-filter>
</service>
```

---

# Registering a WearableListenerService: res/values/wear.xml

```xml
<resources>
    <string-array name="android_wear_capabilities">
        <item>gif_me</item>
    </string-array>
</resources>
```

---

# WearableListenerService

```java
public class GifRequestWearableListenerService
              extends WearableListenerService {
    @Override
    public void onMessageReceived(MessageEvent messageEvent) {
        if (messageEvent.getPath().equals("gif/random")) {
          bytes[] imageBytes = getRandomGif();
          Asset asset = Asset.createFromBytes(imageBytes)
          pushAsset(asset);
        }
    }

    private void pushAsset(Asset asset) {
        PutDataMapRequest request = PutDataMapRequest.create("/image");
        request.getDataMap().putAsset("random_gif", asset);
        Wearable.DataApi.putDataItem(googleApiClient, request.asPutDataRequest());
    }
}
```

---

# Achtung!

- Enable developer options and debugging on the wearable
- Packaged wearable apps aren't automatically deployed for debug builds
- Deploy the right app to the right device
- It's difficult to debug end-to-end. Focus on one end at a time.

---

# Weird stuff

- Retrolambda doesn't currently work with Android Wear. It seems to break the packaging of the wearable app into the mobile app.