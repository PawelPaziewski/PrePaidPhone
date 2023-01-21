package pl.kul

import org.springframework.stereotype.Component
import java.math.BigDecimal

interface PrePaidPhoneValidator {
    fun isValidCommand(command: BuyPrePaidCardCommand): Boolean
    fun isValidCommand(command: TopUpCardCommand): Boolean
    fun isValidCommandAndState(command: MakePhoneCallCommand, state: PrePaidPhone): Boolean
    fun isValidCommandAndState(command: SendSmsCommand, state: PrePaidPhone): Boolean
}

@Component
class PrePaidPhoneValidatorImpl(
    private val callCostCalculator: CallCostCalculator,
    private val smsCostCalculator: SmsCostCalculator,
) : PrePaidPhoneValidator {

    override fun isValidCommand(command: BuyPrePaidCardCommand) = command.initialMoney >= BigDecimal.ZERO;
    override fun isValidCommand(command: TopUpCardCommand) = command.amount > BigDecimal.ZERO
    override fun isValidCommandAndState(command: SendSmsCommand, state: PrePaidPhone) =
        state.moneyOnAccount >= smsCostCalculator.calculate(command.senderPhoneNumber, command.receiverPhoneNumber)

    override fun isValidCommandAndState(command: MakePhoneCallCommand, state: PrePaidPhone) =
        state.moneyOnAccount >= callCostCalculator.calculate(command.callerPhoneNumber,
            command.receiverPhoneNumber,
            command.callDuration)
}