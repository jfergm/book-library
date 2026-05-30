CREATE TABLE floors (
  id BIGSERIAL PRIMARY KEY,
  library_id BIGINT,
  code VARCHAR(120) NOT NULL,
  description TEXT,
  CONSTRAINT fk_library 
    FOREIGN KEY (library_id) 
    REFERENCES libraries(id)
);
