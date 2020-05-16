create table hejia_order(
    id serial primary key,
    order_info_name varchar(255),
    state varchar(255),
    created_date bigint,
    order_code varchar(255) unique,
    receiver_name varchar(255),
    receiver_address varchar(255),
    receiver_phone varchar(255),
    pay_amount varchar(255),
    commodity_item_list text,
    jsonStr text,
    success_num int,
    sum_num int,
    active tinyint(1) default false ,
    created_at date,
    update_at date
)

create table order_type(
    id serial primary key,
    name varchar(255),
    code varchar(255),
    record varchar(1024)
)

create table upstream_order_state(
    id serial primary key,
    name varchar(255),
    code varchar(255),
    record varchar(1024)
)

create table bangjia_service_type(
    id serial primary key ,
    name varchar(255),
    code varchar(255),
    create_time date,
    update_time date
)

create table hejia_service_type(
    id serial primary key ,
    name varchar(255),
    code varchar(255),
    create_time date,
    update_time date
)