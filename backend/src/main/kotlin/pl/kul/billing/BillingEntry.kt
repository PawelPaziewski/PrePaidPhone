package pl.kul.billing

import org.springframework.data.annotation.Id
import pl.kul.DateTimeAdapter
import pl.kul.Money
import java.time.Duration

data class BillingEntry constructor(
    @Id val id: String?,
    val phoneNumber: String,
    val type: BillingEntryType,
    val timestamp: DateTimeAdapter,
    val cost: Money,
    val receiver: String,
    val duration: Duration?,
) {
    constructor(
        phoneNumber: String,
        type: BillingEntryType,
        timestamp: DateTimeAdapter,
        cost: Money,
        receiver: String,
        duration: Duration?,
    ) : this(null, phoneNumber, type, timestamp, cost, receiver, duration)
}

enum class BillingEntryType {
    CALL, SMS
}
