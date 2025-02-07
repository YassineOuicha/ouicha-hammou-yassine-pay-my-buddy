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


