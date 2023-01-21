package pl.kul

import org.axonframework.messaging.responsetypes.ResponseTypes
import org.axonframework.queryhandling.QueryGateway
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

interface PhoneNumberProvider {
    fun getNumber(): String
}

@Component
class PhoneNumberProviderImpl @Autowired constructor(private val queryGateway: QueryGateway) : PhoneNumberProvider {

    private val firstPhoneNumber = 111_111_111L

    override fun getNumber(): String {
        val allPhoneNumbers =
            queryGateway.query(GetAllPhoneNumbersQuery(), ResponseTypes.multipleInstancesOf(String::class.java))
                .get()
        return if (allPhoneNumbers.isEmpty()) {
            firstPhoneNumber
        } else {
            allPhoneNumbers.stream().map { it.replace(" ", "").toLong() }
                .max { o1, o2 -> o1.compareTo(o2) }.get() + 1
        }.toPhoneNumber()
    }
}

private fun Long.toPhoneNumber(): String {
    val string = this.toString()
    return String.format("%s %s %S", string.substring(0, 3), string.substring(3, 6), string.substring(6, 9))
}
