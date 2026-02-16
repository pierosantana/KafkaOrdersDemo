package com.pierosantana.producer;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;

import java.util.List;
import java.util.Properties;

public class OrderProducer {
    public static void main(String[] args) throws InterruptedException {
        // Configuración de Kafka
        Properties props = new Properties();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());

        KafkaProducer<String, String> producer = new KafkaProducer<>(props);

        List<String> nombres = List.of(
                "James", "Olivia", "Liam", "Emma", "Noah",
                "Ava", "William", "Sophia", "Benjamin", "Isabella"
        );

        for (int i = 8, j = 1; i < 23; i++,j++) {

        String orderId = String.valueOf(j);
        String orderDetails = "Pizza para " + nombres.get((int) (Math.random() * nombres.size())) +" a las " + (i) + ":00";
        Thread.sleep(300);

        ProducerRecord<String, String> record = new ProducerRecord<>("orders", orderId, orderDetails);

        producer.send(record, (metadata, exception) -> {
            if (exception == null) {
                System.out.println("Pedido enviado: " + orderDetails);
            } else {
                exception.printStackTrace();
            }
        });

        }
        producer.close();
    }
}
