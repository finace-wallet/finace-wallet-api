INSERT INTO role (role_type, is_delete)
VALUES
    ('ADMIN', FALSE),
    ('USER', FALSE);

INSERT INTO app_user (password, email, is_delete, is_active)
VALUES
    ('$2a$12$O6sUrez7mEg3w8KBuMMc1OAnAsqiQLVbf1VpbA7H8Eap0L1xmpHXS', 'sn050920@gmail.com.com', FALSE, TRUE),
    ('$2a$12$N7OB5iwQ7dFlkFDwTonEwuakQst.7PKRMAR4spdqsbjtmD2vZ8fEG', 'jane.smith@example.com', FALSE, TRUE),
    ('$2a$12$owu6Ly6GmG.lef12ItyI2.5rzYrCY0FtnGhX1.Ba5ZOyJt3jpbIVK', 'mike.jackson@example.com', FALSE, TRUE);

INSERT INTO user_roles (user_id, role_id)
VALUES
    (1, 1),
    (2, 2),
    (3, 2);

INSERT INTO token_black_list (token, user_id)
VALUES
    (null, 1),
    (null, 2),
    (null, 3);

INSERT INTO ownership (name, is_delete)
VALUES
    ('OWNER', FALSE),
    ('VIEWER', FALSE),
    ('CO-OWNER', FALSE);

INSERT INTO wallet (name, amount, current_type, description, is_delete, is_active, initial_amount, spent_amount)
VALUES
    ('Main Wallet', 1000.0, 'USD', 'Primary wallet for daily expenses', FALSE, TRUE, 1000.0, 500.0),
    ('Savings', 5000.0, 'USD', 'Savings for future goals', FALSE, TRUE, 5000.0, 0.0),
    ('Travel Fund', 800.0, 'USD', 'Money saved for travel expenses', FALSE, TRUE, 800.0, 200.0);

INSERT INTO wallet_ownership (is_delete, user_id, wallet_id, ownership)
VALUES
    (FALSE, 1, 1, 1),
    (FALSE, 2, 2, 1),
    (FALSE, 3, 3, 2);

INSERT INTO app_user_setting (type, is_delete, user_id)
VALUES
    ('MONTH', FALSE, 1),
    ('YEAR', FALSE, 2),
    ('WEEK', FALSE, 3);

INSERT INTO profile (full_name, phone_number, address, image_url, birth_date, create_on, update_on, is_delete, user_id)
VALUES
    ('John Doe', '1234567890', '123 Main St, City, Country',null , '1990-01-01', CURRENT_DATE, CURRENT_DATE, FALSE, 1),
    ('Jane Smith', '0987654321', '456 Elm St, City, Country',null , '1985-05-15', CURRENT_DATE, CURRENT_DATE, FALSE, 2),
    ('Mike Jackson', '5555555555', '789 Oak St, City, Country',null , '1978-11-30', CURRENT_DATE, CURRENT_DATE, FALSE, 3);

