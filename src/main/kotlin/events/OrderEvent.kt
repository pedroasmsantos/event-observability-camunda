package event.observability.camunda.events

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty
import java.util.*

data class OrderEvent @JsonCreator constructor(
        @JsonProperty("OrderId") val OrderId: String,
        @JsonProperty("OrderStatus") val OrderStatus: String?
)