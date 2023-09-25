# Spring Boot with JPA app

## Required tools
1. [Java 17](https://adoptopenjdk.net/)
2. [MySQL](https://dev.mysql.com/downloads/mysql/)

##### Features
Following functionalities are available in the application via requests:

- **GET** http request that returns a list of all tasks stored in the database.
- **GET** http request that returns a specific task by their ID.
- **POST** http request that stores a new task in the database.
- **PUT** http request that updates a specific task by their ID.
- **DELETE** http request that deletes a task in the database.

##### How to build the project?
1. Run `mvn clean install`

##### How to run from docker?
1. Run `docker compose -f docker-compose.yaml --profile app up`. This will start the application and mysql containers. The application is accessible from `http://localhost:8080`
2. If only MySQL container is required run `docker compose -f docker-compose.yaml up`

##### Applications properties
| Name                    | Value        |
|-------------------------|--------------|
| Application name        | `task-ms`    |
| Application Port        | `8443`       |
| Docker Application Port | `8080`       |
| MySQL Port              | `3306`       |