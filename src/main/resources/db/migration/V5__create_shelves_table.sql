CREATE TABLE shelves (
  id BIGSERIAL PRIMARY KEY,
  bookcase_id BIGINT,
  code VARCHAR(120) NOT NULL,
  label VARCHAR(255) NOT NULL,
  CONSTRAINT fk_bookcase
    FOREIGN KEY (bookcase_id) 
    REFERENCES bookcases(id)
);
