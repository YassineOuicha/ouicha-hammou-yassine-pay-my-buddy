SHOW DATABASES;
CREATE DATABASE IF NOT EXISTS pay_my_buddy_test;
USE pay_my_buddy_test;
CREATE TABLE user (
    id INT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(100) NOT NULL,
    email VARCHAR(255) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL
);

USE pay_my_buddy_test;
CREATE TABLE transaction (
    id INT PRIMARY KEY AUTO_INCREMENT,
    sender_id INT NOT NULL,
    receiver_id INT NOT NULL,
    description TEXT,
    amount DOUBLE NOT NULL,
    FOREIGN KEY (sender_id) REFERENCES user(id),
    FOREIGN KEY (receiver_id) REFERENCES user(id)
);

USE pay_my_buddy_test;
CREATE TABLE connections (
    user_id INT NOT NULL,
    friend_id INT NOT NULL,
    PRIMARY KEY (user_id, friend_id),
    FOREIGN KEY (user_id) REFERENCES user(id),
    FOREIGN KEY (friend_id) REFERENCES user(id)
);

USE pay_my_buddy_test;
SELECT * FROM user;

USE pay_my_buddy_test;
SELECT * FROM transaction;

USE pay_my_buddy_test;
SELECT * FROM connections;

ALTER TABLE user ADD COLUMN balance DOUBLE NOT NULL DEFAULT 0;

INSERT INTO user (username, email, password, balance) VALUES
('Alice', 'alice@example.com', '$2a$10$7s7FQp6MJm6t8H78RkjO0uXqSOW4jRuFC2Pb/xr6yZLVNLGl/QcAC', 1000.00),
('Bob', 'bob@example.com', '$2a$10$5M.kHGzHPeY7cHg2FlOqAuvph8l7Xc28o2MDvNBVVyokP5tr1jUzO', 500.00),
('Charlie', 'charlie@example.com', '$2a$10$lVx0pxYnuMIzEmBEXYY4qO.RvA3Hd8AeXHYyd6CC2TzBF2V4UN4ve', 2050.00),
('David', 'david@example.com', '$2a$10$R9eEV8sZGprmI13mI59hpeX6A.4JXsFHC56EUK7Ek5fE2xvnBlG6S', 150.00),
('Emma', 'emma@example.com', '$2a$10$9Dk/4FTMck6cHEPcf6kTL.kB2FkO7K6t/nFzRPzMYeG3FBPcdpZfy', 705.00),
('Frank', 'frank@example.com', '$2a$10$0U.S5F1GQMoAGjG6cCImx.TCAZWTvPSPR/G6y3rVJcU6/5BrrA3Vm', 3000.00),
('Grace', 'grace@example.com', '$2a$10$COfR.hqAzA7sJx.Lek5gEe3wR/SuL5Zn6pAQwJUKjFTU/sHigdawe', 120.00),
('Hugo', 'hugo@example.com', '$2a$10$ITywx70hPhFtHPcuOelMe.pEuoZZr1.G/o7q5RR4y4j65YyVvP5Oy', 90.00),
('Ivy', 'ivy@example.com', '$2a$10$A2vXGkksm4NyipPp9vHHbOTPTpFTWzlhKYhss71FLlsoBzBF5xNOm', 1800.00),
('Jack', 'jack@example.com', '$2a$10$pNMr4TeJPKMYwaHUNkk6QOgiRmQjTjIJjcEPEPxFjMiyUp/fbvEmS', 600.00);


INSERT INTO connections (user_id, friend_id) VALUES
(1, 2), (1, 3), (1, 4),  -- Alice is a friend of Bob, Charlie and David
(2, 5), (2, 6),          -- Bob is a friend of Emma and Frank
(3, 7), (3, 8),          -- Charlie is a friend of Grace and Hugo
(4, 9), (4, 10),         -- David is a friend of Ivy and Jack
(5, 1), (5, 6),          -- Emma is a friend of Alice and Frank
(6, 7), (6, 8),          -- Frank is a friend of Grace and Hugo
(7, 9), (7, 10),         -- Grace is a friend of Ivy and Jack
(8, 2), (8, 3),          -- Hugo is a friend of Bob and Charlie
(9, 4), (9, 5),          -- Ivy is a friend of David and Emma
(10, 6), (10, 7);        -- Jack is a friend of Frank and Grace

INSERT INTO transaction (sender_id, receiver_id, description, amount) VALUES
(1, 2, 'Shared lunch', 15.00),
(2, 5, 'Netflix subscription', 8.00),
(3, 7, 'Group purchase', 20.00),
(4, 9, 'Carpool gas', 25.00),
(5, 1, 'Coffee reimbursement', 5.00),
(6, 7, 'Airbnb reservation', 50.00),
(7, 9, 'Concert tickets', 30.00),
(8, 2, 'Pizza night', 12.00),
(9, 4, 'Electric scooter rental', 9.50),
(10, 6, 'Weekend trip contribution', 40.00);

DESC user;
DESC connections;
DESC transaction;

ALTER TABLE connections DROP FOREIGN KEY connections_ibfk_1;
ALTER TABLE connections DROP FOREIGN KEY connections_ibfk_2;

ALTER TABLE transaction DROP FOREIGN KEY transaction_ibfk_1;
ALTER TABLE transaction DROP FOREIGN KEY transaction_ibfk_2;

ALTER TABLE user MODIFY id BIGINT NOT NULL AUTO_INCREMENT;
ALTER TABLE connections MODIFY user_id BIGINT NOT NULL;
ALTER TABLE connections MODIFY friend_id BIGINT NOT NULL;
ALTER TABLE transaction MODIFY sender_id BIGINT NOT NULL;
ALTER TABLE transaction MODIFY receiver_id BIGINT NOT NULL;