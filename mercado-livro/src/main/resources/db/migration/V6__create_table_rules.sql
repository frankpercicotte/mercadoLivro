CREATE TABLE customer_roles(
	customer_id int not null,
    role varchar(100) not null,
    FOREIGN KEY(customer_id) REFERENCES customer(id)
);