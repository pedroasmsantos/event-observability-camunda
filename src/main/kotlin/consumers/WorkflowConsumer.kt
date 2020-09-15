package event.observability.camunda.consumers

import com.fasterxml.jackson.databind.ObjectMapper
import event.observability.camunda.events.OrderEvent
import org.camunda.bpm.engine.MismatchingMessageCorrelationException
import org.camunda.bpm.engine.RuntimeService
import org.slf4j.LoggerFactory
import org.springframework.amqp.rabbit.annotation.RabbitListener
import org.springframework.amqp.support.AmqpHeaders
import org.springframework.messaging.handler.annotation.Header
import org.springframework.stereotype.Service

@Service
class WorkflowConsumer(val runtimeService: RuntimeService){

    private val logger = LoggerFactory.getLogger(javaClass)

    @RabbitListener(queues = ["camundaqueue"])
    fun receivedMessage(event: OrderEvent, @Header(AmqpHeaders.RECEIVED_ROUTING_KEY) routingKey: String/*, messageProperties: MessageProperties*/) {
        println("Received the message $event due to the binding $routingKey")

        val jsonString: String = ObjectMapper().writeValueAsString(event)

        try {
            val result = runtimeService.createMessageCorrelation(routingKey)
                    .processInstanceBusinessKey(event.OrderId)
                    .setVariable("payload", jsonString)
                    .correlateWithResult()
            logger.info("Event $event is running with process instance ${result.processInstance}.")
        } catch (e: MismatchingMessageCorrelationException) {
            logger.warn("Event $event couldn't be related with any workflow. Will be correlated to the error process.")
            runtimeService.createMessageCorrelation("NOT_CORRELATED_MSGS")
                    .processInstanceBusinessKey(event.OrderId)
                    .setVariable("payload", jsonString)
                    .correlateWithResult()
        }
    }
}