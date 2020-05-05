create table hejia_order(
    id serial primary key,
    order_info_name varchar(255),
    created_date bigint,
    order_code varchar(255),
    receiver_name varchar(255),
    receiver_address varchar(255),
    receiver_phone varchar(255),
    order_num int ,--商品数量
    order_amount varchar(255),--订单总额
    preferential_amount varchar(255),--优惠金额
    receivableAmount varchar(255),--应收金额, ---
    pay_amount varchar(255),--实收金额,
    jsonStr text,
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