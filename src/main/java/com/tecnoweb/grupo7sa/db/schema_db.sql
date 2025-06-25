-- =====================================================
-- CU1 - GESTIÓN DE USUARIOS
-- =====================================================
CREATE TABLE USUARIO
(
    id       SERIAL PRIMARY KEY,
    nombre   VARCHAR(100)        NOT NULL,
    apellido VARCHAR(100)        NOT NULL,
    carnet   VARCHAR(20)         NOT NULL,
    email    VARCHAR(255) UNIQUE NOT NULL,
    telefono VARCHAR(20),
    password VARCHAR(255)        NOT NULL,
    rol      VARCHAR(20)         NOT NULL CHECK (rol IN ('RESPONSABLE', 'ADMINISTRATIVO', 'TUTOR')),
    activo   BOOLEAN DEFAULT TRUE,
    registro VARCHAR(20)         NOT NULL
);
CREATE INDEX idx_usuario_rol ON USUARIO (rol);
CREATE INDEX idx_usuario_activo ON USUARIO (activo);
CREATE INDEX idx_usuario_email ON USUARIO (email);

CREATE TABLE TIPO_PARTICIPANTE
(
    id          SERIAL PRIMARY KEY,
    codigo      VARCHAR(10) UNIQUE NOT NULL,
    descripcion VARCHAR(255)       NOT NULL,
    activo      BOOLEAN DEFAULT TRUE
);
CREATE INDEX idx_tipo_participante_codigo ON TIPO_PARTICIPANTE (codigo);

CREATE TABLE PARTICIPANTE
(
    id                   SERIAL PRIMARY KEY,
    carnet               VARCHAR(20)  NOT NULL,
    nombre               VARCHAR(100) NOT NULL,
    apellido             VARCHAR(100) NOT NULL,
    email                VARCHAR(255),
    telefono             VARCHAR(20),
    universidad          VARCHAR(255),
    tipo_participante_id INTEGER      NOT NULL,
    activo               BOOLEAN DEFAULT TRUE,
    registro             VARCHAR(20),
    CONSTRAINT fk_participante_tipo FOREIGN KEY (tipo_participante_id)
        REFERENCES TIPO_PARTICIPANTE (id) ON DELETE RESTRICT
);
CREATE INDEX idx_participante_tipo ON PARTICIPANTE (tipo_participante_id);
CREATE INDEX idx_participante_email ON PARTICIPANTE (email);
CREATE INDEX idx_participante_carnet ON PARTICIPANTE (carnet);

-- =====================================================
-- CU2 - GESTIÓN DE GESTIONES Y CRONOGRAMA
-- =====================================================
CREATE TABLE GESTION
(
    id           SERIAL PRIMARY KEY,
    nombre       VARCHAR(100) UNIQUE NOT NULL,
    descripcion  TEXT,
    fecha_inicio DATE                NOT NULL,
    fecha_fin    DATE                NOT NULL,
    activo       BOOLEAN DEFAULT TRUE,
    CONSTRAINT chk_gestion_fechas CHECK (fecha_fin > fecha_inicio)
);
CREATE INDEX idx_gestion_fechas ON GESTION (fecha_inicio, fecha_fin);
CREATE INDEX idx_gestion_activo ON GESTION (activo);

-- =====================================================
-- CU3 - GESTIÓN DE CURSOS
-- =====================================================
CREATE TABLE CURSO
(
    id             SERIAL PRIMARY KEY,
    nombre         VARCHAR(200) NOT NULL,
    descripcion    TEXT,
    duracion_horas INTEGER      NOT NULL CHECK (duracion_horas > 0),
    nivel          VARCHAR(50),
    logo_url       VARCHAR(500),
    tutor_id       INTEGER      NOT NULL,
    gestion_id     INTEGER      NOT NULL,
    aula           VARCHAR(50),
    cupos_totales  INTEGER      NOT NULL CHECK (cupos_totales > 0),
    cupos_ocupados INTEGER DEFAULT 0 CHECK (cupos_ocupados >= 0),
    fecha_inicio   DATE         NOT NULL,
    fecha_fin      DATE         NOT NULL,
    activo         BOOLEAN DEFAULT TRUE,
    CONSTRAINT fk_curso_tutor FOREIGN KEY (tutor_id)
        REFERENCES USUARIO (id) ON DELETE RESTRICT,
    CONSTRAINT fk_curso_gestion FOREIGN KEY (gestion_id)
        REFERENCES GESTION (id) ON DELETE RESTRICT,
    CONSTRAINT chk_curso_cupos CHECK (cupos_ocupados <= cupos_totales),
    CONSTRAINT chk_curso_fechas CHECK (fecha_fin > fecha_inicio)
);
CREATE INDEX idx_curso_tutor ON CURSO (tutor_id);
CREATE INDEX idx_curso_gestion ON CURSO (gestion_id);
CREATE INDEX idx_curso_fechas ON CURSO (fecha_inicio, fecha_fin);
CREATE INDEX idx_curso_activo ON CURSO (activo);
CREATE INDEX idx_curso_cupos ON CURSO (cupos_totales, cupos_ocupados);

CREATE TABLE PRECIO_CURSO
(
    id                   SERIAL PRIMARY KEY,
    curso_id             INTEGER        NOT NULL,
    tipo_participante_id INTEGER        NOT NULL,
    precio               DECIMAL(10, 2) NOT NULL CHECK (precio > 0),
    activo               BOOLEAN DEFAULT TRUE,
    CONSTRAINT fk_precio_curso FOREIGN KEY (curso_id)
        REFERENCES CURSO (id) ON DELETE CASCADE,
    CONSTRAINT fk_precio_tipo FOREIGN KEY (tipo_participante_id)
        REFERENCES TIPO_PARTICIPANTE (id) ON DELETE RESTRICT,
    CONSTRAINT uk_precio_curso_tipo UNIQUE (curso_id, tipo_participante_id)
);
CREATE INDEX idx_precio_curso ON PRECIO_CURSO (curso_id);
CREATE INDEX idx_precio_tipo ON PRECIO_CURSO (tipo_participante_id);

-- =====================================================
-- CU4 - GESTIÓN DE INSCRIPCIONES
-- =====================================================
CREATE TABLE PREINSCRIPCION
(
    id                   SERIAL PRIMARY KEY,
    participante_id      INTEGER NOT NULL,
    curso_id             INTEGER NOT NULL,
    fecha_preinscripcion TIMESTAMP   DEFAULT NOW(),
    estado               VARCHAR(20) DEFAULT 'PENDIENTE' CHECK (estado IN ('PENDIENTE', 'APROBADA', 'RECHAZADA')),
    observaciones        TEXT,
    CONSTRAINT fk_preinscripcion_participante FOREIGN KEY (participante_id)
        REFERENCES PARTICIPANTE (id) ON DELETE RESTRICT,
    CONSTRAINT fk_preinscripcion_curso FOREIGN KEY (curso_id)
        REFERENCES CURSO (id) ON DELETE RESTRICT,
    CONSTRAINT uk_preinscripcion_participante_curso UNIQUE (participante_id, curso_id)
);
CREATE INDEX idx_preinscripcion_participante ON PREINSCRIPCION (participante_id);
CREATE INDEX idx_preinscripcion_curso ON PREINSCRIPCION (curso_id);
CREATE INDEX idx_preinscripcion_estado ON PREINSCRIPCION (estado);
CREATE INDEX idx_preinscripcion_fecha ON PREINSCRIPCION (fecha_preinscripcion);

CREATE TABLE INSCRIPCION
(
    id                SERIAL PRIMARY KEY,
    participante_id   INTEGER NOT NULL,
    curso_id          INTEGER NOT NULL,
    preinscripcion_id INTEGER NOT NULL,
    fecha_inscripcion TIMESTAMP   DEFAULT NOW(),
    nota_final        DECIMAL(4, 2) CHECK (nota_final >= 0 AND nota_final <= 100),
    estado            VARCHAR(20) DEFAULT 'INSCRITO' CHECK (estado IN ('INSCRITO', 'APROBADO', 'REPROBADO', 'RETIRADO')),
    observaciones     TEXT,
    CONSTRAINT fk_inscripcion_participante FOREIGN KEY (participante_id)
        REFERENCES PARTICIPANTE (id) ON DELETE RESTRICT,
    CONSTRAINT fk_inscripcion_curso FOREIGN KEY (curso_id)
        REFERENCES CURSO (id) ON DELETE RESTRICT,
    CONSTRAINT fk_inscripcion_preinscripcion FOREIGN KEY (preinscripcion_id)
        REFERENCES PREINSCRIPCION (id) ON DELETE RESTRICT,
    CONSTRAINT uk_inscripcion_participante_curso UNIQUE (participante_id, curso_id),
    CONSTRAINT uk_inscripcion_preinscripcion UNIQUE (preinscripcion_id)
);

-- =====================================================
-- CU5 - GESTIÓN DE CONTROL DE CURSOS
-- =====================================================
CREATE TABLE ASISTENCIA
(
    id             SERIAL PRIMARY KEY,
    inscripcion_id INTEGER     NOT NULL,
    fecha          DATE        NOT NULL,
    estado         VARCHAR(15) NOT NULL CHECK (estado IN ('PRESENTE', 'AUSENTE', 'JUSTIFICADO')),
    CONSTRAINT fk_asistencia_inscripcion FOREIGN KEY (inscripcion_id)
        REFERENCES INSCRIPCION (id) ON DELETE CASCADE,
    CONSTRAINT uk_asistencia UNIQUE (inscripcion_id, fecha)
);

CREATE TABLE TAREA
(
    id               SERIAL PRIMARY KEY,
    curso_id         INTEGER      NOT NULL,
    titulo           VARCHAR(100) NOT NULL,
    descripcion      TEXT,
    fecha_asignacion DATE         NOT NULL,
    CONSTRAINT fk_tarea_curso FOREIGN KEY (curso_id)
        REFERENCES CURSO (id) ON DELETE CASCADE
);

CREATE TABLE NOTA_TAREA
(
    id             SERIAL PRIMARY KEY,
    tarea_id       INTEGER       NOT NULL,
    inscripcion_id INTEGER       NOT NULL,
    nota           DECIMAL(5, 2) NOT NULL CHECK (nota >= 0 AND nota <= 100),
    CONSTRAINT fk_nota_tarea FOREIGN KEY (tarea_id)
        REFERENCES TAREA (id) ON DELETE CASCADE,
    CONSTRAINT fk_nota_inscripcion FOREIGN KEY (inscripcion_id)
        REFERENCES INSCRIPCION (id) ON DELETE CASCADE,
    CONSTRAINT uk_tarea_inscripcion UNIQUE (tarea_id, inscripcion_id)
);

-- =====================================================
-- CU6 - GESTIÓN DE PAGOS
-- =====================================================
CREATE TABLE PAGO
(
    id                SERIAL PRIMARY KEY,
    preinscripcion_id INTEGER            NOT NULL,
    fecha_pago        TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    monto             DECIMAL(10, 2)     NOT NULL CHECK (monto > 0),
    recibo            VARCHAR(50) UNIQUE NOT NULL,
    CONSTRAINT fk_pago_preinscripcion FOREIGN KEY (preinscripcion_id)
        REFERENCES PREINSCRIPCION (id) ON DELETE RESTRICT,
    CONSTRAINT uk_pago_preinscripcion UNIQUE (preinscripcion_id)
);

-- =====================================================
-- CU7 - GESTIÓN DE CERTIFICADOS
-- =====================================================
CREATE TABLE CERTIFICADO
(
    id                  SERIAL PRIMARY KEY,
    inscripcion_id      INTEGER             NOT NULL,
    tipo                VARCHAR(20)         NOT NULL CHECK (tipo IN ('PARTICIPACION', 'APROBACION', 'MENCION_HONOR')),
    codigo_verificacion VARCHAR(100) UNIQUE NOT NULL,
    fecha_emision       DATE DEFAULT CURRENT_DATE,
    url_pdf             VARCHAR(500),
    CONSTRAINT fk_certificado_inscripcion FOREIGN KEY (inscripcion_id)
        REFERENCES INSCRIPCION (id) ON DELETE RESTRICT
);
