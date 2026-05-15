CREATE TABLE bookcases (
  id BIGSERIAL PRIMARY KEY,
  section_id BIGINT,
  code VARCHAR(120) NOT NULL,
  label VARCHAR(255) NOT NULL,
  CONSTRAINT fk_section 
    FOREIGN KEY (section_id) 
    REFERENCES sections(id)
);
