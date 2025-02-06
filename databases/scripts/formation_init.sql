CREATE TABLE IF NOT EXISTS formation(
    id SERIAL PRIMARY KEY,
    name VARCHAR NOT NULL,
    description VARCHAR NOT NULL
);

CREATE TABLE IF NOT EXISTS quiz(
    id INTEGER PRIMARY KEY,
    json TEXT NOT NULL
);

CREATE TABLE IF NOT EXISTS video(
    id SERIAL PRIMARY KEY,
    title VARCHAR NOT NULL,
    description VARCHAR NOT NULL,
    video_url VARCHAR NOT NULL,
    caption_url VARCHAR NOT NULL,
    thumbnail_url VARCHAR NOT NULL
);

CREATE TABLE IF NOT EXISTS user_quiz_score(
    user_email VARCHAR NOT NULL,
    quiz_id INTEGER NOT NULL,
    score DOUBLE PRECISION NOT NULL,
    PRIMARY KEY (user_email, quiz_id),
    FOREIGN KEY (quiz_id) REFERENCES quiz(id)
);

CREATE TABLE IF NOT EXISTS badge(
    id INTEGER PRIMARY KEY,
    name VARCHAR UNIQUE NOT NULL,
    description VARCHAR NOT NULL,
    image_url VARCHAR NOT NULL
);

CREATE TABLE IF NOT EXISTS user_badge(
    user_email VARCHAR NOT NULL,
    badge_id INTEGER NOT NULL,
    PRIMARY KEY (user_email, badge_id),
    FOREIGN KEY (badge_id) REFERENCES badge(id)
);