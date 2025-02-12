CREATE TABLE IF NOT EXISTS quiz(
    id SERIAL PRIMARY KEY,
    json TEXT NOT NULL
);

CREATE TABLE IF NOT EXISTS video(
    id SERIAL PRIMARY KEY,
    title VARCHAR NOT NULL,
    description VARCHAR NOT NULL,
    video_url VARCHAR NOT NULL,
    caption_url VARCHAR,
    thumbnail_url VARCHAR NOT NULL
);

CREATE TABLE IF NOT EXISTS user_quiz_score(
    user_email VARCHAR NOT NULL,
    quiz_id SERIAL NOT NULL,
    score DOUBLE PRECISION CHECK (score >= 0.0 AND score <= 1.0) NOT NULL,
    PRIMARY KEY (user_email, quiz_id),
    FOREIGN KEY (quiz_id) REFERENCES quiz(id)
);

CREATE TABLE IF NOT EXISTS user_video_watched(
    user_email VARCHAR NOT NULL,
    video_id SERIAL NOT NULL,
    is_watched BOOLEAN NOT NULL,
    PRIMARY KEY (user_email, video_id),
    FOREIGN KEY (video_id) REFERENCES video(id)
);

CREATE TABLE IF NOT EXISTS badge(
    id SERIAL PRIMARY KEY,
    name VARCHAR UNIQUE NOT NULL,
    description VARCHAR NOT NULL,
    image_url VARCHAR NOT NULL
);

CREATE TABLE IF NOT EXISTS user_badge(
    user_email VARCHAR NOT NULL,
    badge_id SERIAL NOT NULL,
    PRIMARY KEY (user_email, badge_id),
    FOREIGN KEY (badge_id) REFERENCES badge(id)
);