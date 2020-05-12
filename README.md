# Loyalty Service project

This Project Handle the Basic Loyalty Program Services e.g. create account, add transaction, redeem points, etc.  
The Service connect to MySQL DB and in production mode, it requires the following environment variables:  
PORT = this is the application listening port (8080)  
DB_USER = this is the DB user (loyalty)  
DB_PASSWORD = this is the DB user password (loyalty)  
DB_IP = This is the DB IP address, it point to loyaltymysql (need to map to db IP)  
DB_PORT = This is the DB listening port (3306)  

To run for development:   

```
mvn clean compile quarkus:dev  

```

Where dev is the development profile.  


This project uses Quarkus, the Supersonic Subatomic Java Framework.

If you want to learn more about Quarkus, please visit its website: https://quarkus.io/ .

## Running the application in dev mode

You can run your application in dev mode that enables live coding using:  

```
./mvnw quarkus:dev

```

## Packaging and running the application

The application can be packaged using `./mvnw package`.
It produces the `loyalty-service-1.0.0-SNAPSHOT-runner.jar` file in the `/target` directory.
Be aware that it’s not an _über-jar_ as the dependencies are copied into the `target/lib` directory.

The application is now runnable using `java -jar target/loyalty-service-1.0.0-SNAPSHOT-runner.jar`.

## Creating a native executable

You can create a native executable using: `./mvnw package -Pnative`.

Or, if you don't have GraalVM installed, you can run the native executable build in a container using: `./mvnw package -Pnative -Dquarkus.native.container-build=true`.

You can then execute your native executable with: `./target/loyalty-service-1.0.0-SNAPSHOT-runner`

If you want to learn more about building native executables, please consult https://quarkus.io/guides/building-native-image-guide.