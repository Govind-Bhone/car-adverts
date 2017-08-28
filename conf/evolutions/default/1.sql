# --- First database schema

# --- !Ups

CREATE TABLE IF NOT EXISTS car (id INT PRIMARY KEY , title VARCHAR(20), fuel VARCHAR(20), price DOUBLE, mileage INT, registrationDate DATE);

# --- !Downs

drop table if exists car;


