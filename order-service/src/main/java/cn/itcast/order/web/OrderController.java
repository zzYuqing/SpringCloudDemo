package cn.itcast.order.web;

import cn.itcast.order.pojo.Order;
import cn.itcast.order.pojo.User;
import cn.itcast.order.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("order")
public class OrderController {

   @Autowired
   private OrderService orderService;
   @Autowired
   private RestTemplate restTemplate;

    @GetMapping("{orderId}")
    public Order queryOrderByUserId(@PathVariable("orderId") Long orderId) {
        // 根据id查询订单并返回
        Order order=orderService.queryOrderById(orderId);
        //可通过直接http访问
//        String url="http://localhost:8081/user/"+order.getUserId();
        // 将url改为目标服务名
        String url="http://user-service/user/"+order.getUserId();
        order.setUser(restTemplate.getForObject(url, User.class));

        return order;
    }
}
