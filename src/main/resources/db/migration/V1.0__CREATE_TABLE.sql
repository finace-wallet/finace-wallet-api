CREATE TABLE app_user(
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    password VARCHAR NOT NULL ,
    email VARCHAR NOT NULL,
    is_delete BOOLEAN DEFAULT false,
    is_active BOOLEAN DEFAULT false
);

CREATE TABLE role(
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    role_type VARCHAR NOT NULL,
    is_delete BOOLEAN DEFAULT false
);

CREATE TABLE user_roles(
    user_id BIGINT NOT NULL,
    role_id BIGINT NOT NULL,
    PRIMARY KEY (user_id,role_id),
    FOREIGN KEY (user_id) REFERENCES app_user(id),
    FOREIGN KEY (role_id) REFERENCES role(id)
);

CREATE TABLE