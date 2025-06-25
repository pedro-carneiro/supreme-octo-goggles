create schema recipe;

create table recipe.carts
(
    id bigserial not null,
    total_in_cents bigint not null,
    primary key (id)
);

create table recipe.products
(
    id bigserial not null,
    name varchar not null,
    price_in_cents bigint not null,
    primary key (id)
);

create table recipe.recipes
(
    id bigserial not null,
    name varchar not null,
    primary key (id)
);

create table recipe.recipe_items
(
    id bigserial not null,
    recipe_id bigint not null,
    product_id bigint not null,
    quantity int not null,
    primary key (id),
    foreign key(recipe_id) references recipe.recipes (id) on delete cascade,
    foreign key(product_id) references recipe.products (id) on delete cascade
);

create table recipe.cart_items
(
    id bigserial not null,
    cart_id bigint not null,
    product_id bigint not null,
    quantity int not null,
    primary key (id),
    foreign key(cart_id) references recipe.carts (id) on delete cascade,
    foreign key(product_id) references recipe.products (id) on delete cascade
);
