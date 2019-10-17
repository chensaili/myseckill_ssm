package org.myseckill.dao;

import org.apache.ibatis.annotations.Param;
import org.myseckill.entity.Seckill;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface SeckillDao {
    /**
     * 减库存
     */
    int reduceNumber(@Param("seckillId") long seckillId, @Param("killTime")Date killTime);
    /**
     * 根据id查询秒杀商品
     */
    Seckill queryById(long seckillId);
    /**
     * 根据偏移量查询所有秒杀商品
     */
    List<Seckill> queryAll(@Param("offset")int offset,@Param("limit")int limit);

    /**
     * 使用存储过程执行秒杀
     */
    void killByProcedure(Map<String,Object> paraMap);
}
