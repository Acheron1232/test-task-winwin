# Project Setup and Usage

This project consists of multiple services that you can run locally. Below are instructions on how to build and start the project, as well as example API requests using `curl`.

---

## Running the Project

1. **Build each service**  
   Navigate to each service directory and run the Gradle assemble command. For example:

   ```bash
   cd service-a
   ./gradlew assemble
Repeat this step for all services in the project.

Start the project with Docker Compose
Once all services are built, go to the project root and run:

    docker-compose up

This will start all services locally.

Example API Requests
1. Register
    ```bash
   curl -X POST "http://localhost:8080/api/auth/register" \
   -H "Content-Type: application/json" \
   -d '{"email": "zxc@zxc", "password": "zxczxcxzc"}'
2. Login 
    ```bash
   curl -X POST "http://localhost:8080/api/auth/login" \
   -H "Content-Type: application/json" \
   -d '{"email": "zxc@zxc", "password": "zxczxcxzc"}'
3. Process (requires Bearer token)
   ```bash
   curl -X POST "http://localhost:8080/api/process" \
   -H "Content-Type: application/json" \
   -H "Authorization: Bearer <YOUR_TOKEN>" \
   -d '{
   "text": "something"
   }'
4. Transform
   ```bash
   curl -X POST "http://localhost:8081/api/transform" \
   -H "Content-Type: text/plain" \
   -d 'asd21124{}sad'
   


Notes

Note: Replace <YOUR_TOKEN> with the JWT token received from the login endpoint.

Make sure Docker is installed and running before executing docker-compose up.

All services must be assembled before starting Docker Compose.

Update localhost and ports if your setup differs.
