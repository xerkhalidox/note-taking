# Note taking
A simple RESTful app to store and retrieve notes

# Tech stack:
* Maven.
* Java 11.
* Docker
* PgAdmin(optiona) can be used to access the database and query the data in interactive way.
  
The project is a parent maven project with 2 modules Server and Client. The server has the apis and the storing logic and the client is communicating with the server using WebTestClient.

# How to run
* Make sure the software mentioned in tech stack section is installed.
* To setup the database and configure it run
  `docker run --name noteTakingDB -e POSTGRES_USER=user -e POSTGRES_PASSWORD=password -p 5436:5432 -d postgres:14.2-bullseye`
  you can replace user and password and set another port but you need to change the values to reflect in `./server/src/main/resources/application.properties`.
* Run `docker exec -it {db container id or name} /bin/sh` then run `psql -U {POSTGRES_USER used in running the container}` and run `create database notetaking;` to create the database.
* Run `mvn package` in server module to produce the jar file then run the app.
* Run `NoteTakingServerTest` to test the server and store some notes.

# What can be improved?
* Dockerize the process of running the server and database.
* Documenting the APIs using swagger.
* Adding more unit tests and integration tests.
* Implementing spring security and tokenize the APIs using by using JWT tokens for example.
* Implementing rate limiting.
* Implementing logs.




