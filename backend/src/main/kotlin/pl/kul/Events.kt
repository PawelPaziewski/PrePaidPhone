package pl.kul

import java.time.Duration

abstract class AbstractEvent(
    val timestamp: DateTimeAdapter
)

class CardBoughtEvent(
    val phoneNumber: String,
    val initialMoney: Money,
    val owner: CardOwner,
    timestamp: DateTimeAdapter,
) : AbstractEvent(timestamp)

class PhoneCallMadeEvent(
    val callerPhoneNumber: String,
    val receiverPhoneNumber: String,
    val callDuration: Duration,
    val callCost: Money,
    timestamp: DateTimeAdapter,
) : AbstractEvent(timestamp)

class CardTopUpEvent(
    val phoneNumber: String,
    val amount: Money,
    timestamp: DateTimeAdapter,
) : AbstractEvent(timestamp)

class SmsSentEvent(
    val senderPhoneNumber: String,
    val receiverPhoneNumber: String,
    val cost: Money,
    timestamp: DateTimeAdapter,
) : AbstractEvent(timestamp)
