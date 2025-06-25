-- products
insert into recipe.products (name, price_in_cents) values
('salt', 25),
('pepper', 200),
('eggs', 300),
('cheese', 500),
('beef', 1500);

-- recipes
insert into recipe.recipes (name) values
('boiled eggs'),
('fried eggs'),
('cheeseburger');

insert into recipe.recipe_items (recipe_id, product_id, quantity) values
(1, 3, 1),
(2, 1, 1),
(2, 3, 1),
(3, 4, 1),
(3, 5, 1);

-- carts
insert into recipe.carts (total_in_cents) values
(0),
(0),
(0);
