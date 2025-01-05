INSERT INTO event (name)
VALUES ('test')
ON CONFLICT DO NOTHING;

INSERT INTO users ("name",email) VALUES
('user1','user1@gmail.com'),
('user2','user2@gmail.com')
ON CONFLICT DO NOTHING;
--
--INSERT INTO expense (event_id,payer,created_by,summary,total_amount,subtotal_amount,currency,split_type,transaction_date,transaction_time,category,status)
--VALUES ((SELECT id FROM event LIMIT 1),NULL,(SELECT name FROM users LIMIT 1),'Обід в ресторані',2086,2086,'UAH',NULL,'2024-05-16','15:03:00',NULL,'DRAFT')
--ON CONFLICT DO NOTHING;
--
--INSERT INTO item (expense_id,description,price,quantity,total_price) VALUES
--	 ((SELECT id FROM expense WHERE summary = 'Обід в ресторані' LIMIT 1),'Американо з молоком',65,3,195),
--	 ((SELECT id FROM expense WHERE summary = 'Обід в ресторані' LIMIT 1),'Вареники з картоплею по духу',120,NULL,120),
--	 ((SELECT id FROM expense WHERE summary = 'Обід в ресторані' LIMIT 1),'Голубці',125,1,125),
--	 ((SELECT id FROM expense WHERE summary = 'Обід в ресторані' LIMIT 1),'Кава американо',50,1,50),
--	 ((SELECT id FROM expense WHERE summary = 'Обід в ресторані' LIMIT 1),'Капучино',85,2,170),
--	 ((SELECT id FROM expense WHERE summary = 'Обід в ресторані' LIMIT 1),'Кріль в сметані',268,2,536),
--	 ((SELECT id FROM expense WHERE summary = 'Обід в ресторані' LIMIT 1),'Пляцки з білими грибами',215,NULL,215),
--	 ((SELECT id FROM expense WHERE summary = 'Обід в ресторані' LIMIT 1),'Реберця',210,1,210),
--	 ((SELECT id FROM expense WHERE summary = 'Обід в ресторані' LIMIT 1),'Росіл курячий',115,2,230),
--	 ((SELECT id FROM expense WHERE summary = 'Обід в ресторані' LIMIT 1),'Сік апельсиновий',50,0,50),
--	 ((SELECT id FROM expense WHERE summary = 'Обід в ресторані' LIMIT 1),'Супер тертий пляцок з мясом',185,1,185)
--ON CONFLICT DO NOTHING;