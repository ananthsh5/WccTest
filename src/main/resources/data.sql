--Password unencrypted is qwerty123S
INSERT INTO user (id, first_name, last_name, email, password, phone, address, role, register_date)
VALUES (1, 'First', 'User', 'user@bookstore.com', '$2a$10$jmiSOFYc1v46UwHq6V6QOOebRLaGnH9KCsO7TaNYiRQg4y8mWQGhu', '012-345-6789', '1, Jalan Bangsar,Kuala Lumpur', 'USER', now());

INSERT INTO user (id, first_name, last_name, email, password, phone, address, role, register_date)
VALUES (2, 'Wcc', 'Admin', 'admin@bookstore.com', '$2a$10$jmiSOFYc1v46UwHq6V6QOOebRLaGnH9KCsO7TaNYiRQg4y8mWQGhu', '000-000-0000', '1, Jalan Bangsar,Kuala Lumpur', 'ADMIN', now());

