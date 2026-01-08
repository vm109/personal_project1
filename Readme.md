## Docker Compose 
- Create a docker-compose.yml file to define all dependent services
- Create a mongo init service to run a .js script which create indexes for collections on startup
- starting app with docker-compose 
  - ```shell 
    docker-compose up --build

    # with cache
    docker-comopse up --build --no-cache
    ```
    
## Connecting to Mongo
- Mongo is exposed to host on default port 27017
- You can connect to it using MongoDB Compass or any MongoDB client using the following connection string:
  - `mongodb://localhost:27017`


## APP - API
-Cron To Trigger job Listing Ingestion every 1 hr
-Add Pageable Support to fetch listings with page number and size