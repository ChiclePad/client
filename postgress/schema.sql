-- User authentification data
CREATE TABLE chiclepad_user (
  id       SERIAL PRIMARY KEY,
  email    VARCHAR(150) NOT NULL, -- unique will not do, as emails don't depend on casing
  password VARCHAR(60)  NOT NULL CHECK (password != ''),
  salt     CHAR(29)     NOT NULL
);
CREATE UNIQUE INDEX email_id on chiclepad_user (lower(email));

-- Optional details about user
CREATE TABLE chiclepad_user_details (
  id      SERIAL PRIMARY KEY,
  user_id INT REFERENCES chiclepad_user ON DELETE CASCADE,
  name    VARCHAR(100),
  locale  VARCHAR(50)
);

-- One 'organization object' added to the user's profile
CREATE TABLE entry (
  id SERIAL PRIMARY KEY,
  user_id INT REFERENCES chiclepad_user ON DELETE CASCADE,
  created TIMESTAMP NOT NULL
);

-- Category for tagging entries
CREATE TABLE category (
  id      SERIAL PRIMARY KEY,
  user_id INT REFERENCES chiclepad_user ON DELETE CASCADE,
  name    VARCHAR(100) NOT NULL,
  icon    VARCHAR(50) DEFAULT 'CIRCLE',
  color   CHAR(7)     DEFAULT '#cccccc'
);

-- N : M table connecting categories to entries
CREATE TABLE registered_category (
  entry_id INT REFERENCES entry ON DELETE CASCADE,
  category_id INT REFERENCES category ON DELETE CASCADE,
  CONSTRAINT registered_category_id PRIMARY KEY (entry_id, category_id)
);

-- Goals that need to be achieved each day
CREATE TABLE goal (
  id          SERIAL PRIMARY KEY,
  entry_id    INT REFERENCES entry ON DELETE CASCADE,
  description VARCHAR(500) NOT NULL
);

-- Goal finished succesfully for the day ("exercised today")
CREATE TABLE completed_goal (
  id SERIAL PRIMARY KEY,
  goal_id INT REFERENCES goal ON DELETE CASCADE,
  completed_day DATE NOT NULL,
  completed_time TIME NOT NULL
);

-- Pages of user's diary
CREATE TABLE diary_page (
  id SERIAL PRIMARY KEY,
  entry_id INT REFERENCES entry ON DELETE CASCADE,
  text TEXT,
  recorded_day DATE NOT NULL
);

-- Smart board of postit notes with reminders
CREATE TABLE note (
  id            SERIAL PRIMARY KEY,
  entry_id      INT REFERENCES entry ON DELETE CASCADE,
  position_x    INT           NOT NULL DEFAULT 0,
  position_y    INT           NOT NULL DEFAULT 0,
  content       VARCHAR(1000) NOT NULL,
  reminder_time TIMESTAMP
);

-- One line of a To-do list with interval created by deadlines
CREATE TABLE todo (
  id            SERIAL PRIMARY KEY,
  entry_id      INT REFERENCES entry ON DELETE CASCADE,
  description   VARCHAR(240) NOT NULL,
  deadline      TIMESTAMP    NOT NULL,
  soft_deadline TIMESTAMP CHECK (deadline > soft_deadline),
  priority      INT DEFAULT 0
);

-- Table representing entries that the user already completed
CREATE TABLE deleted_entry (
  id           SERIAL PRIMARY KEY,
  entry_id     INT REFERENCES entry ON DELETE CASCADE,
  deleted_time TIMESTAMP NOT NULL
);
