package com.example.jpapractice.api;

import com.example.jpapractice.domain.Address;
import com.example.jpapractice.domain.Order;
import com.example.jpapractice.domain.OrderStatus;
import com.example.jpapractice.repository.OrderRepository;
import com.example.jpapractice.repository.OrderSearch;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class OrderSimpleApiController {

    private final OrderRepository orderRepository;

    @GetMapping("/api/v1/simple-orders")
    public List<Order> orderV1() {
        List<Order> all = orderRepository.findAllByString(new OrderSearch());
        for(Order order : all) {
            order.getMember().getName();
            order.getDelivery().getAddress();
        }
        return all;
    }


    @GetMapping("/api/v2/simple-orders")
    public List<SimpleOrderDto> orderV2() {
        return orderRepository.findAllByString(new OrderSearch())
                .stream()
                .map(SimpleOrderDto::new)
                .collect(Collectors.toList());
    }

    @GetMapping("/api/v3/simple-orders")
    public List<SimpleOrderDto> orderV3() {
        return orderRepository.findAllWithMemberDelivery()
                .stream()
                .map(SimpleOrderDto::new)
                .collect(Collectors.toList());
    }

    @Data
    static class SimpleOrderDto {
        private Long orderId;
        private String name;
        private LocalDateTime orderDate;
        private OrderStatus orderStatus;
        private Address address;

        public SimpleOrderDto(Order o) {
            orderId = o.getId();
            name = o.getMember().getName();
            orderDate = o.getOrderDatetime();
            orderStatus = o.getStatus();
            address = o.getDelivery().getAddress();
        }
    }


}
