package org.myseckill.dao;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.myseckill.entity.Seckill;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;

import static org.junit.Assert.*;
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath:spring/spring-dao.xml"})
public class SeckillDaoTest {
    @Resource
    private SeckillDao seckillDao;
    @Test
    public void testQueryById(){
        long seckillId=1000L;
        Seckill seckill=seckillDao.queryById(seckillId);
        System.out.println(seckill);
    }

    @Test
    public void testQueryAll(){

    }

    @Test
    public void testReduceNumber(){

    }
}