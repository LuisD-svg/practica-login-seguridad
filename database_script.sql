CREATE DATABASE IF NOT EXISTS practica_seguridad CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE practica_seguridad;

DROP TABLE IF EXISTS usuarios;

CREATE TABLE usuarios (
    id_usuario INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password_hash VARCHAR(128) NOT NULL,
    rol ENUM('ADMINISTRADOR', 'EMPLEADO') NOT NULL DEFAULT 'EMPLEADO',
    first_name VARCHAR(60) NOT NULL,
    last_name VARCHAR(60) NOT NULL,
    email VARCHAR(100) NOT NULL,
    telefono VARCHAR(15) NOT NULL,
    fecha_nacimiento DATE NOT NULL,
    direccion VARCHAR(150) NOT NULL,
    user_status VARCHAR(20) NOT NULL DEFAULT 'Active',
    puesto VARCHAR(50) NOT NULL,
    curp_cifrado VARBINARY(255) NOT NULL,
    nss_cifrado VARBINARY(255) NOT NULL,
    salario_cifrado VARBINARY(255) NOT NULL,
    created_date DATETIME DEFAULT CURRENT_TIMESTAMP
);

-- Inserción de usuarios iniciales (Clave AES: 'MiClaveSecretaAES2026')
INSERT INTO usuarios (
    username, password_hash, rol, first_name, last_name, email,
    telefono, fecha_nacimiento, direccion, user_status, puesto,
    curp_cifrado, nss_cifrado, salario_cifrado
) VALUES (
    'admin',
    SHA2('AdminPass2026!', 256),
    'ADMINISTRADOR',
    'Carlos', 'Hernández', 'admin@empresa.com',
    '4451002030', '1988-06-21', 'Av. Reforma #405, Centro', 'Active', 'Gerente General',
    AES_ENCRYPT('HECC880621HGT09', 'MiClaveSecretaAES2026'),
    AES_ENCRYPT('12988845123', 'MiClaveSecretaAES2026'),
    AES_ENCRYPT('38500.00', 'MiClaveSecretaAES2026')
);

INSERT INTO usuarios (
    username, password_hash, rol, first_name, last_name, email,
    telefono, fecha_nacimiento, direccion, user_status, puesto,
    curp_cifrado, nss_cifrado, salario_cifrado
) VALUES (
    'empleado1',
    SHA2('EmpPass2026!', 256),
    'EMPLEADO',
    'Mariana', 'López', 'mariana.lopez@empresa.com',
    '4459876543', '1995-11-14', 'Calle Roble #12, Valle Dorado', 'Active', 'Operador de Mostrador',
    AES_ENCRYPT('LOMM951114MGT03', 'MiClaveSecretaAES2026'),
    AES_ENCRYPT('45159512349', 'MiClaveSecretaAES2026'),
    AES_ENCRYPT('12500.00', 'MiClaveSecretaAES2026')
);

INSERT INTO usuarios (
    username, password_hash, rol, first_name, last_name, email,
    telefono, fecha_nacimiento, direccion, user_status, puesto,
    curp_cifrado, nss_cifrado, salario_cifrado
) VALUES (
    'empleado2',
    SHA2('Emp2Pass2026!', 256),
    'EMPLEADO',
    'Jorge', 'Ramírez', 'jorge.ramirez@empresa.com',
    '4453321144', '2000-03-02', 'Privada Sauces #7', 'Active', 'Auxiliar de Lavandería',
    AES_ENCRYPT('RAZJ000302HGT01', 'MiClaveSecretaAES2026'),
    AES_ENCRYPT('78200045612', 'MiClaveSecretaAES2026'),
    AES_ENCRYPT('9800.00', 'MiClaveSecretaAES2026')
);

SELECT id_usuario, username, rol, first_name, last_name 
FROM usuarios 
WHERE username = 'admin' AND password_hash = SHA2('AdminPass2026!', 256);

SELECT 
    id_usuario, username, rol, first_name, last_name, email,
    CAST(AES_DECRYPT(curp_cifrado, 'MiClaveSecretaAES2026') AS CHAR) AS curp,
    CAST(AES_DECRYPT(nss_cifrado, 'MiClaveSecretaAES2026') AS CHAR) AS nss,
    CAST(AES_DECRYPT(salario_cifrado, 'MiClaveSecretaAES2026') AS CHAR) AS salario
FROM usuarios;

SELECT 
    id_usuario, username, rol, first_name, last_name, email,
    '********' AS curp,
    '********' AS nss,
    '********' AS salario
FROM usuarios
WHERE rol = 'EMPLEADO';