-- Вставляем тестового пользователя
INSERT INTO users (username, password, enabled) VALUES
    ('testuser', '$2a$10$F9QqzljELJr5OeTxci9lXuBGJpxKguXhJHXu5oU8v3Fw0P6lpPdVe', true);

-- Второй пользователь
INSERT INTO users (username, password, enabled) VALUES
    ('devuser', '$2a$10$zYJoH9LjKQGlRVe9gZj4RuQ0se2JlnwbIuHirQ9rh9xAyOaHdZ9iW', true);