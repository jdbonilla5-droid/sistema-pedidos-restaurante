# sistema-pedidos-restaurante
API REST con Spring Boot para la gestión de pedidos de restaurante - Grupo 4
# Sistema de pedidos para restaurante — Grupo 4

API REST para gestionar categorías, platos, clientes, pedidos y detalles de pedidos.

## Tecnologías

- Java 21
- Spring Boot 4.1.2
- Maven mediante Maven Wrapper
- Spring Data JPA
- PostgreSQL 17
- Postman

## Avance del módulo de catálogo

- CRUD de categorías y platos.
- Relación entre platos y categorías.
- Consultas de platos por categoría y disponibilidad.
- Validación de datos de entrada.
- Manejo centralizado de excepciones.
- Protección contra eliminación de categorías con platos asociados.

Los módulos de clientes, pedidos y detalles de pedidos se integrarán con los avances del equipo.

## Preparación de la base de datos

Crear una base de datos llamada `restaurante_db` en PostgreSQL.

La configuración utiliza:

- Servidor: `localhost`
- Puerto: `5432`
- Usuario predeterminado: `postgres`
- Contraseña: variable de entorno `DB_PASSWORD`

Si se utiliza otro usuario, definir también `DB_USERNAME`.

## Ejecutar en Windows PowerShell

Abrir una terminal en la carpeta del proyecto.

Configurar la contraseña para esa sesión:

```powershell
$env:DB_PASSWORD = [System.Net.NetworkCredential]::new('', (Read-Host 'Contraseña de PostgreSQL' -AsSecureString)).Password
```

Iniciar la aplicación en la misma terminal:

```powershell
.\mvnw.cmd spring-boot:run
```

La aplicación utiliza el puerto `8080`.

Durante el desarrollo, Hibernate crea o actualiza las tablas a partir de las entidades mediante `ddl-auto=update`.

## Comprobar la compilación

```powershell
.\mvnw.cmd clean compile
```

Este comando compila el código; no ejecuta las pruebas automatizadas.

## Peticiones de Postman

Importar el archivo:

`postman/restaurante-catalogo.postman_collection.json`

Crear primero una categoría y utilizar su ID al registrar platos.

Las peticiones guardadas contienen ID de ejemplo. Ajustarlos según los registros de cada base de datos. Los registros locales de PostgreSQL no se incluyen en el repositorio.

## Rutas principales

- `/api/categorias`
- `/api/platos`
- `/api/platos/categoria/{categoriaId}`
- `/api/platos/disponibilidad?disponible=true`

## Trabajo en equipo

Cada integrante desarrolla en su propia rama y registra avances mediante commits.

Rama del módulo de catálogo: `feature/platos-categorias`.

Los cambios se integran a `main` mediante pull requests y revisión del equipo.