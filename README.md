# Overlook Hotel
School group project, 3 contributors.
Create a solution for a hotel management with 3 different services : an app for the client, another for the manager and one for the employees

## Tools
The project will be developed using Java and Spring Boot with the following dependencies :
- dependency name here
- docker & docker-compose

To launch and build the project in dev mode :
```bash
docker compose up --build # will pull image and build image if necessary , create necessary network and volumes

# if you already did this you can stop container if any problem arise, as restart: unless-stopped is in the configuration, you have to stop it manually:
docker compose stop

# and if you need to restart it:
docker compose start
```

To run some tests, since everything is running inside container, you can't do it from host and you have to do:
```bash
docker compose exec dev mvn test # will run all tests
docker compose exec dev mvn test -Dtest=ClassName # will run only tests from the specific class you aksed
```

## Database
The database will be created using MySQL, the MCD can be found in the resources folder
To access the database and query directly into it, you can go inside sql container and then do the basic sql command that follows:
```bash
docker compose exec mysql-db mysql -h localhost -u ${MYSQL_USER} -p overlook_hotel
# ${MYSQL_USER} value is inside your .env and after doing this command, will ask for your password ${MYSQL_PASSWORD}
```
