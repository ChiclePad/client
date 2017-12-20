INSERT INTO chiclepad_user (id, email, password)
VALUES (1, 'dijkstra@delivery.com', '$2a$10$1Of/twqCGmMH9T93sXWJDOUAa1HGdnvM.EipaT31r3.Ha2iEXyG.u');
INSERT INTO chiclepad_user_details (user_id, name, locale) VALUES (1, 'Alan Turing', 'cs-CZ');

INSERT INTO entry (id, user_id, created) VALUES ();

INSERT INTO todo (id, entry_id, description, deadline, soft_deadline, priority) VALUES ();

INSERT INTO category (id, user_id, name, icon, color) VALUES
  (1, 1, 'School', 'BUILDING', '#FF8A65'),
  (2, 1, 'Work', 'MONEY', '#CE93D8'),
  (3, 1, 'Shopping', 'SHOPPING_CART', '#80CBC4');
