# Card Serivce

Card Service is a micro service used by front end to create/update card into the system.
The system stores the details of the card in the system with below details:

1. Card number (16 digits)
2. Card Type (Amex/Master Card/Visa)
3. Customer Id (integer)
4. Expiration date (MM/dd/yyyy)

## API Details exposed by the service:

This endpoint will return all cards in system.

```
Endpoint: "/web/card/service/cards"
Method: GET
Output: JSON sample - {"code":200,"message":"OK","data":[{"id":1,"cardNumber":1234567890123456,"cardType":"Amex","expirationDate":"11/30/2011","customerId":1}],"hasError":false,"errorMessage":null}
```

This endpoint will return specific customer cards by passing ID as input

```
Endpoint: "/web/card/service/{customerId}/cards"
Method: GET
Output: 
JSON sample - {"code":200,"message":"OK","data":[{"id":1,"cardNumber":1234567890123456,"cardType":"Amex","expirationDate":"11/30/2011","customerId":1}],"hasError":false,"errorMessage":null}

```

This endpoint will return specific card of a customer

```
Endpoint: "/web/card/service/{customerId}/cards/{cardNumber}"
Method: GET
Output: 

Card number exists in system:

JSON sample - {"code":200,"message":"OK","data":{"id":1,"cardNumber":1234567890123456,"cardType":"Amex","expirationDate":"11/30/2011","customerId":1},"hasError":false,"errorMessage":null}

Card number do not exists in system:
JSON sample - {"code":417,"message":"EXPECTATION_FAILED","data":null,"hasError":true,"errorMessage":"Card is not present"}

```

This endpoint will create/update card in system

```
Endpoint: "/web/card/service/{customerId}/card/{cardNumber}"
Method: POST
Request: {"cardType":"AMEX","expirationDate":"11/30/2011"}
Content-type:application/json

Response:
{"code":200,"message":"OK","data":true}
If customer/card not exists:
{"code":417,"message":"EXPECTATION_FAILED","data":"Customer/Card is not present"}
```