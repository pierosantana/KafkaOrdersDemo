# KafkaOrdersDemo

Proyecto de ejemplo en **Java** que demuestra el funcionamiento básico de **Apache Kafka** con **producer y consumers**. Simula un sistema de pedidos de comida donde los mensajes se reparten automáticamente entre varios consumers de un mismo grupo.

---

## Estructura del proyecto

* `OrderProducer.java` → Envía mensajes de pedidos al topic `orders`.
* `OrderConsumer.java` → Recibe pedidos de Kafka.
* `OrderConsumerTwo.java` → Otro consumer que recibe pedidos del mismo grupo.

---

## Requisitos

* Java 21
* Maven
* Apache Kafka + Zookeeper (en macOS se recomienda instalar con Brew)

---

## Instalación de Kafka y Zookeeper en macOS

```bash
brew install kafka

brew install zookeeper
````

Esto instalará tanto **Kafka** como **Zookeeper** (necesario para pruebas locales).

---

## Levantar los servicios

```bash
brew services start zookeeper

brew services start kafka
```

## Detener los servicios

```bash
brew services stop zookeeper

brew services stop kafka
```

---

## Configuración del topic

Crea el topic `orders` con varias particiones para que los mensajes se repartan entre los consumers:

```bash
kafka-topics --create --topic orders --bootstrap-server localhost:9092 --partitions 4 --replication-factor 1
```

> Esto se hace **solo una vez** en la terminal.

---

## Cómo ejecutar

### 1. Ejecutar los consumers

* Se pueden ejecutar **en ventanas distintas del IDE** o **en terminales separadas**.
* Cada vez que hagas cambios en el código, **ejecuta `mvn clean compile`** antes de volver a correr.

Ejemplo terminal/IDE:

```bash
mvn compile exec:java -Dexec.mainClass="com.pierosantana.consumer.OrderConsumer"
mvn compile exec:java -Dexec.mainClass="com.pierosantana.consumer.OrderConsumerTwo"
```

### 2. Ejecutar el producer

* Una vez los consumers estén corriendo, ejecuta el producer:

```bash
mvn compile exec:java -Dexec.mainClass="com.pierosantana.producer.OrderProducer"
```

* Verás los pedidos enviados y cómo se reparten entre los consumers.

---

## Notas importantes

* Para que los mensajes se **repartan entre consumers**, todos deben usar **el mismo `group.id`**.
* La **distribución de mensajes** depende de las **particiones del topic**.
* Un mismo consumer **no puede leer de múltiples hilos de Kafka** directamente; si quieres procesamiento paralelo dentro de un consumer, se usan hilos internos solo para procesar los mensajes, no para leerlos.
* Este proyecto es un ejemplo **básico para aprender producer, consumer, topics y grupos en Kafka**.

---

## Esquema visual del reparto de mensajes

```
Topic 'orders' (4 particiones)
 ├─ Partición 0
 ├─ Partición 1
 ├─ Partición 2
 └─ Partición 3

Consumers (mismo group.id: "order-notifications")
 ├─ Consumer A --> particiones 0 y 1
 └─ Consumer B --> particiones 2 y 3
```

* Cada consumer recibe solo los mensajes de sus particiones.
* Si agregas más consumers, Kafka **reajusta automáticamente las particiones** entre ellos.

---
