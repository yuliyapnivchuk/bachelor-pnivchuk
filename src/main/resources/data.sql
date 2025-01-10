INSERT INTO event (name)
VALUES ('test')
ON CONFLICT DO NOTHING;

INSERT INTO users ("name",email) VALUES
('user1','user1@gmail.com'),
('user2','user2@gmail.com'),
('user3','user3@gmail.com')
ON CONFLICT DO NOTHING;

--
--INSERT INTO expense (event_id,payer,created_by,summary,total_amount,subtotal_amount,currency,split_type,transaction_date,transaction_time,category,status) VALUES
--	 (1,'user3','user2',NULL,16.7,15.6,'INR','byItem','2010-05-16','09:20:02',NULL,'DRAFT'),
--	 (1,'user1',NULL,'квитки в кіно',1000,950,'UAH','=',NULL,'15:03:00',NULL,'DRAFT'),
--	 (1,'user3',NULL,'Обід в ресторані',2086,2086,'UAH','byItem',NULL,'15:03:00',NULL,'DRAFT')
--	 ON CONFLICT DO NOTHING;
--
--INSERT INTO item (expense_id,description,price,quantity,total_price,split_type) VALUES
--	 (1,'TEA HALIA',1.3,1,1.3,'='),
--	 (1,'SUNNY SIDE UP E',0.5,1,0.5,'='),
--	 (1,'ROJAK',5.5,1,5.5,'='),
--	 (1,'TEA C HALIA',1.4,NULL,1.4,'='),
--	 (1,'PLAIN PRATA',1.1,2,2.2,'='),
--	 (1,'FRIED MEE CHICK',4.7,1,4.7,'%'),
--	 (3,'Американо з молоком',65,3,195,'='),
--	 (3,'Вареники з картоплею по духу',120,NULL,120,'='),
--	 (3,'Голубці',125,1,125,'='),
--	 (3,'Кава американо',50,1,50,'='),
--	 (3,'Капучино',85,2,170,'shares'),
--	 (3,'Кріль в сметані',268,2,536,'='),
--	 (3,'Пляцки з білими грибами',215,NULL,215,'='),
--	 (3,'Реберця',210,1,210,'='),
--	 (3,'Росіл курячий',115,2,230,'='),
--	 (3,'Сік апельсиновий',50,0,50,'='),
--	 (3,'Супер тертий пляцок з мясом',185,1,185,'=')
--	 ON CONFLICT DO NOTHING;
--
--INSERT INTO item_share (user_id,item_id,value) VALUES
--	 (1,1,NULL),
--	 (1,3,NULL),
--	 (1,4,NULL),
--	 (1,5,NULL),
--	 (1,6,80),
--	 (2,6,20),
--	 (2,7,NULL),
--	 (2,8,NULL),
--	 (2,9,NULL),
--	 (2,10,NULL),
--	 (2,11,1),
--	 (1,11,1),
--	 (2,12,NULL),
--	 (2,13,NULL),
--	 (2,14,NULL),
--	 (2,15,NULL),
--	 (2,16,NULL),
--	 (1,17,NULL)
--	 ON CONFLICT DO NOTHING;
--
--INSERT INTO expense_share (user_id,expense_id,value) VALUES
--	 (1,2,NULL),
--	 (2,2,NULL),
--	 (3,2,NULL)
--	 ON CONFLICT DO NOTHING;
