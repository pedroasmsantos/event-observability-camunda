package event.observability.camunda.executors

import org.camunda.bpm.engine.delegate.DelegateExecution
import org.camunda.bpm.engine.delegate.JavaDelegate

class OutputValidationExecutor : JavaDelegate{
    override fun execute(execution: DelegateExecution?) {
        execution!!.setVariable("orderIsValid", true)
    }
}