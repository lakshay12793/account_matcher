# Identity Reconciliation - Bitespeed Backend Task

This is a Spring Boot web service that identifies and links customer identities based on email and phone numbers.

## 🚀 Deployment

🔹 API Endpoints

POST /api/identify

##Description: Identifies a customer and links related contacts.

Request Body:

{ <br>
  "email": "laksh@gmail.com", <br>
  "phoneNumber": "98989474" <br>
} <br>

Response:

{ <br>
    "contact": { <br>
        "primaryContactId": 2, <br>
        "emails": ["laksh@gmail.com"], <br>
        "phoneNumbers": ["98989474"], <br>
        "secondaryContactIds": [] <br>
    }<br>
}<br>

## 🔹 Technologies Used

* Java 17

* Spring Boot

* PostgreSQL

* Render (Hosting)

## 🔹 How to Run Locally

* Clone the repository:

  `git clone https://github.com/your-username/your-repo.git` <br>
  `cd your-repo` 

* Update application.properties with local PostgreSQL credentials.

* Run the application:

`mvn clean package` <br>
`mvn spring-boot:run`

* Test via Postman at `http://localhost:8080/api/identify`.


🔹 Author

👤 Lakshay Ahlawat <br>
📧 Email: lakshay94161@gmail.com <br>
🔗 LinkedIn: linkedin.com/in/lakshay-ahlawat <br>
🔗 GitHub: github.com/lakshay12793
