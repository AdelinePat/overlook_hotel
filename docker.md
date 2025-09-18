Pour redemarrer docker si pb de compilation :
supprimer tout même anonyme lié au container

``docker compose down -v --remove-orphans
docker system prune
docker compose up --build
docker compose stop
``
