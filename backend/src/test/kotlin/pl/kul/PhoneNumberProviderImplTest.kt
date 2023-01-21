package pl.kul

import org.assertj.core.api.Assertions.assertThat
import org.axonframework.messaging.responsetypes.ResponseTypes
import org.axonframework.queryhandling.QueryGateway
import org.junit.jupiter.api.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever
import java.util.concurrent.CompletableFuture

internal class PhoneNumberProviderImplTest {

    private val queryGateway: QueryGateway = mock()
    private val numberProvider = PhoneNumberProviderImpl(queryGateway)

    @Test
    internal fun `should return '111 111 111' while no phone number in db`() {
        whenever(
            queryGateway.query(
                GetAllPhoneNumbersQuery(),
                ResponseTypes.multipleInstancesOf(String::class.java)
            )
        ).thenReturn(CompletableFuture.completedFuture(emptyList()))

        val result = numberProvider.getNumber()
        assertThat(result).isEqualTo("111 111 111")
    }

    @Test
    internal fun `should return '154 154 155' for db with number '154 154 154`() {
        whenever(
            queryGateway.query(
                GetAllPhoneNumbersQuery(), ResponseTypes.multipleInstancesOf(String::class.java)
            )
        ).thenReturn(
            CompletableFuture.completedFuture(listOf("154 154 154"))
        )

        val result = numberProvider.getNumber()
        assertThat(result).isEqualTo("154 154 155")
    }

    @Test
    internal fun `should return '154 154 155' for db with numbers '154 154 154' and '113 115 115'`() {
        whenever(
            queryGateway.query(
                GetAllPhoneNumbersQuery(), ResponseTypes.multipleInstancesOf(String::class.java)
            )
        ).thenReturn(
            CompletableFuture.completedFuture(listOf("154 154 154", "113 115 115"))
        )

        val result = numberProvider.getNumber()
        assertThat(result).isEqualTo("154 154 155")
    }
}