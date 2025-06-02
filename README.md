# GreenWheels

**GreenWheels** es una plataforma distribuida orientada al análisis de datos urbanos en tiempo real, combinando información ambiental (calidad del aire) y operativa (disponibilidad de bicicletas). Desarrollada con una arquitectura de microservicios, está desplegada completamente sobre Kubernetes e implementa tareas automáticas mediante cron jobs para garantizar la actualización continua del sistema.

## 👥 Autores

Desarrollado por:

- [Diego Segovia](https://github.com/diegoseg15)  
- [Marco Rodas](https://github.com/rodasmarco12)

Estudiantes del Máster en Tecnologías Web, Computación en la Nube y Aplicaciones Móviles en la **Universitat de València**.

## 🛠️ Tecnologías utilizadas

- **Kubernetes**
- **Docker**
- **Spring Boot**
- **Spring Cloud**
- **Java 17**
- **MySQL**
- **MongoDB**

## ⚙️ Arquitectura del Sistema

El sistema sigue una estructura por capas:

- 🔴 **API Layer**: Microservicios con lógica de negocio y endpoints expuestos.
- 🟢 **Data-Access Layer**: Microservicios de persistencia dedicados.
- 🔵 **Persistencia Layer**: Bases de datos físicas (MySQL y MongoDB).
- 🔐 **Seguridad JWT**: A través del microservicio `auth-service`.

### 🧩 Microservicios

- `auth-service` (8085): autenticación y emisión de tokens JWT.
- `config-server` (8888): configuración centralizada vía Spring Cloud Config.
- `ayuntamiento-service` (8090): coordinación de datos agregados entre bicicletas y polución.
- `bicicleta-service` (8084): gestión de estaciones de aparcamiento y disponibilidad.
- `polucion-service` (8091): captura y análisis de datos ambientales.
- `data-ayuntamiento` (8087): persistencia de estadísticas urbanas (MongoDB).
- `data-bicicletas` (8083): persistencia híbrida (MySQL + MongoDB).
- `data-polucion` (8086): persistencia híbrida (MySQL + MongoDB).

### 🔄 Cron Jobs (Tareas Automatizadas)

- **Estación** (`8092`): consulta periódica a la API de polución para registrar nuevas estadísticas.
- **Aparcamiento** (`8089`): obtiene disponibilidad de bicicletas en tiempo real.
- **Servicio** (`8095`): agrega datos cruzados de bicicletas y polución para persistirlos como estadísticas consolidadas.

## ☁️ Despliegue en Kubernetes

- Se utilizan **StatefulSet con volúmenes persistentes locales** (`hostPath`) para las bases de datos.
- Las configuraciones se gestionan mediante `ConfigMap` y `Secret`.
- Los servicios usan `ClusterIP` y los pods de base de datos `Headless Service`.
- El acceso externo se gestiona mediante un **Ingress Controller NGINX**.
- Separación de namespaces y estructura modular por microservicio.

## 📊 Bases de datos

- **MongoDB**:
  - `data-ayuntamiento` (puerto 27019)
  - `data-bicicletas` (puerto 27017)
  - `data-polucion` (puerto 27018)

- **MySQL**:
  - `data-bicicletas` (puerto 3308)
  - `data-polucion` (puerto 3309)

## 🧪 Seguridad y Accesos

El sistema emplea autenticación basada en JWT, con control de roles (`USER`, `ADMIN`, `SERVICIO`) y filtros perimetrales para acceso a recursos públicos y protegidos.

## 📄 Licencia

Este proyecto está licenciado bajo la [MIT License](https://opensource.org/licenses/MIT).

Esto significa que cualquier persona es libre de utilizar, siempre que conserve el aviso de copyright original y esta licencia en todas las copias o partes sustanciales del software.

**Limitación de responsabilidad**:  
El software se proporciona "tal cual", sin garantía de ningún tipo, expresa o implícita, incluyendo pero no limitado a garantías de comerciabilidad, idoneidad para un propósito particular y no infracción. En ningún caso los autores o titulares del copyright serán responsables de ningún daño o reclamación derivados del uso del software.

Este proyecto fue desarrollado con fines académicos y formativos, en el marco del Máster en Tecnologías Web, Computación en la Nube y Aplicaciones Móviles de la Universitat de València.

