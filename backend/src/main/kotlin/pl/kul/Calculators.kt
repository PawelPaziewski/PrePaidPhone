package pl.kul

import org.springframework.stereotype.Component
import java.math.BigDecimal
import java.time.Duration

val SMS_COST = BigDecimal("0.20")

class CallCost {
    companion object {
        val minimalDuration: Duration = Duration.ofSeconds(3)
        val cost = Money.withDefaultCurrency().fromAmountAsString("0.01")
    }
}

abstract class CostCalculator {
    protected fun samePhoneProvider(caller: String, receiver: String) =
        caller.substring(0, 2) == receiver.substring(0, 2)
}

interface CallCostCalculator {
    fun calculate(caller: String, receiver: String, callDuration: Duration): Money
}

@Component
class SimpleCallCostCalculator : CallCostCalculator, CostCalculator() {
    override fun calculate(caller: String, receiver: String, callDuration: Duration) =
        if (samePhoneProvider(caller, receiver)) {
            BigDecimal.ZERO.asMoneyWithLocalCurrency()
        } else {
            CallCost.cost.multiply(callDuration.dividedBy(CallCost.minimalDuration))
        }
}

interface SmsCostCalculator {
    fun calculate(caller: String, receiver: String): Money
}

@Component
class SimpleSmsCostCalculator : SmsCostCalculator, CostCalculator() {
    override fun calculate(caller: String, receiver: String) =
        if (samePhoneProvider(caller, receiver)) BigDecimal.ZERO.asMoneyWithLocalCurrency()
        else SMS_COST.asMoneyWithLocalCurrency()
}