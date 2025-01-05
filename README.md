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
  "payedBy": null,
  "shareWith": null,
  "summary": null,
  "items": [
    {
      "id": null,
      "description": "Американо з молоком",
      "price": 65,
      "quantity": 3,
      "totalPrice": 195,
      "splitType": null,
      "divideBetween": [
        "user1",
        "user2"
      ]
    },
    {
      "id": null,
      "description": "Вареники з картоплею по духу",
      "price": 120,
      "quantity": null,
      "totalPrice": 120,
      "splitType": null,
      "divideBetween": [
        "user1"
      ]
    },
    {
      "id": null,
      "description": "Голубці",
      "price": 125,
      "quantity": 1,
      "totalPrice": 125,
      "splitType": null,
      "divideBetween": null
    },
    {
      "id": null,
      "description": "Кава американо",
      "price": 50,
      "quantity": 1,
      "totalPrice": 50,
      "splitType": null,
      "divideBetween": null
    },
    {
      "id": null,
      "description": "Капучино",
      "price": 85,
      "quantity": 2,
      "totalPrice": 170,
      "splitType": null,
      "divideBetween": null
    },
    {
      "id": null,
      "description": "Кріль в сметані",
      "price": 268,
      "quantity": 2,
      "totalPrice": 536,
      "splitType": null,
      "divideBetween": null
    },
    {
      "id": null,
      "description": "Пляцки з білими грибами",
      "price": 215,
      "quantity": null,
      "totalPrice": 215,
      "splitType": null,
      "divideBetween": null
    },
    {
      "id": null,
      "description": "Реберця",
      "price": 210,
      "quantity": 1,
      "totalPrice": 210,
      "splitType": null,
      "divideBetween": null
    },
    {
      "id": null,
      "description": "Росіл курячий",
      "price": 115,
      "quantity": 2,
      "totalPrice": 230,
      "splitType": null,
      "divideBetween": null
    },
    {
      "id": null,
      "description": "Сік апельсиновий",
      "price": 50,
      "quantity": 0.2,
      "totalPrice": 50,
      "splitType": null,
      "divideBetween": null
    },
    {
      "id": null,
      "description": "Супер тертий пляцок з мясом",
      "price": 185,
      "quantity": 1,
      "totalPrice": 185,
      "splitType": null,
      "divideBetween": null
    }
  ],
  "totalAmount": 2086,
  "subtotalAmount": 2086,
  "currency": "UAH",
  "splitType": null,
  "transactionDate": null,
  "transactionTime": "15:03:00",
  "category": null,
  "status": null,
  "createdBy": null
}'
```