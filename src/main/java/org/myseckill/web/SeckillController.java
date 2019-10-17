package org.myseckill.web;

import org.myseckill.dto.Exposer;
import org.myseckill.dto.SeckillExecution;
import org.myseckill.dto.SeckillResult;
import org.myseckill.entity.Seckill;
import org.myseckill.enums.SeckillEnum;
import org.myseckill.exception.RepeatSeckillException;
import org.myseckill.exception.SeckillClosedException;
import org.myseckill.exception.SeckillException;
import org.myseckill.service.SeckillService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@Controller
@RequestMapping(value = "/myseckill")
public class SeckillController {
    //日志
    private Logger logger= LoggerFactory.getLogger(this.getClass());

    @Autowired
    private SeckillService seckillService;

    //获取商品列表
    @RequestMapping(value = "/list",method = RequestMethod.GET)
    public String list(Model model){
        List<Seckill>list=seckillService.getSeckillList();
        System.out.println(list);
        model.addAttribute("list",list);
        return "list";
    }
    //获取指定id的商品详情
    @RequestMapping(value = "/{seckillId}/detail",method = RequestMethod.GET)
    public String detail(@PathVariable("seckillId") Long seckillId, Model model){
        if(seckillId==null){
            return "redirect:/myseckill/list";
        }
        Seckill seckill=seckillService.getSeckillById(seckillId);
        if(seckill==null){
            return "forward:/myseckill/list";
        }
        model.addAttribute("seckill",seckill);
        return "detail";
    }

    //输出秒杀接口
    @RequestMapping(value = "/{seckillId}/exposer",method = RequestMethod.POST,
                    produces = "application/json;charset=UTF-8")
    @ResponseBody
    public SeckillResult<Exposer> exposer(@PathVariable("seckillId") Long seckillId, Model model){
        SeckillResult<Exposer>result;
        try{
            Exposer exposer=seckillService.exportSeckill(seckillId);
            result=new SeckillResult<Exposer>(true,exposer);
        }catch (Exception e){
            logger.error(e.getMessage(),e);
            result=new SeckillResult<Exposer>(false,e.getMessage());
        }
        return result;
    }

    //执行秒杀
    @RequestMapping(value = "/{seckillId}/{md5}/execution",method = RequestMethod.POST,
                    produces = "application/json;charset=UTF-8")
    @ResponseBody
    public SeckillResult<SeckillExecution>execution(@PathVariable("seckillId") Long seckillId,
                                                    @PathVariable("md5")String md5,
                                                    @CookieValue(value = "killPhone",required = false) Long phone){
        if(phone==null){
            //表示用户没有注册
            return new SeckillResult<SeckillExecution>(false,"用户未注册");
        }
        try{
            SeckillResult<SeckillExecution>result;
            //通过存储过程执行秒杀
            SeckillExecution seckillExecution=seckillService.executeSeckillProdure(seckillId,phone,md5);
            return new SeckillResult<SeckillExecution>(true,seckillExecution);
        }catch (RepeatSeckillException e1){
            SeckillExecution seckillExecution=new SeckillExecution(seckillId, SeckillEnum.REPEAT_KILL);
            return new SeckillResult<SeckillExecution>(true,seckillExecution);
        }catch (SeckillClosedException e2){
            SeckillExecution seckillExecution=new SeckillExecution(seckillId, SeckillEnum.END);
            return new SeckillResult<SeckillExecution>(true,seckillExecution);
        }catch (SeckillException e){
            logger.error(e.getMessage());
            SeckillExecution seckillExecution=new SeckillExecution(seckillId, SeckillEnum.INNER_ERROR);
            return new SeckillResult<SeckillExecution>(true,seckillExecution);
        }
    }
    //获取系统时间
    @RequestMapping(value = "/time/now",method = RequestMethod.GET,
            produces = "application/json;charset=UTF-8")
    @ResponseBody
    public SeckillResult<Long>time(){
        Date now=new Date();
        return new SeckillResult<Long>(true,now.getTime());
    }
}
