package pl.kul

import org.axonframework.commandhandling.gateway.CommandGateway
import org.axonframework.messaging.responsetypes.ResponseTypes
import org.axonframework.queryhandling.QueryGateway
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.whenever
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.web.context.WebApplicationContext
import java.math.BigDecimal
import java.time.Duration
import java.util.*
import java.util.concurrent.CompletableFuture

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
internal class MainControllerTest(
    @Autowired private val webAppContext: WebApplicationContext
) {
    private val correctPhoneNumber = "123 456 782"

    private lateinit var mockMvc: MockMvc

    @MockBean
    private lateinit var commandGateway: CommandGateway

    @MockBean
    private lateinit var queryGateway: QueryGateway

    @BeforeEach
    internal fun setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webAppContext).build()
    }

    @Nested
    inner class BuyPrePaidPhoneTest {
        private val buyPhoneCardAddress = "/buyPhoneCard"

        @Test
        internal fun `should has status OK while put to buy phone card with initial money amount and buyer first and last name provided`() {
            whenever(commandGateway.sendAndWait<String>(any())).thenReturn("123 456 789")

            mockMvc.perform(
                put(buyPhoneCardAddress)
                    .param("initialMoney", BigDecimal.TEN.toString())
                    .param("buyerFirstName", "John")
                    .param("buyerLastName", "Kowalski")
            )
                .andExpect(status().isOk)
        }

        @Test
        internal fun `should has status BadRequest while put to buy phone card without initial money amount provided`() {
            mockMvc.perform(
                put(buyPhoneCardAddress)
                    .param("buyerFirstName", "John")
                    .param("buyerLastName", "Kowalski")
            )
                .andExpect(status().isBadRequest)
        }

        @Test
        internal fun `should has status BadRequest while put to buy phone card without buyer first name provided`() {
            mockMvc.perform(
                put(buyPhoneCardAddress)
                    .param("initialMoney", BigDecimal.TEN.toString())
                    .param("buyerLastName", "Kowalski")
            )
                .andExpect(status().isBadRequest)
        }

        @Test
        internal fun `should has status BadRequest while put to buy phone card without buyer last name provided`() {
            mockMvc.perform(
                put(buyPhoneCardAddress)
                    .param("initialMoney", BigDecimal.TEN.toString())
                    .param("buyerFirstName", "John")
            )
                .andExpect(status().isBadRequest)
        }

        @Test
        internal fun `should has status BadRequest while put to buy phone card with initial money amount not parsable to BigDecimal`() {
            mockMvc.perform(
                put(buyPhoneCardAddress)
                    .param("initialMoney", "three")
                    .param("buyerFirstName", "John")
                    .param("buyerLastName", "Kowalski")
            )
                .andExpect(status().isBadRequest)
        }
    }

    @Nested
    inner class MakePhoneCallTest {
        private val makePhoneCallAddress = "/makePhoneCall"
        private val correctSecondPhoneNumber = "987 654 321"

        @Test
        internal fun `should has status OK while put to make phone call with caller phone number, receiver phone number and call duration`() {
            mockMvc.perform(
                put(makePhoneCallAddress)
                    .param("callerPhoneNumber", correctPhoneNumber)
                    .param("receiverPhoneNumber", correctSecondPhoneNumber)
                    .param("callDuration", Duration.ofMinutes(10).toString())
            ).andExpect(status().isOk)
        }

        @Test
        internal fun `should has status BadRequest while put to make phone call with call duration not parsable to Duration`() {
            mockMvc.perform(
                put(makePhoneCallAddress)
                    .param("callerPhoneNumber", correctPhoneNumber)
                    .param("receiverPhoneNumber", correctSecondPhoneNumber)
                    .param("callDuration", "three minutes")
            ).andExpect(status().isBadRequest)
        }

        @Test
        internal fun `should has status BadRequest while put to make phone call without call duration provided`() {
            mockMvc.perform(
                put(makePhoneCallAddress)
                    .param("callerPhoneNumber", correctPhoneNumber)
                    .param("receiverPhoneNumber", correctSecondPhoneNumber)
            ).andExpect(status().isBadRequest)
        }

        @Test
        internal fun `should has status BadRequest while put to make phone call without caller phone number provided`() {
            mockMvc.perform(
                put(makePhoneCallAddress)
                    .param("receiverPhoneNumber", correctSecondPhoneNumber)
                    .param("callDuration", "three minutes")
            ).andExpect(status().isBadRequest)
        }

        @Test
        internal fun `should has status BadRequest while put to make phone call without receiver phone number provided`() {
            mockMvc.perform(
                put(makePhoneCallAddress)
                    .param("callerPhoneNumber", correctSecondPhoneNumber)
                    .param("callDuration", "three minutes")
            ).andExpect(status().isBadRequest)
        }
    }

    @Nested
    inner class SendSmsTest {
        private val sendSmsAddress = "/sendSms"
        private val correctSecondPhoneNumber = "987 654 321"

        @Test
        internal fun `should has status OK while put to send sms with sender and receiver phone numbers`() {
            mockMvc.perform(
                put(sendSmsAddress)
                    .param("senderPhoneNumber", correctPhoneNumber)
                    .param("receiverPhoneNumber", correctSecondPhoneNumber)
            ).andExpect(status().isOk)
        }

        @Test
        internal fun `should has status BadRequest while put to send sms without sender phone number`() {
            mockMvc.perform(
                put(sendSmsAddress)
                    .param("receiverPhoneNumber", correctSecondPhoneNumber)
            ).andExpect(status().isBadRequest)
        }

        @Test
        internal fun `should has status BadRequest while put to send sms without receiver phone number`() {
            mockMvc.perform(
                put(sendSmsAddress)
                    .param("senderPhoneNumber", correctPhoneNumber)
            ).andExpect(status().isBadRequest)
        }
    }

    @Nested
    inner class TopUpCardTest {
        private val topUpCardAddress = "/topUpCard"

        @Test
        internal fun `should has status OK while put to top up card with phone number and amount`() {
            mockMvc.perform(
                put(topUpCardAddress)
                    .param("phoneNumber", correctPhoneNumber)
                    .param("amount", BigDecimal.TEN.toString())
            ).andExpect(status().isOk)
        }

        @Test
        internal fun `should has status BadRequest while put to top up card with amount not parsable to BigDecimal`() {
            mockMvc.perform(
                put(topUpCardAddress)
                    .param("phoneNumber", correctPhoneNumber)
                    .param("amount", "four")
            ).andExpect(status().isBadRequest)
        }

        @Test
        internal fun `should has status BadRequest while put to top up card without amount provided`() {
            mockMvc.perform(
                put(topUpCardAddress)
                    .param("phoneNumber", correctPhoneNumber)
            ).andExpect(status().isBadRequest)
        }

        @Test
        internal fun `should has status BadRequest while put to top up card without phone number provided`() {
            mockMvc.perform(
                put(topUpCardAddress)
                    .param("phoneNumber", correctPhoneNumber)
                    .param("amount", "four")
            ).andExpect(status().isBadRequest)
        }
    }

    @Nested
    inner class CardBalanceTest {
        private val cardBalanceAddress = "/cardBalance"

        @Test
        internal fun `'should has status OK while get to card balance with phone number provided'`() {
            whenever(
                queryGateway.query(
                    GetAccountBalanceQuery(correctPhoneNumber),
                    ResponseTypes.optionalInstanceOf(Money::class.java)
                )
            ).thenReturn(CompletableFuture.completedFuture(Optional.of(BigDecimal.ZERO.asMoneyWithLocalCurrency())))

            mockMvc.perform(get(cardBalanceAddress).param("phoneNumber", correctPhoneNumber))
                .andExpect(status().isOk)
        }

        @Test
        internal fun `'should has status BadRequest while get to card balance without phone number provided'`() {
            mockMvc.perform(get(cardBalanceAddress))
                .andExpect(status().isBadRequest)
        }
    }
}