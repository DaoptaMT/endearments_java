## Start the Application
- Run the following command from the project root:
  - [sh src/main/docker/run-app.sh](src/main/docker/run-app.sh)
- Run flyway migration:
  - [sh src/main/docker/run-migration.sh](src/main/docker/run-migration.sh)

- Check sql (PostgresSQL):
  - docker exec -it postgres_dev psql -U sa -d Endearments
  - \dt dbo.*
  - SELECT * FROM dbo.flyway_schema_history;

# endearments_java
