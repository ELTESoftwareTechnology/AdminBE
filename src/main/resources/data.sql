INSERT INTO roles (id, role_type) VALUES (1, 'ADMIN') ON CONFLICT DO NOTHING;
INSERT INTO roles (id, role_type) VALUES (2, 'USER') ON CONFLICT DO NOTHING;