<h1> ZA Coding Institutions API</h1>

***

The ZA Coding Institutions API project uses Spring Boot to simplify development with its built-in features,
Spring Data JPA to streamline database access, and MySQL is employed for data storage and

<h2>Content:</h2>

1. **[Technologies Used](#technologies-used)**: List of the technologies and tools used in the project.
2. **[Prerequisites](#prerequisites)**: Outlines what you need to have installed to run the project and provides links
   to official installation guides.
3. **[How to Install and Run the Project](#how-to-install-and-run-the-project)**: Offers steps to clone the repository and set up Docker
   containers for running the project.
4. **[API Endpoints](#api-endpoints)**: Describes the available API endpoints, along with a link to the API
   documentation.
5. **[Testing](#testing)**: Brief outline of the project testing strategy.
6. **[Planned Features](#planned-features)**: Outlines the future development plans for the project.

***
<h2 id="technologies-used">Technologies Used</h2>

- Java JDK 17
- Apache Maven v3.9.5
- Spring Boot v3.1.4
- Spring Data JPA
- MySQL v8.0.33
- Docker v24.0.6
- Docker-Compose v2.18.1

***
<h2 id="prerequisites">Prerequisites</h2>
The ZA Coding Institutions API is designed to be deployed and managed with the assistance of Docker Compose, a tool that
simplifies the setup and execution of the APIs required services and components.
To run the API on your machine, ensure you have Docker and Docker Compose installed.

Please refer to the official Docker guides for installing Docker and Docker Compose:

- [Docker](https://docs.docker.com/engine/install/)
- [Docker Compose](https://docs.docker.com/compose/install/)

However, if you wish to build upon and modify the project, you will need all the
above-mentioned [technologies](#technologies-used).

***
<h2 id="how-to-install-and-run-the-project">How to Install and Run the Project</h2>

1. Clone the repo
   ```sh
   git clone https://github.com/baden-dev/za-coding-institutions-api.git
   ```

2. Navigate to the root directory of the project
3. Set Up Docker Containers

    Choose one of the following methods to start the Docker containers based on your preference:

   - To start the services in the foreground (and see their logs in your terminal), use:
      ```sh
      docker compose up
      ```
   - To start the services in detached mode (in the background, without terminal logs), use:
      ```sh
      docker compose up -d
      ```

      To exit a running Docker container in your terminal, you can use the following steps:
      - Press **Ctrl + C** in the terminal where the container is running.
   
     - Alternatively, if you started the container in the background (detached mode), you can use the following command to
       stop and remove the container:
       ```sh
       docker compose down
       ```
4. Once set up is completed, you can interact with the API endpoints using your preferred API testing tools, such as
   Postman, curl, or any other HTTP client of your choice.

***
<h2 id="api-endpoints">API Endpoints</h2>
For access to the API documentation, please visit
the [ZA Coding Institutions API documentation](https://documenter.getpostman.com/view/22824490/2s9YRGyUqW) to find
comprehensive information on available endpoints, request examples, and response formats.

- Base URL: [http://localhost:8080/api/v1](http://localhost:8080/api/v1)

| HTTP Method | Endpoint                       | Description                          |
|-------------|--------------------------------|--------------------------------------|
| GET         | /institutions                  | Retrieve all Institutions            |
| GET         | /institutions/{institution_id} | Retrieve Institution by ID           |
| POST        | /institutions                  | Create a Institution                 |
| PUT         | /institution/{institution_id}  | Update an existing Institution by ID |
| DELETE      | /institution/{institution_id}  | Delete an existing Institution by ID |

***
<h2 id="testing">Testing</h2>
This project includes a suite of unit tests and integration tests to validate the correctness of the implementation.
All unit tests are located in the **src/test/java/com/baden** path from the root repository.
Additionally, integration tests are also available to verify the interactions between various components of the
application.

To run all the test run:

   ```sh
   mvn test
   ```

***
<h2 id="planned-features">Planned Features</h2>
1 - **Expand Institution Database**: I plan to expand the institution database by doing more research and adding more
data and information about different institutions.

2 - **Front-End Development with React**: I will be creating a front-end interface using React to enhance the user
experience.
This will allow users to interact with the API directly in their web browsers, providing a more accessible and
user-friendly way to access and consume information, without the need for specialised API tools.

3 - **End-to-End Testing**: Following the completion of the front-end development, I will introduce end-to-end testing
to ensure the application's functionality and performance from a user's perspective.

4 - **Going Live**: One of my primary goals is to make this project live and accessible to the public, providing a
one-stop comprehensive source of information on coding and tech institutions available in South Africa.
This is with the intention of transforming it into an invaluable and easy to digest resource for students and
individuals seeking information about educational opportunities in the tech field.








