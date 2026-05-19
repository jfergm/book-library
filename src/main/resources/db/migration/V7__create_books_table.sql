CREATE TABLE books (
  id BIGSERIAL PRIMARY KEY,
  author_id BIGINT,
  title VARCHAR(120) NOT NULL,
  isbn VARCHAR(120) NOT NULL,
  CONSTRAINT fk_author 
    FOREIGN KEY (author_id) 
    REFERENCES authors(id)
);
