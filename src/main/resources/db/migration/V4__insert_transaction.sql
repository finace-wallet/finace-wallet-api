INSERT INTO transaction_category_default (name, type, is_delete)
VALUES
    ('Food&Drink', 'EXPENSE', FALSE),
    ('Shopping', 'EXPENSE', FALSE),
    ('Home', 'EXPENSE', FALSE),
    ('Fees', 'EXPENSE', FALSE),
    ('Entertainment', 'EXPENSE', FALSE),
    ('Salary', 'INCOME', FALSE),
    ('Loan', 'INCOME', FALSE),
    ('Business', 'INCOME', FALSE),
    ('Insurance', 'INCOME', FALSE),
    ('Other', 'INCOME', FALSE),
    ('Transport', 'EXPENSE', FALSE);

INSERT INTO transaction_category (name, is_delete, wallet_id, type)
VALUES
    ('Food', FALSE, 1, 'Expense'),
    ('Salary', FALSE, 2, 'Income'),
    ('Transport', FALSE, 1, 'Expense');

INSERT INTO transaction (transaction_date, amount, description, is_delete, is_transfer, is_expense, wallet_id, user_id, transaction_category_id, currency)
VALUES
    ('2024-05-10', 50.0, 'Groceries shopping', FALSE, FALSE, TRUE, 1, 1, 1, 'USD'),
    ('2024-05-11', 1000.0, 'Received salary', FALSE, FALSE, FALSE, 2, 2, 2, 'USD'),
    ('2024-05-12', 30.0, 'Paid for transportation', FALSE, FALSE, TRUE, 1, 3, 3, 'USD');