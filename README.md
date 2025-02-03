# Getting Started

### Start application

1. Execute `docker build -t itstep .` to build a docker image.
2. Execute `docker run --name itstep --rm -p 0.0.0.0:5432:5432 itstep` to start container with postgres database.
3. Execute `./gradlew bootRun` to start spring application.

## WEB REQUESTS
### EXPENSE
#### 1. Analyze receipt

```sh
curl -v -X POST http://127.0.0.1:8080/receipt/analyze -F "file=@./src/test/resources/templates/test3.jpg"
```

#### 2. Create expense

```sh
curl -X POST http://127.0.0.1:8080/expense \
     -H "Content-Type: application/json" \
     -d '{
  "id": null,
  "eventId": 1,
  "payedBy": "user3",
  "summary": "Обід в ресторані",
  "items": [
    {
      "id": null,
      "description": "Американо з молоком",
      "price": 65,
      "quantity": 3,
      "totalPrice": 195,
      "splitType": "=",
      "splitDetails": [
        {
          "userName": "user2",
          "value": null
        }
      ]
    },
    {
      "id": null,
      "description": "Капучино",
      "price": 85,
      "quantity": 2,
      "totalPrice": 170,
      "splitType": "shares",
      "splitDetails": [
        {
          "userName": "user2",
          "value": "1"
        },
        {
          "userName": "user1",
          "value": "1"
        }
      ]
    },
    {
      "id": null,
      "description": "Реберця",
      "price": 210,
      "quantity": 1,
      "totalPrice": 210,
      "splitType": "%",
      "splitDetails": [
        {
          "userName": "user3",
          "value": "20"
        },
        {
          "user": "user1",
          "value": "80"
        }
      ]
    },
    {
      "id": null,
      "description": "Сік апельсиновий",
      "price": 50,
      "quantity": 0.2,
      "totalPrice": 50,
      "splitType": "manual",
      "splitDetails": [
        {
          "userName": "user1",
          "value": 20
        },
        {
          "userName": "user2",
          "value": 30
        }
      ]
    }
  ],
  "totalAmount": 700,
  "subtotalAmount": 625,
  "currency": "UAH",
  "splitType": "byItem",
  "splitDetails": null,
  "transactionDate": null,
  "transactionTime": "15:03:00",
  "category": null,
  "status": null,
  "createdBy": null
}'
```

#### 3. Update expense

```sh
curl -X PUT http://127.0.0.1:8080/ \
     -H "Content-Type: application/json" \
     -d '{
  "id": 1,
  "eventId": 1,
  "payedBy": "user3",
  "summary": "Обід в ресторані 1",
  "items": [
    {
      "id": 34,
      "description": "Американо з молоком",
      "price": 65,
      "quantity": 3,
      "totalPrice": 195,
      "splitType": "=",
      "splitDetails": [
        {
          "id": 57,
          "userName": "user1",
          "value": null
        },
        {
          "userName": "user3",
          "value": null
        }
      ]
    },
    {
      "id": 35,
      "description": "Капучино",
      "price": 85,
      "quantity": 2,
      "totalPrice": 170,
      "splitType": "shares",
      "splitDetails": [
        {
          "id": 58,
          "userName": "user2",
          "value": 2
        },
        {
          "id": 59,
          "userName": "user1",
          "value": 1
        }
      ]
    },
    {
      "id": 37,
      "description": "Сік апельсиновий",
      "price": 50,
      "quantity": 1,
      "totalPrice": 50,
      "splitType": "manual",
      "splitDetails": [
        {
          "userName": "user3",
          "value": 30
        },
        {
          "id": 63,
          "userName": "user2",
          "value": 20
        }
      ]
    }
  ],
  "totalAmount": 705,
  "subtotalAmount": 625,
  "currency": "UAH",
  "splitType": "byItem",
  "splitDetails": [],
  "transactionDate": null,
  "transactionTime": "15:03:00",
  "category": null,
  "status": "DRAFT",
  "createdBy": null
}'
```

#### 4. Get expense

```sh
http://127.0.0.1:8080/expense?expenseId=1
```

#### 5. Submit expense

```sh
curl -X POST http://127.0.0.1:8080/expense/submit \
     -H "Content-Type: application/json" \
     -d '{
  "id": 21,
  "eventId": 1,
  "payedBy": "user1",
  "summary": "квитки в кіно",
  "items": null,
  "totalAmount": 1000,
  "subtotalAmount": 950,
  "currency": "UAH",
  "splitType": "byItem",
  "splitDetails": null,
  "transactionDate": null,
  "transactionTime": "15:03:00",
  "category": null,
  "status": "DRAFT",
  "createdBy": null
}'
```

#### 3. Get balance

```sh
curl -X GET "http://127.0.0.1:8080/balance?user=user1"
```