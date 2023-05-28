#!/bin/sh
if [ "$#" -ne 1 ];  then
  echo "Usage: $0  project_name" >&1
  exit 1
fi

echo "Please Login to OCP using oc login ..... "
echo "Make sure oc command is available"

oc new-project $1
oc new-app mysql-persistent -p DATABASE_SERVICE_NAME=loyaltymysql -p  MYSQL_ROOT_PASSWORD=loyalty -p MYSQL_DATABASE=loyalty -p MYSQL_USER=loyalty -p MYSQL_PASSWORD=loyalty -p MEMORY_LIMIT=512Mi -p VOLUME_CAPACITY=512Mi

echo "Press [Enter] key to setup the DB once MySQL pod started successfully ..."
read

POD_NAME=$(oc get pods -l=name=loyaltymysql -o custom-columns=POD:.metadata.name --no-headers)
echo "MySQL Pod name $POD_NAME"
oc exec $POD_NAME -- mysql -u root loyalty -e "CREATE TABLE loyalty.loyalty_account (id INT NOT NULL AUTO_INCREMENT,balance INT NULL,tier INT NULL,enabled TINYINT NULL, PRIMARY KEY (id));CREATE TABLE loyalty.loyalty_transaction (id INT NOT NULL AUTO_INCREMENT,account_id INT NULL, points INT NULL, name VARCHAR(45) NULL, date DATETIME NULL, PRIMARY KEY (id));INSERT INTO loyalty.loyalty_account (id, balance, tier, enabled) VALUES ('1', '333', '1', '1');INSERT INTO loyalty.loyalty_account (id, balance, tier, enabled) VALUES ('2', '122', '1', '1');INSERT INTO loyalty.loyalty_account (id, balance, tier, enabled) VALUES ('3', '100', '2', '1');INSERT INTO loyalty.loyalty_transaction (id, account_id, points, name, date) VALUES ('1', '1', '100', 'KFC', '2019-01-19 14:55:02');INSERT INTO loyalty.loyalty_transaction (id, account_id, points, name, date) VALUES ('2', '1', '80', 'Carrefour', '2020-01-19 14:55:02');INSERT INTO loyalty.loyalty_transaction (id, account_id, points, name, date) VALUES ('3', '1', '75', 'Car Wash', '2020-02-19 14:55:02');INSERT INTO loyalty.loyalty_transaction (id, account_id, points, name, date) VALUES ('4', '2', '110', 'PizzaHut', '2020-01-19 14:55:02');"

# deploy the applications
echo "Press [Enter] to deploy the applications"
read

oc new-app quay.io/quarkus/ubi-quarkus-native-s2i:19.3.1-java11~https://github.com/osa-ora/Loyalty-Service.git --name=quarkus-loyalty
oc expose service/quarkus-loyalty
oc set probe deployment/quarkus-loyalty --liveness --get-url=http://:8080/health/live --initial-delay-seconds=12
oc set probe deployment/quarkus-loyalty --readiness --get-url=http://:8080/health/ready --initial-delay-seconds=12

oc new-app registry.access.redhat.com/redhat-openjdk-18/openjdk18-openshift~https://github.com/osa-ora/Loyalty-Service.git --name=quarkus-loyalty-java
oc expose service/quarkus-loyalty-java
oc set probe deployment/quarkus-loyalty-java --liveness --get-url=http://:8080/health/live --initial-delay-seconds=12
oc set probe deployment/quarkus-loyalty-java --readiness --get-url=http://:8080/health/ready --initial-delay-seconds=12

oc new-app registry.access.redhat.com/redhat-openjdk-18/openjdk18-openshift~https://github.com/osa-ora/Loyalty-Service-Springboot.git --name=loyalty-service -p JAVA_IMAGE_STREAM_TAG=8
oc expose svc/loyalty-service
oc set probe deployment/loyalty-service --liveness --get-url=http://:8080/actuator/health --initial-delay-seconds=12
oc set probe deployment/loyalty-service --readiness --get-url=http://:8080/actuator/health --initial-delay-seconds=12


echo "Press [Enter] key to do some testing once the apps deployed successfully ..."
read

#do some curl commands for testing
echo "Test Springboot app"
time curl -s $(oc get route loyalty-service -o jsonpath='{.spec.host}')/loyalty/v1/balance/{1,2}?[1-100]
echo "Test Quarkus Java app"
time curl -s $(oc get route quarkus-loyalty-java -o jsonpath='{.spec.host}')/loyalty/v1/balance/{1,2}?[1-100]
echo "Test Quarkus Native app"
time curl -s $(oc get route quarkus-loyalty -o jsonpath='{.spec.host}')/loyalty/v1/balance/{1,2}?[1-100]

echo "Congratulations, we are done!"
