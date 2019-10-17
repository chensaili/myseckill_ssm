package org.myseckill.service.ServiceImpl;

import org.apache.commons.collections.MapUtils;
import org.myseckill.dao.SeckillDao;
import org.myseckill.dao.SuccessKilledDao;
import org.myseckill.dto.Exposer;
import org.myseckill.dto.SeckillExecution;
import org.myseckill.entity.Seckill;
import org.myseckill.entity.SuccessKilled;
import org.myseckill.enums.SeckillEnum;
import org.myseckill.exception.RepeatSeckillException;
import org.myseckill.exception.SeckillClosedException;
import org.myseckill.exception.SeckillException;
import org.myseckill.service.SeckillService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class SeckillServiceImpl implements SeckillService {
    //日志
    private Logger logger= LoggerFactory.getLogger(this.getClass());
    @Autowired
    private SeckillDao seckillDao;
    @Autowired
    private SuccessKilledDao successKilledDao;

    //md5盐值
    String slat="duj3i2hdisu2eh90kxnd;1[s]w3ld";

    public List<Seckill> getSeckillList() {
        List<Seckill>list=seckillDao.queryAll(0,4);
        return list;
    }

    public Seckill getSeckillById(long seckillId) {
        Seckill seckill=seckillDao.queryById(seckillId);
        return seckill;
    }

    public Exposer exportSeckill(long seckillId) {
        Seckill seckill=seckillDao.queryById(seckillId);
        if(seckill==null){
            //表示不存在此id的商品
            return new Exposer(false,seckillId);
        }
        //存在此id商品
        Date startTime=seckill.getStartTime();
        Date endTime=seckill.getEndTime();
        Date nowTime=new Date();
        if(nowTime.getTime()<startTime.getTime()||nowTime.getTime()>endTime.getTime()){
            //表示时间不处于可秒杀的时间段内
            return new Exposer(false,seckillId,nowTime.getTime(),startTime.getTime(),endTime.getTime());
        }else{
            //可以秒杀
            String md5=getMd5(seckillId);
            return new Exposer(true,md5,seckillId);
        }
    }
    //获取md5
    private String getMd5(long seckillId){
        String base=seckillId+"/"+slat;
        String md5= DigestUtils.md5DigestAsHex(base.getBytes());
        return md5;
    }
    //执行秒杀
    @Transactional
    public SeckillExecution executeSeckill(long seckillId, long userPhone, String md5)
            throws RepeatSeckillException, SeckillClosedException, SeckillException {
        if(md5==null||!md5.equals(getMd5(seckillId))){
            throw new SeckillException("seckill data rewrite");
        }
        Date nowTime = new Date();
        //为了减小行级锁的时间，将减库存和记录购买行为调换位置
        try{
            //记录购买行为
            int insertCount = successKilledDao.insertSuccessKilled(seckillId, userPhone);
            if(insertCount<=0){
                throw new RepeatSeckillException("seckill is repeated");
            }else {
                //减库存
                int updateCount=seckillDao.reduceNumber(seckillId,nowTime);
                if(updateCount<=0) {
                    //没有更新到记录，秒杀结束,rollback
                    throw new SeckillClosedException("seckill is closed");
                }else {
                    //秒杀成功，commit
                    SuccessKilled successKilled=successKilledDao.queryWithSeckillById(seckillId,userPhone);
                    return new SeckillExecution(seckillId,SeckillEnum.SUCCESS,successKilled);
                }
            }
        }catch (RepeatSeckillException e1){
            throw e1;
        }catch (SeckillClosedException e2){
            throw e2;
        }catch (Exception e3){
            throw new SeckillException("seckill inner error:"+ e3.getMessage());
        }
        /*try {
            Date nowTime = new Date();
            int updateCount = seckillDao.reduceNumber(seckillId, nowTime);
            //减库存
            if (updateCount <= 0) {
                throw new SeckillClosedException("seckill is closed");
            } else {
                //记录购买行为
                int insertCount = successKilledDao.insertSuccessKilled(seckillId, userPhone);
                if (insertCount <= 0) {
                    throw new RepeatSeckillException("seckill is repeated");
                } else {
                    //秒杀成功
                    SuccessKilled successKilled = successKilledDao.queryWithSeckillById(seckillId, userPhone);
                    return new SeckillExecution(seckillId, SeckillEnum.SUCCESS,successKilled);
                }
            }
        }catch (RepeatSeckillException e1){
            throw e1;
        }catch (SeckillClosedException e2){
            throw e2;
        }catch (SeckillException e3){
            throw e3;
        }*/
    }

    /**
     * 使用存储过程执行秒杀
     * @param seckillId
     * @param userPhone
     * @param md5
     * @return
     * @throws RepeatSeckillException
     * @throws SeckillClosedException
     * @throws SeckillException
     */
    @Transactional
    public SeckillExecution executeSeckillProdure(long seckillId, long userPhone, String md5) throws RepeatSeckillException, SeckillClosedException, SeckillException {
        if (md5==null||!md5.equals(getMd5(seckillId))){
            return new SeckillExecution(seckillId,SeckillEnum.DATA_REWRITE);
        }
        Date killTime =new Date();
        Map<String,Object> map=new HashMap<String, Object>();
        map.put("seckillId",seckillId);
        map.put("phone",userPhone);
        map.put("killTime",killTime);
        map.put("result",null);
        try{
            //执行存储过程之后result被赋值
            seckillDao.killByProcedure(map);
            //获取result
            //下面语句的意思：若目标值"result"依旧为空，那么返回-2
           int result= MapUtils.getInteger(map,"result",-2);
            if(result==1){
                SuccessKilled sk=successKilledDao.
                        queryWithSeckillById(seckillId,userPhone);
                return new SeckillExecution(seckillId,SeckillEnum.SUCCESS,sk);
            }else {
                return new SeckillExecution(seckillId,SeckillEnum.stateof(result));
            }
        }catch (Exception e){
            logger.error(e.getMessage(),e);
            return new SeckillExecution(seckillId,SeckillEnum.INNER_ERROR);
        }
    }
}
