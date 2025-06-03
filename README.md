# 🚲 GreenWheels

**GreenWheels** es una plataforma distribuida para el análisis de datos urbanos en tiempo real, que combina información ambiental (calidad del aire) y operativa (disponibilidad de bicicletas). Fue desarrollada en el marco del Máster en Tecnologías Web, Computación en la Nube y Aplicaciones Móviles de la **Universitat de València**, y está orientada a su despliegue completo en entornos escalables mediante **Kubernetes**.

## 👨‍💻 Autores

Proyecto desarrollado por:

- [Diego Segovia](https://github.com/diegoseg15)
- [Marco Rodas](https://github.com/rodasmarco12)

Estudiantes del Máster en Tecnologías Web, Computación en la Nube y Aplicaciones Móviles.

## 🛠️ Tecnologías utilizadas

- **Java 17**, **Spring Boot** y **Spring Cloud**
- **Docker** y **Docker Compose**
- **Kubernetes**
- **MongoDB** (NoSQL) y **MySQL** (relacional)
- **Spring Security con JWT**
- **NGINX Ingress Controller**
- **Eureka Service Discovery**
- **Spring Cloud Config Server**

## ⚙️ Arquitectura del Sistema

El sistema está organizado en **capas funcionales**, promoviendo la separación de responsabilidades:

### 🔧 Capas principales

- 🔴 **API Layer**: Exposición de endpoints públicos y administrativos.
- 🟢 **Data-Access Layer**: Microservicios dedicados al acceso a bases de datos relacionales y no relacionales.
- 🔵 **Persistencia Layer**: Bases de datos físicas desplegadas en contenedores y gestionadas como servicios de estado.
- 🔐 **Seguridad**: Autenticación y autorización basada en JWT a través del microservicio `auth-service`.

![Arquitectura del Sistema GreenWheels](https://github.com/rodasmarco12/GreenWheels/blob/main/assets/1.png?raw=true)

### 🧩 Microservicios

| Servicio               | Descripción                                              | Puerto | Tipo de Persistencia |
| ---------------------- | -------------------------------------------------------- | ------ | -------------------- |
| `auth-service`         | Gestión de usuarios y generación de tokens JWT           | 8085   | -                    |
| `config-server`        | Centralización de configuración para todos los servicios | 8888   | -                    |
| `ayuntamiento-service` | Agregación de datos urbanos (bicicletas + polución)      | 8090   | -                    |
| `bicicleta-service`    | Gestión lógica de aparcamientos de bicicletas            | 8084   | -                    |
| `polucion-service`     | Gestión lógica de estaciones de polución                 | 8091   | -                    |
| `data-ayuntamiento`    | Persistencia de estadísticas urbanas                     | 8087   | MongoDB              |
| `data-bicicletas`      | Persistencia de aparcamientos                            | 8083   | MySQL + MongoDB      |
| `data-polucion`        | Persistencia de calidad del aire                         | 8086   | MySQL + MongoDB      |

## 🔄 Cron Jobs (Tareas Automatizadas)

| Worker                | Rol          | Puerto | Funcionalidad                                 |
| --------------------- | ------------ | ------ | --------------------------------------------- |
| `worker-estacion`     | ESTACION     | 8092   | Guarda lecturas ambientales periódicamente    |
| `worker-aparcamiento` | APARCAMIENTO | 8089   | Envía eventos de disponibilidad de bicicletas |
| `worker-servicio`     | SERVICIO     | 8095   | Agrega y persiste estadísticas cruzadas       |

### ☸️ Kubernetes

Kubernetes permite orquestar los microservicios del sistema _GreenWheels_ de forma escalable, modular y automatizada. En esta arquitectura, todo se organiza dentro del namespace `twcam` y se divide en tres capas:

- 🔴 **API Layer**: gestiona el acceso externo a través de un Ingress Controller y gateways.
- 🟢 **Data-Access Layer**: se encarga de la lógica de persistencia, conectando con las bases de datos.
- 🔵 **Persistencia Layer**: aloja las bases de datos MySQL y MongoDB usando `StatefulSets` y volúmenes persistentes.
  Cada capa se comunica con la siguiente mediante servicios internos, facilitando el mantenimiento y la escalabilidad del sistema.

  ![Arquitectura de kubernetes de GreenWheels](https://github.com/rodasmarco12/GreenWheels/blob/main/assets/2.png?raw=true)

---

## 🚀 Modos de Despliegue

### 🧪 Despliegue Híbrido

Ideal para desarrollo local y debugging.

1. Navegar a la raíz del proyecto:

   ```bash
   cd MRDS_TWCAM
   ```

2. Levantar las **bases de datos** únicamente (otros servicios comentados en `docker-compose.yml`):

   ```bash
   docker compose up -d
   ```

3. Lanzar los microservicios deseados desde su directorio:

   ```bash
   cd <carpeta-del-servicio>
   mvn spring-boot:run
   ```

4. Se recomienda iniciar primero el `config-server`.

📌 **Notas**:

- Las tareas programadas (cron jobs) son opcionales y no son necesarias para las pruebas básicas.
- Las bases de datos ya incluyen datos precargados para pruebas.

### 📦 Despliegue completo con Docker

1. Asegúrate de **descomentar todos los servicios** en el `docker-compose.yml`.
2. Ejecutar:

   ```bash
   docker compose up -d
   ```

Este modo garantiza la ejecución íntegra del sistema, con todos los microservicios y bases de datos dentro de contenedores aislados.

Claro, aquí tienes una versión **ampliada y detallada** del apartado de **Despliegue en Kubernetes**, ideal tanto para documentación académica como para facilitar la comprensión técnica paso a paso:

## ☁️ Despliegue en Kubernetes (Paso a paso)

El despliegue en Kubernetes es el enfoque más completo y profesional para orquestar los microservicios de GreenWheels. A continuación, se describen los pasos detallados para realizar un despliegue exitoso en un clúster de Kubernetes:

### 🧱 1. Crear el Namespace

Todos los recursos del sistema se agrupan bajo un namespace específico para evitar conflictos con otros servicios del clúster y mantener la modularidad.

```bash
kubectl create -f twcam-namespace.yaml
```

Este archivo define un namespace llamado `twcam` que será usado en todos los recursos posteriores.

### 🔐 2. Crear Secrets y ConfigMaps

Se crean primero los **secrets** con información sensible (como credenciales de base de datos o claves JWT):

```bash
kubectl apply -f twcam-secrets.yaml
```

Después, se cargan los **ConfigMaps** que contienen scripts de inicialización de las bases de datos:

```bash
kubectl apply -f Persistencia_Layer/init-scripts-configmap.yaml
```

Esto asegura que, al iniciarse, las bases de datos contengan la estructura y los datos iniciales necesarios para pruebas y funcionamiento del sistema.

### 💾 3. Configurar el Almacenamiento Persistente

Cada base de datos utiliza volúmenes persistentes que almacenan los datos fuera del ciclo de vida de los contenedores, evitando su pérdida en caso de reinicio:

```bash
kubectl apply -f Persistencia_Layer/persistent-volumes.yaml
```

Este archivo incluye los `PersistentVolume` y `PersistentVolumeClaim` necesarios, configurados con almacenamiento local mediante `hostPath`.

### 🛢️ 4. Desplegar Bases de Datos

Se despliegan primero los **StatefulSets** que controlan la creación de los pods con identidad persistente (necesaria para MongoDB y MySQL):

```bash
kubectl apply -f Persistencia_Layer/mongo-sts.yaml
kubectl apply -f Persistencia_Layer/mysql-sts.yaml
```

Y luego, se crean los servicios correspondientes:

```bash
kubectl apply -f Persistencia_Layer/mongo-services.yaml
kubectl apply -f Persistencia_Layer/mysql-services.yaml
```

- Se utilizan servicios **headless** para permitir el descubrimiento de pods individuales por parte de los microservicios que se conectan a las bases de datos.
- Los pods pueden tardar algunos segundos en estar disponibles. Se puede monitorear con:

```bash
kubectl get pods -n twcam -w
```

### 🧬 5. Desplegar la Capa de Acceso a Datos

Una vez las bases de datos están listas y funcionando, se procede a desplegar los microservicios del **Data-Access Layer**. Estos microservicios son los responsables de interactuar directamente con las bases de datos.

```bash
kubectl apply -f DataAccess_Layer/data-cm.yaml
kubectl apply -f DataAccess_Layer/data-deploy.yaml
kubectl apply -f DataAccess_Layer/data-services.yaml
```

🔁 Este paso puede repetirse si se desea actualizar los servicios de acceso a datos sin reinstalar las bases de datos.

### 🧩 6. Desplegar la Capa de Lógica (API Layer)

Los microservicios principales (ayuntamiento, bicicleta, polución, auth, etc.) se despliegan a través de los manifiestos de la capa API. Incluyen también los gateways de cada bloque.

```bash
kubectl apply -f API_Layer/api-cm.yaml
kubectl apply -f API_Layer/api-deploy.yaml
kubectl apply -f API_Layer/api-service.yaml
```

Perfecto. A continuación te presento una **versión corregida y detallada** del **paso 7: Configurar el Ingress Controller**, con instrucciones específicas para entornos Linux y Windows (CMD), y considerando el despliegue sobre un entorno **bare-metal**, como el que se suele usar en prácticas locales o máquinas virtuales:

### 🌐 7. Configurar el Ingress Controller (NGINX)

El **Ingress Controller** permite exponer los servicios internos de Kubernetes a través de una única IP o dominio público, organizando las rutas mediante reglas declarativas. Esto es esencial para acceder a los microservicios desde fuera del clúster (por ejemplo, desde un navegador o Postman).

#### 📥 Instalación del NGINX Ingress Controller (Bare Metal)

Para instalar el **NGINX Ingress Controller** en entornos sin proveedor cloud (como Minikube, kind o máquinas virtuales en local), ejecuta lo siguiente:

```bash
kubectl apply -f https://raw.githubusercontent.com/kubernetes/ingress-nginx/main/deploy/static/provider/baremetal/deploy.yaml
```

Esto desplegará todos los recursos necesarios en el namespace `ingress-nginx`.

#### ✅ Definir clase por defecto

Es necesario anotar la clase de ingress `nginx` como predeterminada para que los manifiestos `Ingress` la utilicen correctamente:

```bash
kubectl -n ingress-nginx annotate ingressclasses nginx ingressclass.kubernetes.io/is-default-class="true"
```

#### 🔀 Cambiar tipo de servicio a NodePort

Por defecto, el `Ingress Controller` se expone como `LoadBalancer`, lo cual no funcionará en entornos locales. Por eso, lo cambiamos a `NodePort`:

```bash
kubectl patch svc ingress-nginx-controller -n ingress-nginx -p "{\"spec\": {\"type\": \"NodePort\"}}"
```

Esto permitirá acceder al controlador desde el puerto de red del nodo.

#### 📡 Verificar el despliegue

Para verificar que el controlador se ha desplegado correctamente y está corriendo:

```bash
kubectl get pods -n ingress-nginx
kubectl get svc -n ingress-nginx
```

### 🌍 Acceso desde Linux y Windows (CMD)

Una vez desplegado el controlador, obtén la IP del nodo y el puerto asignado al servicio:

```bash
kubectl get svc ingress-nginx-controller -n ingress-nginx
```

Busca el valor bajo la columna `PORT(S)` (por ejemplo: `80:30345/TCP`) y accede mediante:

- En **Linux**:
  Edita el archivo `/etc/hosts`:

  ```bash
  sudo nano /etc/hosts
  ```

  Añade una línea como:

  ```
  192.168.1.100  twcam.local
  ```

- En **Windows (CMD ejecutado como administrador)**:
  Edita el archivo de hosts:

  ```
  notepad C:\Windows\System32\drivers\etc\hosts
  ```

  Añade la misma línea:

  ```
  192.168.1.100  twcam.local
  ```

🔎 Reemplaza `192.168.1.100` por la IP del nodo donde se ejecuta el clúster de Kubernetes.

### 📁 Aplicar reglas de Ingress

Con el Ingress Controller operativo, puedes aplicar el manifiesto `Ingress` de tu API Gateway:

```bash
kubectl apply -f API_Layer/api-ingress.yaml
```

Este recurso define rutas como:

- `/aparcamientos` → redirige al `bicicleta-gateway`
- `/estaciones` → redirige al `pollution-gateway`
- `/auth` → redirige al `auth-service`

Podrás acceder a todas las rutas desde el navegador o Postman usando la URL:

```
http://twcam.local/<ruta>
```

### 🧪 8. Verificación del despliegue

Una vez aplicados todos los manifiestos, es fundamental comprobar que **todos los pods están en estado `Running`**, que los servicios se exponen correctamente y que el Ingress está redireccionando las peticiones.

#### ✅ Comprobar recursos en ejecución

```bash
kubectl get all -n twcam
```

Esto mostrará:

- Pods (estado: `Running`)
- Services (tipo: `ClusterIP` o `Headless`)
- Deployments y ReplicaSets
- Endpoints disponibles para cada microservicio

#### 📜 Comprobar logs de los servicios

Puedes revisar los logs de cada pod para confirmar que las aplicaciones han arrancado correctamente:

```bash
kubectl logs -f deployment/<nombre-del-deployment> -n twcam
```

Ejemplo para bicicletas:

```bash
kubectl logs -f deployment/bicicletas-service -n twcam
```

#### 🔎 Verificar la configuración de los Ingress

Para confirmar que el recurso `Ingress` ha sido correctamente registrado:

```bash
kubectl get ingress -n twcam
```

Este comando te mostrará las rutas expuestas por el Ingress Controller. Verifica que aparezca algo como:

```
NAME              CLASS    HOSTS         ADDRESS         PORTS
twcam-ingress     nginx    twcam.local   <external-ip>   80
```

#### 🌍 Probar acceso desde navegador o Postman

Accede a las rutas definidas en el Ingress, por ejemplo:

```
http://twcam.local/aparcamientos
http://twcam.local/estaciones
http://twcam.local/auth/login
```

🛡️ Si el endpoint está protegido, asegúrate de incluir un **JWT válido** en los headers (`Authorization: Bearer <token>`).

#### 📡 Verificar resolución DNS (local)

Desde tu máquina, puedes probar la resolución del dominio con:

```bash
ping twcam.local
```

Y si usas `curl`:

```bash
curl http://twcam.local/aparcamientos
```

Esto confirmará tanto la resolución del dominio como el funcionamiento de la cadena Ingress → Gateway → Microservicio.

#### 🧰 Verificar conexión entre servicios

Dentro del clúster, puedes hacer pruebas de conectividad entre pods usando:

```bash
kubectl exec -it <nombre-del-pod> -n twcam -- sh
```

Y desde ahí hacer un `curl` interno hacia otros servicios:

```bash
curl http://data-bicicletas-service:8083/api/v1/data/bicicletas
```

Esto valida la resolución de nombres vía DNS interno (`kube-dns`), fundamental para la comunicación entre microservicios.

### 🧹 9. Limpieza del entorno (opcional)

Si deseas eliminar todo el entorno desplegado:

```bash
kubectl delete namespace twcam
```

## 📊 Bases de Datos

| Servicio            | Tipo            | Puerto       | Contenido              |
| ------------------- | --------------- | ------------ | ---------------------- |
| `data-ayuntamiento` | MongoDB         | 27019        | Estadísticas agregadas |
| `data-bicicletas`   | MySQL + MongoDB | 3308 + 27017 | Aparcamientos, eventos |
| `data-polucion`     | MySQL + MongoDB | 3309 + 27018 | Lecturas y estaciones  |

## 🔐 Seguridad y Roles

El sistema utiliza **JWT** para autenticación y autorización. Se define control de acceso por roles:

| Rol            | Descripción                             |
| -------------- | --------------------------------------- |
| `ADMIN`        | Control total sobre el sistema          |
| `APARCAMIENTO` | Envío de eventos desde aparcamientos    |
| `ESTACION`     | Registro de lecturas ambientales        |
| `SERVICIO`     | Agregación y consulta de datos internos |

### 🔑 JWT de prueba

Puedes utilizar los siguientes JWT para pruebas en Postman o Swagger UI:

```txt
ADMIN:
eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9...

SERVICIO:
eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9...

APARCAMIENTO:
eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9...

ESTACION:
eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9...

TODOS LOS ROLES:
eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9...
```

## 📘 Documentación y API

Todos los endpoints cuentan con documentación **OpenAPI/Swagger** accesible desde el navegador. Es necesario introducir un **JWT válido** en la sección de autorización.

## 📄 Licencia

Este proyecto está licenciado bajo la [MIT License](https://opensource.org/licenses/MIT).

> **Aviso legal**:
> Este software se proporciona "tal cual", sin garantía expresa o implícita. Su uso está permitido con fines educativos y personales. Los autores no se hacen responsables de su uso indebido.

## 🎓 Contexto Académico

Este proyecto ha sido desarrollado como parte del módulo de **Persistencia Relacional y No Relacional**, abordando retos reales de integración, orquestación y análisis de datos distribuidos en entornos urbanos.

Se exploran conceptos clave como:

- Diseño orientado a microservicios
- Hibridación de bases de datos (SQL/NoSQL)
- Autenticación y control de acceso robusto
- Automatización con tareas programadas
- Despliegue profesional con Kubernetes
