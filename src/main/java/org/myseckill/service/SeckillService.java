package org.myseckill.service;

import org.myseckill.dto.Exposer;
import org.myseckill.dto.SeckillExecution;
import org.myseckill.entity.Seckill;
import org.myseckill.exception.RepeatSeckillException;
import org.myseckill.exception.SeckillClosedException;
import org.myseckill.exception.SeckillException;

import java.util.List;
import java.util.Map;

/**
 * 从使用者的角度出发编写代码
 */
public interface SeckillService {
    /**
     * 查询所有秒杀商品
     */
    List<Seckill> getSeckillList();

    /**
     * 根据id查询商品
     */
    Seckill getSeckillById(long seckillId);

    /**
     * 判断是否可以执行秒杀
     */
    Exposer exportSeckill(long seckillId);

    /**
     * 执行秒杀
     */
    SeckillExecution executeSeckill(long seckillId, long userPhone, String md5)
            throws RepeatSeckillException, SeckillClosedException, SeckillException;

    /**
     * 使用存储过程
     * @param seckillId
     * @param userPhone
     * @param md5
     * @return
     */
    SeckillExecution executeSeckillProdure(long seckillId, long userPhone, String md5);
}
