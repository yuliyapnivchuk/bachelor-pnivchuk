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
    FOREIGN KEY (event_id) REFERENCES event(id) ON DELETE CASCADE,
    FOREIGN KEY (payer) REFERENCES users(name) ON DELETE CASCADE,
    FOREIGN KEY (created_by) REFERENCES users(name) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS item (
    id SERIAL PRIMARY KEY,
    expense_id INT,
    description VARCHAR(300),
    price NUMERIC,
    quantity INT,
    total_price NUMERIC,
    split_type VARCHAR(20),
    FOREIGN KEY (expense_id) REFERENCES expense(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS image (
    id SERIAL PRIMARY KEY,
    image BYTEA,
    expense_id INT,
    note_id INT,
    FOREIGN KEY (expense_id) REFERENCES expense(id) ON DELETE CASCADE,
    FOREIGN KEY (note_id) REFERENCES notes(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS audio (
    id SERIAL PRIMARY KEY,
    audio BYTEA,
    audio_transcript TEXT,
    expense_id INT,
    note_id INT,
    FOREIGN KEY (expense_id) REFERENCES expense(id) ON DELETE CASCADE,
    FOREIGN KEY (note_id) REFERENCES notes(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS expense_share (
    id SERIAL PRIMARY KEY,
    user_id INT NOT NULL,
    expense_id INT NOT NULL,
    value NUMERIC,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (expense_id) REFERENCES expense(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS item_share (
    id SERIAL PRIMARY KEY,
    user_id INT NOT NULL,
    item_id INT NOT NULL,
    value NUMERIC,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (item_id) REFERENCES item(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS notes (
    id SERIAL PRIMARY KEY,
    expense_id INT NOT NULL,
    created_by VARCHAR(100),
    note_text VARCHAR,
    FOREIGN KEY (expense_id) REFERENCES expense(id) ON DELETE CASCADE
);