#Team 6 Project Charter
####**papaya** -- Spontaneous group collaboration app for universities
Members: Ben Maxfield, Christain Lock, Adam Johnston, Caleb Flynn, Scott Hanberg

###Problem Statement:
Presently, there are no apps dedicated to using college social life to aid studying. Even though there are so many students at each university, these students have no way to easily meet up with other students for study sessions. Additionally, professors have no way of knowing which students are meeting outside of class, or what they are doing if they do meet.  Our goal is to mitigate these problems by developing a cross-platform application that will access location services for users to plan impromptu study sessions. Professors will also be able to see active user logs to gauge common study session topics to develop further.

###Project Objectives:
1. Create an interface to create study sessions and view sessions created by others in an organization.
2. Each study session will have a messaging and file sharing thread along with a session description.
3. A second interface will show all history of activity within an organization with statistics on each member.
4. The interfaces will be developed cross-platform, for browser, iphone, and android.
5. Authentication system will manage organizations and their members and will allow organizers to change permissions of members within their organizations.
6. A backend system will accept API calls from authenticated users devices/apps and will store and distribute user and organization data independent of the client platform.

###Stakeholders:
Users: Professors of university classes and the students of those classes. May also be marketed to other organizations that want to manage spontaneous small group collaboration.
Developers: Adam Johnson, Caleb Flynn, Scott Hanberg, Christian Lock, Ben Maxfield
Project Manager: Ben Maxfield
Project Coordinator: Anoop Ranganathan Santhosh

###Deliverables:
- A web-based mobile application interface for both iOS and Android.
- Backend architecture (AWS) that manages all app data for storage and distribution: AWS Lambda, API Gateway, Cognito, DynamoDB, S3, RDS, and EC2
- A browser interface for professors or organizers to monitor and administrate member activity.
