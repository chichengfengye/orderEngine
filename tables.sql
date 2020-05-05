create table hejia_order(
    id serial primary key,
    created_date bigint,
    state varchar(255),
    receiver_name varchar(255),
    receiver_address varchar(255),
    receiver_phone varchar(255),
    order_num int ,--商品数量
    transport_cost varchar(255),--配送费用
    order_amount varchar(255),--订单总额
    preferential_amount varchar(255),--优惠金额
    receivableAmount varchar(255),--应收金额, ---
    pay_amount varchar(255),--实收金额,
    jsonStr text,
    active tinyint,
    created_at date,
    update_at date
)