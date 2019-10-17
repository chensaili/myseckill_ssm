package org.myseckill.dto;

import java.util.Date;

public class Exposer {
    private boolean isExposed;
    private String md5;
    private long seckillId;
    private long nowTime;
    private long startTime;
    private long endTime;
    //根据需求创建构造方法
    //不可以秒杀（商品原因）
    public Exposer(boolean isExposed, long seckillId) {
        this.isExposed = isExposed;
        this.seckillId = seckillId;
    }
    //不可以秒杀(时间原因)
    public Exposer(boolean isExposed, long seckillId, long nowTime, long startTime, long endTime) {
        this.isExposed = isExposed;
        this.seckillId = seckillId;
        this.nowTime = nowTime;
        this.startTime = startTime;
        this.endTime = endTime;
    }
    //可以秒杀

    public Exposer(boolean isExposed, String md5, long seckillId) {
        this.isExposed = isExposed;
        this.md5 = md5;
        this.seckillId = seckillId;
    }

    public boolean isExposed() {
        return isExposed;
    }

    public void setExposed(boolean exposed) {
        isExposed = exposed;
    }

    public String getMd5() {
        return md5;
    }

    public void setMd5(String md5) {
        this.md5 = md5;
    }

    public long getSeckillId() {
        return seckillId;
    }

    public void setSeckillId(long seckillId) {
        this.seckillId = seckillId;
    }

    public long getNowTime() {
        return nowTime;
    }

    public void setNowTime(long nowTime) {
        this.nowTime = nowTime;
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public long getEndTime() {
        return endTime;
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }

    @Override
    public String toString() {
        return "Exposer{" +
                "isExposed=" + isExposed +
                ", md5='" + md5 + '\'' +
                ", seckillId=" + seckillId +
                ", nowTime=" + nowTime +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                '}';
    }
}
