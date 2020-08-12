# Payment Serivce

Payment Service is a micro service used by front end to create/update payment into the system.
The system stores the details of the payment in the system with below details:

1. Card number (16 digits)
2. Card Type (Amex/Master Card/Visa)
3. Customer Id (integer)
4. Expiration date (MM/dd/yyyy)
5. Payment Amount (5 digit)
6. Status (Open/Closed/Failuer)

## API Details exposed by the service:

This endpoint will return payment of customer from system.

```
Endpoint: "/web/payment/service/{customerId}/payments"
Method: GET
Output: JSON sample - {"code":200,"message":"OK","data":[{"id":1,"amount":234.00,"status":"Closed","cardNumber":1234567890123456,"cardType":"Amex","expirationDate":"11/30/2011"}],"hasError":false,"errorMessage":null}

```

This endpoint will create/update payment in system

```
Endpoint: "/web/card/service/{customerId}/payment"
Method: POST
Request: {"cardNumber":1234567890123456,"cardType":"AMEX","expirationDate":"11/30/2011", "status": "CLOSED", "amount": 234}
Content-type:application/json

Response:
{"code":200,"message":"OK","data":true}
If customer/card not exists:
{"code":417,"message":"EXPECTATION_FAILED","data":"Customer/Card is not present"}
```