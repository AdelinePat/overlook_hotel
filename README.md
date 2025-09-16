# Overlook Hotel
School group project, 3 contributors.
Create a solution for a hotel management with 3 different services : an app for the client, another for the manager and one for the employees

## Tools
The project will be developed using Java and Spring Boot with the following dependencies :
- dependency name here
- docker & docker-compose

## Setup
Don't forget to create a .env file with the key variable that you find in .env.example

To launch and build the project in dev mode :
```bash
docker compose up --build 
```
This will pull image and build image if necessary , create necessary network and volumes

if you already did this you can stop container if any problem arise, as restart: unless-stopped is in the configuration, you have to stop it manually:
```bash
docker compose stop
```

If you need to restart it:
```bash
docker compose start
```

To run some tests, since everything is running inside container, you can't do it from host and you have to do:
Run all tests:
```bash
docker compose exec dev mvn test 
```
Run only tests from the specific class (replace ClassName by the wanted test class)
```bash
docker compose exec dev mvn test -Dtest=ClassName
```

## Database
The database will be created using MySQL, the MCD can be found in the resources folder
To access the database and query directly into it, you can go inside sql container and then do the basic sql command that follows:
```bash
docker compose exec mysql-db sh -c 'mysql -h localhost -u "$MYSQL_USER" -p"$MYSQL_PASSWORD" overlook_hotel'
```
Else, you can use this command to run the query inside queriesTest.sql
```bash
docker compose exec mysql-db sh -c 'mysql -h localhost -u "$MYSQL_USER" -p"$MYSQL_PASSWORD" "$MYSQL_DATABASE" < /queriesTest.sql' 
```
$MYSQL_USER and $MYSQL_PASSWORD value are inside environment variables of mysql-db container and will directly connect to MySQL CLI inside container

[//]: # (docker compose exec mysql-db mysql -h localhost -u ${MYSQL_USER} -p overlook_hotel)
[//]: # (docker compose exec mysql-db sh -c 'mysql -h localhost -u "$MYSQL_USER" -p overlook_hotel')
