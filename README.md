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
| Thymeleaf           | -        | Motor de plantillas HTML (vistas MVC)    |
| Bootstrap           | 5.3.2    | Framework CSS para estilos y layouts     |
| Anime.js            | 3.2.1    | Librería de animaciones JavaScript       |
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

### 7. Capa de DTOs (Data Transfer Objects)

Se introdujo el patrón **DTO** para desacoplar las entidades JPA de la capa de presentación. Un DTO es una clase plana que transporta sólo los datos que cada operación necesita, sin exponer la entidad completa ni sus relaciones Hibernate.

#### ¿Por qué usar DTOs y no las entidades directamente?

| Problema con entidades directas | Solución con DTOs |
|---|---|
| Se exponen campos internos (IDs generados, relaciones lazy) | El DTO expone únicamente lo que la vista necesita |
| Riesgo de disparar consultas N+1 al serializar relaciones | El DTO aplana los datos, sin navegar relaciones |
| Cambios en la entidad rompen el contrato con la vista | La entidad y el DTO evolucionan de forma independiente |
| El formulario puede intentar modificar campos que no debería | El DTO de creación sólo acepta los campos del formulario |

#### DTOs implementados

| DTO | Dirección | Propósito |
|---|---|---|
| `CreateUsuarioDTO` | Formulario → Servicio | Recibe nombre, apellido y correo para crear un usuario |
| `ListUsuarioDTO` | Servicio → Vista | Expone id, nombre, apellido y correo para el listado |
| `CreateLibroDTO` | Formulario → Servicio | Recibe título, año y un `CreateAutorDTO` anidado |
| `ListLibroDTO` | Servicio → Vista | Expone id, título, año y el nombre completo del autor como `String` |
| `CreateAutorDTO` | Anidado en `CreateLibroDTO` | Transporta nombre y apellido del autor al crear un libro |
| `ListAutorDTO` | Servicio → Vista | Expone id, nombre y apellido para el listado de autores |
| `CreateAutorConLibroDTO` | Formulario → Servicio | Recibe nombre, apellido y una lista opcional de `CreateLibroSinAutorDTO` |
| `CreateLibroSinAutorDTO` | Anidado en `CreateAutorConLibroDTO` | Transporta título y año de publicación de un libro al crear un autor |
| `CreatePrestamoDTO` | Formulario → Servicio | Recibe `idLibro` y, según el modo, `idUsuario` (existente) o nombre/apellido/correo (nuevo) |
| `ListPrestamosDTO` | Servicio → Vista | Expone id del préstamo, nombre completo del usuario, título del libro y fecha |

> **Nota sobre anidamiento:** `CreateLibroDTO` contiene un `CreateAutorDTO` como campo. Esto modela la relación del formulario: al crear un libro siempre se especifica un autor, ya sea existente o nuevo. Spring MVC enlaza los campos anidados usando notación de punto (`autor.nombre`, `autor.apellido`).

> **Nota sobre colecciones en DTOs:** `CreateAutorConLibroDTO` usa `List<CreateLibroSinAutorDTO>` (no `Set`) porque Spring MVC requiere una colección indexable para hacer binding desde inputs HTML con notación `libros[0].titulo`, `libros[1].titulo`, etc. Un `Set` no tiene índice definido y no puede recibir este tipo de binding.

> **Nota sobre DTO de préstamo con modo dual:** `CreatePrestamoDTO` tiene dos rutas posibles. Si `idUsuario` no es nulo, el servicio busca al usuario por ID (modo existente). Si es nulo, utiliza nombre, apellido y correo para crear un usuario nuevo en la misma transacción. Este diseño permite que un solo DTO y un solo endpoint soporten ambos flujos sin necesidad de dos formularios o dos rutas distintas.

### 8. Capa de servicios

Se implementó la **capa de servicio** separando la lógica de negocio de los controladores y repositorios.

#### Estructura

Cada servicio sigue el patrón **interfaz + implementación**:

```
LibroService (interfaz)
    └── LibroServiceImpl (implementación anotada con @Service)
```

Esto permite:
- Sustituir la implementación sin tocar el código que la usa (principio de inversión de dependencias).
- Testear el controlador mockeando la interfaz.

#### Lógica de negocio destacada en `LibroServiceImpl`

Al crear un libro, el servicio resuelve el autor de forma automática antes de persistir:

```java
if (autorRepository.existsByNombreAndApellido(nombre, apellido)) {
    autor = autorRepository.findByNombreAndApellido(nombre, apellido); // reutiliza
} else {
    autor = new Autor();  // crea uno nuevo
    autorRepository.save(autor);
}
```

El controlador no sabe si el autor era nuevo o existente — esa decisión pertenece al servicio.

#### Lógica de negocio destacada en `AutorServiceImpl`

Al crear un autor, los libros son opcionales. El servicio guarda primero el autor para obtener su ID generado y luego lo asocia a cada libro:

```java
Autor autorGuardado = AutorRepository.save(autor); // obtiene el ID generado

if (createAutorConLibroDTO.getLibros() != null && !createAutorConLibroDTO.getLibros().isEmpty()) {
    for (CreateLibroSinAutorDTO libro : createAutorConLibroDTO.getLibros()) {
        Libro nuevoLibro = new Libro();
        nuevoLibro.setTitulo(libro.getTitulo());
        nuevoLibro.setAnioPublicacion(libro.getAnioPublicacion());
        nuevoLibro.setIdAutor(autorGuardado); // FK al autor recién persistido
        libroRepository.save(nuevoLibro);
    }
}
```

El autor se persiste en un paso separado antes de los libros porque necesita tener un ID asignado para actuar como clave foránea.

El servicio también expone dos variantes de listado:
- `obtenerAutores()` — retorna `List<ListAutorDTO>`, usada por el desplegable de `LibroController`.
- `obtenerAutoresPaginado(page, size)` — retorna `Page<ListAutorDTO>`, usada por la vista de autores.

#### Lógica de negocio destacada en `PrestamoServiceImpl`

Al registrar un préstamo, el servicio resuelve el usuario antes de persistir. El campo `idUsuario` en el DTO actúa como discriminador:

```java
private Usuario crearUsuarioPrestamo(CreatePrestamoDTO dto) {
    if (dto.getIdUsuario() != null) {
        // Modo existente: busca por ID
        return usuarioRepository.findById(dto.getIdUsuario())
            .orElseThrow(() -> new IllegalArgumentException("El usuario no existe"));
    }
    // Modo nuevo: valida correo único y persiste
    if (usuarioRepository.existsByCorreo(dto.getCorreo())) {
        throw new IllegalArgumentException("El correo ya está registrado");
    }
    Usuario usuario = new Usuario();
    usuario.setNombre(dto.getNombre());
    usuario.setApellido(dto.getApellido());
    usuario.setCorreo(dto.getCorreo());
    return usuarioRepository.save(usuario);
}
```

El controlador no distingue entre ambos modos; la decisión es exclusiva del servicio.

#### Métodos de listado sin paginación para desplegables

`LibroService` y `UsuarioService` exponen métodos que retornan `List<T>` (sin paginar) para poblar los `<select>` de los formularios:

```java
List<ListLibroDTO>   obtenerTodosLosLibros();    // LibroService
List<ListUsuarioDTO> obtenerTodosLosUsuarios();  // UsuarioService
```

Estos métodos conviven con sus variantes paginadas (`obtenerLibros`, `listarUsuarios`) que usan `Page<T>` para las tablas de listado. La distinción es intencionada: un dropdown necesita todos los registros a la vez, mientras que una tabla pagina para no saturar la vista.

#### Validaciones en el servicio

Las validaciones de negocio (campos obligatorios, correo duplicado) se lanzaron como `IllegalArgumentException` dentro del servicio. El controlador las captura y las redirige a la vista como mensajes de error:

```java
// En el servicio
if (usuarioRepository.existsByEmail(correo)) {
    throw new IllegalArgumentException("El correo ya está registrado.");
}

// En el controlador
try {
    usuarioService.crearUsuario(dto);
} catch (IllegalArgumentException e) {
    redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
}
```

### 9. Capa de controladores (Spring MVC)

Se implementaron cuatro controladores siguiendo el patrón **MVC** de Spring:

#### `UsuarioController` — mapeado en `"/"`

| Método | Ruta | Acción |
|---|---|---|
| `@GetMapping` | `/` | Carga la lista paginada de usuarios y retorna la vista `index` |
| `@PostMapping` | `/` | Recibe el formulario, llama al servicio y redirige |

#### `LibroController` — mapeado en `"/libros"`

| Método | Ruta | Acción |
|---|---|---|
| `@GetMapping` | `/libros` | Carga libros paginados + lista de autores, retorna vista `libros` |
| `@PostMapping` | `/libros` | Recibe el formulario de libro, crea y redirige |

#### `AutorController` — mapeado en `"/autores"`

| Método | Ruta | Acción |
|---|---|---|
| `@GetMapping` | `/autores` | Carga autores paginados y retorna la vista `autores` |
| `@PostMapping` | `/autores` | Recibe el formulario de autor (con o sin libros), crea y redirige |

#### `PrestamoController` — mapeado en `"/prestamos"`

| Método | Ruta | Acción |
|---|---|---|
| `@GetMapping` | `/prestamos` | Carga préstamos paginados + todos los libros y usuarios para los dropdowns, retorna vista `prestamos` |
| `@PostMapping` | `/prestamos` | Recibe el formulario de préstamo (usuario existente o nuevo), crea y redirige |

El `GET` carga simultáneamente tres colecciones en el modelo: la página de préstamos y las listas completas de libros y usuarios. Estas últimas poblán los `<select>` del formulario y no requieren paginación.

#### Conceptos clave aplicados

**`@ModelAttribute` a nivel de método:**
```java
@ModelAttribute("createUsuarioDTO")
public CreateUsuarioDTO createUsuarioDTO() {
    return new CreateUsuarioDTO();
}
```
Spring invoca este método antes de cualquier handler del controlador, garantizando que el objeto de respaldo del formulario (`th:object`) siempre esté disponible en el modelo, incluso si el handler lanza una excepción.

**Paginación con `Page<T>` de Spring Data:**
```java
Page<ListUsuarioDTO> usuarios = usuarioService.listarUsuarios(page, size);
model.addAttribute("usuarios", usuarios);
model.addAttribute("totalPages", usuarios.getTotalPages());
```
`Page<T>` encapsula la lista de resultados junto con metadatos de paginación (total de elementos, total de páginas, página actual), sin necesidad de consultas adicionales.

**Patrón PRG (Post-Redirect-Get):**

Los controladores nunca retornan una vista directamente después de un POST. En su lugar redirigen con `redirect:/ruta`. Esto evita que el navegador reenvíe el formulario al refrescar la página.

```
POST /prestamos  →  procesa  →  redirect:/prestamos  →  GET /prestamos  →  vista
```

Los mensajes de éxito/error se transmiten entre el POST y el GET usando **Flash Attributes**, que sobreviven a la redirección y se eliminan automáticamente después de ser leídos:

```java
redirectAttributes.addFlashAttribute("successMessage", "Préstamo registrado exitosamente.");
return "redirect:/prestamos";
```

### 10. Capa de vistas (Thymeleaf + Bootstrap + Anime.js)

Se implementaron cuatro vistas HTML bajo `src/main/resources/templates/`:

| Archivo | Ruta | Descripción |
|---|---|---|
| `index.html` | `/` | Listado de usuarios con formulario de creación |
| `libros.html` | `/libros` | Listado de libros con formulario de creación |
| `autores.html` | `/autores` | Listado de autores con formulario de creación (con o sin libros) |
| `prestamos.html` | `/prestamos` | Listado de préstamos con formulario de creación (usuario existente o nuevo) |

#### Enlace formulario ↔ controlador en Thymeleaf

`th:object` y `th:field` enlazan el formulario HTML con el DTO del modelo:

```html
<form th:action="@{/}" th:object="${createUsuarioDTO}" method="post">
    <input type="text" th:field="*{nombre}">
    <!-- genera: name="nombre", id="nombre", value="" -->
</form>
```

Para objetos anidados, Thymeleaf usa notación de punto:
```html
<input type="hidden" th:field="*{autor.nombre}">
<!-- genera: name="autor.nombre" — Spring MVC lo deserializa al campo anidado -->
```

#### Selector de autor existente con `th:attr`

Para pasar datos arbitrarios a atributos HTML `data-*` desde Thymeleaf se usa `th:attr`:

```html
<option th:each="autor : ${autores}"
        th:value="${autor.idAutor}"
        th:text="${autor.nombre + ' ' + autor.apellido}"
        th:attr="data-nombre=${autor.nombre},data-apellido=${autor.apellido}">
</option>
```

El JavaScript lee esos atributos para autocompletar los campos ocultos del formulario al seleccionar un autor.

#### Paginación en la vista

```html
<li th:each="i : ${#numbers.sequence(0, totalPages - 1)}"
    th:classappend="${i == currentPage} ? 'active'">
    <a th:href="@{/(page=${i}, size=10)}" th:text="${i + 1}">1</a>
</li>
```

`#numbers.sequence(inicio, fin)` es una utilidad de Thymeleaf que genera un rango de enteros, útil para construir controles de paginación dinámicos.

#### Sidebar con Anime.js

El sidebar colapsa (70px) y se expande (240px) animando el ancho del elemento con Anime.js. El fade de los labels se encadena usando el callback `complete`:

```javascript
anime({
    targets: sidebar,
    width: 240,
    duration: 280,
    easing: 'easeOutQuart',
    complete() {
        anime({ targets: '.nav-label', opacity: 1, duration: 160 });
    }
});
```

Los ítems del sidebar sin ruta implementada tienen clase `soon` (`pointer-events: none`, opacidad reducida) y un badge "Pronto" que aparece al expandir. A medida que se implementan nuevas vistas, el ítem correspondiente pasa de `.soon` a un enlace funcional con `.active` en la vista que le pertenece.

#### Formulario oculto con toggle animado

El formulario de creación tiene `display: none` por defecto y se muestra con una animación de entrada usando `easeOutBack` (efecto de rebote suave):

```javascript
function showForm() {
    formEl.style.display = 'block';
    anime({ targets: formEl, opacity: [0, 1], translateY: [-18, 0],
            duration: 380, easing: 'easeOutBack' });
}
```

Si el servidor devuelve un error de validación (flash attribute `errorMessage`), el formulario se abre automáticamente al cargar la página:

```javascript
const hasError = document.querySelector('.alert-danger');
if (hasError) showForm();
```

#### Toggle autor existente / autor nuevo (libros)

El formulario de libros ofrece dos modos para especificar el autor:

- **Autor existente**: muestra un `<select>` con autores de la BD. Al seleccionar, el JS copia `data-nombre` y `data-apellido` a los hidden inputs reales del formulario.
- **Autor nuevo**: muestra inputs de texto que sincronizan en tiempo real los mismos hidden inputs.

En ambos casos el servidor recibe `autor.nombre` y `autor.apellido`. La lógica de si es autor nuevo o existente reside enteramente en `LibroServiceImpl`.

#### Toggle usuario existente / usuario nuevo (préstamos)

El formulario de préstamos aplica el mismo patrón de toggle pills para el usuario:

- **Usuario existente**: muestra un `<select>` con todos los usuarios de la BD. Al seleccionar, el JS copia `data-nombre`, `data-apellido` y `data-correo` a los hidden inputs, y también escribe `idUsuario`.
- **Usuario nuevo**: muestra inputs de texto (nombre, apellido, correo). El JS sincroniza en tiempo real a los hidden inputs. El hidden `idUsuario` queda vacío, lo que el servicio interpreta como señal para crear un nuevo usuario.

```javascript
// Modo existente: escribe idUsuario + datos del usuario
hiddenIdUsuario.value = opt.value;
hiddenNombre.value    = opt.dataset.nombre;

// Modo nuevo: limpia idUsuario para que el servicio cree uno nuevo
hiddenIdUsuario.value = '';
```

Al cambiar de panel, Anime.js anima la transición (fade + colapso de altura con `easeInQuart` para ocultar, `easeOutQuart` para mostrar). La validación previa al submit verifica que los hidden fields tengan valor antes de dejar pasar el formulario; si no, ejecuta una animación de shake sobre los toggle pills.

#### Binding dinámico de colecciones indexadas (vista de autores)

El formulario de autores permite agregar cero o más libros de forma dinámica. Cada libro es una fila generada por JavaScript con inputs nombrados con índice explícito:

```javascript
function agregarLibro() {
    const idx = libroIndex++;
    row.innerHTML = `
        <input type="text"   name="libros[${idx}].titulo">
        <input type="number" name="libros[${idx}].anioPublicacion">
    `;
}
```

Spring MVC recibe los parámetros `libros[0].titulo`, `libros[1].titulo`, etc. y los deserializa automáticamente en `List<CreateLibroSinAutorDTO>` del DTO. Esto requiere que la colección en el DTO sea un `List` (indexable), no un `Set`.

Al eliminar una fila, la función `reindexarLibros()` recorre el DOM y reasigna los atributos `name` correlativamente, garantizando que los índices no queden con huecos (lo que impediría el binding):

```javascript
function reindexarLibros() {
    container.querySelectorAll('.libro-row').forEach((row, i) => {
        row.querySelector('input[type="text"]').name   = `libros[${i}].titulo`;
        row.querySelector('input[type="number"]').name = `libros[${i}].anioPublicacion`;
    });
}
```

---

## Tareas planificadas

El archivo `Tasks.md` define los escenarios de uso que guían el desarrollo futuro:

- ✅ Registro de usuarios
- ✅ Registro de libros (con autor nuevo o existente)
- ✅ Registro de autores (con o sin libros asociados)
- ✅ Registro de préstamos (con usuario existente o nuevo)
- ⬜ Consultas de libros por autor, año, título, disponibilidad
- ⬜ Consultas de autores por nombre, libro o ranking de préstamos
- ⬜ Consultas de usuarios y préstamos

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
│       │   ├── controller/
│       │   │   ├── UsuarioController.java      ← GET / y POST /
│       │   │   ├── LibroController.java        ← GET /libros y POST /libros
│       │   │   ├── AutorController.java        ← GET /autores y POST /autores
│       │   │   └── PrestamoController.java     ← GET /prestamos y POST /prestamos
│       │   ├── dto/
│       │   │   ├── CreateUsuarioDTO.java
│       │   │   ├── ListUsuarioDTO.java
│       │   │   ├── CreateLibroDTO.java
│       │   │   ├── ListLibroDTO.java
│       │   │   ├── CreateAutorDTO.java
│       │   │   ├── ListAutorDTO.java
│       │   │   ├── CreateAutorConLibroDTO.java
│       │   │   ├── CreateLibroSinAutorDTO.java
│       │   │   ├── CreatePrestamoDTO.java
│       │   │   └── ListPrestamosDTO.java
│       │   ├── model/
│       │   │   ├── Autor.java
│       │   │   ├── Libro.java
│       │   │   ├── Prestamo.java
│       │   │   └── Usuario.java
│       │   ├── repository/
│       │   │   ├── AutorRepository.java
│       │   │   ├── LibroRepository.java
│       │   │   ├── PrestamoRepository.java
│       │   │   └── UsuarioRepository.java
│       │   └── service/
│       │       ├── AutorService.java
│       │       ├── LibroService.java
│       │       ├── PrestamoService.java
│       │       ├── UsuarioService.java
│       │       └── impl/
│       │           ├── AutorServiceImpl.java
│       │           ├── LibroServiceImpl.java
│       │           ├── PrestamoServiceImpl.java
│       │           └── UsuarioServiceImpl.java
│       └── resources/
│           ├── application.properties
│           └── templates/
│               ├── index.html                  ← Vista de usuarios (/)
│               ├── libros.html                 ← Vista de libros (/libros)
│               ├── autores.html                ← Vista de autores (/autores)
│               └── prestamos.html              ← Vista de préstamos (/prestamos)
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

**Capa de datos:**
- Diseño de modelos relacionales y su mapeo a entidades JPA.
- Uso del patrón Repository con Spring Data.
- Manejo de relaciones entre entidades (`@OneToMany`, `@ManyToOne`).
- Uso de Docker para gestionar la infraestructura de base de datos en desarrollo.

**Capa de negocio:**
- Separación de responsabilidades con el patrón Service (interfaz + implementación).
- Uso de DTOs para desacoplar la presentación de las entidades JPA.
- Validaciones de negocio con excepciones controladas.
- Lógica de upsert (crear o reutilizar) a nivel de servicio.
- DTOs con modo dual: un mismo DTO soporta dos flujos (usuario existente vs. nuevo) según qué campos estén presentes.
- Distinción entre métodos de listado paginado (`Page<T>`) para tablas y sin paginar (`List<T>`) para desplegables.

**Capa web:**
- Patrón MVC con Spring Web y Thymeleaf.
- Patrón PRG (Post-Redirect-Get) con Flash Attributes.
- Paginación con `Page<T>` de Spring Data.
- `@ModelAttribute` para garantizar disponibilidad del DTO en el modelo.
- Binding de formularios con objetos anidados (`th:object`, `th:field`, `th:attr`).

**Frontend:**
- Layouts responsivos con Bootstrap 5.
- Animaciones declarativas con Anime.js (sidebar, formularios, alertas, transiciones de panel).
- Sincronización de campos de formulario mediante JavaScript vanilla.
- Binding dinámico de colecciones con inputs indexados (`name="libros[i].campo"`) y re-indexado del DOM al eliminar filas.
- Toggle pills animados para alternar entre modos de formulario (existente / nuevo).
- Validación client-side con animación de shake antes de enviar el formulario al servidor.