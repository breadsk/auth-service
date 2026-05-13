-- Insertar admin (si no existe)
INSERT INTO users (username, password)
SELECT * FROM (SELECT 'admin', '$2a$10$N9qo8uLOickgx2ZMRZoMy.Mr/.bN4Yk5ZqZJkM3ZqZJkM3ZqZJkM') AS tmp
WHERE NOT EXISTS (SELECT 1 FROM users WHERE username = 'admin');

-- Insertar user1 (si no existe)
INSERT INTO users (username, password)
SELECT * FROM (SELECT 'user1', '$2a$10$CwTycUXWue0Thq9StjUM0uJpz4Q7q5q5q5q5q5q5q5q5q5q5q5q5') AS tmp
WHERE NOT EXISTS (SELECT 1 FROM users WHERE username = 'user1');

-- Roles para admin
INSERT INTO user_roles (user_id, role)
SELECT id, 'ROLE_ADMIN' FROM users WHERE username = 'admin'
ON DUPLICATE KEY UPDATE role = role;

INSERT INTO user_roles (user_id, role)
SELECT id, 'ROLE_USER' FROM users WHERE username = 'admin'
ON DUPLICATE KEY UPDATE role = role;

-- Roles para user1
INSERT INTO user_roles (user_id, role)
SELECT id, 'ROLE_USER' FROM users WHERE username = 'user1'
ON DUPLICATE KEY UPDATE role = role;