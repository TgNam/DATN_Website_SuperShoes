use db_supershoes;
/*INSERT INTO account*/
INSERT INTO account (created_at, created_by, status, updated_at, updated_by, email, name, rewards, role)
VALUES (NOW(6), 'Nguyễn Văn A', 'ACTIVE', NOW(6), 'Nguyễn Văn A', 'NguyenVanA@example.com', 'Nguyễn Văn A', 100, 'ADMIN');
INSERT INTO account (created_at, created_by, status, updated_at, updated_by, email, name, rewards, role)
VALUES (NOW(6), 'Nguyễn Văn A', 'ACTIVE', NOW(6), 'Nguyễn Văn A', 'NguyenVanB@example.com', 'Nguyễn Văn B', 100, 'EMPLOYEE');
INSERT INTO account (created_at, created_by, status, updated_at, updated_by, email, name, rewards, role)
VALUES (NOW(6), 'Nguyễn Văn A', 'ACTIVE', NOW(6), 'Nguyễn Văn A', 'NguyenVanC@example.com', 'NguyenVanC', 100, 'EMPLOYEE');
INSERT INTO account (created_at, created_by, status, updated_at, updated_by, email, name, rewards, role)
VALUES (NOW(6), 'Nguyễn Văn D', 'ACTIVE', NOW(6), 'Nguyễn Văn D', 'NguyenVanD@example.com', 'Nguyễn Văn D', 100, 'USER');
INSERT INTO account (created_at, created_by, status, updated_at, updated_by, email, name, rewards, role)
VALUES (NOW(6), 'Nguyễn Văn E', 'ACTIVE', NOW(6), 'Nguyễn Văn E', 'NguyenVanE@example.com', 'Nguyễn Văn E', 100, 'USER');
INSERT INTO account (created_at, created_by, status, updated_at, updated_by, email, name, rewards, role)
VALUES (NOW(6), 'Nguyễn Văn F', 'ACTIVE', NOW(6), 'Nguyễn Văn F', 'NguyenVanF@example.com', 'Nguyễn Văn F', 100, 'USER');

/*INSERT INTO account*/
INSERT INTO address (created_at, created_by, status, updated_at, updated_by, address, name, phone_number, id_account)
VALUES (NOW(6), 'Nguyễn Văn D', 'ACTIVE', NOW(6), 'Nguyễn Văn D', 'Phú Đô, Nam Từ Liêm, Hà Nội, Việt Nam', 'Nguyễn Văn D', '0983729333', 5);
INSERT INTO address (created_at, created_by, status, updated_at, updated_by, address, name, phone_number, id_account)
VALUES (NOW(6), 'Nguyễn Văn E', 'ACTIVE', NOW(6), 'Nguyễn Văn E', 'Phú Đô, Nam Từ Liêm, Hà Nội, Việt Nam', 'Nguyễn Văn E', '0983729333', 6);
INSERT INTO address (created_at, created_by, status, updated_at, updated_by, address, name, phone_number, id_account)
VALUES (NOW(6), 'Nguyễn Văn D', 'ACTIVE', NOW(6), 'Nguyễn Văn D', 'Mễ Trì, Nam Từ Liêm, Hà Nội, Việt Nam', 'Nguyễn Văn D', '0983729333', 5);
INSERT INTO address (created_at, created_by, status, updated_at, updated_by, address, name, phone_number, id_account)
VALUES (NOW(6), 'Nguyễn Văn E', 'ACTIVE', NOW(6), 'Nguyễn Văn E', 'Mễ Trì, Nam Từ Liêm, Hà Nội, Việt Nam', 'Nguyễn Văn E', '0983729333', 6);
INSERT INTO address (created_at, created_by, status, updated_at, updated_by, address, name, phone_number, id_account)
VALUES (NOW(6), 'Nguyễn Văn D', 'ACTIVE', NOW(6), 'Nguyễn Văn D', 'Mỹ Đình, Nam Từ Liêm, Hà Nội, Việt Nam', 'Nguyễn Văn D', '0983729333', 5);

/*INSERT INTO guest*/
INSERT INTO guest (created_at, created_by, status, updated_at, updated_by, address, name, phone_number)
VALUES (NOW(6), 'Nguyễn Văn Q', 'ACTIVE', NOW(6), 'Nguyễn Văn Q', 'Phú Đô, Nam Từ Liêm, Hà Nội, Việt Nam', 'Nguyễn Văn Q', '0983729333');
INSERT INTO guest (created_at, created_by, status, updated_at, updated_by, address, name, phone_number)
VALUES (NOW(6), 'Nguyễn Văn W', 'ACTIVE', NOW(6), 'Nguyễn Văn W', 'Phú Đô, Nam Từ Liêm, Hà Nội, Việt Nam', 'Nguyễn Văn W', '0983729333');
INSERT INTO guest (created_at, created_by, status, updated_at, updated_by, address, name, phone_number)
VALUES (NOW(6), 'Nguyễn Văn E', 'ACTIVE', NOW(6), 'Nguyễn Văn E', 'Mễ Trì, Nam Từ Liêm, Hà Nội, Việt Nam', 'Nguyễn Văn E', '0983729333');
INSERT INTO guest (created_at, created_by, status, updated_at, updated_by, address, name, phone_number)
VALUES (NOW(6), 'Nguyễn Văn R', 'ACTIVE', NOW(6), 'Nguyễn Văn R', 'Mễ Trì, Nam Từ Liêm, Hà Nội, Việt Nam', 'Nguyễn Văn R', '0983729333');
INSERT INTO guest (created_at, created_by, status, updated_at, updated_by, address, name, phone_number)
VALUES (NOW(6), 'Nguyễn Văn T', 'ACTIVE', NOW(6), 'Nguyễn Văn T', 'Mỹ Đình, Nam Từ Liêm, Hà Nội, Việt Nam', 'Nguyễn Văn T', '0983729333');

/*INSERT INTO category*/
INSERT INTO category (created_at, created_by, status, updated_at, updated_by, name)
VALUES (NOW(6), 'Nguyễn Văn A', 'ACTIVE', NOW(6), 'Nguyễn Văn A', 'Sneakers');
INSERT INTO category (created_at, created_by, status, updated_at, updated_by, name)
VALUES (NOW(6), 'Nguyễn Văn A', 'ACTIVE', NOW(6), 'Nguyễn Văn A', 'Boots');
INSERT INTO category (created_at, created_by, status, updated_at, updated_by, name)
VALUES (NOW(6), 'Nguyễn Văn A', 'ACTIVE', NOW(6), 'Nguyễn Văn A', 'Slippers');
INSERT INTO category (created_at, created_by, status, updated_at, updated_by, name)
VALUES (NOW(6), 'Nguyễn Văn A', 'ACTIVE', NOW(6), 'Nguyễn Văn A', 'Running Shoes');
INSERT INTO category (created_at, created_by, status, updated_at, updated_by, name)
VALUES (NOW(6), 'Nguyễn Văn A', 'ACTIVE', NOW(6), 'Nguyễn Văn A', 'Sandals');

/*INSERT INTO brand*/
INSERT INTO brand (created_at, created_by, status, updated_at, updated_by, name)
VALUES (NOW(6), 'Nguyễn Văn A', 'ACTIVE', NOW(6), 'Nguyễn Văn A', 'Nike');
INSERT INTO brand (created_at, created_by, status, updated_at, updated_by, name)
VALUES (NOW(6), 'Nguyễn Văn A', 'ACTIVE', NOW(6), 'Nguyễn Văn A', 'Adidas');
INSERT INTO brand (created_at, created_by, status, updated_at, updated_by, name)
VALUES (NOW(6), 'Nguyễn Văn A', 'ACTIVE', NOW(6), 'Nguyễn Văn A', 'Converse');
INSERT INTO brand (created_at, created_by, status, updated_at, updated_by, name)
VALUES (NOW(6), 'Nguyễn Văn A', 'ACTIVE', NOW(6), 'Nguyễn Văn A', 'ASICS');

/*INSERT INTO material*/
INSERT INTO material (created_at, created_by, status, updated_at, updated_by, name)
VALUES (NOW(6), 'Nguyễn Văn A', 'ACTIVE', NOW(6), 'Nguyễn Văn A', 'Suede');
INSERT INTO material (created_at, created_by, status, updated_at, updated_by, name)
VALUES (NOW(6), 'Nguyễn Văn A', 'ACTIVE', NOW(6), 'Nguyễn Văn A', 'Canvas');
INSERT INTO material (created_at, created_by, status, updated_at, updated_by, name)
VALUES (NOW(6), 'Nguyễn Văn A', 'ACTIVE', NOW(6), 'Nguyễn Văn A', 'Knit Fabric');
INSERT INTO material (created_at, created_by, status, updated_at, updated_by, name)
VALUES (NOW(6), 'Nguyễn Văn A', 'ACTIVE', NOW(6), 'Nguyễn Văn A', 'PU Leather');

/*INSERT INTO shoe_sole*/
INSERT INTO shoe_sole (created_at, created_by, status, updated_at, updated_by, name)
VALUES (NOW(6), 'Nguyễn Văn A', 'ACTIVE', NOW(6), 'Nguyễn Văn A', 'Rubber');
INSERT INTO shoe_sole (created_at, created_by, status, updated_at, updated_by, name)
VALUES (NOW(6), 'Nguyễn Văn A', 'ACTIVE', NOW(6), 'Nguyễn Văn A', 'Foam');

/*INSERT INTO size*/
INSERT INTO size (created_at, created_by, status, updated_at, updated_by, name)
VALUES (NOW(6), 'Nguyễn Văn A', 'ACTIVE', NOW(6), 'Nguyễn Văn A', '38');
INSERT INTO size (created_at, created_by, status, updated_at, updated_by, name)
VALUES (NOW(6), 'Nguyễn Văn A', 'ACTIVE', NOW(6), 'Nguyễn Văn A', '39');
INSERT INTO size (created_at, created_by, status, updated_at, updated_by, name)
VALUES (NOW(6), 'Nguyễn Văn A', 'ACTIVE', NOW(6), 'Nguyễn Văn A', '40');
INSERT INTO size (created_at, created_by, status, updated_at, updated_by, name)
VALUES (NOW(6), 'Nguyễn Văn A', 'ACTIVE', NOW(6), 'Nguyễn Văn A', '41');
INSERT INTO size (created_at, created_by, status, updated_at, updated_by, name)
VALUES (NOW(6), 'Nguyễn Văn A', 'ACTIVE', NOW(6), 'Nguyễn Văn A', '42');
INSERT INTO size (created_at, created_by, status, updated_at, updated_by, name)
VALUES (NOW(6), 'Nguyễn Văn A', 'ACTIVE', NOW(6), 'Nguyễn Văn A', '43');
INSERT INTO size (created_at, created_by, status, updated_at, updated_by, name)
VALUES (NOW(6), 'Nguyễn Văn A', 'ACTIVE', NOW(6), 'Nguyễn Văn A', '44');

/*INSERT INTO color */
INSERT INTO color (created_at, created_by, status, updated_at, updated_by, code_color, name)
VALUES (NOW(6), 'Nguyễn Văn A', 'ACTIVE', NOW(6), 'Nguyễn Văn A', '#FFFFFF', 'White');
INSERT INTO color (created_at, created_by, status, updated_at, updated_by, code_color, name)
VALUES (NOW(6), 'Nguyễn Văn A', 'ACTIVE', NOW(6), 'Nguyễn Văn A', '#000000', 'Black');
INSERT INTO color (created_at, created_by, status, updated_at, updated_by, code_color, name)
VALUES (NOW(6), 'Nguyễn Văn A', 'ACTIVE', NOW(6), 'Nguyễn Văn A', '#FF0000', 'Red');
INSERT INTO color (created_at, created_by, status, updated_at, updated_by, code_color, name)
VALUES (NOW(6), 'Nguyễn Văn A', 'ACTIVE', NOW(6), 'Nguyễn Văn A', '#FFFF00', 'Yellow');
INSERT INTO color (created_at, created_by, status, updated_at, updated_by, code_color, name)
VALUES (NOW(6), 'Nguyễn Văn A', 'ACTIVE', NOW(6), 'Nguyễn Văn A', '#0000FF', 'Blue');

/*INSERT INTO product */
INSERT INTO product (created_at, created_by, status, updated_at, updated_by, gender, image, name, product_code, id_brand, id_category, id_material, id_shoe_sole)
VALUES (NOW(6), 'Nguyễn Văn A', 'ACTIVE', NOW(6), 'Nguyễn Văn A', true, NULL, 'Giày Nike Air Jordan 1 Low Paris', 'CV3043-100', 1, 1, 1, 2);
INSERT INTO product (created_at, created_by, status, updated_at, updated_by, gender, image, name, product_code, id_brand, id_category, id_material, id_shoe_sole)
VALUES (NOW(6), 'Nguyễn Văn A', 'ACTIVE', NOW(6), 'Nguyễn Văn A', true, NULL, 'GIÀY Adidas Neo Vl Court White Black Gum', 'ID6015', 2, 1, 1, 2);
INSERT INTO product (created_at, created_by, status, updated_at, updated_by, gender, image, name, product_code, id_brand, id_category, id_material, id_shoe_sole)
VALUES (NOW(6), 'Nguyễn Văn A', 'ACTIVE', NOW(6), 'Nguyễn Văn A', true, NULL, 'Giày Adidas Samba OG Black Gum', 'IE3676', 2, 1, 1, 2);
INSERT INTO product (created_at, created_by, status, updated_at, updated_by, gender, image, name, product_code, id_brand, id_category, id_material, id_shoe_sole)
VALUES (NOW(6), 'Nguyễn Văn A', 'ACTIVE', NOW(6), 'Nguyễn Văn A', true, NULL, 'Giày Nike Air Jordan 1 Low SE Hemp Light British Tan', 'HF5753-221', 1, 1, 1, 2);
INSERT INTO product (created_at, created_by, status, updated_at, updated_by, gender, image, name, product_code, id_brand, id_category, id_material, id_shoe_sole)
VALUES (NOW(6), 'Nguyễn Văn A', 'ACTIVE', NOW(6), 'Nguyễn Văn A', true, NULL, 'Giày Adidas Ultraboost Light 23 White Orange', 'Hq6351', 2, 1, 1, 2);
INSERT INTO product (created_at, created_by, status, updated_at, updated_by, gender, image, name, product_code, id_brand, id_category, id_material, id_shoe_sole)
VALUES (NOW(6), 'Nguyễn Văn A', 'ACTIVE', NOW(6), 'Nguyễn Văn A', true, NULL, 'Nike Court Legacy White Desert Ochre Next Nature', 'DH3162-100', 1, 1, 1, 2);

/*INSERT INTO product_detail */
INSERT INTO product_detail (created_at, created_by, status, updated_at, updated_by, price, quantity, id_color, id_product, id_size)
VALUES (NOW(6), 'Nguyễn Văn A', 'ACTIVE', NOW(6), 'Nguyễn Văn A', 999.99, 10, 1, 1, 1);
INSERT INTO product_detail (created_at, created_by, status, updated_at, updated_by, price, quantity, id_color, id_product, id_size)
VALUES (NOW(6), 'Nguyễn Văn A', 'ACTIVE', NOW(6), 'Nguyễn Văn A', 1000, 10, 2, 1, 2);
INSERT INTO product_detail (created_at, created_by, status, updated_at, updated_by, price, quantity, id_color, id_product, id_size)
VALUES (NOW(6), 'Nguyễn Văn A', 'ACTIVE', NOW(6), 'Nguyễn Văn A', 998.99, 10, 1, 1, 3);
INSERT INTO product_detail (created_at, created_by, status, updated_at, updated_by, price, quantity, id_color, id_product, id_size)
VALUES (NOW(6), 'Nguyễn Văn A', 'ACTIVE', NOW(6), 'Nguyễn Văn A', 997.99, 10, 4, 1, 4);
INSERT INTO product_detail (created_at, created_by, status, updated_at, updated_by, price, quantity, id_color, id_product, id_size)
VALUES (NOW(6), 'Nguyễn Văn A', 'ACTIVE', NOW(6), 'Nguyễn Văn A', 996.99, 10, 1, 1, 5);

INSERT INTO product_detail (created_at, created_by, status, updated_at, updated_by, price, quantity, id_color, id_product, id_size)
VALUES (NOW(6), 'Nguyễn Văn A', 'ACTIVE', NOW(6), 'Nguyễn Văn A', 999.99, 10, 1, 2, 1);
INSERT INTO product_detail (created_at, created_by, status, updated_at, updated_by, price, quantity, id_color, id_product, id_size)
VALUES (NOW(6), 'Nguyễn Văn A', 'ACTIVE', NOW(6), 'Nguyễn Văn A', 1000, 10, 2, 2, 2);
INSERT INTO product_detail (created_at, created_by, status, updated_at, updated_by, price, quantity, id_color, id_product, id_size)
VALUES (NOW(6), 'Nguyễn Văn A', 'ACTIVE', NOW(6), 'Nguyễn Văn A', 998.99, 10, 1, 2, 3);
INSERT INTO product_detail (created_at, created_by, status, updated_at, updated_by, price, quantity, id_color, id_product, id_size)
VALUES (NOW(6), 'Nguyễn Văn A', 'ACTIVE', NOW(6), 'Nguyễn Văn A', 997.99, 10, 3, 2, 4);
INSERT INTO product_detail (created_at, created_by, status, updated_at, updated_by, price, quantity, id_color, id_product, id_size)
VALUES (NOW(6), 'Nguyễn Văn A', 'ACTIVE', NOW(6), 'Nguyễn Văn A', 996.99, 10, 1, 2, 5);

INSERT INTO product_detail (created_at, created_by, status, updated_at, updated_by, price, quantity, id_color, id_product, id_size)
VALUES (NOW(6), 'Nguyễn Văn A', 'ACTIVE', NOW(6), 'Nguyễn Văn A', 999.99, 10, 1, 3, 1);
INSERT INTO product_detail (created_at, created_by, status, updated_at, updated_by, price, quantity, id_color, id_product, id_size)
VALUES (NOW(6), 'Nguyễn Văn A', 'ACTIVE', NOW(6), 'Nguyễn Văn A', 1000, 10, 2, 3, 2);
INSERT INTO product_detail (created_at, created_by, status, updated_at, updated_by, price, quantity, id_color, id_product, id_size)
VALUES (NOW(6), 'Nguyễn Văn A', 'ACTIVE', NOW(6), 'Nguyễn Văn A', 998.99, 10, 1, 3, 3);
INSERT INTO product_detail (created_at, created_by, status, updated_at, updated_by, price, quantity, id_color, id_product, id_size)
VALUES (NOW(6), 'Nguyễn Văn A', 'ACTIVE', NOW(6), 'Nguyễn Văn A', 997.99, 10, 3, 3, 4);
INSERT INTO product_detail (created_at, created_by, status, updated_at, updated_by, price, quantity, id_color, id_product, id_size)
VALUES (NOW(6), 'Nguyễn Văn A', 'ACTIVE', NOW(6), 'Nguyễn Văn A', 996.99, 10, 1, 3, 5);

INSERT INTO product_detail (created_at, created_by, status, updated_at, updated_by, price, quantity, id_color, id_product, id_size)
VALUES (NOW(6), 'Nguyễn Văn A', 'ACTIVE', NOW(6), 'Nguyễn Văn A', 999.99, 10, 1, 4, 1);
INSERT INTO product_detail (created_at, created_by, status, updated_at, updated_by, price, quantity, id_color, id_product, id_size)
VALUES (NOW(6), 'Nguyễn Văn A', 'ACTIVE', NOW(6), 'Nguyễn Văn A', 1000, 10, 2, 4, 2);
INSERT INTO product_detail (created_at, created_by, status, updated_at, updated_by, price, quantity, id_color, id_product, id_size)
VALUES (NOW(6), 'Nguyễn Văn A', 'ACTIVE', NOW(6), 'Nguyễn Văn A', 998.99, 10, 1, 4, 3);
INSERT INTO product_detail (created_at, created_by, status, updated_at, updated_by, price, quantity, id_color, id_product, id_size)
VALUES (NOW(6), 'Nguyễn Văn A', 'ACTIVE', NOW(6), 'Nguyễn Văn A', 997.99, 10, 3, 4, 4);
INSERT INTO product_detail (created_at, created_by, status, updated_at, updated_by, price, quantity, id_color, id_product, id_size)
VALUES (NOW(6), 'Nguyễn Văn A', 'ACTIVE', NOW(6), 'Nguyễn Văn A', 996.99, 10, 1, 4, 5);

INSERT INTO product_detail (created_at, created_by, status, updated_at, updated_by, price, quantity, id_color, id_product, id_size)
VALUES (NOW(6), 'Nguyễn Văn A', 'ACTIVE', NOW(6), 'Nguyễn Văn A', 999.99, 10, 1, 5, 1);
INSERT INTO product_detail (created_at, created_by, status, updated_at, updated_by, price, quantity, id_color, id_product, id_size)
VALUES (NOW(6), 'Nguyễn Văn A', 'ACTIVE', NOW(6), 'Nguyễn Văn A', 1000, 10, 2, 5, 2);
INSERT INTO product_detail (created_at, created_by, status, updated_at, updated_by, price, quantity, id_color, id_product, id_size)
VALUES (NOW(6), 'Nguyễn Văn A', 'ACTIVE', NOW(6), 'Nguyễn Văn A', 998.99, 10, 1, 5, 3);
INSERT INTO product_detail (created_at, created_by, status, updated_at, updated_by, price, quantity, id_color, id_product, id_size)
VALUES (NOW(6), 'Nguyễn Văn A', 'ACTIVE', NOW(6), 'Nguyễn Văn A', 997.99, 10, 3, 5, 4);
INSERT INTO product_detail (created_at, created_by, status, updated_at, updated_by, price, quantity, id_color, id_product, id_size)
VALUES (NOW(6), 'Nguyễn Văn A', 'ACTIVE', NOW(6), 'Nguyễn Văn A', 996.99, 10, 1, 5, 5);

INSERT INTO product_detail (created_at, created_by, status, updated_at, updated_by, price, quantity, id_color, id_product, id_size)
VALUES (NOW(6), 'Nguyễn Văn A', 'ACTIVE', NOW(6), 'Nguyễn Văn A', 999.99, 10, 1, 6, 1);
INSERT INTO product_detail (created_at, created_by, status, updated_at, updated_by, price, quantity, id_color, id_product, id_size)
VALUES (NOW(6), 'Nguyễn Văn A', 'ACTIVE', NOW(6), 'Nguyễn Văn A', 1000, 10, 2, 6, 2);
INSERT INTO product_detail (created_at, created_by, status, updated_at, updated_by, price, quantity, id_color, id_product, id_size)
VALUES (NOW(6), 'Nguyễn Văn A', 'ACTIVE', NOW(6), 'Nguyễn Văn A', 998.99, 10, 1, 6, 3);
INSERT INTO product_detail (created_at, created_by, status, updated_at, updated_by, price, quantity, id_color, id_product, id_size)
VALUES (NOW(6), 'Nguyễn Văn A', 'ACTIVE', NOW(6), 'Nguyễn Văn A', 997.99, 10, 3, 6, 4);
INSERT INTO product_detail (created_at, created_by, status, updated_at, updated_by, price, quantity, id_color, id_product, id_size)
VALUES (NOW(6), 'Nguyễn Văn A', 'ACTIVE', NOW(6), 'Nguyễn Văn A', 996.99, 10, 1, 6, 5);

/*INSERT INTO product_favorite */
INSERT INTO product_favorite (created_at, created_by, status, updated_at, updated_by, id_account, id_product)
VALUES (NOW(6), 'Nguyễn Văn A', 'ACTIVE', NOW(6), 'Nguyễn Văn A', 4, 1);
INSERT INTO product_favorite (created_at, created_by, status, updated_at, updated_by, id_account, id_product)
VALUES (NOW(6), 'Nguyễn Văn A', 'ACTIVE', NOW(6), 'Nguyễn Văn A', 4, 2);
INSERT INTO product_favorite (created_at, created_by, status, updated_at, updated_by, id_account, id_product)
VALUES (NOW(6), 'Nguyễn Văn A', 'ACTIVE', NOW(6), 'Nguyễn Văn A', 4, 3);
INSERT INTO product_favorite (created_at, created_by, status, updated_at, updated_by, id_account, id_product)
VALUES (NOW(6), 'Nguyễn Văn A', 'ACTIVE', NOW(6), 'Nguyễn Văn A', 4, 4);
INSERT INTO product_favorite (created_at, created_by, status, updated_at, updated_by, id_account, id_product)
VALUES (NOW(6), 'Nguyễn Văn A', 'ACTIVE', NOW(6), 'Nguyễn Văn A', 4, 5);
INSERT INTO product_favorite (created_at, created_by, status, updated_at, updated_by, id_account, id_product)
VALUES (NOW(6), 'Nguyễn Văn A', 'ACTIVE', NOW(6), 'Nguyễn Văn A', 5, 1);
INSERT INTO product_favorite (created_at, created_by, status, updated_at, updated_by, id_account, id_product)
VALUES (NOW(6), 'Nguyễn Văn A', 'ACTIVE', NOW(6), 'Nguyễn Văn A', 5, 2);
INSERT INTO product_favorite (created_at, created_by, status, updated_at, updated_by, id_account, id_product)
VALUES (NOW(6), 'Nguyễn Văn A', 'ACTIVE', NOW(6), 'Nguyễn Văn A', 5, 3);
INSERT INTO product_favorite (created_at, created_by, status, updated_at, updated_by, id_account, id_product)
VALUES (NOW(6), 'Nguyễn Văn A', 'ACTIVE', NOW(6), 'Nguyễn Văn A', 5, 4);
INSERT INTO product_favorite (created_at, created_by, status, updated_at, updated_by, id_account, id_product)
VALUES (NOW(6), 'Nguyễn Văn A', 'ACTIVE', NOW(6), 'Nguyễn Văn A', 5, 5);

/*INSERT INTO promotion */
INSERT INTO promotion (created_at, created_by, status, updated_at, updated_by, code_promotion, end_at, name, note, start_at, type, value)
VALUES (NOW(6), 'Nguyễn Văn A', 'ONGOING', NOW(6), 'Nguyễn Văn A', 'PROMO123', '2024-12-31 23:59:59', 'Holiday Sale', 'Discount for the holiday season', '2024-08-02 00:00:00', 1, 10.00);
INSERT INTO promotion (created_at, created_by, status, updated_at, updated_by, code_promotion, end_at, name, note, start_at, type, value)
VALUES (NOW(6), 'Nguyễn Văn A', 'UPCOMING', NOW(6), 'Nguyễn Văn A', 'PROMO103', '2024-12-31 23:59:59', 'Holiday Sale', 'Discount for the holiday season', '2024-08-18 00:00:00', 1, 10.00);
INSERT INTO promotion (created_at, created_by, status, updated_at, updated_by, code_promotion, end_at, name, note, start_at, type, value)
VALUES (NOW(6), 'Nguyễn Văn A', 'FINISHED', NOW(6), 'Nguyễn Văn A', 'PROMO123', '2024-07-31 23:59:59', 'Holiday Sale', 'Discount for the holiday season', '2024-07-02 00:00:00', 1, 10.00);
INSERT INTO promotion (created_at, created_by, status, updated_at, updated_by, code_promotion, end_at, name, note, start_at, type, value)
VALUES (NOW(6), 'Nguyễn Văn A', 'ENDING_SOON', NOW(6), 'Nguyễn Văn A', 'PROMO123', '2024-08-31 23:59:59', 'Holiday Sale', 'Discount for the holiday season', '2024-08-02 00:00:00', 1, 10.00);

/*INSERT INTO promotion_detail */
INSERT INTO promotion_detail (created_at, created_by, status, updated_at, updated_by, promotion_price, id_product_detail, id_promotion)
VALUES (NOW(6), 'Nguyễn Văn A', 'ONGOING', NOW(6), 'Nguyễn Văn A', 99.99, 1, 1);

INSERT INTO promotion_detail (created_at, created_by, status, updated_at, updated_by, promotion_price, id_product_detail, id_promotion)
VALUES (NOW(6), 'Nguyễn Văn A', 'ONGOING', NOW(6), 'Nguyễn Văn A', 100.00, 2, 1);

INSERT INTO promotion_detail (created_at, created_by, status, updated_at, updated_by, promotion_price, id_product_detail, id_promotion)
VALUES (NOW(6), 'Nguyễn Văn A', 'ONGOING', NOW(6), 'Nguyễn Văn A', 99.89, 3, 1);

INSERT INTO promotion_detail (created_at, created_by, status, updated_at, updated_by, promotion_price, id_product_detail, id_promotion)
VALUES (NOW(6), 'Nguyễn Văn A', 'UPCOMING', NOW(6), 'Nguyễn Văn A', 99.79, 4, 2);

INSERT INTO promotion_detail (created_at, created_by, status, updated_at, updated_by, promotion_price, id_product_detail, id_promotion)
VALUES (NOW(6), 'Nguyễn Văn A', 'FINISHED', NOW(6), 'Nguyễn Văn A', 99.69, 5, 3);

INSERT INTO promotion_detail (created_at, created_by, status, updated_at, updated_by, promotion_price, id_product_detail, id_promotion)
VALUES (NOW(6), 'Nguyễn Văn A', 'ENDING_SOON', NOW(6), 'Nguyễn Văn A', 99.99, 6, 4);

/*INSERT INTO voucher */
INSERT INTO voucher (created_at, created_by, status, updated_at, updated_by, code_voucher, end_at, maximum_discount, min_bill_value, name, note, quantity, start_at, type, value)
VALUES (NOW(6), 'Nguyễn Văn A', 'ONGOING', NOW(6), 'Nguyễn Văn A', 'VOUCHER1', '2024-12-31 23:59:59', 500.00, 1000.00, 'Summer Sale Voucher', 'Voucher for summer sale', 100, '2024-08-01 00:00:00', 1, 10.00);
INSERT INTO voucher (created_at, created_by, status, updated_at, updated_by, code_voucher, end_at, maximum_discount, min_bill_value, name, note, quantity, start_at, type, value)
VALUES (NOW(6), 'Nguyễn Văn A', 'UPCOMING', NOW(6), 'Nguyễn Văn A', 'VOUCHER2', '2024-12-31 23:59:59', 500.00, 1000.00, 'Fall Sale Voucher', 'Voucher for fall sale', 100, '2024-08-01 00:00:00', 1, 10.00);
INSERT INTO voucher (created_at, created_by, status, updated_at, updated_by, code_voucher, end_at, maximum_discount, min_bill_value, name, note, quantity, start_at, type, value)
VALUES (NOW(6), 'Nguyễn Văn A', 'FINISHED', NOW(6), 'Nguyễn Văn A', 'VOUCHER3', '2024-04-30 23:59:59', 500.00, 1000.00, 'Spring Sale Voucher', 'Voucher for spring sale', 100, '2024-01-01 00:00:00', 1, 10.00);
INSERT INTO voucher (created_at, created_by, status, updated_at, updated_by, code_voucher, end_at, maximum_discount, min_bill_value, name, note, quantity, start_at, type, value)
VALUES (NOW(6), 'Nguyễn Văn A', 'ENDING_SOON', NOW(6), 'Nguyễn Văn A', 'VOUCHER4', '2024-07-31 23:59:59', 500.00, 1000.00, 'Summer2 Sale Voucher', 'Voucher for summer2 sale', 100, '2024-07-09 00:00:00', 1, 10.00);

/*INSERT INTO account_voucher */
INSERT INTO account_voucher (created_at, created_by, status, updated_at, updated_by, id_account, voucher_id)
VALUES (NOW(6), 'Nguyễn Văn A', 'ACTIVE', NOW(6), 'Nguyễn Văn A', 5, 1);
INSERT INTO account_voucher (created_at, created_by, status, updated_at, updated_by, date_of_use, id_account, voucher_id)
VALUES ('2024-04-27 23:59:59.000000', 'Nguyễn Văn A', 'ACTIVE', '2024-04-27 23:59:59.000000', 'Nguyễn Văn E', '2024-04-27 23:59:59.000000', 5, 3);

/*INSERT INTO bill */
INSERT INTO bill (created_at, created_by, status, updated_at, updated_by, address, name_customer, note, phone_number, price_discount, receive_date, total_amount, total_merchandise, type, id_customer)
VALUES (NOW(6), 'Nguyễn Văn E', 'PENDING ', NOW(6), 'Nguyễn Văn E', 'Phú Đô, Nam Từ Liêm, Hà Nội, Việt Nam', 'Trần Văn B', 'Giao hàng nhanh', '0123456789', 0, '2024-08-12 09:00:00', 3000.00, 3000.00, 1, 5);
INSERT INTO bill (created_at, created_by, status, updated_at, updated_by, address, name_customer, note, phone_number, price_discount, receive_date, total_amount, total_merchandise, type, id_customer)
VALUES (NOW(6), 'Nguyễn Văn E', 'PENDING ', NOW(6), 'Nguyễn Văn E', 'Phú Đô, Nam Từ Liêm, Hà Nội, Việt Nam', 'Trần Văn B', 'Giao hàng nhanh', '0123456789', 0, '2024-08-12 09:00:00', 3000.00, 3000.00, 1, 5);

INSERT INTO bill (created_at, created_by, status, updated_at, updated_by, address, name_customer, note, phone_number, price_discount, receive_date, total_amount, total_merchandise, type, id_customer, id_employees)
VALUES (NOW(6), 'Nguyễn Văn D', 'CONFIRMED', NOW(6), 'Nguyễn Văn A', 'Phú Đô, Nam Từ Liêm, Hà Nội, Việt Nam', 'Nguyễn Văn D', 'Giao hàng nhanh', '0123456789', 0, '2024-08-12 09:00:00', 3000.00, 3000.00, 1, 4, 1);
INSERT INTO bill (created_at, created_by, status, updated_at, updated_by, address, name_customer, note, phone_number, price_discount, receive_date, total_amount, total_merchandise, type, id_customer, id_employees)
VALUES (NOW(6), 'Nguyễn Văn D', 'CONFIRMED', NOW(6), 'Nguyễn Văn A', 'Phú Đô, Nam Từ Liêm, Hà Nội, Việt Nam', 'Nguyễn Văn D', 'Giao hàng nhanh', '0123456789', 0, '2024-08-12 09:00:00', 3000.00, 3000.00, 1, 4, 1);

INSERT INTO bill (created_at, created_by, status, updated_at, updated_by, address, name_customer, note, phone_number, price_discount, receive_date, total_amount, total_merchandise, type, id_customer, id_employees)
VALUES (NOW(6), 'Nguyễn Văn D', 'SHIPPED ', NOW(6), 'Nguyễn Văn A', 'Phú Đô, Nam Từ Liêm, Hà Nội, Việt Nam', 'Nguyễn Văn D', 'Giao hàng nhanh', '0123456789', 0, '2024-08-12 09:00:00', 3000.00, 3000.00, 1, 4, 1);
INSERT INTO bill (created_at, created_by, status, updated_at, updated_by, address, name_customer, note, phone_number, price_discount, receive_date, total_amount, total_merchandise, type, id_customer, id_employees)
VALUES (NOW(6), 'Nguyễn Văn D', 'SHIPPED ', NOW(6), 'Nguyễn Văn A', 'Phú Đô, Nam Từ Liêm, Hà Nội, Việt Nam', 'Nguyễn Văn D', 'Giao hàng nhanh', '0123456789', 0, '2024-08-12 09:00:00', 3000.00, 3000.00, 1, 4, 1);

INSERT INTO bill (created_at, created_by, status, updated_at, updated_by, address, name_customer, note, phone_number, price_discount, receive_date, total_amount, total_merchandise, type, id_customer, id_employees,delivery_date)
VALUES (NOW(6), 'Nguyễn Văn D', 'COMPLETED', NOW(6), 'Nguyễn Văn A', 'Phú Đô, Nam Từ Liêm, Hà Nội, Việt Nam', 'Nguyễn Văn D', 'Giao hàng nhanh', '0123456789', 0, '2024-08-12 09:00:00', 3000.00, 3000.00, 1, 4, 1,NOW(6));
INSERT INTO bill (created_at, created_by, status, updated_at, updated_by, address, name_customer, note, phone_number, price_discount, receive_date, total_amount, total_merchandise, type, id_customer, id_employees,delivery_date)
VALUES (NOW(6), 'Nguyễn Văn D', 'COMPLETED', NOW(6), 'Nguyễn Văn A', 'Phú Đô, Nam Từ Liêm, Hà Nội, Việt Nam', 'Nguyễn Văn D', 'Giao hàng nhanh', '0123456789', 0, '2024-08-12 09:00:00', 3000.00, 3000.00, 1, 4, 1,NOW(6));

INSERT INTO bill (created_at, created_by, status, updated_at, updated_by, address, name_customer, note, phone_number, price_discount, receive_date, total_amount, total_merchandise, type, id_customer)
VALUES (NOW(6), 'Nguyễn Văn E', 'CANCELLED ', NOW(6), 'Nguyễn Văn E', 'Phú Đô, Nam Từ Liêm, Hà Nội, Việt Nam', 'Trần Văn B', 'Giao hàng nhanh', '0123456789', 0, '2024-08-12 09:00:00', 1000.00, 1000.00, 1, 5);
INSERT INTO bill (created_at, created_by, status, updated_at, updated_by, address, name_customer, note, phone_number, price_discount, receive_date, total_amount, total_merchandise, type, id_customer)
VALUES (NOW(6), 'Nguyễn Văn E', 'CANCELLED ', NOW(6), 'Nguyễn Văn E', 'Phú Đô, Nam Từ Liêm, Hà Nội, Việt Nam', 'Trần Văn B', 'Giao hàng nhanh', '0123456789', 0, '2024-08-12 09:00:00', 1000.00, 1000.00, 1, 5);

/*INSERT INTO bill_detail */
INSERT INTO bill_detail (created_at, created_by, status, updated_at, updated_by, note, price_discount, quantity, id_bill, id_product_detail)
VALUES (NOW(6), 'Nguyễn Văn E', 'PENDING', NOW(6), 'Nguyễn Văn E', '', 1000.00, 3, 1, 7);
INSERT INTO bill_detail (created_at, created_by, status, updated_at, updated_by, note, price_discount, quantity, id_bill, id_product_detail)
VALUES (NOW(6), 'Nguyễn Văn E', 'PENDING', NOW(6), 'Nguyễn Văn E', '', 1000.00, 3, 2, 7);
INSERT INTO bill_detail (created_at, created_by, status, updated_at, updated_by, note, price_discount, quantity, id_bill, id_product_detail)
VALUES (NOW(6), 'Nguyễn Văn D', 'CONFIRMED', NOW(6), 'Nguyễn Văn A', '', 1000.00, 3, 3, 7);
INSERT INTO bill_detail (created_at, created_by, status, updated_at, updated_by, note, price_discount, quantity, id_bill, id_product_detail)
VALUES (NOW(6), 'Nguyễn Văn D', 'CONFIRMED', NOW(6), 'Nguyễn Văn A', '', 1000.00, 3, 4, 7);
INSERT INTO bill_detail (created_at, created_by, status, updated_at, updated_by, note, price_discount, quantity, id_bill, id_product_detail)
VALUES (NOW(6), 'Nguyễn Văn D', 'SHIPPED ', NOW(6), 'Nguyễn Văn A', '', 1000.00, 3, 5, 7);
INSERT INTO bill_detail (created_at, created_by, status, updated_at, updated_by, note, price_discount, quantity, id_bill, id_product_detail)
VALUES (NOW(6), 'Nguyễn Văn D', 'SHIPPED ', NOW(6), 'Nguyễn Văn A', '', 1000.00, 3, 6, 7);
INSERT INTO bill_detail (created_at, created_by, status, updated_at, updated_by, note, price_discount, quantity, id_bill, id_product_detail)
VALUES (NOW(6), 'Nguyễn Văn D', 'COMPLETED', NOW(6), 'Nguyễn Văn A', '', 1000.00, 3, 7, 7);
INSERT INTO bill_detail (created_at, created_by, status, updated_at, updated_by, note, price_discount, quantity, id_bill, id_product_detail)
VALUES (NOW(6), 'Nguyễn Văn D', 'COMPLETED', NOW(6), 'Nguyễn Văn A', '', 1000.00, 3, 8, 7);
INSERT INTO bill_detail (created_at, created_by, status, updated_at, updated_by, note, price_discount, quantity, id_bill, id_product_detail)
VALUES (NOW(6), 'Nguyễn Văn E', 'CANCELLED', NOW(6), 'Nguyễn Văn A', '', 1000.00, 1, 9, 7);
INSERT INTO bill_detail (created_at, created_by, status, updated_at, updated_by, note, price_discount, quantity, id_bill, id_product_detail)
VALUES (NOW(6), 'Nguyễn Văn E', 'CANCELLED', NOW(6), 'Nguyễn Văn A', '', 1000.00, 1, 10, 7);

/*INSERT INTO payment_method */
INSERT INTO payment_method (created_at, created_by, status, updated_at, updated_by, method_name, note)
VALUES (NOW(6), 'Nguyễn Văn A', 'ACTIVE', NOW(6), 'Nguyễn Văn A', 'QR payment', 'Thanh toán QR');
INSERT INTO payment_method (created_at, created_by, status, updated_at, updated_by, method_name, note)
VALUES (NOW(6), 'Nguyễn Văn A', 'ACTIVE', NOW(6), 'Nguyễn Văn A', 'Cash payment', 'Thanh toán tiền mặt');

/*INSERT INTO pay_bill */
INSERT INTO pay_bill (created_at, created_by, status, updated_at, updated_by, amount, note, trading_code, id_bill, id_payment_method)
VALUES (NOW(6), 'Nguyễn Văn E', 'PENDING_PAYMENT', NOW(6), 'Nguyễn Văn E', 3000.00, 'Chưa thanh toán', '', 1, 1);
INSERT INTO pay_bill (created_at, created_by, status, updated_at, updated_by, amount, note, trading_code, id_bill, id_payment_method)
VALUES (NOW(6), 'Nguyễn Văn E', 'PENDING_PAYMENT', NOW(6), 'Nguyễn Văn E', 3000.00, 'Chưa thanh toán', '', 2, 1);
INSERT INTO pay_bill (created_at, created_by, status, updated_at, updated_by, amount, note, trading_code, id_bill, id_payment_method)
VALUES (NOW(6), 'Nguyễn Văn D', 'COMPLETED', NOW(6), 'Nguyễn Văn D', 3000.00, 'Đã thanh toán', '', 3, 1);
INSERT INTO pay_bill (created_at, created_by, status, updated_at, updated_by, amount, note, trading_code, id_bill, id_payment_method)
VALUES (NOW(6), 'Nguyễn Văn D', 'PENDING_PAYMENT', NOW(6), 'Nguyễn Văn D', 3000.00, 'Chưa thanh toán', '', 4, 1);
INSERT INTO pay_bill (created_at, created_by, status, updated_at, updated_by, amount, note, trading_code, id_bill, id_payment_method)
VALUES (NOW(6), 'Nguyễn Văn D', 'COMPLETED', NOW(6), 'Nguyễn Văn D', 1500.00, 'Đã thanh toán', '', 5, 1);
INSERT INTO pay_bill (created_at, created_by, status, updated_at, updated_by, amount, note, trading_code, id_bill, id_payment_method)
VALUES (NOW(6), 'Nguyễn Văn D', 'COMPLETED', NOW(6), 'Nguyễn Văn D', 1500.00, 'Đã thanh toán', '', 5, 2);
INSERT INTO pay_bill (created_at, created_by, status, updated_at, updated_by, amount, note, trading_code, id_bill, id_payment_method)
VALUES (NOW(6), 'Nguyễn Văn D', 'PENDING_PAYMENT', NOW(6), 'Nguyễn Văn D', 3000.00, 'Chưa thanh toán', '', 6, 1);
INSERT INTO pay_bill (created_at, created_by, status, updated_at, updated_by, amount, note, trading_code, id_bill, id_payment_method)
VALUES (NOW(6), 'Nguyễn Văn D', 'COMPLETED', NOW(6), 'Nguyễn Văn D', 3000.00, 'Đã thanh toán', '', 7, 1);
INSERT INTO pay_bill (created_at, created_by, status, updated_at, updated_by, amount, note, trading_code, id_bill, id_payment_method)
VALUES (NOW(6), 'Nguyễn Văn D', 'COMPLETED', NOW(6), 'Nguyễn Văn D', 3000.00, 'Đã thanh toán', '', 8, 2);
INSERT INTO pay_bill (created_at, created_by, status, updated_at, updated_by, amount, note, trading_code, id_bill, id_payment_method)
VALUES (NOW(6), 'Nguyễn Văn E', 'PENDING_PAYMENT', NOW(6), 'Nguyễn Văn E', 1000.00, 'Chưa thanh toán', '', 9, 1);
INSERT INTO pay_bill (created_at, created_by, status, updated_at, updated_by, amount, note, trading_code, id_bill, id_payment_method)
VALUES (NOW(6), 'Nguyễn Văn E', 'COMPLETED', NOW(6), 'Nguyễn Văn E', 1000.00, 'Đã thanh toán', '', 10, 2);

