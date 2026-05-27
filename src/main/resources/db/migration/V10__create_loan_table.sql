CREATE TABLE loans (
  id BIGSERIAL PRIMARY KEY,
  user_id BIGINT,
  book_copy_id BIGINT,
  loan_date TIMESTAMP NOT NULL,
  due_date TIMESTAMP NOT NULL,
  return_date TIMESTAMP,
  status VARCHAR(100),
  notes VARCHAR(255) NOT NULL,
  CONSTRAINT fk_user
    FOREIGN KEY (user_id) 
    REFERENCES users(id),
  CONSTRAINT fk_book_copy
    FOREIGN KEY (book_copy_id) 
    REFERENCES book_copies(id)
);
