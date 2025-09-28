 # Overlook Hotel 🏨
**School group project** 📌, 3 contributors. 
Solution for hotel management with 3 different services : 
- 👤 **Client app** (rent a/multiple room(s), rent a/multiple place for an event, comment on past stay)
- 🧑‍💼 **Manager app** (CRUD employee, manage employee schedule, view analytics, can do everything clients can in their stead)
- 🔒 **Authentication & Security** : Account and session management with Spring Security.

___  

🚀 Features  

- 🛎️ Reservation management: rooms + event halls.  
- 🛠️ Manager interface: full employee management and supervision.  
- 📊 Statistics & Analytics.  
- 📝 Customer reviews after stay.  
- 🔐 Security with Spring Security (login/logout, roles).  
- 🐳 Docker containerization with MySQL & Spring Boot application.  
  
___  

🛠️ Technologies  
  
- ☕ Java + Spring Boot  
- 🎨 Thymeleaf (HTML templates)  
- 🔄 Spring Data JPA + Hibernate  
- 🔑 Spring Security  
- ⚙️ Lombok  
- 🐳 Docker & Docker Compose  

---  
📂 ***Project Structure***  
  
````
📂 overlook_hotel  
 ┣ 📂 src/main/java/overlook_hotel  
 ┃ ┣ 📂 config          # Configuration Spring (sécurité, beans, etc.)    
 ┃ ┣ 📂 controller      # Controllers Spring MVC  
 ┃ ┣ 📂 model           # Entities + DTO  
 ┃ ┣ 📂 repository      # JPA Repositories  
 ┃ ┣ 📂 service         # Business logic  
 ┃ ┣ 📂 specification   # Critères dynamiques pour les requêtes JPA  
 ┃ ┣ 📂 util            # Fonctions utilitaires  
 ┣ 📂 resources  
 ┃ ┣ 📂 templates       # Thymeleaf templates (login, réservation, etc.)  
 ┃ ┣ 📂 static  
 ┃ ┃ ┣ 📂 css           # Styles CSS  
 ┃ ┃ ┣ 📂 img           # Images (logo, background, icônes…)  
 ┃ ┣ 📄 mpd.jpg         # Schéma de la base de données  
 ┣ 📄 docker-compose.yml  
 ┣ 📄 init.sql  
 ┣ 📄 README.md  

  
````  
    
___   
  
 # Installation ⚡


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
```bash
docker compose stop
```
The containers are set to restart automatically; stop them manually if not needed

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
- Run a specific test:
```bash
docker compose exec dev mvn test -Dtest=ClassName#methodName
```

## Database
- Database: MySQL
- Schema: MPD available in the `resources` folder as mpd.jpg
- Initialization: script available in `init.sql` in root

### Access the database
1. Enter the MySQL container `mysql-db` and connect using the environment variables:
```bash
docker compose exec mysql-db sh -c 'mysql -h localhost -u "$MYSQL_USER" -p"$MYSQL_PASSWORD" "$MYSQL_DATABASE"'
```
2. Or run queries from queriesTest.sql
```bash
docker compose exec mysql-db sh -c 'mysql -h localhost -u "$MYSQL_USER" -p"$MYSQL_PASSWORD" "$MYSQL_DATABASE" < /queriesTest.sql' 
``` 

> Notes:
`$MYSQL_USER`, `$MYSQL_PASSWORD` and `$MYSQL_DATABASE` are set in the mysql-db container environment variables


---  