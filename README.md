#  Notes service Test

Service for managing notes     

### Prerequisites

#### [openjdk version 21](https://openjdk.org/projects/jdk/21/)

#### [MongoDB](https://www.mongodb.com/)

#### [Maven](https://maven.apache.org/)

#### [Docker](https://www.docker.com/)

#### run MongoDB 

using provided docker compose

```bash
 docker compose up mongodb
```

### Compiling, Building and Testing

On project root folder

```bash
mvn clean intall
```

It will create notes-service.jar executable jar file inside target folder

### Running 

once build

```bash
java -jar pex-test-backend
```

The application will try to read the following environment variables

- REST_SERVER_PORT   
- MONGO_HOST
- MONGO_USER
- MONGO_PASS
- MONGO_DB

if the variables are not set it will read the information from the internal .env file

default internal .env file values

<pre>
    REST_SERVER_PORT=9090
    MONGO_HOST=localhost
    MONGO_PORT=27017
    MONGO_USER=root
    MONGO_PASS=example
    MONGO_DB=admin
</pre>


### Run using provided docker compose

build note-test-server docker image

```bash
docker image build --platform=linux/amd64 -t note-test-server:v1 . 
```

run compose file

```bash
go docker compose up
```

it will expose port 9090

# How to use

- The service is in charge of managing the notes using .
 

### Create a new counter

```bash
curl -XPOST -H "Content-type: application/json" 'http://[server]:[port]/v1/counter'
```
response body

```json
{
  "value":0,
  "id":"546005a8-90e1-4969-b741-eb98518ab0ac"
}
``` 

### Increment a counter

```bash
curl -XPATCH -H "Content-type: application/json" 'http://[server]:[port]/v1/counter/[counterID]/increment'
```
response body

```json
{
  "value":1,
  "id":"546005a8-90e1-4969-b741-eb98518ab0ac"
}
``` 

### Decrement a counter

```bash
curl -XPATCH -H "Content-type: application/json" 'http://[server]:[port]/v1/counter/[counterID]/decrement'
```
response body

```json
{
  "value":-1,
  "id":"546005a8-90e1-4969-b741-eb98518ab0ac"
}
``` 
### Reset a counter

```bash
curl -XPATCH -H "Content-type: application/json" 'http://[server]:[port]/v1/counter/[counterID]/reset'
```
response body

```json
{
  "value":0,
  "id":"546005a8-90e1-4969-b741-eb98518ab0ac"
}
``` 