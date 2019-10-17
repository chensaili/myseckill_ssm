package org.myseckill.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.myseckill.dto.Exposer;
import org.myseckill.dto.SeckillExecution;
import org.myseckill.entity.Seckill;
import org.myseckill.entity.SuccessKilled;
import org.myseckill.exception.RepeatSeckillException;
import org.myseckill.exception.SeckillClosedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

import static org.junit.Assert.*;
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath:spring/spring-dao.xml",
                       "classpath:spring/spring-service.xml"})
public class SeckillServiceTest {
    private Logger logger= LoggerFactory.getLogger(this.getClass());
    @Autowired
    private SeckillService seckillService;
    @Test
    public void testGetSeckillList(){
        List<Seckill>list=seckillService.getSeckillList();
        logger.info("seckill={}",list);
    }
    @Test
    public void testGetById(){
        long id=1000;
        Seckill seckill=seckillService.getSeckillById(id);
        logger.info("seckill={}",seckill);
    }
    //集成测试
    @Test
    public void testSeckillService(){
        long seckillId=1002L;
        //通过检验商品是否存在和是否在秒杀时间内来决定是否可以进行秒杀
        Exposer exposer=seckillService.exportSeckill(seckillId);
        if(exposer.isExposed()){
            //可以秒杀
            String md5=exposer.getMd5();
            long userPhone=18852007146L;
            try{
                SeckillExecution seckillExecution=seckillService.executeSeckill(seckillId,userPhone,md5);
                logger.info("seckillExecution={}",seckillExecution);
            }catch (RepeatSeckillException e1){
                logger.error(e1.getMessage());
            }catch (SeckillClosedException e2){
                logger.error(e2.getMessage());
            }
        }else {
            logger.warn("exposer={}",exposer);
        }
    }
    //使用存储过程的测试
    @Test
    public void executeSeckillProdure(){
        long seckillId=1001;
        long phone=18852007143L;
        Exposer exposer=seckillService.exportSeckill(seckillId);
        if(exposer.isExposed()){
            String md5=exposer.getMd5();
           SeckillExecution execution= seckillService.executeSeckillProdure(seckillId,phone,md5);
            logger.info(execution.getStateInfo());
        }
    }
}