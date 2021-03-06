CREATE DATABASE myseckill;
use myseckill;
-- 创建秒杀库存表

CREATE TABLE seckill(
`seckill_id` bigint NOT NULL AUTO_INCREMENT COMMENT '商品库存id',
`name` varchar(120) NOT NULL COMMENT '商品名称',
`number`int NOT NULL COMMENT '库存数量',
`create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
`start_time` timestamp NOT NULL COMMENT '秒杀开启时间',
`end_time` timestamp NOT NULL COMMENT '秒杀结束时间',
PRIMARY KEY(seckill_id),
KEY idx_start_time(start_time),
KEY idx_end_time(end_time),
KEY idx_create_time(create_time)
 )ENGINE=InnoDB AUTO_INCREMENT=1000 DEFAULT CHARSET=utf8 COMMENT ='秒杀库存表';

--往库存表存入数据
insert into
       seckill(name,number,start_time,end_time)
values
    ('1000秒杀iPhone6',100,'2016-01-01 00:00:00','2016-01-02 00:00:00'),
    ('500ipad',200,'2016-01-01 00:00:00','2016-01-02 00:00:00'),
    ('300xiaomi6',300,'2016-01-01 00:00:00','2016-01-02 00:00:00'),
    ('200hongmi',400,'2016-01-01 00:00:00','2016-01-02 00:00:00');


--秒杀成功明细表
CREATE TABLE success_killed(
`seckill_id` bigint NOT NULL COMMENT '商品库存id',
`user_phone` bigint NOT NULL COMMENT '用户手机号',
`state` tinyint NOT NULL DEFAULT -1 COMMENT '状态标示：-1：无效 0：成功 1：已付款 2：已发货 ',
`create_time` timestamp NOT NULL COMMENT '创建时间',
PRIMARY KEY(seckill_id,user_phone),
KEY idx_create_time(create_time)
)ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT ='秒杀成功明细表';