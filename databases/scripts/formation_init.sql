CREATE TABLE IF NOT EXISTS formation(
    id SERIAL PRIMARY KEY,
    name VARCHAR NOT NULL,
    description VARCHAR NOT NULL,
)

CREATE TABLE IF NOT EXISTS quizz(
    id SERIAL PRIMARY KEY,
    formation_id INTEGER NOT NULL,
    json VARCHAR NOT NULL,
    FOREIGN KEY (formation_id) REFERENCES formation(id)
)

CREATE TABLE IF NOT EXISTS video(
    id SERIAL PRIMARY KEY,
    formation_id INTEGER NOT NULL,
    title VARCHAR NOT NULL,
    description VARCHAR NOT NULL,
    url VARCHAR NOT NULL,
    duration INTEGER NOT NULL,
    quiz_id INTEGER,
    FOREIGN KEY (formation_id) REFERENCES formation(id),
    FOREIGN KEY (quiz_id) REFERENCES quizz(id)
)