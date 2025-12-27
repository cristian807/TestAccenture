# 🏪 Franchises Management API

<p align="center">
  <img src="https://img.shields.io/badge/Spring%20Boot-3.5.9-brightgreen?style=for-the-badge&logo=springboot" alt="Spring Boot">
  <img src="https://img.shields.io/badge/Java-17-orange?style=for-the-badge&logo=openjdk" alt="Java">
  <img src="https://img.shields.io/badge/MySQL-8.0-blue?style=for-the-badge&logo=mysql" alt="MySQL">
  <img src="https://img.shields.io/badge/Docker-Ready-2496ED?style=for-the-badge&logo=docker" alt="Docker">
</p>

<p align="center">
  <b>API REST para gestión de franquicias, sucursales y productos</b><br>
  Desarrollado con Clean Architecture y completamente dockerizado
</p>

---

## 📋 Tabla de Contenidos

- [Descripción](#-descripción)
- [Arquitectura](#-arquitectura)
- [Tecnologías](#-tecnologías)
- [Requisitos Previos](#-requisitos-previos)
- [Instalación y Ejecución](#-instalación-y-ejecución)
- [Endpoints de la API](#-endpoints-de-la-api)
- [Ejemplos de Uso con Postman](#-ejemplos-de-uso-con-postman)
  - [Franquicias](#franquicias)
  - [Sucursales](#sucursales)
  - [Productos](#productos)
- [Estructura del Proyecto](#-estructura-del-proyecto)
- [Autor](#-autor)

---

## 📖 Descripción

Sistema de gestión de franquicias que permite administrar:

| Entidad | Descripción |
|---------|-------------|
| **Franquicia** | Entidad principal con nombre y lista de sucursales |
| **Sucursal** | Pertenece a una franquicia y contiene productos |
| **Producto** | Pertenece a una sucursal, tiene nombre y stock |

### ✨ Características Principales

- ✅ CRUD completo para Franquicias, Sucursales y Productos
- ✅ Endpoint especial para obtener el producto con mayor stock por sucursal
- ✅ Actualización de nombres (franquicia, sucursal, producto)
- ✅ Actualización de stock de productos
- ✅ Dockerizado con MySQL
- ✅ Clean Architecture

---

## 🏗 Arquitectura

El proyecto implementa **Clean Architecture** con las siguientes capas:

```
┌─────────────────────────────────────────────────────────────────┐
│                    INTERFACE ADAPTERS                           │
│  ┌─────────────────────────────────────────────────────────┐   │
│  │  Controllers (REST API) + DTOs (Request/Response)        │   │
│  └─────────────────────────────────────────────────────────┘   │
├─────────────────────────────────────────────────────────────────┤
│                    APPLICATION LAYER                            │
│  ┌─────────────────────────────────────────────────────────┐   │
│  │  Use Cases (Casos de Uso / Lógica de Negocio)           │   │
│  └─────────────────────────────────────────────────────────┘   │
├─────────────────────────────────────────────────────────────────┤
│                      DOMAIN LAYER                               │
│  ┌─────────────────────────────────────────────────────────┐   │
│  │  Models (Entidades de Dominio) + Repository Interfaces   │   │
│  └─────────────────────────────────────────────────────────┘   │
├─────────────────────────────────────────────────────────────────┤
│                   INFRASTRUCTURE LAYER                          │
│  ┌─────────────────────────────────────────────────────────┐   │
│  │  JPA Entities + Mappers + Repository Implementations     │   │
│  └─────────────────────────────────────────────────────────┘   │
└─────────────────────────────────────────────────────────────────┘
```

---

## 🛠 Tecnologías

| Tecnología | Versión | Uso |
|------------|---------|-----|
| Java | 17 | Lenguaje de programación |
| Spring Boot | 3.5.9 | Framework principal |
| Spring Data JPA | - | Persistencia de datos |
| MySQL | 8.0 | Base de datos |
| Lombok | - | Reducción de código boilerplate |
| Docker | - | Contenedorización |
| Docker Compose | - | Orquestación de contenedores |
| Gradle | 8.5 | Gestión de dependencias |

---

## 📦 Requisitos Previos

Antes de ejecutar la aplicación, asegúrate de tener instalado:

- **Docker** (versión 20.x o superior)
- **Docker Compose** (versión 2.x o superior)
- **Git** (para clonar el repositorio)

### Verificar instalación:

```bash
# Verificar Docker
docker --version

# Verificar Docker Compose
docker-compose --version

# Verificar Git
git --version
```

---

## 🚀 Instalación y Ejecución

### Paso 1: Clonar el Repositorio

```bash
git clone https://github.com/tu-usuario/franquicies.git
cd franquicies
```

### Paso 2: Ejecutar con Docker Compose

```bash
# Construir y levantar los contenedores
docker-compose up -d --build
```

### Paso 3: Verificar que los servicios estén corriendo

```bash
docker-compose ps
```

Deberías ver algo como:

```
NAME               IMAGE             STATUS                    PORTS
franchises_app     franquicies-app   Up (healthy)              0.0.0.0:8080->8080/tcp
franchises_mysql   mysql:8.0         Up (healthy)              0.0.0.0:3307->3306/tcp
```

### Paso 4: ¡La API está lista! 🎉

```
URL Base: http://localhost:8080/api
```

### Comandos Útiles

```bash
# Ver logs de la aplicación
docker logs franchises_app -f

# Detener los servicios
docker-compose down

# Detener y eliminar datos (volúmenes)
docker-compose down -v

# Reconstruir sin caché
docker-compose build --no-cache
```

---

## 📡 Endpoints de la API

### Resumen de Endpoints

| Método | Endpoint | Descripción |
|--------|----------|-------------|
| **FRANQUICIAS** | | |
| `POST` | `/api/franchises` | Crear nueva franquicia |
| `GET` | `/api/franchises` | Obtener todas las franquicias |
| `GET` | `/api/franchises/{id}` | Obtener franquicia por ID |
| `DELETE` | `/api/franchises/{id}` | Eliminar franquicia |
| `PATCH` | `/api/franchises/{id}/name` | Actualizar nombre de franquicia |
| **SUCURSALES** | | |
| `POST` | `/api/branches` | Crear nueva sucursal |
| `GET` | `/api/branches` | Obtener todas las sucursales |
| `GET` | `/api/branches/{id}` | Obtener sucursal por ID |
| `GET` | `/api/branches/franchise/{franchiseId}` | Obtener sucursales por franquicia |
| `DELETE` | `/api/branches/{id}` | Eliminar sucursal |
| `PATCH` | `/api/branches/{id}/name` | Actualizar nombre de sucursal |
| **PRODUCTOS** | | |
| `POST` | `/api/products` | Crear nuevo producto |
| `GET` | `/api/products` | Obtener todos los productos |
| `GET` | `/api/products/{id}` | Obtener producto por ID |
| `GET` | `/api/products/branch/{branchId}` | Obtener productos por sucursal |
| `DELETE` | `/api/products/{id}` | Eliminar producto |
| `PATCH` | `/api/products/{id}/stock` | Actualizar stock de producto |
| `PATCH` | `/api/products/{id}/name` | Actualizar nombre de producto |
| `GET` | `/api/products/top-stock/franchise/{franchiseId}` | **Top stock por sucursal** |

---

## 🧪 Ejemplos de Uso con Postman

> **Nota:** Importa la colección de Postman o sigue los ejemplos a continuación.
> 
> **URL Base:** `http://localhost:8080/api`

---

### FRANQUICIAS

#### 1️⃣ Crear Nueva Franquicia

**Endpoint:** `POST /api/franchises`

**Request Body:**
```json
{
    "name": "McDonalds"
}
```

**Response (201 Created):**
```json
{
    "success": true,
    "message": "Franchise created successfully",
    "data": {
        "id": 1,
        "name": "McDonalds"
    }
}
```

📸 **Captura de Postman:**

![Crear Franquicia](./docs/images/01-crear-franquicia.png)

---

#### 2️⃣ Obtener Todas las Franquicias

**Endpoint:** `GET /api/franchises`

**Response (200 OK):**
```json
{
    "success": true,
    "message": "Operation successful",
    "data": [
        {
            "id": 1,
            "name": "McDonalds"
        },
        {
            "id": 2,
            "name": "Subway"
        }
    ]
}
```

📸 **Captura de Postman:**

![Obtener Franquicias](./docs/images/02-obtener-franquicias.png)

---

#### 3️⃣ Obtener Franquicia por ID

**Endpoint:** `GET /api/franchises/1`

**Response (200 OK):**
```json
{
    "success": true,
    "message": "Operation successful",
    "data": {
        "id": 1,
        "name": "McDonalds"
    }
}
```

📸 **Captura de Postman:**

![Obtener Franquicia por ID](./docs/images/03-obtener-franquicia-id.png)

---

#### 4️⃣ Actualizar Nombre de Franquicia

**Endpoint:** `PATCH /api/franchises/1/name`

**Request Body:**
```json
{
    "name": "McDonalds Colombia"
}
```

**Response (200 OK):**
```json
{
    "success": true,
    "message": "Franchise name updated successfully",
    "data": {
        "id": 1,
        "name": "McDonalds Colombia"
    }
}
```

📸 **Captura de Postman:**

![Actualizar Nombre Franquicia](./docs/images/04-actualizar-nombre-franquicia.png)

---

#### 5️⃣ Eliminar Franquicia

**Endpoint:** `DELETE /api/franchises/2`

**Response (200 OK):**
```json
{
    "success": true,
    "message": "Franchise deleted successfully",
    "data": null
}
```

📸 **Captura de Postman:**

![Eliminar Franquicia](./docs/images/05-eliminar-franquicia.png)

---

### SUCURSALES

#### 6️⃣ Crear Nueva Sucursal

**Endpoint:** `POST /api/branches`

**Request Body:**
```json
{
    "name": "Sucursal Centro",
    "franchiseId": 1
}
```

**Response (201 Created):**
```json
{
    "success": true,
    "message": "Branch created successfully",
    "data": {
        "id": 1,
        "name": "Sucursal Centro",
        "franchiseId": 1
    }
}
```

📸 **Captura de Postman:**

![Crear Sucursal](./docs/images/06-crear-sucursal.png)

---

#### 7️⃣ Obtener Todas las Sucursales

**Endpoint:** `GET /api/branches`

**Response (200 OK):**
```json
{
    "success": true,
    "message": "Operation successful",
    "data": [
        {
            "id": 1,
            "name": "Sucursal Centro",
            "franchiseId": 1
        },
        {
            "id": 2,
            "name": "Sucursal Norte",
            "franchiseId": 1
        }
    ]
}
```

📸 **Captura de Postman:**

![Obtener Sucursales](./docs/images/07-obtener-sucursales.png)

---

#### 8️⃣ Obtener Sucursales por Franquicia

**Endpoint:** `GET /api/branches/franchise/1`

**Response (200 OK):**
```json
{
    "success": true,
    "message": "Operation successful",
    "data": [
        {
            "id": 1,
            "name": "Sucursal Centro",
            "franchiseId": 1
        },
        {
            "id": 2,
            "name": "Sucursal Norte",
            "franchiseId": 1
        }
    ]
}
```

📸 **Captura de Postman:**

![Sucursales por Franquicia](./docs/images/08-sucursales-por-franquicia.png)

---

#### 9️⃣ Actualizar Nombre de Sucursal

**Endpoint:** `PATCH /api/branches/1/name`

**Request Body:**
```json
{
    "name": "Sucursal Centro Principal"
}
```

**Response (200 OK):**
```json
{
    "success": true,
    "message": "Branch name updated successfully",
    "data": {
        "id": 1,
        "name": "Sucursal Centro Principal",
        "franchiseId": 1
    }
}
```

📸 **Captura de Postman:**

![Actualizar Nombre Sucursal](./docs/images/09-actualizar-nombre-sucursal.png)

---

#### 🔟 Eliminar Sucursal

**Endpoint:** `DELETE /api/branches/2`

**Response (200 OK):**
```json
{
    "success": true,
    "message": "Branch deleted successfully",
    "data": null
}
```

📸 **Captura de Postman:**

![Eliminar Sucursal](./docs/images/10-eliminar-sucursal.png)

---

### PRODUCTOS

#### 1️⃣1️⃣ Crear Nuevo Producto

**Endpoint:** `POST /api/products`

**Request Body:**
```json
{
    "name": "Big Mac",
    "stock": 100,
    "branchId": 1
}
```

**Response (201 Created):**
```json
{
    "success": true,
    "message": "Product created successfully",
    "data": {
        "id": 1,
        "name": "Big Mac",
        "stock": 100,
        "branchId": 1
    }
}
```

📸 **Captura de Postman:**

![Crear Producto](./docs/images/11-crear-producto.png)

---

#### 1️⃣2️⃣ Obtener Todos los Productos

**Endpoint:** `GET /api/products`

**Response (200 OK):**
```json
{
    "success": true,
    "message": "Operation successful",
    "data": [
        {
            "id": 1,
            "name": "Big Mac",
            "stock": 100,
            "branchId": 1
        },
        {
            "id": 2,
            "name": "McFlurry",
            "stock": 200,
            "branchId": 1
        },
        {
            "id": 3,
            "name": "Papas Fritas",
            "stock": 150,
            "branchId": 2
        }
    ]
}
```

📸 **Captura de Postman:**

![Obtener Productos](./docs/images/12-obtener-productos.png)

---

#### 1️⃣3️⃣ Obtener Productos por Sucursal

**Endpoint:** `GET /api/products/branch/1`

**Response (200 OK):**
```json
{
    "success": true,
    "message": "Operation successful",
    "data": [
        {
            "id": 1,
            "name": "Big Mac",
            "stock": 100,
            "branchId": 1
        },
        {
            "id": 2,
            "name": "McFlurry",
            "stock": 200,
            "branchId": 1
        }
    ]
}
```

📸 **Captura de Postman:**

![Productos por Sucursal](./docs/images/13-productos-por-sucursal.png)

---

#### 1️⃣4️⃣ Actualizar Stock de Producto

**Endpoint:** `PATCH /api/products/1/stock`

**Request Body:**
```json
{
    "stock": 250
}
```

**Response (200 OK):**
```json
{
    "success": true,
    "message": "Product stock updated successfully",
    "data": {
        "id": 1,
        "name": "Big Mac",
        "stock": 250,
        "branchId": 1
    }
}
```

📸 **Captura de Postman:**

![Actualizar Stock](./docs/images/14-actualizar-stock.png)

---

#### 1️⃣5️⃣ Actualizar Nombre de Producto

**Endpoint:** `PATCH /api/products/1/name`

**Request Body:**
```json
{
    "name": "Big Mac Premium"
}
```

**Response (200 OK):**
```json
{
    "success": true,
    "message": "Product name updated successfully",
    "data": {
        "id": 1,
        "name": "Big Mac Premium",
        "stock": 250,
        "branchId": 1
    }
}
```

📸 **Captura de Postman:**

![Actualizar Nombre Producto](./docs/images/15-actualizar-nombre-producto.png)

---

#### 1️⃣6️⃣ Eliminar Producto

**Endpoint:** `DELETE /api/products/2`

**Response (200 OK):**
```json
{
    "success": true,
    "message": "Product deleted successfully",
    "data": null
}
```

📸 **Captura de Postman:**

![Eliminar Producto](./docs/images/16-eliminar-producto.png)

---

#### ⭐ 1️⃣7️⃣ Producto con Mayor Stock por Sucursal (ENDPOINT ESPECIAL)

> **Este endpoint retorna el producto con mayor stock de CADA sucursal de una franquicia específica.**

**Endpoint:** `GET /api/products/top-stock/franchise/1`

**Response (200 OK):**
```json
{
    "success": true,
    "message": "Top stock products by branch for franchise 1",
    "data": [
        {
            "productId": 1,
            "productName": "Big Mac Premium",
            "stock": 250,
            "branchId": 1,
            "branchName": "Sucursal Centro Principal"
        },
        {
            "productId": 3,
            "productName": "Papas Fritas",
            "stock": 150,
            "branchId": 2,
            "branchName": "Sucursal Norte"
        }
    ]
}
```

📸 **Captura de Postman:**

![Top Stock por Franquicia](./docs/images/17-top-stock-franquicia.png)

---

## 📁 Estructura del Proyecto

```
franquicies/
├── 📄 docker-compose.yml          # Orquestación de contenedores
├── 📄 Dockerfile                  # Construcción de imagen Docker
├── 📄 build.gradle                # Dependencias del proyecto
├── 📄 README.md                   # Documentación
├── 📁 docs/
│   └── 📁 images/                 # Capturas de Postman
├── 📁 src/
│   └── 📁 main/
│       ├── 📁 java/com/accenture/franquicies/
│       │   ├── 📄 FranquiciesApplication.java
│       │   │
│       │   ├── 📁 Domain/                    # 🔵 CAPA DE DOMINIO
│       │   │   ├── 📁 Models/
│       │   │   │   ├── 📄 Franchise.java
│       │   │   │   ├── 📄 Branch.java
│       │   │   │   └── 📄 Product.java
│       │   │   └── 📁 Repository/
│       │   │       ├── 📄 FranchiseRepository.java
│       │   │       ├── 📄 BranchRepository.java
│       │   │       └── 📄 ProductRepository.java
│       │   │
│       │   ├── 📁 Application/               # 🟢 CAPA DE APLICACIÓN
│       │   │   └── 📁 UseCase/
│       │   │       ├── 📁 Franchises/
│       │   │       │   ├── 📄 CreateFranchiseUseCase.java
│       │   │       │   ├── 📄 GetAllFranchise.java
│       │   │       │   ├── 📄 GetByIdFranchise.java
│       │   │       │   ├── 📄 DeleteByIdFranchise.java
│       │   │       │   └── 📄 UpdateFranchiseNameUseCase.java
│       │   │       ├── 📁 Branches/
│       │   │       │   └── 📄 ... (CRUD + UpdateName)
│       │   │       └── 📁 Products/
│       │   │           ├── 📄 ... (CRUD)
│       │   │           ├── 📄 UpdateProductStockUseCase.java
│       │   │           ├── 📄 UpdateProductNameUseCase.java
│       │   │           └── 📄 GetTopStockProductsByFranchiseUseCase.java
│       │   │
│       │   ├── 📁 Infraestructure/           # 🟠 CAPA DE INFRAESTRUCTURA
│       │   │   ├── 📁 Config/
│       │   │   │   └── 📄 GlobalExceptionHandler.java
│       │   │   └── 📁 Persistence/
│       │   │       ├── 📁 Entity/
│       │   │       │   ├── 📄 FranchiseEntity.java
│       │   │       │   ├── 📄 BranchEntity.java
│       │   │       │   └── 📄 ProductEntity.java
│       │   │       ├── 📁 Mappers/
│       │   │       │   └── 📄 ...Mapper.java
│       │   │       └── 📁 Repository/
│       │   │           ├── 📄 Jpa...Repository.java
│       │   │           └── 📁 Impl/
│       │   │               └── 📄 ...RepositoryImpl.java
│       │   │
│       │   └── 📁 Interfaceadapter/          # 🟣 ADAPTADORES DE INTERFAZ
│       │       └── 📁 Controllers/
│       │           ├── 📄 FranchiseController.java
│       │           ├── 📄 BranchController.java
│       │           ├── 📄 ProductController.java
│       │           └── 📁 DTO/
│       │               ├── 📁 Request/
│       │               └── 📁 Response/
│       │
│       └── 📁 resources/
│           └── 📄 application.properties
```

---

## ✅ Checklist de Criterios de Aceptación

| # | Criterio | Estado |
|---|----------|--------|
| 1 | Proyecto desarrollado en Spring Boot | ✅ |
| 2 | Endpoint para agregar nueva franquicia | ✅ |
| 3 | Endpoint para agregar sucursal a franquicia | ✅ |
| 4 | Endpoint para agregar producto a sucursal | ✅ |
| 5 | Endpoint para eliminar producto de sucursal | ✅ |
| 6 | Endpoint para modificar stock de producto | ✅ |
| 7 | Endpoint top stock por sucursal/franquicia | ✅ |
| 8 | Persistencia de datos (MySQL) | ✅ |

### Puntos Extra Implementados

| Punto Extra | Estado |
|-------------|--------|
| Empaquetado con Docker | ✅ |
| Actualizar nombre de franquicia | ✅ |
| Actualizar nombre de sucursal | ✅ |
| Actualizar nombre de producto | ✅ |
| Clean Architecture | ✅ |

---

## 🧪 Colección de Postman

Puedes importar la colección de Postman para probar todos los endpoints fácilmente:

📥 [Descargar Colección de Postman](./docs/Franchises-API.postman_collection.json)

---

## 👤 Autor

**Cristian Moquera**

- GitHub: [@tu-usuario](https://github.com/tu-usuario)
- LinkedIn: [Tu Perfil](https://linkedin.com/in/tu-perfil)

---

## 📄 Licencia

Este proyecto fue desarrollado como prueba técnica para **Accenture**.

---

<p align="center">
  <b>⭐ Si te gustó este proyecto, no olvides darle una estrella! ⭐</b>
</p>
