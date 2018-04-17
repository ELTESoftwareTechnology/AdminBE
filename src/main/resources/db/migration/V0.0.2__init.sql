CREATE TABLE roles(
  id BIGSERIAL PRIMARY KEY,
  role_type text NOT NULL
);

CREATE TABLE users (
  id    BIGSERIAL PRIMARY KEY,
  email text UNIQUE NOT NULL,
  first_name text NOT NULL,
  last_name text NOT NULL,
  password text NOT NULL,
  username text UNIQUE NOT NULL,
  role_id BIGINT NOT NULL REFERENCES roles(id)
);

CREATE SEQUENCE hibernate_sequence;

INSERT INTO roles (id, role_type) VALUES (1, 'ADMIN')ON CONFLICT DO NOTHING ;
INSERT INTO roles (id, role_type) VALUES (2, 'USER')ON CONFLICT DO NOTHING ;
INSERT INTO roles (id, role_type) VALUES (3, 'DOCTOR')ON CONFLICT DO NOTHING ;
INSERT INTO roles (id, role_type) VALUES (4, 'RESEARCHER')ON CONFLICT DO NOTHING ;