3.3.2 

GET http://localhost:7000/api/trips/trips

HTTP/1.1 200 OK
Date: Mon, 04 Nov 2024 09:19:05 GMT
Content-Type: application/json
Content-Length: 727

[
{
"id": 3,
"startTime": [
15,
0
],
"endTime": [
17,
0
],
"startPostion": 3,
"name": "Forest Hike",
"price": 200,
"category": "FOREST",
"guide": {
"id": 1,
"firstName": "John",
"lastName": "Doe",
"email": "john.doe@example.com",
"phone": "123456789",
"yearsOfExperience": 10
}
},
{
"id": 2,
"startTime": [
9,
0
],
"endTime": [
11,
0
],
"startPostion": 1,
"name": "Trip to the Beach",
"price": 100,
"category": "BEACH",
"guide": {
"id": 1,
"firstName": "John",
"lastName": "Doe",
"email": "john.doe@example.com",
"phone": "123456789",
"yearsOfExperience": 10
}
},
{
"id": 1,
"startTime": [
12,
0
],
"endTime": [
14,
0
],
"startPostion": 2,
"name": "City Tour",
"price": 150,
"category": "CITY",
"guide": {
"id": 1,
"firstName": "John",
"lastName": "Doe",
"email": "john.doe@example.com",
"phone": "123456789",
"yearsOfExperience": 10
}
}
]

GET http://localhost:7000/api/trips/1

HTTP/1.1 200 OK
Date: Mon, 04 Nov 2024 09:25:51 GMT
Content-Type: application/json
Content-Length: 245

{
"id": 1,
"startTime": [
9,
0
],
"endTime": [
11,
0
],
"startPostion": 1,
"name": "Trip to the Beach",
"price": 100,
"category": "BEACH",
"guide": {
"id": 1,
"firstName": "John",
"lastName": "Doe",
"email": "john.doe@example.com",
"phone": "123456789",
"yearsOfExperience": 10
}
}



POST http://localhost:7000/api/trips/create

HTTP/1.1 201 Created
Date: Mon, 04 Nov 2024 09:30:15 GMT
Content-Type: application/json
Content-Length: 136

{
"id": 5,
"startTime": [
9,
0
],
"endTime": [
11,
0
],
"startPostion": 1,
"name": "TEST TRIP TO THE BEACH",
"price": 100,
"category": "BEACH",
"guide": null
}



PUT http://localhost:7000/api/trips/update/1

HTTP/1.1 200 OK
Date: Mon, 04 Nov 2024 09:34:19 GMT
Content-Type: application/json
Content-Length: 262

{
"id": 1,
"startTime": [
3,
0
],
"endTime": [
15,
0
],
"startPostion": 1,
"name": "TEST TRIP TO THE BEACH BUT UPDATED",
"price": 100,
"category": "BEACH",
"guide": {
"id": 1,
"firstName": "John",
"lastName": "Doe",
"email": "john.doe@example.com",
"phone": "123456789",
"yearsOfExperience": 10
}
}



DELETE http://localhost:7000/api/trips/delete/1

HTTP/1.1 200 OK
Date: Mon, 04 Nov 2024 09:40:29 GMT
Content-Type: text/plain
Content-Length: 45

Trip with id 1 has been successfully deleted.



PUT http://localhost:7000/api/trips/addGuideToTrip/4/1

HTTP/1.1 204 No Content
Date: Mon, 04 Nov 2024 09:54:49 GMT
Content-Type: text/plain

<Response body is empty>;

Response code: 204 (No Content); Time: 66ms (66 ms); Content length: 0 bytes (0 B)



GET http://localhost:7000/api/trips/getTripsByGuide/1

HTTP/1.1 200 OK
Date: Mon, 04 Nov 2024 09:50:05 GMT
Content-Type: application/json
Content-Length: 727

[
{
"id": 3,
"startTime": [
12,
0
],
"endTime": [
14,
0
],
"startPostion": 2,
"name": "City Tour",
"price": 150,
"category": "CITY",
"guide": {
"id": 1,
"firstName": "John",
"lastName": "Doe",
"email": "john.doe@example.com",
"phone": "123456789",
"yearsOfExperience": 10
}
},
{
"id": 2,
"startTime": [
15,
0
],
"endTime": [
17,
0
],
"startPostion": 3,
"name": "Forest Hike",
"price": 200,
"category": "FOREST",
"guide": {
"id": 1,
"firstName": "John",
"lastName": "Doe",
"email": "john.doe@example.com",
"phone": "123456789",
"yearsOfExperience": 10
}
},
{
"id": 1,
"startTime": [
9,
0
],
"endTime": [
11,
0
],
"startPostion": 1,
"name": "Trip to the Beach",
"price": 100,
"category": "BEACH",
"guide": {
"id": 1,
"firstName": "John",
"lastName": "Doe",
"email": "john.doe@example.com",
"phone": "123456789",
"yearsOfExperience": 10
}
}
]



3.3.5
Theoretical question: Why do we suggest a PUT method for adding a guide to a trip instead of a POST
method?

The PUT method is idempotent, meaning that the result of the request is the same regardless of how many times it is executed.
This is important when adding a guide to a trip, as we want to ensure that the guide is added only once.
If we used the POST method, the guide could be added multiple times, which could lead to inconsistencies in the data.
Therefore, the PUT method is more suitable for this operation as it ensures that the guide is added only once and maintains the integrity of the data.
