package event.observability.camunda.events

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty

data class OrderStartedEvent @JsonCreator constructor(
        @JsonProperty("UserId") val userId: String,
        @JsonProperty("Id") val id: String,
        @JsonProperty("CreationDate") val creationDate: String
)