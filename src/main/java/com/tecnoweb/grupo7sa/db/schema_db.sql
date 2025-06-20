-- =====================================================
-- 1. TABLA: tipo_participante
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
-- 2. TABLA: usuario (usuarios del sistema)
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
-- 3. TABLA: participante (estudiantes)
-- =====================================================
CREATE TABLE participante
(
    id                   SERIAL PRIMARY KEY,
    apellido             VARCHAR(100) NOT NULL,
    carnet               VARCHAR(20)  NOT NULL UNIQUE,
    carrera              VARCHAR(100),
    email                VARCHAR(255) NOT NULL UNIQUE,
    facultad             VARCHAR(100),
    nombre               VARCHAR(100) NOT NULL,
    telefono             VARCHAR(20),
    universidad          VARCHAR(255),
    tipo_participante_id INTEGER      NOT NULL,
    FOREIGN KEY (tipo_participante_id) REFERENCES tipo_participante (id)
);