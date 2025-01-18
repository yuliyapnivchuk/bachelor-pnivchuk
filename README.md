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
curl -X POST http://127.0.0.1:8080/expense/create \
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
          "user": "user2",
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
          "user": "user2",
          "value": "1"
        },
        {
          "user": "user1",
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
          "user": "user3",
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
          "user": "user1",
          "value": 20
        },
        {
          "user": "user2",
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

#### 3. Get balance

```sh
curl -X GET "http://127.0.0.1:8080/balance?user=user1"
```