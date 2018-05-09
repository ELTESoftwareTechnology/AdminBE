CREATE TABLE notifications (
  id    BIGSERIAL PRIMARY KEY,
  from_user_id BIGINT NOT NULL REFERENCES users(id),
  created_at TIMESTAMP NOT NULL,
  content TEXT,
  email TEXT,
  type TEXT
);
