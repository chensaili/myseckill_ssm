package org.myseckill.dto;

import org.myseckill.entity.SuccessKilled;
import org.myseckill.enums.SeckillEnum;

/**
 * 在dto包中创建SeckillExecution.java，用于封装秒杀是否成功的结果（该对象用来返回给页面）
 */
public class SeckillExecution {
    private long seckillId;
    private int state;
    private String stateInfo;
    //秒杀成功对象
    private SuccessKilled successKilled;
    //构造方法
    //秒杀失败
    public SeckillExecution(long seckillId, SeckillEnum enums) {
        this.seckillId = seckillId;
        this.state = enums.getState();
        this.stateInfo = enums.getStateInfo();
    }
    //秒杀成功
    public SeckillExecution(long seckillId, SeckillEnum enums, SuccessKilled successKilled) {
        this.seckillId = seckillId;
        this.state = enums.getState();
        this.stateInfo = enums.getStateInfo();
        this.successKilled = successKilled;
    }

    public long getSeckillId() {
        return seckillId;
    }

    public void setSeckillId(long seckillId) {
        this.seckillId = seckillId;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getStateInfo() {
        return stateInfo;
    }

    public void setStateInfo(String stateInfo) {
        this.stateInfo = stateInfo;
    }

    public SuccessKilled getSuccessKilled() {
        return successKilled;
    }

    public void setSuccessKilled(SuccessKilled successKilled) {
        this.successKilled = successKilled;
    }

    @Override
    public String toString() {
        return "SeckillExecution{" +
                "seckillId=" + seckillId +
                ", state=" + state +
                ", stateInfo='" + stateInfo + '\'' +
                ", successKilled=" + successKilled +
                '}';
    }
}
