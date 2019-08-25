# SwagTech [![HitCount](http://hits.dwyl.io/robin-singh-rana/SwagTech.svg)](http://hits.dwyl.io/robin-singh-rana/SwagTech)

Developed an end to end **Ecommerce web Application using Spring MVC and Spring Boot** with multiple
modules

### Functionalities:

1. User Registeration

2. **CRUD Operations** like

* User can add product to his cart
* Admin can add product to the product list
* Admin can edit the product details
* Admin can delete the product from the list

3. **Spring Security**

* User can login the site
* The entire site will change according the role. Whether the client is User or Admin
* user can logout after completing.

4. **Spring WebFlow**

* after adding products the cart the User can checkout using spring WebFlow
* Confirming User Details
* Confirming Shipping and Billing Address
* Receipt
* If the user cancel the webflow it will go to cancel Page
* If the user submits the checkout it will go to thank you page with the timing of delivery Report

### Tools and Technologies:

* **Technology** : Java, Spring MVC, Spring Boot, Hibernate, JSPs, Maven, HTML, CSS, JS, Bootstrap.
* **Application Servicer**: Apache Tomcat Server [Embedded]
* **Database** : MySQL.

### Installation:

1. Development Platform - Eclipse / IntelliJ Idea
   * [Download Eclipse](https://www.eclipse.org/downloads/packages/eclipse-ide-java-ee-developers/mars2)
   * [Download IntelliJ Idea](https://www.jetbrains.com/idea/download/#section=windows)
2. Server - Apache Tomcat Server [No need if using embedded]

   * [Download Apache Server](https://tomcat.apache.org/download-70.cgi)

3. Build Tool - Maven

   * [Download Maven](https://maven.apache.org/download.cgi)

4. Database - MySQL Database

   * [Download MySQL](https://www.mysql.com/downloads/)

5. Configuring tomcat with Eclipse (windows) - [Click Here](https://javatutorial.net/run-tomcat-from-eclipse)

6. Installation of maven in eclipse - [Click Here](https://stackoverflow.com/questions/8620127/maven-in-eclipse-step-by-step-installation)

7. Clone the repository and import it to eclipse

8. Hit run as Spring Boot Application.

### ScreenShots:

* Home Page:

![Alt text](https://github.com/robin-singh-rana/SwagTech/blob/master/src/main/resources/project/homepage.png "Home Page")

* Sign-Up/Login:

![Alt text](https://github.com/robin-singh-rana/SwagTech/blob/master/src/main/resources/project/signup_login.png)

* Profile:

![Alt text](https://github.com/robin-singh-rana/SwagTech/blob/master/src/main/resources/project/profile.png)

* Shop/GRIDWALL:

![Alt text](https://github.com/robin-singh-rana/SwagTech/blob/master/src/main/resources/project/shop.png)

* PDP:

![Alt text](https://github.com/robin-singh-rana/SwagTech/blob/master/src/main/resources/project/pdp.png)

* Cart:

![Alt text](https://github.com/robin-singh-rana/SwagTech/blob/master/src/main/resources/project/cart.png)

* Checkout-1:

![Alt text](https://github.com/robin-singh-rana/SwagTech/blob/master/src/main/resources/project/checkout.png)

* Checkout-2:

![Alt text](https://github.com/robin-singh-rana/SwagTech/blob/master/src/main/resources/project/checkout_2.png)

* Order Confirmation:

![Alt text](https://github.com/robin-singh-rana/SwagTech/blob/master/src/main/resources/project/order_confirmation.png)

* Order History:

![Alt text](https://github.com/robin-singh-rana/SwagTech/blob/master/src/main/resources/project/order_history.png)

#### Somethings wrong!!

If you find that something's wrong with this package, you can let me know by raising an issue on the GitHub issue tracker

#### Contribution

Contributors are most welcome.

# Running the project with MySQL

This project uses an in-memory database so that you don't have to install a database in order to run it. However, converting it to run with another relational database such as MySQL or PostgreSQL is very easy. Since the project uses Spring Data and the Repository pattern, it's even fairly easy to back the same service with MongoDB. 

Here is what you would do to back the services with MySQL, for example: 

### In pom.xml add: 

```
        <dependency>
            <groupId>mysql</groupId>
            <artifactId>mysql-connector-java</artifactId>
        </dependency>
```

### Append this to the end of application.yml: 

```
---
spring:
  profiles: mysql

  datasource:
    driverClassName: com.mysql.jdbc.Driver
    url: jdbc:mysql://<your_mysql_host_or_ip>/bootexample
    username: <your_mysql_username>
    password: <your_mysql_password>

  jpa:
    hibernate:
      dialect: org.hibernate.dialect.MySQLInnoDBDialect
      ddl-auto: update # todo: in non-dev environments, comment this out:


hotel.service:
  name: 'test profile:'
```

### Then run is using the 'mysql' profile:

```
        java -jar -Dspring.profiles.active=mysql target/swagtech-0.5.0.war
or
        mvn spring-boot:run -Drun.jvmArguments="-Dspring.profiles.active=mysql"
```

# Attaching to the app remotely from your IDE

Run the service with these command line options:

```
mvn spring-boot:run -Drun.jvmArguments="-Xdebug -Xrunjdwp:transport=dt_socket,server=y,suspend=y,address=5005"
```
and then you can connect to it remotely using your IDE. For example, from IntelliJ You have to add remote debug configuration: Edit configuration -> Remote.

# Questions and Comments: robinsinghra@gmail.com

# Thankyou for your time!
