package io.github.gustavosdelgado.microorder.controller;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @GetMapping
    public ResponseEntity<String> get() {

        Message message = new Message("hello from Order Service".getBytes());
        rabbitTemplate.send("order.created", message);
        return ResponseEntity.ok("hello");
    }

}
