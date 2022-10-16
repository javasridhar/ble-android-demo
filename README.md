# ble-android-demo
Implementation of BLE Demo in Android

Project scope:-

            The project is about a demonstration of pairing bluetooth devices and getting the raw data which will be sent to server. 
            Once the server saved the raw data it will send a notification to android device.
            
            
Technical specification:-

          Min Version - Android 6
          Max Version - Android 12
          Java - 1.8
          Kotlin - 1.7.10
          Push Notification - FCM
          Local DB - Room
          Architecture - MVVM, Live Data
          Network call - Retrofit, JSON
          Async call - Coroutines
          
          
 Deployment Instructions:-
 
          * Install the app after importing the project
          * Allow all permissions
          * Once the permissions are granted, app will scan nearby bluetooth devices and it will display the devices in a list
          * Click any device then pairing/connection will be started
          * Meanwhile the raw data of selected device will be stored in Room DB
          * Raw data will be sent to server for remote storage
          * Finally app will receive notification with raw data of selected device
