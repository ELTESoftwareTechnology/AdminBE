CREATE TABLE chunk_datas(
  id BIGSERIAL PRIMARY KEY,
  encrypted_data text NOT NULL
);

CREATE TABLE chunk_infos (
  id    BIGSERIAL PRIMARY KEY,
  chunk_data_id BIGINT NOT NULL REFERENCES chunk_datas(id),
  from_user_id BIGINT NOT NULL REFERENCES users(id),
  to_user_id BIGINT NOT NULL REFERENCES users(id),
  created_at TIMESTAMP NOT NULL,
  expiration TIMESTAMP,
  comment TEXT
);
