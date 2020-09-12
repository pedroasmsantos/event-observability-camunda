/*package event.observability.camunda.consumers

import org.camunda.bpm.engine.ProcessEngine;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
class WorkflowConsumer{
    @Autowired
    private var camunda: ProcessEngine? = null

    fun WorkflowConsumer(){

    }

    fun WorkflowConsumer(camunda: ProcessEngine?) {
        this.camunda = camunda
    }

    @RabbitListener(queues = ["camundaqueue"])
    @Transactional
    fun propagate(message: String) {
        System.out.println("hello :)")

        camunda?.runtimeService?.createMessageCorrelation(message)
                ?.processInstanceBusinessKey(message)
                ?.correlateWithResult()
    }
}*/