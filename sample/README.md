# uuid62-sample

This is a simple Spring Boot REST API, which exists as a sample project of using the `uuid62` Spring Boot and Jackson 
integration.

Requires Java 8.

## To Build

macOS or *nix:

    ./gradlew build

Windows:    

    gradlew.bat build
    
## To Run

macOS or *nix:

    ./gradlew bootRun

Windows:    

    gradlew.bat bootRun
    
...or just run the jar file after building, like so:

    java -jar uuid62-sample-<version>.jar
    
This will start an HTTP server available at `http://localhost:8080`. A simple API is then available for performing CRUD
on UUIDs. See the code for the complete example, however the API spec is below:

## API

### Requests

#### **POST** - `/uuids`

Create a specific UUID

##### CURL

```sh
curl -X POST "http://localhost:8080/uuids" \
     --data-urlencode "id={id}"
```

##### Body Parameters

- **id** `string`: the UUID to create

#### **POST** - `/uuids/random`

Generate a random UUID

##### CURL

```sh
curl -X POST "http://localhost:8080/uuids/random"
```

#### **GET** - `/uuids`

Get all stored UUIDs

##### CURL

```sh
curl -X GET "http://localhost:8080/uuids"
```

#### **GET** - `/uuids/{id}`

Retrieve a specific UUID

##### CURL

```sh
curl -X GET "http://localhost:8080/uuids/{id}"
```

##### Path Parameters

- **id** `string`: the UUID to retrieve

#### **DELETE** - `/uuids/{id}`

Delete a specific UUID

##### CURL

```sh
curl -X DELETE "http://localhost:8080/uuids/{id}"
```

##### Path Parameters

- **id** `string`: the UUID to delete
