INSERT INTO chiclepad_user (email, password)
VALUES ('dijkstra@delivery.com', '$2a$10$1Of/twqCGmMH9T93sXWJDOUAa1HGdnvM.EipaT31r3.Ha2iEXyG.u');
INSERT INTO chiclepad_user_details (user_id, name, locale) VALUES (1, 'Alan Turing', 'cs-CZ');

INSERT INTO entry (user_id, created) VALUES (1, NOW()), (1, NOW()), (1, NOW()), (1, NOW()), (1, NOW()), (1, NOW()),
  (1, NOW()), (1, NOW()), (1, NOW()), (1, NOW()), (1, NOW()), (1, NOW()), (1, NOW()), (1, NOW()), (1, NOW()),
  (1, NOW()), (1, NOW()), (1, NOW()), (1, NOW()), (1, NOW()), (1, NOW()), (1, NOW()), (1, NOW());

INSERT INTO goal (entry_id, description) VALUES (1, 'Exercise every day'), (2, 'Code nicely'),
  (3, 'Code nicer'), (4, 'Eat less');

INSERT INTO completed_goal (goal_id, completed_day, completed_time) VALUES (1, NOW() - INTERVAL '1' DAY, NOW()),
  (1, NOW() - INTERVAL '2' DAY, NOW()), (1, NOW() - INTERVAL '4' DAY, NOW()), (1, NOW() - INTERVAL '5' DAY, NOW())
  , (1, NOW() - INTERVAL '6' DAY, NOW()), (1, NOW() - INTERVAL '7' DAY, NOW()), (1, NOW() - INTERVAL '8' DAY, NOW()),
  (2, NOW() - INTERVAL '1' DAY, NOW()), (2, NOW() - INTERVAL '4' DAY, NOW()), (2, NOW() - INTERVAL '5' DAY, NOW()),
  (2, NOW() - INTERVAL '6' DAY, NOW()), (2, NOW() - INTERVAL '10' DAY, NOW()), (2, NOW() - INTERVAL '11' DAY, NOW()),
  (4, NOW() - INTERVAL '1' DAY, NOW()), (4, NOW() - INTERVAL '2' DAY, NOW());

INSERT INTO note (entry_id, content) VALUES (5, 'Dorob diplomku'), (6, 'Dorob projekt');
INSERT INTO note (entry_id, content, reminder_time) VALUES (7, 'Kup mlieko', NOW() + INTERVAL '10' DAY);

INSERT INTO todo (entry_id, description, deadline, priority)
VALUES (8, 'Start own bussiness', NOW() + INTERVAL '10' DAY, 1),
  (9, 'Finish school', NOW() + INTERVAL '4' DAY, -2),
  (10, 'Hide bodies', NOW() - INTERVAL '1' DAY, 1);
INSERT INTO todo (entry_id, description, deadline, soft_deadline, priority)
VALUES (11, 'Buy gifts', NOW(), NOW() - INTERVAL '3' DAY, -15);

INSERT INTO diary_page (entry_id, text, recorded_day) VALUES (12,
                                                              'Today I read that: Harry Potter is a series of fantasy novels written by British author J. K. Rowling. The novels chronicle the life of a young wizard, Harry Potter, and his friends Hermione Granger and Ron Weasley, all of whom are students at Hogwarts School of Witchcraft and Wizardry. The main story arc concerns Harry''s struggle against Lord Voldemort, a dark wizard who intends to become immortal, overthrow the wizard governing body known as the Ministry of Magic, and subjugate all wizards and muggles, a reference term that means non-magical people.',
                                                              NOW() - INTERVAL '4' DAY),
  (13,
   'Dear diary: Lorem ipsum dolor sit amet, consectetur adipiscing elit. Etiam egestas consequat sem eu rhoncus. Aenean lorem sem, vulputate et dui et, imperdiet tempor mauris. Vivamus eget libero ac mauris luctus venenatis. Ut scelerisque risus in erat pulvinar consectetur. Suspendisse porta nunc et leo mollis, at placerat dui vulputate. ',
   NOW() - INTERVAL '5' DAY);
INSERT INTO diary_page (entry_id, text, recorded_day) VALUES (14, 'test', NOW() - INTERVAL '10' DAY),
  (15, 'test2', NOW() - INTERVAL '11' DAY),
  (16, 'test3', NOW() - INTERVAL '20' DAY),
  (17, 'test4', NOW() - INTERVAL '23' DAY),
  (18, 'test5', NOW() - INTERVAL '24' DAY),
  (19, 'test6', NOW() - INTERVAL '25' DAY),
  (20, 'test7', NOW() - INTERVAL '26' DAY),
  (21, 'test8', NOW() - INTERVAL '40' DAY),
  (22, 'test9', NOW() - INTERVAL '41' DAY),
  (23, 'test10', NOW() - INTERVAL '42' DAY);

INSERT INTO category (user_id, name, icon, color) VALUES
  (1, 'School', 'BUILDING', '#FF8A65'),
  (1, 'Work', 'MONEY', '#CE93D8'),
  (1, 'Shopping', 'SHOPPING_CART', '#80CBC4');

INSERT INTO registered_category (entry_id, category_id) VALUES (2, 1);
INSERT INTO registered_category (entry_id, category_id) VALUES (7, 3);
INSERT INTO registered_category (entry_id, category_id) VALUES (12, 1), (13, 2);
INSERT INTO registered_category (entry_id, category_id) VALUES (10, 1);
