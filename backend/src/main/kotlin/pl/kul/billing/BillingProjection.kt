package pl.kul.billing

import org.axonframework.eventhandling.EventHandler
import org.axonframework.queryhandling.QueryHandler
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import pl.kul.GetBillingQuery
import pl.kul.PhoneCallMadeEvent
import pl.kul.SmsSentEvent

@Service
class BillingProjection @Autowired constructor(private val repository: BillingRepository) {

    @EventHandler
    fun handle(event: SmsSentEvent) {
        repository.save(BillingEntry(event.senderPhoneNumber,
            BillingEntryType.SMS,
            event.timestamp,
            event.cost,
            event.receiverPhoneNumber,
            null))
    }

    @EventHandler
    fun handle(event: PhoneCallMadeEvent) {
        repository.save(BillingEntry(event.callerPhoneNumber,
            BillingEntryType.CALL,
            event.timestamp,
            event.callCost,
            event.receiverPhoneNumber,
            event.callDuration))
    }

    @QueryHandler
    fun on(query: GetBillingQuery): List<BillingEntry> {
        return repository.findAllByPhoneNumber(query.phoneNumber)
    }
}