package pl.kul

import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import java.math.BigDecimal

internal class SimpleSmsCostCalculatorTest {

    private val calculator = SimpleSmsCostCalculator()

    @Test
    fun `should return zero cost while caller and receiver are same provider`() {
        //given
//        same phone provider is while three first digits of the phone numbers are equal
        val caller = "123 456 789"
        val receiver = "123 489 785"
        //when
        val result = calculator.calculate(caller, receiver)
        //then
        Assertions.assertThat(result.amount).isEqualByComparingTo(BigDecimal.ZERO)
        Assertions.assertThat(result.currency.currencyCode).isEqualTo("PLN")
    }

    @Test
    internal fun `should return cost 0 20 PLN while caller and receiver are not same provider`() {
//        given
        val caller = "123 456 789"
        val receiver = "456 456 789"
//        when
        val result = calculator.calculate(caller, receiver)
//        then
        Assertions.assertThat(result.amount).isEqualByComparingTo(BigDecimal("0.20"))
        Assertions.assertThat(result.currency.currencyCode).isEqualTo("PLN")
    }
}