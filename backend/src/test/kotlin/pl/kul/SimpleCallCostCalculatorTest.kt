package pl.kul

import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import java.math.BigDecimal
import java.time.Duration

internal class SimpleCallCostCalculatorTest {

    private val anyDuration = Duration.ofMinutes(15)
    private val calculator = SimpleCallCostCalculator()

    @Test
    fun `should return zero cost while same provider and any call duration`() {
        //given
//        same phone provider is while three first digits of the phone numbers are equal
        val caller = "123 456 789"
        val receiver = "123 489 785"
        //when
        val result = calculator.calculate(caller, receiver, anyDuration)
        //then
        Assertions.assertThat(result.amount).isEqualByComparingTo(BigDecimal.ZERO)
        Assertions.assertThat(result.currency.currencyCode).isEqualTo("PLN")
    }


    @Test
    internal fun `should return 3 PLN while caller and receiver not from same provider and call duration 15 minutes`() {
//        given
        val caller = "145 786 452"
        val receiver = "584 256 324"
        val duration = Duration.ofMinutes(15)
//        when
        val result = calculator.calculate(caller, receiver, duration)
//        then
        Assertions.assertThat(result.amount).isEqualByComparingTo(BigDecimal.valueOf(3))
        Assertions.assertThat(result.currency.currencyCode).isEqualTo("PLN")
    }

    @Test
    internal fun `should return 0,01 PLN while caller and receiver not from same provider and call duration 3 seconds`() {
        //        given
        val caller = "145 786 452"
        val receiver = "584 256 324"
        val duration = Duration.ofSeconds(3)
//        when
        val result = calculator.calculate(caller, receiver, duration)
//        then
        Assertions.assertThat(result.amount).isEqualByComparingTo(BigDecimal.valueOf(0.01))
        Assertions.assertThat(result.currency.currencyCode).isEqualTo("PLN")
    }

    @Test
    internal fun `should return 0,01 PLN while caller and receiver not from same provider and call duration 4 seconds`() {
        //        given
        val caller = "145 786 452"
        val receiver = "584 256 324"
        val duration = Duration.ofSeconds(4)
//        when
        val result = calculator.calculate(caller, receiver, duration)
//        then
        Assertions.assertThat(result.amount).isEqualByComparingTo(BigDecimal.valueOf(0.01))
        Assertions.assertThat(result.currency.currencyCode).isEqualTo("PLN")
    }

    @Test
    internal fun `should return 0,01 PLN while caller and receiver not from same provider and call duration 5 seconds`() {
        //        given
        val caller = "145 786 452"
        val receiver = "584 256 324"
        val duration = Duration.ofSeconds(5)
//        when
        val result = calculator.calculate(caller, receiver, duration)
//        then
        Assertions.assertThat(result.amount).isEqualByComparingTo(BigDecimal.valueOf(0.01))
        Assertions.assertThat(result.currency.currencyCode).isEqualTo("PLN")
    }

    @Test
    internal fun `should return 0,02 PLN while caller and receiver not from same provider and call duration 6 seconds`() {
        //        given
        val caller = "145 786 452"
        val receiver = "584 256 324"
        val duration = Duration.ofSeconds(6)
//        when
        val result = calculator.calculate(caller, receiver, duration)
//        then
        Assertions.assertThat(result.amount).isEqualByComparingTo(BigDecimal.valueOf(0.02))
        Assertions.assertThat(result.currency.currencyCode).isEqualTo("PLN")
    }
}