package pl.kul.accountBalance

import org.assertj.core.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import pl.kul.*
import java.math.BigDecimal
import java.time.Duration

internal class AccountBalanceProjectionTest {

    private lateinit var projection: AccountBalanceProjection
    private lateinit var repository: AccountBalanceRepository

    private val anyTimestamp = DateTimeAdapter.now()
    private val anyCardOwner = CardOwner("Jan", "Kowalski")
    private val anyPhoneNumber = "987 654 321"

    @BeforeEach
    internal fun setUp() {
        repository = HashMapDB()
        projection = AccountBalanceProjection(repository)
    }

    private val anyMoney = BigDecimal.TEN.asMoneyWithLocalCurrency()

    @Test
    internal fun `should save account balance to db on CardBoughtEvent`() {
        val phoneNumber = "123 456 789"
        val initialMoney = BigDecimal.TEN.asMoneyWithLocalCurrency()

        projection.on(
            CardBoughtEvent(
                phoneNumber, initialMoney, anyCardOwner, anyTimestamp
            )
        )

        Assertions.assertThat(repository.findById(phoneNumber)).isNotEmpty.get()
            .isEqualTo(AccountBalance(phoneNumber, initialMoney))
    }

    @Nested
    inner class CardTopUpEventTest {
        private val phoneNumberInDb = "123 456 789"
        private val phoneNumberNotPresentInDb = "987 654 321"
        private val initialMoney = BigDecimal.TEN.asMoneyWithLocalCurrency()

        @BeforeEach
        internal fun setUp() {
            prepareDb(phoneNumberInDb, phoneNumberNotPresentInDb, initialMoney)
        }

        @Test
        internal fun `should increase money on account while TopUpEvent and AccountBalance found in db`() {
            val topUpAmount = BigDecimal.TEN.asMoneyWithLocalCurrency()

            projection.on(CardTopUpEvent(phoneNumberInDb, topUpAmount, anyTimestamp))

            Assertions.assertThat(repository.findById(phoneNumberInDb)).isNotEmpty.get()
                .isEqualTo(AccountBalance(phoneNumberInDb, initialMoney + topUpAmount))
        }

        @Test
        internal fun `should do nothing while TopUpEvent and AccountBalance not found in db`() {
            projection.on(CardTopUpEvent(phoneNumberNotPresentInDb, anyMoney, anyTimestamp))

            Assertions.assertThat(repository.findById(phoneNumberNotPresentInDb)).isEmpty
        }
    }

    @Nested
    inner class PhoneCallMadeEventTest {
        private val phoneNumberInDb = "123 456 789"
        private val phoneNumberNotPresentInDb = "987 654 321"
        private val initialMoney = BigDecimal.TEN.asMoneyWithLocalCurrency()

        private val anyDuration = Duration.ZERO

        @BeforeEach
        internal fun setUp() {
            prepareDb(phoneNumberInDb, phoneNumberNotPresentInDb, initialMoney)
        }

        @Test
        internal fun `should decrease money on account while PhoneCallMadeEvent and AccountBalance found in db`() {
            val callCost = BigDecimal.ONE.asMoneyWithLocalCurrency()

            projection.on(
                PhoneCallMadeEvent(
                    phoneNumberInDb, anyPhoneNumber, anyDuration, callCost, anyTimestamp
                )
            )

            Assertions.assertThat(repository.findById(phoneNumberInDb)).isNotEmpty.get()
                .isEqualTo(AccountBalance(phoneNumberInDb, initialMoney - callCost))
        }

        @Test
        internal fun `should do nothing while MadePhoneCallEvent and AccountBalance not found in db`() {

            projection.on(
                PhoneCallMadeEvent(
                    phoneNumberNotPresentInDb, anyPhoneNumber, anyDuration, anyMoney, anyTimestamp
                )
            )

            Assertions.assertThat(repository.findById(phoneNumberNotPresentInDb)).isEmpty
        }
    }

    @Nested
    inner class SmsSentEventTest {
        private val phoneNumberInDb = "123 456 789"
        private val phoneNumberNotPresentInDb = "987 654 321"
        private val initialMoney = BigDecimal.TEN.asMoneyWithLocalCurrency()

        @BeforeEach
        internal fun setUp() {
            prepareDb(phoneNumberInDb, phoneNumberNotPresentInDb, initialMoney)
        }

        @Test
        internal fun `should decrease money on account while SmsSentEvent and AccountBalance found in db`() {
            val smsCost = BigDecimal.ONE.asMoneyWithLocalCurrency()
            projection.on(
                SmsSentEvent(
                    phoneNumberInDb, anyPhoneNumber, smsCost, anyTimestamp
                )
            )

            Assertions.assertThat(repository.findById(phoneNumberInDb)).isNotEmpty.get()
                .isEqualTo(AccountBalance(phoneNumberInDb, initialMoney - smsCost))
        }

        @Test
        internal fun `should do nothing while SmsSentEvent and AccountBalance not found in db`() {
            projection.on(
                SmsSentEvent(
                    phoneNumberNotPresentInDb, anyPhoneNumber, anyMoney, anyTimestamp
                )
            )

            Assertions.assertThat(repository.findById(phoneNumberNotPresentInDb)).isEmpty
        }
    }

    @Nested
    inner class GetAccountBalanceQueryTest {
        private val phoneNumberInDb = "123 456 789"
        private val phoneNumberNotPresentInDb = "987 654 321"
        private val initialMoney = BigDecimal.TEN.asMoneyWithLocalCurrency()

        @BeforeEach
        internal fun setUp() {
            prepareDb(phoneNumberInDb, phoneNumberNotPresentInDb, initialMoney)
        }

        @Test
        internal fun `should return Optional of AccountBalance while phone number found in db`() {
            val accountBalance = projection.getAccountBalance(GetAccountBalanceQuery(phoneNumberInDb))

            Assertions.assertThat(accountBalance).isPresent.get().isEqualTo(initialMoney)
        }

        @Test
        internal fun `should return empty optional while phone number not found in db`() {
            val accountBalance = projection.getAccountBalance(GetAccountBalanceQuery(phoneNumberNotPresentInDb))

            Assertions.assertThat(accountBalance).isEmpty
        }
    }

    @Nested
    inner class GetAllPhoneNumbersQueryTest {
        private val anySecondPhoneNumber = "123 456 780"

        @Test
        internal fun `should return empty list while db is empty`() {
            val allNumbers = projection.getAllNumbers(GetAllPhoneNumbersQuery())

            Assertions.assertThat(allNumbers).isEmpty()
        }

        @Test
        internal fun `should return list containing elements saved to db`() {
            repository.saveAll(
                listOf(
                    AccountBalance(anyPhoneNumber, anyMoney), AccountBalance(anySecondPhoneNumber, anyMoney)
                )
            )

            val allNumbers = projection.getAllNumbers(GetAllPhoneNumbersQuery())

            Assertions.assertThat(allNumbers).isNotEmpty
                .hasSize(2)
                .containsAll(listOf(anyPhoneNumber, anySecondPhoneNumber))
        }
    }


    private fun prepareDb(phoneNumberPresent: String, phoneNumberNotPresent: String, initialMoney: Money) {
        projection.on(
            CardBoughtEvent(
                phoneNumberPresent, initialMoney, anyCardOwner, anyTimestamp
            )
        )

        Assertions.assertThat(repository.findById(phoneNumberPresent)).isNotEmpty.get()
            .isEqualTo(AccountBalance(phoneNumberPresent, initialMoney))

        Assertions.assertThat(repository.findById(phoneNumberNotPresent)).isEmpty
    }
}