CREATE TABLE IF NOT EXISTS users (
  id BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY NOT NULL,
  name VARCHAR(255) NOT NULL,
  email VARCHAR(512) NOT NULL,
  CONSTRAINT UQ_USER_EMAIL UNIQUE (email)
);

CREATE TABLE IF NOT EXISTS items (
  id BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY NOT NULL,
  name VARCHAR(255) NOT NULL,
  description VARCHAR(512) NOT NULL,
  owner_id BIGINT REFERENCES users(id) ON DELETE CASCADE,
  available BOOLEAN NOT NULL
);

CREATE TABLE IF NOT EXISTS bookings (
  id BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY NOT NULL,
  start_date TIMESTAMP WITHOUT TIME ZONE NOT NULL,
  end_date TIMESTAMP WITHOUT TIME ZONE NOT NULL,
  item_id BIGINT REFERENCES items(id) ON DELETE CASCADE,
  booker_id BIGINT REFERENCES users(id) ON DELETE CASCADE,
  status VARCHAR(10) NOT NULL
);

CREATE TABLE IF NOT EXISTS comments (
  id BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY NOT NULL,
  text VARCHAR(512) NOT NULL,
  created_time TIMESTAMP WITHOUT TIME ZONE NOT NULL,
  item_id BIGINT REFERENCES items(id) ON DELETE CASCADE,
  author_id BIGINT REFERENCES users(id) ON DELETE CASCADE
);
