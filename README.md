# WOW test app
I Have Developed The Android App As Required By WOW

#Note
I developed this app using my own server. I apologize for slow speed of video streaming as my server is not fast & strong enough

#Tools used
1. Android Studio (To Build The App)
2. Visual Studio (To Build The Server API)
3. I Have Used Code From My Own Apps. I have parts of the code from them & changed them to fit the needs
4. I Have Used Answers by fabric(App Analytics) & Crashlytics (Crash Logging Library)
5. I Used Digits By Fabric To Build OTP Authentication System.

# API Details
## Authorization API
   ```
POST /api/v1/auth HTTP/1.1
Host: wowapp.marveldeal.com
Content-Type: application/x-www-form-urlencoded
email=wilsonmani88%40gmail.com&phone=+919718928987&token=7d89as7d89sa7d89s7a89780yilkhjkhjk
   ```
Sample Response:
```
{
   "status": "Success: Welcome Back Dear User",
   "token": "A3E64C0E2673B443E51A59F99E6816E9"
}
```
## GetUploadedVideo API
```
POST /api/v1/getVideos HTTP/1.1
Host: wowapp.marveldeal.com
Content-Type: application/x-www-form-urlencoded
token=A3E64C0E2673B443E51A59F99E6816E9
```
Sample Response:
```
{
  "videos": [
    {
      "title": "Eat My Potty",
      "description": "HAHAH",
      "tags": "potty, hahaha,a",
      "datetime": "2016-09-08T12:47:22.507",
      "videoURL": "http://wowapp.marveldeal.com/VideoUploads/4jJ6UP.avi"
    }
  ]
}
```

