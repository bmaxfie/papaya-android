#Team 6 Product Backlog
####**papaya** -- Spontaneous group collaboration for universities
Members: Ben Maxfield, Christian Lock, Caleb Flynn, Adam Johnston, Scott Hanberg

###Problem Statement:
Presently, there are no apps dedicated to using college social life to aid studying. Even though there are so many students at each university, these students have no way to easily meet up with other students for study sessions. Additionally, professors have no way of knowing which students are meeting outside of class, or what they are doing if they do meet.  Our goal is to mitigate these problems by developing a cross-platform application that will access location services for users to plan impromptu study sessions. Professors will also be able to see active user logs to gauge common study session topics to develop further.

###Background Information:
####**Audience:**
Currently, it can be hard for students to meet new classmates with which to study. Papaya will be most useful to those in their non-major classes where they have little prior experience with their classmates. Professors also lack ways to tell if their students are putting in the effort required for the course and are reliant on grades to gauge this, giving them a glimpse of the study habits of their students helps them understand their students better.

####**Similar Platforms:**
With the emergence of "Find My Friends" on iPhone and the increasing popularity of other location-based social platforms, Papaya attempts to jump to the forefront as a platform to interact not only with your accepted friends, but with other classmates as well. By aiming for a college based audience, Papaya is an app dedicated to bringing the social aspect of college into a productive study-environment. This will push the edge of how we use social technology.

####**Limitations:**
Although there are many apps that promote studying, they are flawed in the sense that they do not help users locate and find others to study. By using location services, our app helps users find classmates, and locate current and future study spots at any given time. Other location-based social applications are either not effectively targeting the education market or they do not balance productivity and social activity, which our app will attempt to do by giving administration rights
to class organizers.

###Requirements:
####**Functional**
- **As a student, I would like to:**
    1. Log in to the app
    2. Create a study session
    3. Display to other students what topics will be studied at the study session and other session specific data (a session description)
    4. Show I joined a study session
    5. Set a time frame that the study session will be active
    6. Have my study sessions location displayed
    7. End study sessions when the host has left the nearby area
    8. Filter what study sessions appear on the map based on class
    9. Invite people to a study session
    10. Post to an active study session to communicate with those who are a part of it
    11. Remove my post from public view at an active study session
    12. Share files (if time allows) 
    13. View active study sessions that are near me on a map
    14. Create a study session for the future (if time allows)
    15. Filter study sessions viewable by time frame (if time allows)
    16. Transfer ownership (transfer host) of a study session
    17. Add friends to a friends list and have them appear differently on maps
    18. See which other students are at which active study sessions
- **As a professor, I would like to:**
    1. See who has been to a study session
    2. See the dates of the past study sessions
    3. View the topics discussed from past study sessions
    4. View files uploaded to a study session (stretch goal)
    5. View active study sessions on a map
    6. Post and view comments to an active study session
    7. Create professor sponsored study sessions without having to be located nearby
    8. Remove any post on any active study session
- **As a Teaching Assistant, I would like to (all if time allows):**
    1. View active study sessions on a map, including who is attending them
    2. Post and view comments to an active study session
    3. Create TA sponsored study sessions without having to be located nearby
    4. Remove any student post on any active study session

####**Non-functional**
1. We must be able to use the app on an android device.
2. We must be able to use the app on an apple device (stretch goal).
3. The app must implement location (and GPS) services.
4. The app will integrate a back-end web service using AWS, to manage all user data.
5. Interface will be simple and user friendly.
6. The app will have fast response times, with no back-end call delay above a second.
7. Our app will prevent security and privacy breaches. All data will be encrypted during transit between our service and our client (HTTPS). And all necessary client encryption keys and passwords will be stored as securely as possible on the client devices (Android Keystore System on Android devices).
8. Our app will enforce user roles (student, professor, teaching assistant) and their associated permitted actions.
9. The app will be accessible on all screen sizes and resolutions but will be built primarily for late generation Android, iPhone and large browser screens.
10. The app and back-end service should support at least 100 concurrent users during this development phase.

