# Social-network
SpringBoot Social-network. 
# Functionality
### Base
- Login/Logout
- Registration
- View own profile
- View other users profiles
- Follow some user or unfollow
- Make a post
- Edit or delete your post
- Add image for post
- Search posts by tag
- Leave a comment
- Edit or delete your comment
- Delete your comment
- Leave like/dislike on post
- Send private message to any user
- Edit profile(change email, password, profile image)
- If you forgot you password you can reset your password by email
- Implemented pagination
### Admin
- Edit any user's profile
- Give admin rights to any user

# Technologies used
### Stack
- Java 14
- Spring: SpringBoot, MVC, Data JPA, Security, Mail
- Maven
- Mysql
- Thymleaf
- Javascript
- Html, CSS, Bootstrap
- Flyway Migration
- Test: JUnit, Mockito
- MailTrap to get messages
### Enviroment
- Heroku

# Run
1. Download the zip file or clone the repository:
  git clone hhttps://github.com/MaximRom00/Social-network.git
2. Create Mysql database. Set username and password in src/main/resources/application.yaml
3. Run this application using maven: Run the app using maven:
```
mvn spring-boot:run
```
The app will start running at http://localhost:8081.

# Application is available on Heroku
Link: https://spring-social-network.herokuapp.com/

Credentials for Admin:
- login: Max
- passwrod: 1

Credentials for User:
- login: user
- passwrod: 2

Credentials for User:
- login: anton
- passwrod: a

# Screenshots
### Login Form
![1 Login Page](https://user-images.githubusercontent.com/95149324/160821883-71a5dbae-e716-4f44-b19a-befeed1bc49f.png)
### Registration Form
![2 Registration Form](https://user-images.githubusercontent.com/95149324/160821968-196fadcb-1aaf-4d67-94b9-b7b18f3d0d8e.png)
### Private message page  
![4 Private Message](https://user-images.githubusercontent.com/95149324/160822027-ebc36fe0-220b-494b-a2b5-560e6a5e96a5.png)
### User's profile
![6 User profile](https://user-images.githubusercontent.com/95149324/160822079-f7e6a5a3-305a-4889-bb0a-84acc55561d3.png)
### Other screenshots you can see in https://github.com/MaximRom00/Social-network/tree/main/screenshots
