package event.observability.camunda.configurations

import org.springframework.amqp.core.Queue
import org.springframework.amqp.rabbit.connection.ConnectionFactory
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.amqp.rabbit.listener.RabbitListenerEndpointRegistrar
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.messaging.converter.MappingJackson2MessageConverter
import org.springframework.messaging.handler.annotation.support.DefaultMessageHandlerMethodFactory

@Configuration
class RabbitMQConfig{
    val queueName = "camundaqueue"

    @Bean
    fun queue(): Queue? {
        return Queue(queueName, true)
    }

    /*@Bean
    fun container(connectionFactory: ConnectionFactory?,
                  listenerAdapter: MessageListenerAdapter?): SimpleMessageListenerContainer? {
        val container = SimpleMessageListenerContainer()
        container.connectionFactory = connectionFactory
        container.setQueueNames(queueName)
        container.setMessageListener(listenerAdapter)

        return container
    }*/

    @Bean
    fun rabbitTemplate(connectionFactory: ConnectionFactory?): RabbitTemplate? {
        val rabbitTemplate = RabbitTemplate(connectionFactory)
        rabbitTemplate.messageConverter = producerJackson2MessageConverter()
        return rabbitTemplate
    }

    @Bean
    fun producerJackson2MessageConverter(): Jackson2JsonMessageConverter? {
        return Jackson2JsonMessageConverter()
    }

    @Bean
    fun myHandlerMethodFactory(): DefaultMessageHandlerMethodFactory? {
        val factory = DefaultMessageHandlerMethodFactory()
        factory.setMessageConverter(MappingJackson2MessageConverter())
        return factory
    }

    @Override
    fun configureRabbitListeners(registrar: RabbitListenerEndpointRegistrar) {
        registrar.messageHandlerMethodFactory = myHandlerMethodFactory()
    }

    /*@Bean
    fun listenerAdapter(receiver: WorkflowConsumer?): MessageListenerAdapter? {
        return MessageListenerAdapter(receiver, "receiveMessage")
    }*/

}