# 🚀 Conquest Microservice — Run Guide (Windows)

Complete end-to-end guide to run the **Conquest Ticket Management System** on a **Windows** machine.

---

## 📋 Architecture Overview

```
┌──────────────────────────────────────────────────────────────────┐
│                        Frontend (port 8000)                      │
│                         Vanilla HTML/JS/CSS                      │
└──────────────────────┬───────────────────────────────────────────┘
                       │ REST calls
                       ▼
┌──────────────────────────────────────────────────────────────────┐
│                   API Gateway (port 9090)                         │
│               Spring Cloud Gateway + Eureka Discovery            │
└──────────┬────────────────────────────────┬──────────────────────┘
           │                                │
           ▼                                ▼
┌─────────────────────┐          ┌─────────────────────┐
│  User Service (9001)│          │ Ticket Service (9002)│
│   Spring Boot + JPA │          │  Spring Boot + JPA   │
│     MySQL DB        │          │     MySQL DB         │
└─────────────────────┘          └─────────────────────┘
           │                                │
           └────────────┬───────────────────┘
                        ▼
┌──────────────────────────────────────────────────────────────────┐
│                  Eureka Server (port 9000)                        │
│                  Service Discovery Registry                      │
└──────────────────────────────────────────────────────────────────┘
```

| Service            | Port   | Description                        |
| ------------------ | ------ | ---------------------------------- |
| Eureka Server      | `9000` | Service discovery registry         |
| User Service       | `9001` | User CRUD + roles (MySQL)          |
| Ticket Service     | `9002` | Ticket CRUD + status mgmt (MySQL)  |
| API Gateway        | `9090` | Routes requests to microservices   |
| Frontend           | `8000` | Vanilla HTML/CSS/JS dashboard      |

---

## ✅ Prerequisites

Install the following on your Windows machine **before** starting:

### 1. Java 17 (JDK)

Download and install **JDK 17** (Oracle or OpenJDK):
- **Oracle:** https://www.oracle.com/java/technologies/javase/jdk17-archive-downloads.html
- **OpenJDK (Adoptium):** https://adoptium.net/temurin/releases/?version=17

After installation, verify:
```cmd
java -version
```
Expected output: `java version "17.x.x"` or `openjdk version "17.x.x"`

> **Set `JAVA_HOME`:** Go to **System Properties → Environment Variables → System Variables**, add:
> - Variable: `JAVA_HOME`
> - Value: `C:\Program Files\Java\jdk-17` (your actual path)
>
> Also add `%JAVA_HOME%\bin` to the `Path` variable.

### 2. Apache Maven

Download from: https://maven.apache.org/download.cgi

After extracting, add the `bin` folder to your system `Path`. Verify:
```cmd
mvn -version
```

### 3. MySQL Server

Download and install **MySQL Community Server**: https://dev.mysql.com/downloads/mysql/

During installation:
- Set **root password** to: `3217023719@Ck`
- Keep the default port: `3306`

Verify MySQL is running:
```cmd
mysql -u root -p
```

### 4. Python 3 (for Frontend)

Download from: https://www.python.org/downloads/

> ⚠️ During installation, **check "Add Python to PATH"**.

Verify:
```cmd
python --version
```

### 5. Git (Optional — for cloning)

Download from: https://git-scm.com/download/win

---

## 🗄️ Step 1 — Clone the Repository

Open **Command Prompt** or **PowerShell**:

```cmd
git clone https://github.com/CHINMAYKUDALKAR/conquest_microservice.git
cd conquest_microservice
```

---

## 🗃️ Step 2 — Create MySQL Databases

Open **MySQL Command Line Client** or use the `mysql` command:

```cmd
mysql -u root -p
```

Enter password: `3217023719@Ck`

Then run:

```sql
CREATE DATABASE IF NOT EXISTS user_service_db;
CREATE DATABASE IF NOT EXISTS ticket_service_db;
SHOW DATABASES;
```

You should see both `user_service_db` and `ticket_service_db` in the list.

Type `exit` to quit MySQL.

> **Note:** The tables are auto-created by Hibernate (`spring.jpa.hibernate.ddl-auto=update`), so you only need to create the databases.

---

## ⚡ Step 3 — Start All Services (In Order)

> **IMPORTANT:** Open **5 separate Command Prompt / PowerShell windows** — one for each service. Each service must keep running in its own terminal.

### 3.1 — Start Eureka Server (Terminal 1)

```cmd
cd conquest_microservice\eureka_server_app\eureka_server_app
mvnw.cmd spring-boot:run
```

Wait until you see:
```
Started EurekaServerAppApplication in X seconds
```

✅ **Verify:** Open http://localhost:9000 in your browser. You should see the **Eureka Dashboard**.

---

### 3.2 — Start User Service (Terminal 2)

```cmd
cd conquest_microservice\user_service\user_service
mvnw.cmd spring-boot:run
```

Wait until you see:
```
Started UserMicroServiceApplication in X seconds
```

✅ **Verify:** Open http://localhost:9001/users — should return `[]` (empty array).

---

### 3.3 — Start Ticket Service (Terminal 3)

```cmd
cd conquest_microservice\ticket_service\ticket_service
mvnw.cmd spring-boot:run
```

Wait until you see:
```
Started TicketServiceApplication in X seconds
```

✅ **Verify:** Open http://localhost:9002/tickets — should return `[]`.

---

### 3.4 — Start API Gateway (Terminal 4)

```cmd
cd conquest_microservice\api_gateway\api_gateway
mvnw.cmd spring-boot:run
```

Wait until you see:
```
Started ApiGatewayApplication in X seconds
```

✅ **Verify:** Check the Eureka dashboard at http://localhost:9000 — you should see **3 services** registered:
- `USER-MICRO-SERVICE`
- `TICKET-SERVICE`
- `API_GATEWAY`

---

### 3.5 — Start Frontend (Terminal 5)

```cmd
cd conquest_microservice\frontend
python -m http.server 8000
```

✅ **Verify:** Open http://localhost:8000 — you should see the **Conquest Ticket System** dashboard.

---

## 🎉 Step 4 — You're Done!

All services are now running. Here are the URLs:

| Service          | URL                           |
| ---------------- | ----------------------------- |
| Eureka Dashboard | http://localhost:9000         |
| User Service     | http://localhost:9001/users   |
| Ticket Service   | http://localhost:9002/tickets |
| API Gateway      | http://localhost:9090         |
| Frontend UI      | http://localhost:8000         |

### Quick Test — Create a User via API

```cmd
curl -X POST http://localhost:9001/users -H "Content-Type: application/json" -d "{\"name\":\"Chinmay\",\"email\":\"chinmay@test.com\",\"phone\":\"9999999999\",\"role\":\"CUSTOMER\"}"
```

Or use **Postman** with:
- **URL:** `POST http://localhost:9001/users`
- **Body (JSON):**
```json
{
  "name": "Chinmay",
  "email": "chinmay@test.com",
  "phone": "9999999999",
  "role": "CUSTOMER"
}
```

### Quick Test — Create a Ticket via Frontend

1. Open http://localhost:8000
2. Click **"+ New Ticket"**
3. Enter an issue description and user ID
4. Click **"Create Ticket"**

---

## 🛑 Stopping All Services

Press `Ctrl+C` in each terminal window to stop that service.

---

## 🔧 Troubleshooting

### ❌ `mvnw.cmd` is not recognized
Make sure you are in the correct directory (the one containing `mvnw.cmd`). The full paths should be:
```
conquest_microservice\eureka_server_app\eureka_server_app\mvnw.cmd
conquest_microservice\user_service\user_service\mvnw.cmd
conquest_microservice\ticket_service\ticket_service\mvnw.cmd
conquest_microservice\api_gateway\api_gateway\mvnw.cmd
```

### ❌ Port already in use
Find and kill the process using the port:
```cmd
netstat -ano | findstr :9000
taskkill /PID <PID_NUMBER> /F
```

### ❌ MySQL connection refused
1. Ensure MySQL is running — check **Services** (`Win + R` → `services.msc` → find "MySQL")
2. Verify credentials: username=`root`, password=`3217023719@Ck`, port=`3306`
3. Ensure databases exist: run `SHOW DATABASES;` in MySQL CLI

### ❌ Java version error
Ensure `JAVA_HOME` points to JDK 17:
```cmd
echo %JAVA_HOME%
java -version
```

### ❌ Frontend can't reach API Gateway (CORS error)
The API Gateway has CORS configured. Make sure:
- API Gateway is running on port `9090`
- Frontend is accessed via http://localhost:8000 (not `127.0.0.1` or file:///)

### ❌ Services not showing in Eureka
Wait 30 seconds after starting each service — Eureka registration takes time. Refresh the Eureka dashboard at http://localhost:9000.

---

## 📁 Project Structure

```
conquest_microservice/
├── eureka_server_app/       → Service Discovery (port 9000)
│   └── eureka_server_app/
├── user_service/            → User CRUD API (port 9001)
│   └── user_service/
├── ticket_service/          → Ticket CRUD API (port 9002)
│   └── ticket_service/
├── api_gateway/             → API Gateway (port 9090)
│   └── api_gateway/
├── frontend/                → Web UI (port 8000)
│   ├── index.html
│   ├── style.css
│   └── app.js
├── start.sh                 → One-click startup script (macOS/Linux)
└── run.md                   → This guide
```

---

## 💡 Tips

- **First run will be slow** — Maven downloads all dependencies on first launch
- **Use `start.sh` on macOS/Linux** — it starts everything automatically with one command
- **Eureka takes ~20s to start** — always start it first and wait before launching other services
- **Hibernate auto-creates tables** — no need to run SQL scripts for table creation
