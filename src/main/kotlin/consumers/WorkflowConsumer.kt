package event.observability.camunda.consumers

import event.observability.camunda.events.OrderStartedEvent
import org.camunda.bpm.engine.MismatchingMessageCorrelationException
import org.camunda.bpm.engine.RuntimeService
import org.slf4j.LoggerFactory
import org.springframework.amqp.rabbit.annotation.RabbitListener
import org.springframework.stereotype.Service

@Service
class WorkflowConsumer(val runtimeService: RuntimeService){

    private val logger = LoggerFactory.getLogger(javaClass)

    @RabbitListener(queues = ["camundaqueue"])
    fun receivedMessage(event: OrderStartedEvent) {
        println("Received Message From RabbitMQ: $event")

        try {
            val result = runtimeService.createMessageCorrelation("OrderStartedEvent")
                    .processInstanceBusinessKey(event.id)
                    .correlateWithResult()
            logger.info("Event $event is running with process instance ${result.processInstance}")
        } catch (e: MismatchingMessageCorrelationException) {
            logger.warn("Event $event couldn't be related with any workflow. Trying as  NOT_CORRELATED_MSGS")
            runtimeService.createMessageCorrelation("NOT_CORRELATED_MSGS")
                    .processInstanceBusinessKey(event.id)
                    .correlateWithResult()
        }
    }
}