CREATE TABLE sections (
  id BIGSERIAL PRIMARY KEY,
  floor_id BIGINT,
  code VARCHAR(120) NOT NULL,
  label VARCHAR(255) NOT NULL,
  description TEXT,
  CONSTRAINT fk_floor 
    FOREIGN KEY (floor_id) 
    REFERENCES floors(id)
);
