CREATE TABLE IF NOT EXISTS app_user (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    password VARCHAR(255) NOT NULL,
    email VARCHAR(255) UNIQUE NOT NULL,
    is_delete BOOLEAN DEFAULT FALSE,
    is_active BOOLEAN DEFAULT TRUE
    );

CREATE TABLE IF NOT EXISTS app_user_setting (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    type VARCHAR(255) DEFAULT 'MONTH',
    is_delete BOOLEAN DEFAULT FALSE,
    user_id BIGINT,
    FOREIGN KEY (user_id) REFERENCES app_user(id)
    );

CREATE TABLE IF NOT EXISTS ownership (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    is_delete BOOLEAN DEFAULT FALSE
    );

CREATE TABLE IF NOT EXISTS profile (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    full_name VARCHAR(255),
    phone_number VARCHAR(20),
    address VARCHAR(255),
    image_url VARCHAR(255),
    birth_date DATE,
    create_on DATE,
    update_on DATE,
    is_delete BOOLEAN DEFAULT FALSE,
    user_id BIGINT,
    FOREIGN KEY (user_id) REFERENCES app_user(id)
    );

CREATE TABLE IF NOT EXISTS role (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    role_type VARCHAR(255) UNIQUE NOT NULL,
    is_delete BOOLEAN DEFAULT FALSE
    );

CREATE TABLE IF NOT EXISTS user_roles (
    user_id BIGINT NOT NULL,
    role_id BIGINT NOT NULL,
    PRIMARY KEY (user_id, role_id),
    FOREIGN KEY (user_id) REFERENCES app_user(id),
    FOREIGN KEY (role_id) REFERENCES role(id)
    );

CREATE TABLE IF NOT EXISTS token_black_list (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    token VARCHAR(255),
    user_id BIGINT,
    FOREIGN KEY (user_id) REFERENCES app_user(id)
    );

CREATE TABLE IF NOT EXISTS wallet (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    amount DOUBLE,
    current_type VARCHAR(255),
    description VARCHAR(255),
    is_delete BOOLEAN DEFAULT FALSE,
    is_active BOOLEAN DEFAULT TRUE,
    initial_amount DOUBLE,
    spent_amount DOUBLE DEFAULT 0.0
    );

CREATE TABLE IF NOT EXISTS wallet_ownership (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    is_delete BOOLEAN DEFAULT FALSE,
    user_id BIGINT,
    wallet_id BIGINT,
    ownership BIGINT,
    FOREIGN KEY (user_id) REFERENCES app_user(id),
    FOREIGN KEY (wallet_id) REFERENCES wallet(id),
    FOREIGN KEY (ownership) REFERENCES ownership(id)
    );
