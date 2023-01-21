package pl.kul

import org.axonframework.commandhandling.CommandHandler
import org.axonframework.eventsourcing.EventSourcingHandler
import org.axonframework.modelling.command.AggregateIdentifier
import org.axonframework.modelling.command.AggregateLifecycle.apply
import org.axonframework.spring.stereotype.Aggregate

@Aggregate
class PrePaidPhone() {

    @AggregateIdentifier
    lateinit var phoneNumber: String
    lateinit var moneyOnAccount: Money
    lateinit var owner: CardOwner

    @CommandHandler
    constructor(command: BuyPrePaidCardCommand, validator: PrePaidPhoneValidator, numberProvider: PhoneNumberProvider) : this() {
        if (!validator.isValidCommand(command)) {
            throw ValidationException("Initial money cannot be less then zero")
        }
        apply(
            CardBoughtEvent(
                numberProvider.getNumber(),
                command.initialMoney.asMoneyWithLocalCurrency(),
                command.owner,
                DateTimeAdapter.now()
            )
        )
    }

    @EventSourcingHandler
    fun on(event: CardBoughtEvent) {
        phoneNumber = event.phoneNumber
        moneyOnAccount = event.initialMoney
        owner = event.owner
    }

    @CommandHandler
    fun handle(command: MakePhoneCallCommand, validator: PrePaidPhoneValidator, costCalculator: CallCostCalculator) {
        if (!validator.isValidCommandAndState(command, this)) {
            throw ValidationException("Could not make call because of no money on account")
        }
        apply(
            PhoneCallMadeEvent(
                phoneNumber,
                command.receiverPhoneNumber,
                command.callDuration,
                costCalculator.calculate(command.callerPhoneNumber, command.receiverPhoneNumber, command.callDuration),
                DateTimeAdapter.now()
            )
        )
    }

    @EventSourcingHandler
    fun on(event: PhoneCallMadeEvent) {
        moneyOnAccount -= event.callCost
    }

    @CommandHandler
    fun handle(command: SendSmsCommand, validator: PrePaidPhoneValidator, costCalculator: SmsCostCalculator) {
        if (!validator.isValidCommandAndState(command, this)) {
            throw ValidationException("Could not make call because of no money on account")
        }
        apply(
            SmsSentEvent(
                phoneNumber,
                command.receiverPhoneNumber,
                costCalculator.calculate(command.senderPhoneNumber, command.receiverPhoneNumber),
                DateTimeAdapter.now()
            )
        )
    }

    @EventSourcingHandler
    fun on(event: SmsSentEvent) {
        moneyOnAccount -= event.cost
    }

    @CommandHandler
    fun handle(command: TopUpCardCommand, validator: PrePaidPhoneValidator) {
        if (!validator.isValidCommand(command)) {
            throw ValidationException("Cannot top up account with amount less or equal then zero")
        }
        apply(CardTopUpEvent(phoneNumber, command.amount.asMoneyWithLocalCurrency(), DateTimeAdapter.now()))
    }

    @EventSourcingHandler
    fun on(event: CardTopUpEvent) {
        moneyOnAccount = moneyOnAccount.plus(event.amount)
    }
}