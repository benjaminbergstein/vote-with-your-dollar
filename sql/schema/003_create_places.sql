CREATE TABLE places (
    id SERIAL PRIMARY KEY,
    osm_id VARCHAR(40),
    uuid VARCHAR(200),
    name VARCHAR(200)
);

CREATE UNIQUE INDEX id_idx ON places (id);
CREATE UNIQUE INDEX osm_id_idx ON places (osm_id);
CREATE UNIQUE INDEX uuid_idx ON places (uuid);
