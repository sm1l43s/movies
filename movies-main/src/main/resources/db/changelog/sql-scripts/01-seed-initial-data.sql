-- Заполнение таблицы country
INSERT INTO country (name) VALUES
                               ('США'),
                               ('Россия'),
                               ('Франция');

-- Заполнение таблицы genre
INSERT INTO genre (name) VALUES
                             ('Драма'),
                             ('Комедия'),
                             ('Боевик'),
                             ('Фантастика'),
                             ('Триллер');

-- Заполнение таблицы movie
INSERT INTO movie (name_ru, name_en, poster_url, trailer_url, description, slogan, year, movie_length, rating_imdb, rating_kinopoisk, votes_score, number_of_votes) VALUES
                                                                                                                                                                        ('Зеленая миля', 'The Green Mile', 'https://example.com/posters/green_mile.jpg', 'https://example.com/trailers/green_mile.mp4', 'В тюрьме для смертников появляется заключенный с необычными способностями.', 'Пол Эджкомб не верил в чудеса. Пока не столкнулся с одним из них', '1999', 189, 8.6, 9.1, 4.8, 1500000),
                                                                                                                                                                        ('Брат', 'Brother', 'https://example.com/posters/brat.jpg', 'https://example.com/trailers/brat.mp4', 'Демобилизовавшись, Данила Багров вернулся в родной городок. Но скучная жизнь провинциального городка не устраивала его, и он решил поехать в Петербург, где, по слухам, уже несколько лет процветает его старший брат.', 'Не брат ты мне...', '1997', 100, 8.0, 8.3, 4.5, 900000),
                                                                                                                                                                        ('Леон', 'Leon', 'https://example.com/posters/leon.jpg', 'https://example.com/trailers/leon.mp4', 'Профессиональный убийца Леон неожиданно становится опекуном девочки Матильды, когда её семья погибает от рук коррумпированных полицейских.', 'Вы не можете остановить то, что вы не можете видеть', '1994', 133, 8.5, 8.7, 4.7, 1200000);

-- Заполнение таблицы movie_genres
INSERT INTO movie_genres (movie_id, genre_id) VALUES
                                                  (1, 1), -- Зеленая миля - Драма
                                                  (2, 1), -- Брат - Драма
                                                  (2, 3), -- Брат - Боевик
                                                  (3, 1), -- Леон - Драма
                                                  (3, 3), -- Леон - Боевик
                                                  (3, 5); -- Леон - Триллер

-- Заполнение таблицы movie_countries
INSERT INTO movie_countries (movie_id, country_id) VALUES
                                                       (1, 1), -- Зеленая миля - США
                                                       (2, 2), -- Брат - Россия
                                                       (3, 3); -- Леон - Франция

-- Заполнение таблицы person
INSERT INTO person (name_ru, name_en, poster_url, birthday, birth_place_id) VALUES
                                                                                ('Том Хэнкс', 'Tom Hanks', 'https://example.com/persons/tom_hanks.jpg', '1956-07-09', 1),
                                                                                ('Сергей Бодров', 'Sergei Bodrov', 'https://example.com/persons/sergei_bodrov.jpg', '1971-12-27', 2),
                                                                                ('Жан Рено', 'Jean Reno', 'https://example.com/persons/jean_reno.jpg', '1948-07-30', 3),
                                                                                ('Фрэнк Дарабонт', 'Frank Darabont', 'https://example.com/persons/frank_darabont.jpg', '1959-01-28', 1),
                                                                                ('Алексей Балабанов', 'Alexei Balabanov', 'https://example.com/persons/alexei_balabanov.jpg', '1959-02-25', 2),
                                                                                ('Люк Бессон', 'Luc Besson', 'https://example.com/persons/luc_besson.jpg', '1959-03-18', 3);

-- Заполнение таблицы profession
INSERT INTO profession (profession_name) VALUES
                                             ('Актер'),
                                             ('Режиссер'),
                                             ('Сценарист'),
                                             ('Продюсер');

-- Заполнение таблицы person_movies
INSERT INTO person_movies (person_id, movie_id) VALUES
                                                    (1, 1), -- Том Хэнкс в Зеленой миле
                                                    (2, 2), -- Сергей Бодров в Брате
                                                    (3, 3), -- Жан Рено в Леоне
                                                    (4, 1), -- Фрэнк Дарабонт - режиссер Зеленой мили
                                                    (5, 2), -- Алексей Балабанов - режиссер Брата
                                                    (6, 3); -- Люк Бессон - режиссер Леона

-- Заполнение таблицы type
INSERT INTO type (name) VALUES
                            ('Полнометражный'),
                            ('Сериал'),
                            ('Короткометражный');

-- Заполнение таблицы users
INSERT INTO users (email, password, first_name, last_name, birth_day) VALUES
                                                                          ('user1@example.com', '$2a$10$hKDVYxLefVHV/vtuPhWD3OigtRyOykRLDdUAp80Z1crSoS1lFqaFS', 'Иван', 'Иванов', '1990-01-01'),
                                                                          ('user2@example.com', '$2a$10$hKDVYxLefVHV/vtuPhWD3OigtRyOykRLDdUAp80Z1crSoS1lFqaFS', 'Петр', 'Петров', '1985-05-15'),
                                                                          ('user3@example.com', '$2a$10$hKDVYxLefVHV/vtuPhWD3OigtRyOykRLDdUAp80Z1crSoS1lFqaFS', 'Мария', 'Сидорова', '1992-11-23');

-- Заполнение таблицы privilege
INSERT INTO privilege (privilege_name) VALUES
                                           ('USER'),
                                           ('ADMIN'),
                                           ('MODERATOR');

-- Заполнение таблицы users_privileges
INSERT INTO users_privileges (user_id, privilege_id) VALUES
                                                         (1, 1), -- Иван - USER
                                                         (2, 1), -- Петр - USER
                                                         (2, 3), -- Петр - MODERATOR
                                                         (3, 1), -- Мария - USER
                                                         (3, 2); -- Мария - ADMIN

-- Заполнение таблицы review
INSERT INTO review (title, description, created_at, author_id) VALUES
                                                                   ('Великолепный фильм!', 'Зеленая миля - это шедевр кинематографа. Том Хэнкс великолепен в своей роли.', '2023-01-15', 1),
                                                                   ('Культовое кино', 'Брат - это классика российского кино. Сергей Бодров навсегда останется в наших сердцах.', '2023-02-20', 2),
                                                                   ('Шедевр от Бессона', 'Леон - один из лучших фильмов в истории. Жан Рено и юная Натали Портман создали незабываемый дуэт.', '2023-03-10', 3);