package org.myseckill.dao;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.myseckill.entity.Seckill;
import org.myseckill.entity.SuccessKilled;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;

import static org.junit.Assert.*;
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath:spring/spring-dao.xml"})
public class SuccessKilledDaoTest {
    @Resource
    private SuccessKilledDao successKilledDao;
    @Test
    public void testInsertSuccessKilled(){
        long seckillId=1003L;
        long userPhone=18852007141L;
        int i=successKilledDao.insertSuccessKilled(seckillId,userPhone);
        System.out.println(i);
    }
    @Test
    public void testQueryWithSeckillById(){
        long seckillId=1003L;
        long userPhone=18852007141L;
        SuccessKilled successKilled= successKilledDao.queryWithSeckillById(seckillId,userPhone);
        System.out.println(successKilled);
    }
}