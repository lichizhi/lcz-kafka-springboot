package com.lcz.study.producer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/kafka")
public class KafkaProducerController {

    private final static String topic = "topic01";

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @PostMapping("send")
    public String send(@RequestParam("msg") String msg) {
        kafkaTemplate.send(topic, "lcz-msg", msg);
        return String.format("消息 %s 发送成功！", msg);
    }

    @PostMapping("send/transaction1")
    public String send1(@RequestParam("msg") String msg) {
        return kafkaTemplate.executeInTransaction(kafkaOperations -> {
            kafkaOperations.send(topic, "trans_key", msg);
            kafkaOperations.send(topic, "trans_key2", msg);
            kafkaOperations.send(topic, "trans_key3", msg);

            return String.format("事务消息 %s 发送成功！", msg);
        });
    }

    @Transactional
    @PostMapping("send/transaction2")
    public String send2(@RequestParam("msg") String msg) {
        kafkaTemplate.send(topic, "trans_key", msg);
        kafkaTemplate.send(topic, "trans_key2", msg);
        kafkaTemplate.send(topic, "trans_key3", msg);

        return String.format("事务消息 %s 发送成功！", msg);
    }
}
