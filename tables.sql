create table bangjia_service_type(
    id serial primary key ,
    name varchar(255),
    code varchar(255),
    created_at datetime,
    updated_at datetime
)

create table hejia_service_type(
    id serial primary key ,
    name varchar(255),
    code varchar(255),
    created_at datetime,
    updated_at datetime
)

create table bangjia_order(
    id serial primary key,
    hejia_order_code varchar(255),
    ordertype varchar(255),
    number_0 integer default 0,
    servicemode varchar(255),
    guarantee integer,
    guarantee_name varchar(255),
    originname varchar(255),
    factorynumber varchar(255),
    repairdate varchar(255),
    price double default 0,
    username varchar(255),
    mobile varchar(255),
    province varchar(255),
    city varchar(255),
    county varchar(255),
    town varchar(255),
    address varchar(255),
    mbuyprice double default 0,
    mbuydate varchar(255),
    createname varchar(255),
    note varchar(255),
    state integer,
    created_at datetime,
    updated_at datetime
)

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
    created_at datetime,
    update_at datetime
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

