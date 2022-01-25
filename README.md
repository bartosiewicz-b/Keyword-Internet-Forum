# Keyword - Internet Forum

## Description

This is a simple internet forum application, that allows it's users to register, login, create groups, posts within them and add comments.


## Technologies

* Spring Boot 2.5.6, Java 11, Maven 3.6.3
* Angular 12.2.10.


## Documentation

[Backend Api Documentation](https://htmlpreview.github.io/?https://github.com/bartosiewicz-b/Keyword-Internet-Forum/blob/master/Keyword-Spring/target/generated-docs/index.html)


## Setup

### Frontend With Angular:

Download the "Keyword-Angular" folder. In terminal run
```
$ npm install
```
to install dependencies, and 
```
$ ng serve
```
to run on default port 4200.

### Backend with Spring:

Download the "Keyword-Spring" folder. Add to path "src/main/resources", "application.properties" file and configure your database provider like this:

```
spring.datasource.driver-class-name=<paste your driver name here>
spring.datasource.url=<paste your url here>
spring.datasource.username=<paste your username here>
spring.datasource.password=<paste your password here>
```
In the same file configure jwt variables:

```
jwt.secret=<paste your random secret here>
jwt.expiryTime=30000000
jwt.refreshActionTime=1728000000
jwt.refreshExpiryTime=2592000000
```


In terminal run

```
$ mvn clean install
```

to install the dependencies, and

```
$ mvn spring-boot:run
```

to run on default port 8080.

