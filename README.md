# Misplace App

Misplace allows you to checkin objects and things around you and add a social aspect to misplacing objects.

**Created By:** Nayana Teja Chiluvuri (tejachil@vt.edu)

**Source:** https://github.com/tejachil/HokieManager/tree/master/HokieManager/

**License:** GNU General Purpose License Version 2 (LICENSE.txt)


## Purpose
Have you ever lost your keys? Forgot where you parked your car? Don't know where your wallet is? Well Misplace attempts to make all of our clumsy traits a little less clumsy. The idea behind Misplace is that we put a NFC tag on objects or things we commonly misplace or want to track and we "check them in" from time to time. Everytime an object is scanned with the smartphone, the app recognizes the tag, gets the current location and the time, and uploads it to the webserver. Users can then access all of their tracked objects and view their locations based on their checkin. 

Let's say John lost can't find his keys and he has been good about checking them in from time to time. He can query the webserver to determine the last place his keys were checked in. John is at work and he identifies that he checked in his keys an hour ago in his apartment. He can then notify his roommate who is in the apartment and his roommate is then able to navigate to the precise location that John had checked in his keys at to see if they are there.

### Current Features
The app is still in its initial stages of development so the current features of the app are limited. The following features have been implemented:
* Minimalistic authentication and user management on both the client and server side
* Tags can be checked in and a message can be stored with the tag ID on the webserver
* App has an intent filter so the activity for checking in or adding tags is automatically launched when a tag is scanned without needing to open the app
* Webserver Host and Port can be modified from within the App
* Preliminary organization and structure in the webserver for organizing the tags (HashTables with multiple keys for querying)
* Fragmented Tabs UI implemented for the various activities
* Action Bar implemented in the Activities to conform with the UI design etiquette of the latest Android API (Jellybean)

### Future Vision
I hope to add much more functionality to the app in the near future. The features that will be implemented in the next stages of the app development include the following:
* Google Maps overlay to show positions of various tags and checkins so they can be traced easier
* Filters for querying tags and displaying on the map
* Idea of "friends" for the users so thet tags can be shared between the users
* Interface between Facebook or Google authentication for the users rather than own login to make experience more social
