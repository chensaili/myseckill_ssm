package org.myseckill.dao;

import org.apache.ibatis.annotations.Param;
import org.myseckill.entity.Seckill;
import org.myseckill.entity.SuccessKilled;

public interface SuccessKilledDao {
    /**
     * 插入购买明细
     */
    int insertSuccessKilled(@Param("seckillId")long seckillId,@Param("userPhone")long userPhone);

    /**
     * 根据id查询SuccessKilled并携带秒杀产品对象实体
     */
    SuccessKilled queryWithSeckillById(@Param("seckillId") long seckillId, @Param("userPhone")long userPhone);
}
