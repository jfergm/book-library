CREATE TABLE book_copies (
  id BIGSERIAL PRIMARY KEY,
  book_id BIGINT,
  shelf_id BIGINT,
  code VARCHAR(120) NOT NULL,
  status VARCHAR(50) NOT NULL,
  CONSTRAINT fk_book
    FOREIGN KEY (book_id) 
    REFERENCES books(id),
  CONSTRAINT fk_shelf
    FOREIGN KEY (shelf_id) 
    REFERENCES shelves(id)
);
