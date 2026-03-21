CREATE TABLE autores(
    id_autor SERIAL PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    apellido VARCHAR(100) NOT NULL
);

CREATE TABLE libros(
    id_libro SERIAL PRIMARY KEY,
    titulo VARCHAR(100) NOT NULL,
    id_autor INT NOT NULL,
    anio_publicacion SMALLINT,
    CONSTRAINT fk_libro_autor
        FOREIGN KEY (id_autor) REFERENCES autores(id_autor)
);

CREATE TABLE usuarios(
    id_usuario SERIAL PRIMARY KEY,
    nombre VARCHAR(100),
    apellido VARCHAR(100),
    correo VARCHAR(100) UNIQUE NOT NULL
);

CREATE TABLE prestamos(
    id_prestamo SERIAL PRIMARY KEY,
    id_libro INT NOT NULL,
    fecha_prestamo DATE NOT NULL,
    id_usuario INT NOT NULL,
    CONSTRAINT fk_prestamo_libro
        FOREIGN KEY (id_libro) REFERENCES libros(id_libro),
    CONSTRAINT fk_prestamo_usuario
        FOREIGN KEY (id_usuario) REFERENCES usuarios(id_usuario)
);