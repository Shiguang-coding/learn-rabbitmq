package com.shiguang.mq.stream;

import com.rabbitmq.stream.Environment;
import com.rabbitmq.stream.Producer;

import java.nio.charset.StandardCharsets;
import java.util.concurrent.CountDownLatch;

/**
 * Created By Shiguang On 2024/10/13 20:37
 */

public class TestProducer {
    public static void main(String[] args) throws InterruptedException {
        Environment environment = Environment.builder()
                .host("192.168.10.66")
                .port(33333)
                .username("shiguang")
                .password("123456")
                .build();

        Producer producer = environment.producerBuilder()
                .stream("stream.shiguang.test")
                .build();

        byte[] messagePayload = "hello rabbit stream".getBytes(StandardCharsets.UTF_8);

        CountDownLatch countDownLatch = new CountDownLatch(1);

        producer.send(
                producer.messageBuilder().addData(messagePayload).build(),
                confirmationStatus -> {
                    if (confirmationStatus.isConfirmed()) {
                        System.out.println("[生产者端]the message made it to the broker");
                    } else {
                        System.out.println("[生产者端]the message did not make it to the broker");
                    }
                    countDownLatch.countDown();
                });
        countDownLatch.await();
        producer.close();
        environment.close();
    }
}
