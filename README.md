个人针对RabbitMq做的一些练习测试, 涵盖了RabbitMq的原生API的使用,以及与Spring-boot结合的使用。

**重点说明：**

   	1.	default exchange的规则, 当没有明确定义exchange的时候,也就是为空时, 路由规则是将发送到default exchange中的消息,路由到与routingKey名称相同的Queue中, 如果没有则丢弃消息.


RabbitMQ介绍:

RabbitMQ： 是一个基于AMQP协议，使用Erlang语言开发的企业级消息系统。    我们在项目中主要使用RabbitMQ来完成系统之间的消息通信，降低了系统之间的耦合性，异步执行提高了系统的运行效率。我们使用的是RabbitMQ中的Topic主题模式， 主要用于异步更新索引库与异步生成freemarker静态页面。我们首先需要定义工厂类，定义交换机exchange，并声明消息发送的队列queue，并将队列绑定到exchange中，由于是Topic模式所以消息发送就会携带routeKey,也就是路由键，我们的队列会定义pattern，也就是匹配消息的路由键（*或者#），如果匹配上了，exchange就将消息发送到该队列中。，使用spring整合RabbitMQ的方式，使用rabbitTemplate模板，发送商品ID以及routeKey配置监听器，如果监听到有消息的话，就获取body，也就是消息的内容，获取路由键，判断是删除还是新增，是删除的话就调用删除的逻辑保存就调用保存的逻辑。    生成freemarker页面，定义一个queue绑定到发送的同一个exchange中，pattern匹配进行精确匹配，只有当goods.add的时候才进行新增页面。
模式：    simple 简单队列， 也就是一对一的关系，一个发一个收（这三种采用的交换机是，NameLess  exchange  匿名交换机）    work queue 工作队列，也就是一个生产者， 对应多个消费者， 这多个消费者多事从同一个队列中取消息，采用轮循的方式。      fair  dispatch 公平分发，在工作队列基础之上，如果哪个消费者处理的较慢，就会少处理一些， 处理的快的就会多处理一些。前面都只是适用于在统一队列中取值的。    publish/subscribe  订阅与发布（使用Fanout exchange），一个生产者多个消费者，一个交换机，多个队列，一个消费者对应一个队列，消息发送到交换机，每个队列都需要绑定到交换机，发送消息经过交换机，转发给队列中。![img](file:///C:/Users/MY/AppData/Local/Temp/enhtmlclip/Image(1).png)![img](file:///C:/Users/MY/AppData/Local/Temp/enhtmlclip/Image(2).png)   路由模式( 使用direct exchange )         ![img](file:///C:/Users/MY/AppData/Local/Temp/enhtmlclip/Image(3).png)![img](file:///C:/Users/MY/AppData/Local/Temp/enhtmlclip/Image(4).png)        消息发送到交换机中，设置交换机模式为direct，也就是路由模式，一个消息发送的时候会携带一个路由键，消息发送到交换机中， 只有消息的路由键与队列完全匹配才会将该消息 发送到队列中，一个队列可以多次绑定到同一个交换机中，声明不同的匹配 路由键。    // 绑定队列到交换机channel.queueBind(QUEUE_NAME, EXCHANGE_NAME, "update");channel.queueBind(QUEUENAME, EXCHANGENAME, "delete"）        6   主题模式（使用Topic exchange）：         可以在队列中设置匹配规则时，对路由键进行模糊匹配（ # 匹配多个，*匹配一个 ）         ![img](file:///C:/Users/MY/AppData/Local/Temp/enhtmlclip/Image(5).png)![img](file:///C:/Users/MY/AppData/Local/Temp/enhtmlclip/Image(6).png)

消息应答机制：    默认是ack是true，也就是自动应答，一旦rabbitMq将消息发给消费者，就会立刻从内存中删掉消息，那么这样当消费者服务挂了，消息就会丢失了。设置为false，也就是手动应答basicAck 。RabbitMQ收到应答之后才会删除消息，而且同一时刻服务器只能发送一个消息。
持久化     保证消息与队列不会丢失，将durable设置为true即可。
RabbitMQ的消息确认机制（验证消息是否成功发出去了呢）：    事物与Confirm两种：    事务控制：        ![img](file:///C:/Users/MY/AppData/Local/Temp/enhtmlclip/Image(7).png)  发生异常可以channel。rollback();但是采用事务机制实现会降低RabbitMQ 的消息吞吐量，
Confirm：    ![img](file:///C:/Users/MY/AppData/Local/Temp/enhtmlclip/Image(8).png)1. 普通 confirm 模式：每发送一条消息后，调用 waitForConfirms()方法，等待服务器端confirm。实际上是一种串行 confirm 了。2. 批量 confirm 模式：每发送一批消息后，调用 waitForConfirms()方法，等待服务器端confirm。3. 异步 confirm 模式：提供一个回调方法，服务端 confirm 了一条或者多条消息后 Client 端会回调这个方法    项目之间可以进行可靠的异步通信，从而让多个程序可以并行执行，提高了系统的执行效率，也降低了系统之间的耦合性。
