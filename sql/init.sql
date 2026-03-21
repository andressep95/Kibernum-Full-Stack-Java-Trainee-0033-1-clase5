CREATE TABLE autores(
    id_autor INT AUTO_INCREMENT,
    nombre VARCHAR(100) NOT NULL,
    apellido VARCHAR(100) NOT NULL,
    PRIMARY KEY (id_autor)
);

CREATE TABLE libros(
    id_libro INT AUTO_INCREMENT,
    titulo VARCHAR(100) NOT NULL,
    id_autor INT NOT NULL,
    anio_publicacion YEAR,
    PRIMARY KEY (id_libro),
    FOREIGN KEY (id_autor) REFERENCES autores(id_autor)
);

CREATE TABLE usuarios(
    id_usuario INT AUTO_INCREMENT,
    nombre VARCHAR(100),
    apellido VARCHAR(100),
    correo VARCHAR(100) UNIQUE NOT NULL,
    PRIMARY KEY (id_usuario)
);

CREATE TABLE prestamos(
    id_prestamo INT AUTO_INCREMENT,
    id_libro INT NOT NULL,
    fecha_prestamo DATE NOT NULL,
    id_usuario INT NOT NULL,
    PRIMARY KEY (id_prestamo),
    FOREIGN KEY (id_libro) REFERENCES libros(id_libro),
    FOREIGN KEY (id_usuario) REFERENCES usuarios(id_usuario)
);