# APIForge 🚀

APIForge is a powerful Spring Boot application for rapid API prototyping and mock data generation. It enables developers to quickly design, create, and test API endpoints with dynamic mock data without building a complete backend infrastructure.

## 🎯 What is APIForge?

APIForge is a comprehensive platform that allows you to:
- **Create and manage API endpoints** with custom schemas
- **Generate realistic mock data** using JavaFaker
- **Define entity schemas** with various field types
- **Log and monitor API calls** for analysis
- **Manage projects** and organize your API designs
- **Serve mock APIs** for frontend development and testing

## 🌟 How APIForge Helps You

### For Frontend Developers
- Test your frontend applications without waiting for backend completion
- Get realistic mock data that matches your expected API responses
- Quickly iterate on UI/UX with dynamic data

### For Backend Developers
- Prototype API designs before full implementation
- Share API specifications with team members
- Test different data scenarios and edge cases

### For Teams
- Collaborate on API design and data models
- Maintain consistent mock data across development environments
- Document and version your API schemas

## 🛠️ Features

- **Project Management**: Organize your APIs into projects
- **Schema Definition**: Create custom entity schemas with various field types
- **Dynamic Data Generation**: Generate realistic mock data using JavaFaker
- **API Endpoint Creation**: Define RESTful endpoints with custom responses
- **Mock API Server**: Serve your mock APIs instantly
- **API Call Logging**: Track and monitor API usage
- **User Management**: Handle user authentication and authorization
- **OpenAPI Documentation**: Auto-generated API documentation with Swagger UI

## 📋 Requirements

- **Java**: 21 or higher
- **Maven**: 3.8+ 
- **Database**: MySQL (configurable)

## 🚀 Setup & Installation

### 1. Clone the Repository
```bash
git clone https://github.com/AhmedAbdelshakour1/api-forge.git
cd apiforge
```

### 2. Environment Configuration
**⚠️ Important**: Create a `.env` file in the project root directory or configure `application.properties` with your settings.

#### Example `.env` file:
```env
# Database Configuration
DB_HOST=localhost
DB_PORT=3306
DB_NAME=apiforge_db
DB_USERNAME=your_username
DB_PASSWORD=your_password


# Server Configuration
SERVER_PORT=8080
```

#### Alternative: Update `application.properties`
```properties
# Database
spring.datasource.url=jdbc:mysql://localhost:3306/apiforge_db
spring.datasource.username=your_username
spring.datasource.password=your_password
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true

# Server
server.port=8080
```

### 3. Database Setup
```bash
# Create MySQL database
mysql -u root -p
CREATE DATABASE apiforge_db;
```

### 4. Build the Project
```bash
./mvnw clean install
```

### 5. Run the Application
```bash
./mvnw spring-boot:run
```

## 🎮 Usage

### Access Points
- **Main Application**: `http://localhost:8080`
- **API Documentation**: `http://localhost:8080/swagger-ui.html`
- **API Base URL**: `http://localhost:8080/api`


## 🔧 Building for Production

```bash
./mvnw clean package -Pprod
java -jar target/apiforge-0.0.1-SNAPSHOT.jar
```

## 🎉 Getting Started Quickly

1. Clone the repo
2. Add your `.env` file with database credentials
3. Run `./mvnw spring-boot:run`
4. Visit `http://localhost:8080/swagger-ui.html`
5. Start creating projects and generating mock data!

## 🔄 Complete Workflow: From User to Dynamic API

This section provides a comprehensive step-by-step workflow showing how to create a user and build dynamic APIs from scratch.

### Step 1: Create a User 👤

First, create a user who will own the projects and schemas.

**Request:**
```bash
curl -X POST http://localhost:8080/api/users \
  -H "Content-Type: application/json" \
  -d '{
    "username": "john_doe",
    "email": "john@example.com",
    "password": "securePassword123"
  }'
```

**Response:**
```json
{
  "id": 1,
  "username": "john_doe",
  "email": "john@example.com"
}
```

### Step 2: Create a Project 📁

Create a project to organize your API schemas and endpoints.

**Request:**
```bash
curl -X POST http://localhost:8080/api/projects \
  -H "Content-Type: application/json" \
  -d '{
    "name": "E-commerce API",
    "description": "Product catalog and user management API",
    "isPublic": false,
    "userId": 1
  }'
```

**Response:**
```json
{
  "id": 1,
  "name": "E-commerce API",
  "description": "Product catalog and user management API",
  "userId": 1,
  "createdAt": "2025-09-22T10:00:00",
  "updatedAt": "2025-09-22T10:00:00",
  "public": false
}
```

### Step 3: Create Entity Schema 📋

Define the structure of your data with entity schemas.

**Request:**
```bash
curl -X POST http://localhost:8080/api/schemas \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Product",
    "description": "Product entity for e-commerce",
    "projectId": 1
  }'
```

**Response:**
```json
{
  "id": 1,
  "name": "Product",
  "description": "Product entity for e-commerce",
  "projectId": 1,
  "createdAt": "2025-09-22T10:05:00"
}
```

### Step 4: Add Schema Fields 🏗️

Define the fields for your entity schema with various data types.

**Add Product Name Field:**
```bash
curl -X POST http://localhost:8080/api/fields \
  -H "Content-Type: application/json" \
  -d '{
    "fieldName": "name",
    "fieldType": "string",
    "isRequired": true,
    "array" : false,
    "minLength" : 3,
    "maxLength" : 5,
    "schemaId": 1
  }'
```

**Add Product Price Field:**
```bash
curl -X POST http://localhost:8080/api/fields \
  -H "Content-Type: application/json" \
  -d '{
    "fieldName": "price",
    "fieldType": "number",
    "isRequired": true,
    "minValue" : 1,
    "maxValue" : 2000,
    "schemaId": 1
  }'
```

**Add Product Description Field:**
```bash
curl -X POST http://localhost:8080/api/fields \
  -H "Content-Type: application/json" \
  -d '{
    "fieldName": "description",
    "fieldType": "string",
    "minLength" : 3,
    "maxLength" : 500,
    "isRequired": false,
    "schemaId": 1
  }'
```

**Add Stock Quantity Field:**
```bash
curl -X POST http://localhost:8080/api/fields \
  -H "Content-Type: application/json" \
  -d '{
    "fieldName": "stock_quantity",
    "fieldType": "number",
    "isRequired": true,
    "minValue" : 1,
    "maxValue" : 1000,
    "schemaId": 1
  }'
```

### Step 5: Create API Endpoints 🔗

Define API endpoints that will serve your mock data.

**Create GET Single Product Endpoint:**
```bash
curl -X POST http://localhost:8080/api/endpoints \
  -H "Content-Type: application/json" \
  -d '{
    "path": "/products/{id}",
    "method": "GET",
    "responseSchemaId" : 1,
    "requestSchemaId" : 1,
    "responseType": "array",
    "defaultCount": 20,
    "statusCode": 200
  }'
```

### Step 6: Access Dynamic Mock APIs 🚀

Now you can access your dynamically created mock APIs.

**Get Single Product (Mock API):**
```bash
curl http://localhost:8080/mock/products/1
```

**Get Product List (Mock API):**
```bash
curl http://localhost:8080/mock/products/1?count=10
```

**Sample Generated Data:**
```json
{
  "id": 1,
  "name": "Wireless Bluetooth Headphones",
  "price": 89.99,
  "description": "High-quality wireless headphones with noise cancellation",
  "stockQuantity": 45
}
```

### Available Field Types 📊

When creating schema fields, you can use these field types:
- `string` - Text data (names, titles)
- `number` - numbers (quantities, IDs, prices, ratings)
- `boolean` - True/false values
- `date` - Date values
- `email` - Email addresses
- `uuid` - Website URLs

### Mock Data Generation Features 🎯

APIForge uses JavaFaker to generate realistic data:
- **Names**: Realistic first and last names
- **Addresses**: Complete addresses with cities and states
- **Prices**: Realistic price ranges
- **Descriptions**: Product descriptions and lorem ipsum text
- **Categories**: Common product categories
- **Dates**: Random dates within reasonable ranges
- **Numbers**: Realistic quantities and IDs

### Use Cases for Dynamic APIs 💡

1. **Frontend Development**: Test React/Vue/Angular apps with realistic data
2. **Mobile App Testing**: Provide consistent mock data for mobile development
3. **API Documentation**: Generate examples for API documentation
4. **Load Testing**: Create endpoints for performance testing
5. **Prototyping**: Quickly validate API designs with stakeholders
6. **Integration Testing**: Test third-party integrations with mock data

### Workflow Summary 📝

```
User Creation → Project Creation → Schema Definition → Field Addition → 
Endpoint Creation → Mock API Access
```

This complete workflow allows you to go from zero to a fully functional mock API in minutes, enabling rapid prototyping and testing of your applications.

---

**Happy API Forging!** 🔨✨
