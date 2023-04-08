# REST-API
REST API for social media application using Spring 

# How to launch an application?

1. **Setup MySQL**

###### `docker run --detach --env MYSQL_ROOT_PASSWORD=root1234 --env MYSQL_USER=janir --env MYSQL_PASSWORD=root --env MYSQL_DATABASE=rest-api --name rest-api --publish 3306:3306 mysql:8-oracle`

###### *`MYSQL_ROOT_PASSWORD`, `MYSQL_USER` and `MYSQL_PASSWORD`* can be changed to your own preference. I have used the same as in `src/main/resources/application.properties`

2. **Launch the application**

##### It will be in `src\main\java\pl\poznan\put\rest\webservice\restapi\RestApiApplication.java`.

3. **Open a browser**

##### Rest Api with JPA will be available at: `localhost:8080\jpa\users`

**Technologies used:**
* Java 17
* Rest API
* Spring Boot 3
* Spring Security
* Spring Dev Tools
* Spring Data JPA
* Spring Hibernate
* MySQL 8
* Docker
