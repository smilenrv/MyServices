# Customer Serivce

Customer Service is a micro service used by front end to create/update new user profile into the system.
The system stores the details of the customer in the system with below details:

1. Name (20 alphanumeric)
2. Age (2 digit)
3. Email Address
4. Address (100 alphanumeric)
5. Phone number (10 digit)

## API Details exposed by the service:

This endpoint will return all customers in system.

```
Endpoint: "/web/customer/service/customers"
Method: GET
Output: JSON sample - {"code":200,"message":"OK","data":[{"id":1,"name":"prakash","age":23,"emailAddress":"test@gmail.com","address":"chennai","phoneNumber":"1234"}]}
```

This endpoint will return specific customer detail by passing ID as input

```
Endpoint: "/web/customer/service/customers/{id}"
Method: GET
Output: 
JSON sample - {"code":200,"message":"OK","data":{"id":1,"name":"prakash","age":23,"emailAddress":"test@gmail.com","address":"chennai","phoneNumber":"1234"}}

```

This endpoint will validate customer exists in system by passing ID as input

```
Endpoint: "/web/customer/service/customers/{id}"
Method: GET
Output: 

ID exists in system:

JSON sample - {"code":200,"message":"OK","data":{"id":1,"name":"prakash","age":23,"emailAddress":"test@gmail.com","address":"chennai","phoneNumber":"1234"}}

ID do not exists in system:
JSON sample - {"code":417,"message":"EXPECTATION_FAILED","data":"Customer is not present"}

```

This endpoint will create customer in system

```
Endpoint: "/web/customer/service/customer"
Method: POST
Request: {"name":"test","age":3,"emailAddress":"test@test.com","address":"chennai 600047","phoneNumber":"1234567"}
Content-type:application/json

Response:
{"code":200,"message":"OK","data":true}
```

This endpoint will update customer exists in system by passing ID as input

```
Endpoint: "web/customer/service/customer/{id}"
Method: Post
Request:
Path variable - ID (Integer - 1-2digits)
Request body - {"name":"test","age":3,"emailAddress":"test@test.com","address":"chennai 600047","phoneNumber":"1234567"}
Content-type:application/json

Response:
If ID exists:
{"code":200,"message":"OK","data":true}
If ID not exists:
{"code":417,"message":"EXPECTATION_FAILED","data":"Customer is not present"}
```

# sonar
mvn sonar:sonar -Dsonar.host.url=http://localhost:9000