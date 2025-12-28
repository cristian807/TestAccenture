# API para gestion de franquicias

<p align="center">
  <b>API REST para gestión de franquicias, sucursales y productos</b><br>
  Desarrollado con Clean Architecture y completamente dockerizado
</p>

---

## Tabla de Contenidos

- [Descripción](#-descripción)
- [Tecnologías](#-tecnologías)
- [Instalación y Ejecución](#-instalación-y-ejecución)
- [Endpoints de la API](#-endpoints-de-la-api)
- [Documentación de swagger](#-documentación-de-swagger)
- [Autor](#-autor)

## Descripción

Sistema de gestión de franquicias que permite administrar:

| Entidad | Descripción |
|---------|-------------|
| **Franquicia** | Entidad principal con nombre y lista de sucursales |
| **Sucursal** | Pertenece a una franquicia y contiene productos |
| **Producto** | Pertenece a una sucursal, tiene nombre y stock |

## Tecnologías

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


### Verificar instalación:

```bash
# Verificar Docker
docker --version

# Verificar Docker Compose
docker-compose --version

# Verificar Git
git --version
```


## Instalación y Ejecución

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
```

### Paso 4: La API está lista

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
## Endpoints de la API

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

## Documentación de swagger

> Importa la colección de Postman que esta en la carpeta principal del proyecto ( **TestAccenture.postman_collection.json** ) o utiliza la documentacion de Swagger en http://localhost:8080/swagger-ui.html.
> 
> **URL Base:** `http://localhost:8080/api`

## Desarrollador

**Cristian Mosquera Mosquera**

- LinkedIn: [Ver mi perfil](https://www.linkedin.com/in/cristian-mosquera-mosquera/)


