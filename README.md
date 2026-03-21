# Biblioteca - Sistema de Gestión de Biblioteca

Proyecto educativo desarrollado con **Spring Boot** que implementa un sistema básico de gestión de biblioteca, incluyendo el manejo de libros, autores, usuarios y préstamos.

---

## Tecnologías utilizadas

| Tecnología          | Versión  | Rol                                      |
|---------------------|----------|------------------------------------------|
| Java                | 17       | Lenguaje principal                       |
| Spring Boot         | 3.5.12   | Framework de aplicación                  |
| Spring Data JPA     | -        | Capa de persistencia (ORM con Hibernate) |
| Spring Web MVC      | -        | Capa web (REST / MVC)                    |
| Thymeleaf           | -        | Motor de plantillas HTML (preparado)     |
| MySQL               | 8.4      | Base de datos relacional                 |
| Docker / Compose    | -        | Contenedor para la base de datos         |
| Maven               | Wrapper  | Herramienta de construcción              |

---

## Lo que se llevó a cabo

### 1. Modelo de dominio (entidades JPA)

Se diseñaron e implementaron las siguientes entidades con sus relaciones:

- **`Autor`** — Representa a un autor de libros. Tiene nombre y apellido. Relación `@OneToMany` con `Libro`.
- **`Libro`** — Representa un libro del catálogo. Tiene título, año de publicación y una referencia a su autor (`@ManyToOne`). Relación `@OneToMany` con `Prestamo`.
- **`Usuario`** — Representa a un miembro de la biblioteca. Tiene nombre, apellido y correo electrónico único. Relación `@OneToMany` con `Prestamo`.
- **`Prestamo`** — Representa el préstamo de un libro a un usuario. Registra la fecha de préstamo. Tiene relaciones `@ManyToOne` con `Libro` y `Usuario`.

Todas las relaciones fueron definidas de forma bidireccional con las anotaciones JPA correspondientes (`@OneToMany`, `@ManyToOne`), incluyendo:
- Cascade para eliminación en cascada.
- `orphanRemoval` para limpieza automática de registros huérfanos.
- Restricciones de nulidad (`nullable = false`) en campos obligatorios.
- Constraint de unicidad en el email del usuario.

### 2. Capa de repositorios

Se implementaron cuatro repositorios usando el patrón **Spring Data JPA**, todos extendiendo `JpaRepository<Entidad, Integer>`:

- `AutorRepository`
- `LibroRepository`
- `UsuarioRepository`
- `PrestamoRepository`

Esto provee operaciones CRUD completas de forma automática sin necesidad de escribir SQL manualmente.

### 3. Esquemas de base de datos

Se crearon dos archivos SQL con la definición de tablas:

- `sql/init.sql` — Esquema para **MySQL**.
- `sql/postgres.sql` — Esquema alternativo para **PostgreSQL**.

Ambos incluyen las definiciones de tablas, claves primarias, claves foráneas y restricciones de integridad referencial.

### 4. Configuración de la aplicación

- `application.properties` configurado para conexión a MySQL en localhost:3306.
- Hibernate con `ddl-auto: update` para facilitar el desarrollo iterativo.
- Activado el log de consultas SQL (`show-sql=true`) para observabilidad durante el desarrollo.

### 5. Infraestructura con Docker

Se incluyó un archivo `docker-compose.yml` que levanta un contenedor de **MySQL 8.4** con:
- Nombre de servicio: `springboot`
- Puerto expuesto: `3306`
- Volumen persistente: `db_data`
- Health check configurado para asegurar disponibilidad antes de conectar la app.

### 6. Uso de Archetype (herramienta auxiliar)

El archivo `arch.md` documenta el uso de **Archetype**, una herramienta de generación de código que crea entidades Java a partir de esquemas SQL (PostgreSQL). Esto facilita el scaffolding inicial del modelo de dominio a partir de la base de datos.

---

## Tareas planificadas

El archivo `Tasks.md` define los escenarios de uso que guían el desarrollo futuro:

- Registro de libros (con autores nuevos, existentes, múltiples o desconocidos)
- Registro de autores
- Registro de usuarios
- Registro de préstamos
- Consultas de libros por autor, año, título, disponibilidad
- Consultas de autores por nombre, libro o ranking de préstamos
- Consultas de usuarios y préstamos

---

## Cómo ejecutar el proyecto

### 1. Levantar la base de datos con Docker

```bash
docker-compose up -d
```

### 2. Ejecutar la aplicación

```bash
./mvnw spring-boot:run
```

> La aplicación estará disponible en `http://localhost:8080`

---

## Estructura del proyecto

```
biblioteca/
├── src/
│   └── main/
│       ├── java/cl/playground/biblioteca/
│       │   ├── BibliotecaApplication.java
│       │   ├── model/
│       │   │   ├── Autor.java
│       │   │   ├── Libro.java
│       │   │   ├── Prestamo.java
│       │   │   └── Usuario.java
│       │   └── repository/
│       │       ├── AutorRepository.java
│       │       ├── LibroRepository.java
│       │       ├── PrestamoRepository.java
│       │       └── UsuarioRepository.java
│       └── resources/
│           └── application.properties
├── sql/
│   ├── init.sql
│   └── postgres.sql
├── docker-compose.yml
├── Tasks.md
├── arch.md
└── pom.xml
```

---

## Contexto educativo

Este proyecto es una actividad de aprendizaje orientada a practicar:

- Diseño de modelos relacionales y su mapeo a entidades JPA.
- Uso del patrón Repository con Spring Data.
- Configuración de Spring Boot con base de datos relacional.
- Manejo de relaciones entre entidades (uno a muchos, muchos a uno).
- Uso de Docker para gestionar la infraestructura de base de datos en desarrollo.