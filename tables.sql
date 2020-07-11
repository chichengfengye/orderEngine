create table llj_order(
    id serial primary key,
    uuid varchar(255) unique COMMENT '源商家的ID',
    protocol int COMMENT '协议，0 http 1 https',
    url varchar(255) COMMENT '请求网址（不包括协议在内的地址）',
    type int COMMENT '请求类型，0 get，1 post',
    meta_type int COMMENT '参数类型，0 application/json, 1 application/x-www-form-urlencoded, 2 multipart/form-data ',
    args text COMMENT '参数key，json',
    created_at datetime,
    updated_at datetime
) COMMENT = '下游帮家的订单表，上游的数据会被转换到此表，然后程序读取该表中的数据创建订单。'

create table merchent_req(
    id serial primary key,
    uuid varchar(255) unique COMMENT '商家的唯一ID',
    protocol int COMMENT '协议，0 http 1 https',
    url varchar(255) COMMENT '请求网址（不包括协议在内的地址）',
    type int COMMENT '请求类型，0 get，1 post',
    meta_type int COMMENT '参数类型，0 application/json, 1 application/x-www-form-urlencoded, 2 multipart/form-data ',
    args text COMMENT '参数key，json',
    created_at datetime,
    updated_at datetime
) COMMENT = '上游商家请求信息'

create table entity_map(
    id serial primary key,
    uuid varchar(255) unique COMMENT '商家的唯一ID，将字段按照商家分类.这些字段组合后即为某个商家的订单字段',
    field varchar(255) unique COMMENT '字段原始名称',
    field_map varchar(255) COMMENT '映射名称',
    resolve_func varchar(255) COMMENT '解析函数位置',
    created_at datetime,
    updated_at datetime
) COMMENT = '上游与下游的订单实体映关系表，订单的字段应该支持mybatis修改表结构'


create table merchant(
    id serial primary  key,
    uuid varchar(255) unique COMMENT '唯一ID',
    nick_name varchar(255) COMMENT '商家名称',
    table_name varchar(255) COMMENT '该商家对应的实体数据表的核心名称关键字',
    status int COMMENT '商家状态，0 禁用 1启用',
    created_at datetime,
    updated_at datetime
) COMMENT = '上游商家表，存储商家基本信息以及启用状态'


/****************************** 2020.07.04**********************************/
create table lvdi_order(
    id serial primary  key,
    order_id varchar(255) COMMENT '订单ID',
    order_no varchar(255) COMMENT '订单编号',
    add_time varchar(255) COMMENT '下单时间',
    receiver_name varchar(255) COMMENT '收件人',
    receiverMobile int COMMENT '收件号码',
    receiver_address varchar(255) COMMENT '收件地址',
    status int COMMENT '订单状态',
    created_at datetime,
    updated_at datetime
)

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
/**^^^^^^^^^^^^^^^^^^^^ 2020.07.04 ^^^^^^^^^^^^^^^^^^^^^***/

