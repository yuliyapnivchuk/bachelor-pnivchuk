CREATE TABLE IF NOT EXISTS users (
  id SERIAL PRIMARY KEY,
  name VARCHAR(100) NOT NULL UNIQUE,
  email VARCHAR(20) NOT NULL UNIQUE
);

CREATE TABLE IF NOT EXISTS event (
    id SERIAL PRIMARY KEY,
    name VARCHAR(100)
);

CREATE TABLE IF NOT EXISTS expense (
    id SERIAL PRIMARY KEY,
    event_id INT NOT NULL,
    payer VARCHAR(100),
    created_by VARCHAR(100),
    summary VARCHAR(150),
    total_amount NUMERIC,
    subtotal_amount NUMERIC,
    currency VARCHAR(10),
    split_type VARCHAR(20),
    transaction_date DATE,
    transaction_time TIME,
    category VARCHAR(100),
    status VARCHAR(20),
    image VARCHAR(100),
    FOREIGN KEY (event_id) REFERENCES event(id) ON DELETE CASCADE,
    FOREIGN KEY (payer) REFERENCES users(name) ON DELETE CASCADE,
    FOREIGN KEY (created_by) REFERENCES users(name) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS item (
    id SERIAL PRIMARY KEY,
    expense_id INT,
    description VARCHAR(300),
    price NUMERIC,
    quantity NUMERIC,
    total_price NUMERIC,
    split_type VARCHAR(20),
    FOREIGN KEY (expense_id) REFERENCES expense(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS notes (
    id SERIAL PRIMARY KEY,
    expense_id INT NOT NULL,
    created_by VARCHAR(100),
    note_text VARCHAR,
    FOREIGN KEY (expense_id) REFERENCES expense(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS split_details (
    id SERIAL PRIMARY KEY,
    user_name VARCHAR(100),
    expense_id INT,
    item_id INT,
    value NUMERIC,
    FOREIGN KEY (expense_id) REFERENCES expense(id) ON DELETE CASCADE,
    FOREIGN KEY (item_id) REFERENCES item(id) ON DELETE CASCADE
);