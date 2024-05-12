CREATE TABLE IF NOT EXISTS transaction_category_default (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    type VARCHAR(255) NOT NULL,
    is_delete BOOLEAN DEFAULT FALSE
    );

CREATE TABLE IF NOT EXISTS transaction_category (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    is_delete BOOLEAN DEFAULT FALSE,
    wallet_id BIGINT,
    type VARCHAR(255) NOT NULL,
    FOREIGN KEY (wallet_id) REFERENCES wallet(id)
    );

CREATE TABLE IF NOT EXISTS transaction (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    transaction_date DATE,
    amount DOUBLE NOT NULL,
    description VARCHAR(255),
    is_delete BOOLEAN DEFAULT FALSE,
    is_transfer BOOLEAN DEFAULT FALSE,
    is_expense BOOLEAN NOT NULL,
    wallet_id BIGINT,
    user_id BIGINT,
    transaction_category_id BIGINT,
    currency VARCHAR(3) NOT NULL,
    FOREIGN KEY (wallet_id) REFERENCES wallet(id),
    FOREIGN KEY (user_id) REFERENCES app_user(id),
    FOREIGN KEY (transaction_category_id) REFERENCES transaction_category(id)
    );
