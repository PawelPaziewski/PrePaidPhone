package pl.kul

import org.assertj.core.api.Assertions
import org.axonframework.test.aggregate.AggregateTestFixture
import org.axonframework.test.aggregate.FixtureConfiguration
import org.axonframework.test.matchers.Matchers.*
import org.hamcrest.Matchers.instanceOf
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.eq
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever
import java.math.BigDecimal
import java.time.Duration
import kotlin.reflect.KClass

internal class PrePaidPhoneTest {

    private lateinit var fixture: FixtureConfiguration<PrePaidPhone>
    private val validator = mock<PrePaidPhoneValidator>()
    private val phoneNumberProvider = mock<PhoneNumberProvider>()
    private val callCostCalculator = mock<CallCostCalculator>()
    private val smsCostCalculator = mock<SmsCostCalculator>()

    @BeforeEach
    internal fun setUp() {
        fixture = AggregateTestFixture(PrePaidPhone::class.java)
            .registerInjectableResource(validator)
            .registerInjectableResource(phoneNumberProvider)
            .registerInjectableResource(callCostCalculator)
            .registerInjectableResource(smsCostCalculator)
        fixture.setReportIllegalStateChange(false)

        whenever(phoneNumberProvider.getNumber()).thenReturn("123 456 789")
    }

    @Nested
    inner class BuyPrePaidCardCommandTest {
        private val command = BuyPrePaidCardCommand(BigDecimal.TEN, CardOwner("Jan", "Kowalski"))

        @Test
        internal fun `should produce event while valid buy card command used`() {
            whenever(validator.isValidCommand(command)).thenReturn(true)

            fixture
                .givenNoPriorActivity()
                .`when`(command)
                .expectEventMatchingType(CardBoughtEvent::class)
                .expectState {
                    Assertions.assertThat(it.owner).isEqualTo(command.owner)
                    Assertions.assertThat(it.moneyOnAccount)
                        .isEqualTo(command.initialMoney.asMoneyWithLocalCurrency())
                }
        }

        @Test
        internal fun `should throw Validation Exception while invalid buy card command used`() {
            whenever(validator.isValidCommand(command)).thenReturn(false)

            fixture.givenNoPriorActivity()
                .`when`(command)
                .expectException(ValidationException::class.java)
        }
    }

    @Nested
    inner class MakePhoneCallCommandTest {

        private val previousCommand = BuyPrePaidCardCommand(BigDecimal.TEN, CardOwner("Jan", "Kowalski"))
        private val command = MakePhoneCallCommand("123 456 789", "789 456 123", Duration.ofMinutes(10))

        @BeforeEach
        internal fun setUp() {
            whenever(validator.isValidCommand(previousCommand)).thenReturn(true)
        }

        @Test
        internal fun `should produce event while valid phone call made`() {
            whenever(validator.isValidCommandAndState(eq(command), any())).thenReturn(true)
            whenever(callCostCalculator.calculate(any(), any(), any()))
                .thenReturn(BigDecimal.TEN.asMoneyWithLocalCurrency())

            fixture
                .givenCommands(previousCommand)
                .`when`(command)
                .expectEventMatchingType(PhoneCallMadeEvent::class)
                .expectState {
                    Assertions.assertThat(it.moneyOnAccount)
                        .isEqualTo((previousCommand.initialMoney - BigDecimal.TEN).asMoneyWithLocalCurrency())
                }
        }

        @Test
        internal fun `should throw Validation Exception while invalid phone call made`() {
            whenever(validator.isValidCommandAndState(eq(command), any())).thenReturn(false)

            fixture
                .givenCommands(previousCommand)
                .`when`(command)
                .expectException(ValidationException::class.java)
        }
    }

    @Nested
    inner class SendSmsCommandTest {

        private val previousCommand = BuyPrePaidCardCommand(BigDecimal.TEN, CardOwner("Jan", "Kowalski"))
        private val command = SendSmsCommand("123 456 789", "987 654 321")

        @BeforeEach
        internal fun setUp() {
            whenever(validator.isValidCommand(previousCommand)).thenReturn(true)
        }

        @Test
        internal fun `should send sms while valid command and state`() {
            whenever(validator.isValidCommandAndState(eq(command), any())).thenReturn(true)
            whenever(smsCostCalculator.calculate(any(), any()))
                .thenReturn(BigDecimal.ONE.asMoneyWithLocalCurrency())

            fixture.givenCommands(previousCommand)
                .`when`(command)
                .expectEventMatchingType(SmsSentEvent::class)
                .expectState {
                    Assertions.assertThat(it.moneyOnAccount)
                        .isEqualTo((previousCommand.initialMoney - BigDecimal.ONE).asMoneyWithLocalCurrency())
                }
        }

        @Test
        internal fun `should throw ValidationException while command or state not valid`() {
            whenever(validator.isValidCommandAndState(eq(command), any())).thenReturn(false)

            fixture.givenCommands(previousCommand)
                .`when`(command)
                .expectException(ValidationException::class.java)
        }
    }

    @Nested
    inner class TopUpCardCommandTest {

        private val previousCommand = BuyPrePaidCardCommand(BigDecimal.TEN, CardOwner("Jan", "Kowalski"))
        private val command = TopUpCardCommand("123 456 789", BigDecimal.TEN)

        @BeforeEach
        internal fun setUp() {
            whenever(validator.isValidCommand(previousCommand)).thenReturn(true)
        }

        @Test
        internal fun `should top up card while valid command used`() {
            whenever(validator.isValidCommand(command)).thenReturn(true)

            fixture.givenCommands(previousCommand)
                .`when`(command)
                .expectEventMatchingType(CardTopUpEvent::class)
                .expectState {
                    Assertions.assertThat(it.moneyOnAccount)
                        .isEqualTo((previousCommand.initialMoney + command.amount).asMoneyWithLocalCurrency())
                }
        }

        @Test
        internal fun `should throw ValidationException while command`() {
            whenever(validator.isValidCommand(command)).thenReturn(false)

            fixture.givenCommands(previousCommand)
                .`when`(command)
                .expectException(ValidationException::class.java)
        }
    }
}

private fun <T> org.axonframework.test.aggregate.ResultValidator<T>.expectEventMatchingType(eventClass: KClass<out AbstractEvent>) =
    expectEventsMatching(
        payloadsMatching(
            exactSequenceOf(
                instanceOf<AbstractEvent>(eventClass.java),
                andNoMore()
            )
        )
    )
