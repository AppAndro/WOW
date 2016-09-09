# WOW app
I Have Developed The Android App As Required By WOW. <br> <b>I mostly focused on showing my skills. So, I used both libraries & own implementations in this app. It Took Me Around 5-6 Hours To Build This App & Its Server Implementation(ASP.NET Backend)</b>

<br>
I Haven't used any HTTP library but i know how to use them(AsyncHttpLibrary). I Didn't used them beacause currently there are only 2 total API requests. It was not necessary to use a library just for 2 call.
<br>
<br>
I have Made A Seperate Domain :- http://wowapp.marveldeal.com to build the API

#Build APK
You Can Download APK from <a href="https://github.com/AppAndro/WOW/blob/master/app-debug.apk?raw=true">here</a>
#Note
I developed this app using my own server. I apologize for slow speed of video streaming as my server is not fast & strong enough

#Tools used
1. Android Studio (To Build The App)
2. Visual Studio (To Build The Server API)
3. I Have Used Code From My Own Apps. I have parts of the code from them & changed them to fit the needs
4. I Have Used Answers by fabric(App Analytics) & Crashlytics (Crash Logging Library)
5. I Used Digits By Fabric To Build OTP Authentication System because I didn't wanted to buy an SMS Plan.

# API Programmed On Server
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
## Upload Video API
```

POST /api/v1/upload HTTP/1.1
Host: wowapp.marveldeal.com
Content-Type: multipart/form-data; boundary=----WebKitFormBoundary7MA4YWxkTrZu0gW

------WebKitFormBoundary7MA4YWxkTrZu0gW
Content-Disposition: form-data; name="token"

A3E64C0E2673B443E51A59F99E6816E9
------WebKitFormBoundary7MA4YWxkTrZu0gW
Content-Disposition: form-data; name="title"

My Sport Video
------WebKitFormBoundary7MA4YWxkTrZu0gW
Content-Disposition: form-data; name="Desc"

It is My First Video
------WebKitFormBoundary7MA4YWxkTrZu0gW
Content-Disposition: form-data; name="tags"

vide, sport, haha
------WebKitFormBoundary7MA4YWxkTrZu0gW
Content-Disposition: form-data; name="file"; filename=""
Content-Type: 


------WebKitFormBoundary7MA4YWxkTrZu0gW--
```
Sample Response:
```
Video Recieved
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
      "title": "My Skating Video",
      "description": "Test Description",
      "tags": "skating, sport",
      "datetime": "2016-09-08T12:47:22.507",
      "videoURL": "http://wowapp.marveldeal.com/VideoUploads/4jJ6UP.avi"
    },
    {
      "title": "My Skating Video 2",
      "description": "Test Description 2",
      "tags": "skating, sport, 2",
      "datetime": "2016-09-08T12:47:22.507",
      "videoURL": "http://wowapp.marveldeal.com/VideoUploads/4jJ6UP2.avi"
    }

  ]
}
```

