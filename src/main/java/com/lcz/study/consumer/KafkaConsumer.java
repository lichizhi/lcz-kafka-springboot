package com.lcz.study.consumer;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.KafkaListeners;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Component;

@Component
public class KafkaConsumer {

    // 同一组内多个消费者
    @KafkaListeners(value = {
            @KafkaListener(topics = "topic01", groupId = "g1"),
            @KafkaListener(topics = "topic01", groupId = "g1"),
            @KafkaListener(topics = "topic01", groupId = "g1")
    })
    public void listenGroup1MultiConsumer(ConsumerRecord<String, String> record) {
        System.out.println("g1 group multi consumer msg: " + record.value());
    }

    // 多个消费组
    @KafkaListener(topics = "topic01", groupId = "g2")
    public void listenGroup2(ConsumerRecord<String, String> record) {
        System.out.println("g2 group consumer msg: " + record.value());
    }

    // 将topic02的数据包装后发送到topic03
    @KafkaListener(topics = "topic02", groupId = "g1")
    @SendTo("topic03")
    public String listenGroupSendTo(ConsumerRecord<String, String> record) {
        System.out.println("g1 group topic02 consumer msg: " + record.value());

        return record.value() + "_lcz";
    }

    @KafkaListener(topics = "topic03", groupId = "g1")
    public void listenGroupTopic3(ConsumerRecord<String, String> record) {
        System.out.println("g1 group topic03 consumer msg: " + record.value());
    }
}
