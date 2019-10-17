package org.myseckill.dao.cache;

import com.dyuproject.protostuff.LinkedBuffer;
import com.dyuproject.protostuff.ProtostuffIOUtil;
import com.dyuproject.protostuff.runtime.RuntimeSchema;
import org.myseckill.entity.Seckill;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

//Redis数据访问对象
public class RedisDao {
    private Logger logger= LoggerFactory.getLogger(this.getClass());
    private JedisPool jedisPool;
    public RedisDao(String ip,int port){
        jedisPool=new JedisPool(ip,port);
    }
    private RuntimeSchema<Seckill>schema=RuntimeSchema.createFrom(Seckill.class);
    public Seckill getSeckill(long seckillId){
        //过程：拿到字节数组，然后反序列化为seckill对象
        try{
            Jedis jedis=jedisPool.getResource();
            try {
                String key="seckill:"+seckillId;
                //采用自定义序列化
                byte[]bytes=jedis.get(key.getBytes());
                if(bytes!=null){
                    Seckill seckill=schema.newMessage();//空对象，用来装获取的Seckill
                    //按照schema把数据bytes传递到seckill中
                    //seckill被赋值了
                    ProtostuffIOUtil.mergeFrom(bytes,seckill,schema);
                    return seckill;
                }
            }finally {
                jedis.close();
            }
        }catch (Exception e){
            logger.error(e.getMessage(),e);
        }
        return null;
    }
    //这个方法就是把seckill对象传递到redis中
    public String putSeckill(Seckill seckill){
        //过程：把seckill对象序列化为一个字节数组，存储到Redis中
        try{
            Jedis jedis=jedisPool.getResource();
            try {
                String key="seckill:"+seckill.getSeckillId();
                byte[]bytes=ProtostuffIOUtil.toByteArray(seckill,schema,
                        LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE));
                //缓存时间
                int timeout=60*60;//一个小时
                //setex超时缓存
                String result=jedis.setex(key.getBytes(),timeout,bytes);
                return result;
            }finally {
                jedis.close();
            }
        }catch (Exception e){
            logger.error(e.getMessage(),e);
        }
        return  null ;
    }
}
