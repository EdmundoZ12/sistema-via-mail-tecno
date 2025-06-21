-- =====================================================
-- SISTEMA PARA CAPACITACIÓN UNIDAD CICIT
-- Schema con orden correcto de dependencias
-- =====================================================

-- =====================================================
-- 1. TABLA: tipo_participante (Sin dependencias)
-- =====================================================
CREATE TABLE tipo_participante
(
    id          SERIAL PRIMARY KEY,
    activo      BOOLEAN      NOT NULL DEFAULT true,
    codigo      VARCHAR(10)  NOT NULL UNIQUE,
    descripcion TEXT,
    nombre      VARCHAR(100) NOT NULL UNIQUE
);

-- =====================================================
-- 2. TABLA: usuario (Sin dependencias externas)
-- =====================================================
CREATE TABLE usuario
(
    id       SERIAL PRIMARY KEY,
    activo   BOOLEAN      NOT NULL DEFAULT true,
    apellido VARCHAR(100) NOT NULL,
    email    VARCHAR(255) NOT NULL UNIQUE,
    registro VARCHAR(50)  NOT NULL UNIQUE,
    telefono VARCHAR(20),
    nombre   VARCHAR(100) NOT NULL,
    carnet   VARCHAR(20)  NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    rol      VARCHAR(30)  NOT NULL CHECK (rol IN ('RESPONSABLE', 'ADMINISTRATIVO', 'TUTOR'))
);

-- =====================================================
-- 3. TABLA: participante (Depende de tipo_participante)
-- =====================================================
CREATE TABLE participante
(
    id                   SERIAL PRIMARY KEY,
    apellido             VARCHAR(100) NOT NULL,
    carnet               VARCHAR(20)  NOT NULL,
    registro             VARCHAR(50),
    carrera              VARCHAR(100),
    email                VARCHAR(255),
    facultad             VARCHAR(100),
    nombre               VARCHAR(100) NOT NULL,
    telefono             VARCHAR(20),
    universidad          VARCHAR(255),
    tipo_participante_id INTEGER      NOT NULL,
    FOREIGN KEY (tipo_participante_id) REFERENCES tipo_participante (id)
        ON UPDATE CASCADE ON DELETE RESTRICT
);

-- =====================================================
-- 4. TABLA: gestion (Sin dependencias)
-- =====================================================
CREATE TABLE gestion
(
    id           SERIAL PRIMARY KEY,
    nombre       VARCHAR(100),
    fecha_inicio DATE,
    fecha_fin    DATE,
    estado       BOOLEAN NOT NULL DEFAULT true,
    descripcion  TEXT
);

-- =====================================================
-- 5. TABLA: curso (Sin dependencias)
-- =====================================================
CREATE TABLE curso
(
    id             SERIAL PRIMARY KEY,
    nombre         VARCHAR(200),
    descripcion    TEXT,
    logo_url       VARCHAR(500),
    duracion_horas INTEGER,
    modalidad      VARCHAR(20),
    nivel          VARCHAR(20),
    requisitos     TEXT,
    activo         BOOLEAN NOT NULL DEFAULT true
);

-- =====================================================
-- 6. TABLA: precio_curso (Depende de curso y tipo_participante)
-- =====================================================
CREATE TABLE precio_curso
(
    id                   SERIAL PRIMARY KEY,
    curso_id             INTEGER        NOT NULL,
    tipo_participante_id INTEGER        NOT NULL,
    precio               DECIMAL(10, 2) NOT NULL,
    activo               BOOLEAN        NOT NULL DEFAULT true,
    FOREIGN KEY (curso_id) REFERENCES curso (id)
        ON UPDATE CASCADE ON DELETE RESTRICT,
    FOREIGN KEY (tipo_participante_id) REFERENCES tipo_participante (id)
        ON UPDATE CASCADE ON DELETE RESTRICT
);

-- =====================================================
-- 7. TABLA: curso_gestion (Depende de curso, gestion y usuario)
-- =====================================================
CREATE TABLE curso_gestion
(
    id             SERIAL PRIMARY KEY,
    curso_id       INTEGER NOT NULL,
    gestion_id     INTEGER NOT NULL,
    tutor_id       INTEGER NOT NULL,
    cupos_totales  INTEGER NOT NULL,
    cupos_ocupados INTEGER NOT NULL DEFAULT 0,
    aula           VARCHAR(50),
    estado         BOOLEAN NOT NULL DEFAULT true,
    FOREIGN KEY (curso_id) REFERENCES curso (id)
        ON UPDATE CASCADE ON DELETE RESTRICT,
    FOREIGN KEY (gestion_id) REFERENCES gestion (id)
        ON UPDATE CASCADE ON DELETE RESTRICT,
    FOREIGN KEY (tutor_id) REFERENCES usuario (id)
        ON UPDATE CASCADE ON DELETE RESTRICT
);

-- =====================================================
-- 8. TABLA: cronograma_curso (Depende de curso_gestion)
-- =====================================================
CREATE TABLE cronograma_curso
(
    id               SERIAL PRIMARY KEY,
    curso_gestion_id INTEGER NOT NULL,
    fase             VARCHAR(50),
    fecha_inicio     DATE,
    fecha_fin        DATE,
    activo           BOOLEAN NOT NULL DEFAULT true,
    descripcion      TEXT,
    FOREIGN KEY (curso_gestion_id) REFERENCES curso_gestion (id)
        ON UPDATE CASCADE ON DELETE CASCADE
);

