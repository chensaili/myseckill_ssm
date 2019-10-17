package org.myseckill.dao.cache;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.myseckill.dao.SeckillDao;
import org.myseckill.entity.Seckill;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.*;
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath:spring/spring-dao.xml"})
public class RedisDaoTest {
    private long id=1003;
    @Autowired
    private RedisDao redisDao;
    @Autowired
    private SeckillDao seckillDao;
    @Test
    public void testSeckill() throws Exception{
        //从redis中取对象
        Seckill seckill=redisDao.getSeckill(id);
        if(seckill==null){
            //若redis中无，那么从数据库取，并将其放入redis缓存区
            seckill=seckillDao.queryById(id);
            if(seckill!=null){
                String result=redisDao.putSeckill(seckill);
                System.out.println(result);
                seckill=redisDao.getSeckill(id);
                System.out.println(seckill);
            }
        }else{
            System.out.println(seckill);
        }
    }

}