# Overlook Hotel
School group project, 3 contributors.
Solution for hotel management with 3 different services : 
- Client app (rent a/multiple room(s), rent a/multiple place for an event, comment on past stay)
- Manager app (CRUD employee, manage employee schedule, view analytics, can do everything clients can in their stead)

## Tools
- Language & Framework: Java, Spring Boot
- Dependencies: Thymeleaf, DevTools, Spring Security, Validation, JPA, Lombok
- Containerization: Docker & Docker Compose

## Setup
1. Create .env file
Copy `.env.example` and fill in the necessary values:
```bash
cp .env.example .env
```
2. Build and run the project in dev mode
```bash
docker compose up --build
```
This will pull & build images and create necessary network and volumes
3. Stop containers (if needed)
Since the configuration makes the container restart unless you manually stop it, you'll have to stop it if you don't need it anymore
```bash
docker compose stop
```
4. Restart containers
```bash
docker compose start
```
## Running tests
Since the app runs inside containers, you need to execute tests from the `dev` container:
- Run all tests:
```bash
docker compose exec dev mvn test 
```
- Run a specific test class:
```bash
docker compose exec dev mvn test -Dtest=ClassName
```

## Database
- Database: MySQL
- Schema: MPD available in the `resources` folder as mpd.jpg
- Initialization: script available in `init.sql` in root
Don't forget to create a .env file with the key variable that you find in .env.example

### Access the database
1. Enter the MySQL container `mysql-db` and connect using the environment variables:
```bash
docker compose exec mysql-db sh -c 'mysql -h localhost -u "$MYSQL_USER" -p"$MYSQL_PASSWORD" "$MYSQL_DATABASE"'
```
2. Or run queries from queriesTest.sql
```bash
docker compose exec mysql-db sh -c 'mysql -h localhost -u "$MYSQL_USER" -p"$MYSQL_PASSWORD" "$MYSQL_DATABASE" < /queriesTest.sql' 
``` 
Notes:
`$MYSQL_USER, $MYSQL_PASSWORD and $MYSQL_DATABASE are set in the mysql-db container environment variables`