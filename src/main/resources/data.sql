INSERT INTO users ("name",email) VALUES
('user1','user1@gmail.com'),
('user2','user2@gmail.com'),
('user3','user3@gmail.com')
ON CONFLICT DO NOTHING;
