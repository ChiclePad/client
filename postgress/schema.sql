-- User authentification data
CREATE TABLE chiclepad_user (
  id SERIAL PRIMARY KEY,
  email VARCHAR(100) NOT NULL, -- unique will not do, as emails don't depend on casing
  password CHAR(24) NOT NULL CHECK (password != '')
);
CREATE UNIQUE INDEX email_id on chiclepad_user (lower(email));

-- Optional details about user
CREATE TABLE chiclepad_user_details (
  id SERIAL PRIMARY KEY,
  user_id INT REFERENCES chiclepad_user,
  name VARCHAR(100) NOT NULL,
  locale VARCHAR(50) NOT NULL
);

-- One 'organization object' added to the user's profile
CREATE TABLE entry (
  id SERIAL PRIMARY KEY,
  user_id INT REFERENCES chiclepad_user,
  created TIMESTAMP NOT NULL
);

-- Category for tagging entries
CREATE TABLE category (
  id SERIAL PRIMARY KEY,
  name VARCHAR(50) NOT NULL,
  icon VARCHAR(50) DEFAULT 'fa-circle-o',
  color CHAR(7) DEFAULT '#cccccc'
);

-- N : M table connecting catiegories to entries
CREATE TABLE registered_category
(
  entry_id INT REFERENCES entry,
  category_id INT REFERENCES category
);

-- Goals that need to be achieved each day
CREATE TABLE goal (
  id SERIAL PRIMARY KEY,
  entry_id INT REFERENCES entry,
  description TEXT NOT NULL
);

-- Goal finished succesfully for the day ("exercised today")
CREATE TABLE completed_goal (
  id SERIAL PRIMARY KEY,
  goal_id INT REFERENCES goal,
  completed_day DATE NOT NULL,
  completed_time TIME NOT NULL
);

-- Pages of user's diary
CREATE TABLE diary_page (
  id SERIAL PRIMARY KEY,
  entry_id INT REFERENCES entry,
  text TEXT,
  recorded_day DATE NOT NULL
);

-- Smart board of postit notes with reminders
CREATE TABLE note (
  id SERIAL PRIMARY KEY,
  entry_id INT REFERENCES entry,
  position_x INT NOT NULL DEFAULT 0,
  position_y INT NOT NULL DEFAULT 0,
  content TEXT NOT NULL,
  reminder_time TIMESTAMP
);

-- One line of a To-do list with interval created by deadlines
CREATE TABLE todo (
  id SERIAL PRIMARY KEY,
  entry_id INT REFERENCES entry,
  description VARCHAR(500) NOT NULL,
  deadline TIMESTAMP NOT NULL,
  soft_deadline TIMESTAMP CHECK (deadline > soft_deadline),
  priority INT DEFAULT 0
);
-- User authentification data
CREATE TABLE chiclepad_user (
  id SERIAL PRIMARY KEY,
  email VARCHAR(100) NOT NULL, -- unique will not do, as emails don't depend on casing
  password CHAR(24) NOT NULL CHECK (password != '')
);
CREATE UNIQUE INDEX email_id on chiclepad_user (lower(email));

-- Optional details about user
CREATE TABLE chiclepad_user_details (
  id SERIAL PRIMARY KEY,
  user_id INT REFERENCES chiclepad_user,
  name VARCHAR(100) NOT NULL,
  locale VARCHAR(50) NOT NULL
);

-- One 'organization object' added to the user's profile
CREATE TABLE entry (
  id SERIAL PRIMARY KEY,
  user_id INT REFERENCES chiclepad_user,
  created TIMESTAMP NOT NULL
);

-- Category for tagging entries
CREATE TABLE category (
  id SERIAL PRIMARY KEY,
  name VARCHAR(50) NOT NULL,
  icon VARCHAR(50) DEFAULT 'fa-circle-o',
  color CHAR(7) DEFAULT '#cccccc'
);

-- N : M table connecting catiegories to entries
CREATE TABLE registered_category
(
  entry_id INT REFERENCES entry,
  category_id INT REFERENCES category
);

-- Goals that need to be achieved each day
CREATE TABLE goal (
  id SERIAL PRIMARY KEY,
  entry_id INT REFERENCES entry,
  description TEXT NOT NULL
);

-- Goal finished succesfully for the day ("exercised today")
CREATE TABLE completed_goal (
  id SERIAL PRIMARY KEY,
  goal_id INT REFERENCES goal,
  completed_day DATE NOT NULL,
  completed_time TIME NOT NULL
);

-- Pages of user's diary
CREATE TABLE diary_page (
  id SERIAL PRIMARY KEY,
  entry_id INT REFERENCES entry,
  text TEXT,
  recorded_day DATE NOT NULL
);

-- Smart board of postit notes with reminders
CREATE TABLE note (
  id SERIAL PRIMARY KEY,
  entry_id INT REFERENCES entry,
  position_x INT NOT NULL DEFAULT 0,
  position_y INT NOT NULL DEFAULT 0,
  content TEXT NOT NULL,
  reminder_time TIMESTAMP
);

-- One line of a To-do list with interval created by deadlines
CREATE TABLE todo (
  id SERIAL PRIMARY KEY,
  entry_id INT REFERENCES entry,
  description VARCHAR(500) NOT NULL,
  deadline TIMESTAMP NOT NULL,
  soft_deadline TIMESTAMP CHECK (deadline > soft_deadline),
  priority INT DEFAULT 0
);
