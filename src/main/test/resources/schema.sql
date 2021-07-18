CREATE TABLE IF NOT EXISTS app_refresh_tokens (
    id BIGINT NOT NULL AUTO_INCREMENT,
    token_id VARCHAR(255) NOT NULL UNIQUE,
    refresh_token TEXT NOT NULL,
    PRIMARY KEY (id)
);