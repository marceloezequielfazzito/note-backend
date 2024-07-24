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

- The service is in charge of managing notes using mongodb as persistence layer.
 

### GET notes title and date created

```
GET http://[server]:[port]/api/v1/notes?tags=[BUSINESS,PERSONAL,IMPORTANT]  - optional query params tags

example 

http://localhost:9090/api/v1/notes?tags=BUSINESS,PERSONAL
```
response 200 ok

```json
[
  {
    "id": "66a0389e5b899534ecef66a9",
    "title": "new note",
    "dateCreated": "2024-07-23T23:11:26.379"
  },
  {
    "id": "66a037f04c2fec5300cba6e9",
    "title": "new note 2",
    "dateCreated": "2024-07-23T23:08:32.306"
  }
]
``` 

### GET note Text

```
GET http://[server]:[port]/api/v1/notes/[id]/text

example 

http://localhost:9090/api/v1/notes/66a0389e5b899534ecef66a9/text

```
response 200 OK

```json

{
  "text": "this is the note text note"
}

``` 

response 400 note not found

```json

{
  "timestamp": "2024-07-24T21:03:14.150+00:00",
  "status": 404,
  "error": "Not Found",
  "message": "note id:  66a0389e5b899534ecef66a1 not found",
  "path": "/api/v1/notes/66a0389e5b899534ecef66a1/text"
}

``` 

### GET note Text stats

```
GET http://[server]:[port]/api/v1/notes/[id]/stats

example 

http://localhost:9090/api/v1/notes/66a0389e5b899534ecef66a9/stats

```
response 200 OK

```json

{
  "note": 2,
  "the": 1,
  "this": 1,
  "is": 1,
  "text": 1
}

``` 

response 400 note not found

```json

{
  "timestamp": "2024-07-24T21:03:14.150+00:00",
  "status": 404,
  "error": "Not Found",
  "message": "note id:  66a0389e5b899534ecef66a1 not found",
  "path": "/api/v1/notes/66a0389e5b899534ecef66a1/text"
}

``` 


### POST create note

```
POST http://[server]:[port]/api/v1/notes

example 

http://localhost:9090/api/v1/notes

```
request body 

```json

{
  "title":"new note",                    mandatory
  "text": "this is the note text note",  mandatory
  "tags":["BUSINESS,PERSONAL,IMPORTANT"] optional

}


```
response 200 OK

```json

{
  "id": "66a16d61db8f7f5152e7ece6",
  "title": "new note",
  "text": "this is the note text note",
  "tags": [
    "BUSINESS"
  ],
  "createdDate": "2024-07-24T21:08:49.770843069"
}

```

### PUT update note

```
PUT http://[server]:[port]/api/v1/notes

example 

http://localhost:9090/api/v1/notes

```
request body

```json

{
  "id":"66a037303b4a203552c5fc12b", mandatory     
  "title":"new note",               mandatory
  "text": "this is the note text",  mandatory
  "tags":["IMPORTANT"]              optional

}


```
response 200 OK

```json

{
  "id": "66a037303b4a203552c5fc12b",
  "title": "new note",
  "text": "this is the note text",
  "tags": [
    "IMPORTANT"
  ],
  "createdDate": "2024-07-24T21:08:49.77"
}

```

response 400 note not found

```json

{
  "timestamp": "2024-07-24T21:03:14.150+00:00",
  "status": 404,
  "error": "Not Found",
  "message": "note id:  66a037303b4a203552c5fc12b not found",
  "path": "/api/v1/notes/66a0389e5b899534ecef66a1/text"
}

``` 

### Delete remove note

```
DELETE http://[server]:[port]/api/v1/[id]

example 

http://localhost:9090/api/v1/669fe14c719286786352fb1e

```
response 200 OK

empty body

response 400 note not found

```json

{
  "timestamp": "2024-07-24T21:03:14.150+00:00",
  "status": 404,
  "error": "Not Found",
  "message": "note id:  669fe14c719286786352fb1e not found",
  "path": "/api/v1/notes/66a0389e5b899534ecef66a1/text"
}

``` 