package com.sky.Task;

import com.sky.entity.Orders;
import com.sky.mapper.OrderMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Component
@Slf4j
public class OrderTask {

    @Autowired
    private OrderMapper orderMapper;


    //处理超时订单
    @Scheduled(cron = "0 * * * * ?")
    public void processTimeoutOrder(){
        log.info("处理超时订单,{}",new Date());
        LocalDateTime time = LocalDateTime.now().plusMinutes(-15);
        List<Orders> orderList = orderMapper.getByStatusAndOrderTimeLT(Orders.PENDING_PAYMENT,time);
        if(orderList != null && orderList.size() > 0){
            orderList.forEach(
                    order ->{
                        order.setStatus(Orders.CANCELLED);
                        order.setCancelReason("out time");
                        order.setCancelTime(LocalDateTime.now());
                        orderMapper.update(order);
                    }
            );
        }
    }

    //处理“派送中”的订单（每日凌晨1点）
    @Scheduled(cron = "0 0 1 * * ?")
    public void processDeliverOrder(){
        log.info("处理派送中订单:{}",new Date());
        LocalDateTime time = LocalDateTime.now().plusMinutes(-60);
        List<Orders> orderList = orderMapper.getByStatusAndOrderTimeLT(Orders.DELIVERY_IN_PROGRESS.intValue(), time);

        if(orderList != null){
            orderList.forEach( order ->{
                order.setStatus(Orders.COMPLETED);
                orderMapper.update(order);
            });
        }
    }
}
