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

## Deploy on OpenShift - MySQL
Run the following commands:
```
oc new-project dev
oc new-app mysql-persistent -p DATABASE_SERVICE_NAME=loyaltymysql -p  MYSQL_ROOT_PASSWORD=loyalty -p MYSQL_DATABASE=loyalty -p MYSQL_USER=loyalty -p MYSQL_PASSWORD=loyalty -p MEMORY_LIMIT=512Mi -p VOLUME_CAPACITY=512Mi
```
Once the pod is up and running, run the following commands:
```
POD_NAME=$(oc get pods -l=name=loyaltymysql -o custom-columns=POD:.metadata.name --no-headers)

oc exec $POD_NAME -- mysql -u root loyalty -e "CREATE TABLE loyalty.loyalty_account (id INT NOT NULL AUTO_INCREMENT,balance INT NULL,tier INT NULL,enabled TINYINT NULL, PRIMARY KEY (id));CREATE TABLE loyalty.loyalty_transaction (id INT NOT NULL AUTO_INCREMENT,account_id INT NULL, points INT NULL, name VARCHAR(45) NULL, date DATETIME NULL, PRIMARY KEY (id));INSERT INTO loyalty.loyalty_account (id, balance, tier, enabled) VALUES ('1', '333', '1', '1');INSERT INTO loyalty.loyalty_account (id, balance, tier, enabled) VALUES ('2', '122', '1', '1');INSERT INTO loyalty.loyalty_account (id, balance, tier, enabled) VALUES ('3', '100', '2', '1');INSERT INTO loyalty.loyalty_transaction (id, account_id, points, name, date) VALUES ('1', '1', '100', 'KFC', '2019-01-19 14:55:02');INSERT INTO loyalty.loyalty_transaction (id, account_id, points, name, date) VALUES ('2', '1', '80', 'Carrefour', '2020-01-19 14:55:02');INSERT INTO loyalty.loyalty_transaction (id, account_id, points, name, date) VALUES ('3', '1', '75', 'Car Wash', '2020-02-19 14:55:02');INSERT INTO loyalty.loyalty_transaction (id, account_id, points, name, date) VALUES ('4', '2', '110', 'PizzaHut', '2020-01-19 14:55:02');"
```

## Deploy on OpenShift (Java)
Run the following script after instantiate the MySQL DB
```
oc new-app registry.access.redhat.com/redhat-openjdk-18/openjdk18-openshift~https://github.com/osa-ora/Loyalty-Service.git --name=quarkus-loyalty-java
oc expose service/quarkus-loyalty-java
```
To test it:
```
curl -s $(oc get route quarkus-loyalty-java -o jsonpath='{.spec.host}')/loyalty/v1/balance/1
```

## Deploy on OpenShift (Native)
Run the following script after instantiate the MySQL DB
```
oc new-app quay.io/quarkus/ubi-quarkus-native-s2i:19.3.1-java11~https://github.com/osa-ora/Loyalty-Service.git --name=quarkus-loyalty
oc expose service/quarkus-loyalty
```
To test it:
```
curl -s $(oc get route quarkus-loyalty -o jsonpath='{.spec.host}')/loyalty/v1/balance/1
```

## Deploy SpingBoot, Quarkus Java and Quarkus Native on OpenShift
The following script will instantiate the project, DB, deploy all flavours and test them.
```
curl https://raw.githubusercontent.com/osa-ora/Loyalty-Service/master/script/quarkus-test.sh > quarkus-test.sh
chmod +x quarkus-test.sh
./quarkus-test.sh dev
```

---
---

# Network Policy Demo


```
oc new-project db-project
oc new-app mysql-persistent -p DATABASE_SERVICE_NAME=loyaltymysql -p  MYSQL_ROOT_PASSWORD=loyalty -p MYSQL_DATABASE=loyalty -p MYSQL_USER=loyalty -p MYSQL_PASSWORD=loyalty -p MEMORY_LIMIT=512Mi -p VOLUME_CAPACITY=512Mi -n db-project

oc patch dc loyaltymysql -n db-project --type='json' -p='[
  {
    "op": "add",
    "path": "/spec/template/metadata/labels/app",
    "value": "loyaltymysql"
  }
]'

//once the POD up and running ...
POD_NAME=$(oc get pods -l=name=loyaltymysql -o custom-columns=POD:.metadata.name --no-headers)
oc exec $POD_NAME -- mysql -u root loyalty -e "CREATE TABLE loyalty.loyalty_account (id INT NOT NULL AUTO_INCREMENT,balance INT NULL,tier INT NULL,enabled TINYINT NULL, PRIMARY KEY (id));CREATE TABLE loyalty.loyalty_transaction (id INT NOT NULL AUTO_INCREMENT,account_id INT NULL, points INT NULL, name VARCHAR(45) NULL, date DATETIME NULL, PRIMARY KEY (id));INSERT INTO loyalty.loyalty_account (id, balance, tier, enabled) VALUES ('1', '333', '1', '1');INSERT INTO loyalty.loyalty_account (id, balance, tier, enabled) VALUES ('2', '122', '1', '1');INSERT INTO loyalty.loyalty_account (id, balance, tier, enabled) VALUES ('3', '100', '2', '1');INSERT INTO loyalty.loyalty_transaction (id, account_id, points, name, date) VALUES ('1', '1', '100', 'KFC', '2019-01-19 14:55:02');INSERT INTO loyalty.loyalty_transaction (id, account_id, points, name, date) VALUES ('2', '1', '80', 'Carrefour', '2020-01-19 14:55:02');INSERT INTO loyalty.loyalty_transaction (id, account_id, points, name, date) VALUES ('3', '1', '75', 'Car Wash', '2020-02-19 14:55:02');INSERT INTO loyalty.loyalty_transaction (id, account_id, points, name, date) VALUES ('4', '2', '110', 'PizzaHut', '2020-01-19 14:55:02');"

//
oc new-project app-project
oc new-app registry.access.redhat.com/redhat-openjdk-18/openjdk18-openshift~https://github.com/osa-ora/Loyalty-Service.git --name=quarkus-loyalty-java -n app-project
oc set env deployment/quarkus-loyalty-java DB_IP=loyaltymysql.db-project.svc.cluster.local
oc expose service/quarkus-loyalty-java -n app-project

oc patch deployment quarkus-loyalty-java -n app-project --type='json' -p='[
  {
    "op": "add",
    "path": "/spec/template/metadata/labels/deployment",
    "value": "quarkus-loyalty-java"
  },
  {
    "op": "add",
    "path": "/spec/template/metadata/labels/app",
    "value": "quarkus-loyalty-java"
  }
]'
oc label namespace app-project name=app-project

//without any network policies ...
curl -s $(oc get route quarkus-loyalty-java -n app-project -o jsonpath='{.spec.host}')/loyalty/v1/balance/1

//with deny all ..
oc apply -f https://raw.githubusercontent.com/osa-ora/Loyalty-Service/refs/heads/master/np/deny-all.yaml -n db-project
oc rollout restart deployment quarkus-loyalty-java -n app-project

curl -s $(oc get route quarkus-loyalty-java -n app-project -o jsonpath='{.spec.host}')/loyalty/v1/balance/1

//with permit app to db only ..
oc apply -f https://raw.githubusercontent.com/osa-ora/Loyalty-Service/refs/heads/master/np/policy.yaml -n db-project
oc rollout restart deployment quarkus-loyalty-java -n app-project

curl -s $(oc get route quarkus-loyalty-java -n app-project -o jsonpath='{.spec.host}')/loyalty/v1/balance/1


```


