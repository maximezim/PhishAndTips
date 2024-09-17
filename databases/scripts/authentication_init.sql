CREATE DATABASE authentication_db;
CREATE DATABASE phishing_db;

-- Switch to authentication_db and create tables
\connect authentication_db;

CREATE TABLE IF NOT EXISTS user_data
(
    id            SERIAL PRIMARY KEY,
    first_name    VARCHAR NOT NULL,
    last_name     VARCHAR NOT NULL,
    email         VARCHAR NOT NULL,
    password_hash VARCHAR NOT NULL,
    role          VARCHAR NOT NULL
);

\connect phishing_db;

CREATE TABLE IF NOT EXISTS phishing_campaign
(
    id            SERIAL PRIMARY KEY,
    campaign_name VARCHAR NOT NULL,
    created_at    TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS phishing_email
(
    id              SERIAL PRIMARY KEY,
    campaign_id     INTEGER REFERENCES phishing_campaign (id),
    recipient_email VARCHAR NOT NULL,
    sent_at         TIMESTAMP,
    opened_at       TIMESTAMP,
    clicked_at      TIMESTAMP
);
