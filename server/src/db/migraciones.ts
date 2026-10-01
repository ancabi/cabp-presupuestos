/**
 * Migraciones SQL en orden. Cada una se aplica una sola vez y queda registrada
 * en la tabla `migraciones`. No modificar una migración ya publicada: añadir otra.
 */
export const migraciones: { nombre: string; sql: string[] }[] = [
  {
    nombre: '001_inicial',
    sql: [
      `CREATE TABLE usuarios (
        id INT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
        email VARCHAR(255) NOT NULL UNIQUE,
        nombre VARCHAR(100) NOT NULL,
        password_hash VARCHAR(255) NOT NULL,
        rol ENUM('admin','usuario') NOT NULL DEFAULT 'usuario',
        activo TINYINT(1) NOT NULL DEFAULT 1,
        creado_en DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP
      ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci`,
      `CREATE TABLE sesiones (
        token_hash CHAR(64) PRIMARY KEY,
        usuario_id INT UNSIGNED NOT NULL,
        expira_en DATETIME NOT NULL,
        creado_en DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
        INDEX (expira_en),
        FOREIGN KEY (usuario_id) REFERENCES usuarios(id) ON DELETE CASCADE
      ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4`,
      `CREATE TABLE ajustes (
        clave VARCHAR(100) PRIMARY KEY,
        valor JSON NOT NULL
      ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4`,
      `CREATE TABLE clientes (
        id INT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
        dni VARCHAR(20) NOT NULL DEFAULT '',
        nombre VARCHAR(100) NOT NULL,
        apellidos VARCHAR(150) NOT NULL DEFAULT '',
        direccion VARCHAR(255) NOT NULL DEFAULT '',
        ciudad VARCHAR(100) NOT NULL DEFAULT '',
        provincia VARCHAR(100) NOT NULL DEFAULT '',
        empresa VARCHAR(150) NOT NULL DEFAULT '',
        notas TEXT NOT NULL,
        creado_en DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
        actualizado_en DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
        INDEX (dni), INDEX (nombre), INDEX (apellidos)
      ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci`,
      `CREATE TABLE cliente_telefonos (
        id INT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
        cliente_id INT UNSIGNED NOT NULL,
        telefono VARCHAR(30) NOT NULL,
        UNIQUE (cliente_id, telefono),
        FOREIGN KEY (cliente_id) REFERENCES clientes(id) ON DELETE CASCADE
      ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4`,
      `CREATE TABLE cliente_emails (
        id INT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
        cliente_id INT UNSIGNED NOT NULL,
        email VARCHAR(255) NOT NULL,
        UNIQUE (cliente_id, email),
        FOREIGN KEY (cliente_id) REFERENCES clientes(id) ON DELETE CASCADE
      ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4`,
      `CREATE TABLE adjuntos (
        id INT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
        cliente_id INT UNSIGNED NOT NULL,
        tipo ENUM('imagen','pdf') NOT NULL,
        nombre_original VARCHAR(255) NOT NULL,
        fichero VARCHAR(255) NOT NULL,
        mime VARCHAR(100) NOT NULL,
        tamano INT UNSIGNED NOT NULL,
        subido_en DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
        FOREIGN KEY (cliente_id) REFERENCES clientes(id) ON DELETE CASCADE
      ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci`,
      `CREATE TABLE distribuidores (
        id INT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
        nombre VARCHAR(150) NOT NULL,
        direccion VARCHAR(255) NOT NULL DEFAULT '',
        email VARCHAR(255) NOT NULL DEFAULT '',
        telefono VARCHAR(30) NOT NULL DEFAULT '',
        ciudad VARCHAR(100) NOT NULL DEFAULT '',
        provincia VARCHAR(100) NOT NULL DEFAULT '',
        pais VARCHAR(100) NOT NULL DEFAULT '',
        numero_cta VARCHAR(50) NOT NULL DEFAULT '',
        iban VARCHAR(50) NOT NULL DEFAULT '',
        swift VARCHAR(20) NOT NULL DEFAULT ''
      ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci`,
      `CREATE TABLE productos (
        id INT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
        distribuidor_id INT UNSIGNED NOT NULL,
        nombre VARCHAR(255) NOT NULL,
        precio DECIMAL(12,2) NOT NULL DEFAULT 0,
        activo TINYINT(1) NOT NULL DEFAULT 1,
        FOREIGN KEY (distribuidor_id) REFERENCES distribuidores(id) ON DELETE CASCADE
      ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci`,
      `CREATE TABLE contadores (
        tipo ENUM('presupuesto','factura') NOT NULL,
        anio SMALLINT UNSIGNED NOT NULL,
        ultimo_numero INT UNSIGNED NOT NULL,
        PRIMARY KEY (tipo, anio)
      ) ENGINE=InnoDB`,
      `CREATE TABLE documentos (
        id INT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
        tipo ENUM('presupuesto','factura') NOT NULL,
        anio SMALLINT UNSIGNED NOT NULL,
        numero INT UNSIGNED NOT NULL,
        fecha DATE NOT NULL,
        cliente_id INT UNSIGNED NOT NULL,
        distribuidor_id INT UNSIGNED NULL,
        presupuesto_origen_id INT UNSIGNED NULL UNIQUE,
        ganancia DECIMAL(12,2) NOT NULL DEFAULT 0,
        restaurante DECIMAL(12,2) NOT NULL DEFAULT 0,
        pasaje DECIMAL(12,2) NOT NULL DEFAULT 0,
        combustible DECIMAL(12,2) NOT NULL DEFAULT 0,
        otros DECIMAL(12,2) NOT NULL DEFAULT 0,
        hotel DECIMAL(12,2) NOT NULL DEFAULT 0,
        transporte DECIMAL(12,2) NOT NULL DEFAULT 0,
        kilometros DECIMAL(10,2) NOT NULL DEFAULT 0,
        num_viajes INT UNSIGNED NOT NULL DEFAULT 2,
        precio_gasolina DECIMAL(8,3) NOT NULL DEFAULT 0,
        aplica_ganancia TINYINT(1) NOT NULL DEFAULT 1,
        aplica_iva TINYINT(1) NOT NULL DEFAULT 1,
        iva_porcentaje DECIMAL(5,2) NOT NULL,
        total_manual_activo TINYINT(1) NOT NULL DEFAULT 0,
        total_manual DECIMAL(12,2) NOT NULL DEFAULT 0,
        porcentaje_reparto TINYINT UNSIGNED NOT NULL DEFAULT 50,
        total_sin_iva DECIMAL(12,2) NOT NULL DEFAULT 0,
        total_iva DECIMAL(12,2) NOT NULL DEFAULT 0,
        total_con_iva DECIMAL(12,2) NOT NULL DEFAULT 0,
        texto_concepto TEXT NOT NULL,
        texto_forma_pago TEXT NOT NULL,
        texto_explicativo TEXT NOT NULL,
        calc_tipo ENUM('pitagoras','stepper') NOT NULL DEFAULT 'pitagoras',
        valor_a DOUBLE NOT NULL DEFAULT 0,
        valor_b DOUBLE NOT NULL DEFAULT 0,
        valor_c DOUBLE NOT NULL DEFAULT 0,
        valor_aux DOUBLE NOT NULL DEFAULT 0,
        creado_por INT UNSIGNED NULL,
        creado_en DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
        actualizado_en DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
        UNIQUE KEY uq_numero (tipo, anio, numero),
        INDEX (anio, tipo),
        FOREIGN KEY (cliente_id) REFERENCES clientes(id) ON DELETE RESTRICT,
        FOREIGN KEY (distribuidor_id) REFERENCES distribuidores(id) ON DELETE SET NULL,
        FOREIGN KEY (presupuesto_origen_id) REFERENCES documentos(id) ON DELETE SET NULL,
        FOREIGN KEY (creado_por) REFERENCES usuarios(id) ON DELETE SET NULL
      ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci`,
      `CREATE TABLE documento_lineas (
        id INT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
        documento_id INT UNSIGNED NOT NULL,
        orden INT UNSIGNED NOT NULL,
        producto_id INT UNSIGNED NULL,
        nombre_producto VARCHAR(255) NOT NULL,
        cantidad DECIMAL(12,2) NOT NULL,
        precio DECIMAL(12,2) NOT NULL,
        FOREIGN KEY (documento_id) REFERENCES documentos(id) ON DELETE CASCADE,
        FOREIGN KEY (producto_id) REFERENCES productos(id) ON DELETE SET NULL
      ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci`,
    ],
  },
  {
    nombre: '002_cliente_codigo_postal',
    sql: [
      `ALTER TABLE clientes
         ADD COLUMN codigo_postal VARCHAR(10) NOT NULL DEFAULT '' AFTER direccion,
         ADD INDEX (codigo_postal)`,
    ],
  },
];
