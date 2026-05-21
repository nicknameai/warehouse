CREATE TABLE products (
    id INTEGER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    name VARCHAR(255) NOT NULL UNIQUE,
    article VARCHAR(255) NOT NULL UNIQUE,
    description TEXT NOT NULL,
    purchase_price INTEGER NOT NULL CHECK (purchase_price >= 0),
    sale_price INTEGER NOT NULL CHECK (sale_price >= 0),
    unit VARCHAR(100) NOT NULL
);

CREATE TABLE roles (
    id INTEGER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    name VARCHAR(50) NOT NULL UNIQUE
);

INSERT INTO roles (name)
VALUES ('ADMIN'), ('STOREKEEPER');

CREATE TABLE users (
    id INTEGER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    login VARCHAR(255) NOT NULL UNIQUE,
    password_hash VARCHAR(255) NOT NULL,
    name VARCHAR(255) NOT NULL,
    role_id INTEGER NOT NULL,
    CONSTRAINT fk_users_role
        FOREIGN KEY (role_id) REFERENCES roles (id)
);

CREATE TABLE suppliers (
    id INTEGER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    name VARCHAR(255) NOT NULL UNIQUE,
    phone VARCHAR(50) NOT NULL,
    email VARCHAR(255) NOT NULL,
    address TEXT NOT NULL,
    description TEXT NOT NULL
);

CREATE TABLE clients (
    id INTEGER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    name VARCHAR(255) NOT NULL UNIQUE,
    phone VARCHAR(50) NOT NULL,
    email VARCHAR(255) NOT NULL,
    address TEXT NOT NULL,
    description TEXT NOT NULL
);

CREATE TABLE warehouse_stock (
    id INTEGER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    product_id INTEGER NOT NULL UNIQUE,
    quantity INTEGER NOT NULL CHECK (quantity > 0),
    CONSTRAINT fk_warehouse_stock_product
        FOREIGN KEY (product_id) REFERENCES products (id)
);

CREATE TABLE invoices (
    id INTEGER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    invoice_date DATE NOT NULL,
    invoice_type VARCHAR(20) NOT NULL CHECK (invoice_type IN ('INCOMING', 'OUTGOING')),
    supplier_id INTEGER,
    client_id INTEGER,
    comment TEXT,
    CONSTRAINT fk_invoices_supplier
        FOREIGN KEY (supplier_id) REFERENCES suppliers (id),
    CONSTRAINT fk_invoices_client
        FOREIGN KEY (client_id) REFERENCES clients (id),
    CONSTRAINT chk_invoices_exactly_one_party
        CHECK (
            (supplier_id IS NOT NULL AND client_id IS NULL)
            OR (supplier_id IS NULL AND client_id IS NOT NULL)
        )
);

CREATE TABLE invoice_items (
    id INTEGER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    invoice_id INTEGER NOT NULL,
    product_id INTEGER NOT NULL,
    quantity INTEGER NOT NULL CHECK (quantity > 0),
    price INTEGER NOT NULL CHECK (price >= 0),
    CONSTRAINT fk_invoice_items_invoice
        FOREIGN KEY (invoice_id) REFERENCES invoices (id),
    CONSTRAINT fk_invoice_items_product
        FOREIGN KEY (product_id) REFERENCES products (id)
);
