package event.observability.camunda.listeners

import org.camunda.bpm.engine.delegate.DelegateTask
import org.camunda.bpm.engine.delegate.TaskListener
import java.util.logging.Level
import java.util.logging.Logger
import javax.mail.*
import javax.mail.internet.InternetAddress
import javax.mail.internet.MimeMessage

class UserValidationListener : TaskListener {
    override fun notify(delegateTask: DelegateTask) {
        val taskId = delegateTask.id
        try {
            val properties = System.getProperties()
            properties["mail.smtp.host"] = HOST
            properties["mail.smtp.port"] = "465"
            properties["mail.smtp.ssl.enable"] = "true"
            properties["mail.smtp.auth"] = "true"

            // Get the Session object and pass username and password
            val session = Session.getInstance(properties, object : Authenticator() {
                override fun getPasswordAuthentication(): PasswordAuthentication {
                    return PasswordAuthentication(ORIGIN, ORIGIN_PASSWORD)
                }
            })

            val message = MimeMessage(session)
            message.setFrom(InternetAddress(ORIGIN))
            message.addRecipient(Message.RecipientType.TO, InternetAddress(RECIPIENT))
            message.subject = "Task assigned: " + delegateTask.name
            message.setText("An order has failed. Please review the task assigned: http://localhost:8080/app/tasklist/default/#/?task=$taskId")
            Transport.send(message)
            LOGGER.info("Task Assignment Email successfully sent to the email $RECIPIENT.")
        } catch (e: Exception) {
            LOGGER.log(Level.WARNING, "Email could not be sent", e)
        }
    }

    companion object {
        private const val HOST = "smtp.gmail.com"
        private const val ORIGIN = "eventobservability@gmail.com"
        private const val ORIGIN_PASSWORD = "EventObservability2020"
        private const val RECIPIENT = "eventobservability@gmail.com"
        private val LOGGER = Logger.getLogger(UserValidationListener::class.java.name)
    }
}