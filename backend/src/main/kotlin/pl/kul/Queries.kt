package pl.kul

data class GetAccountBalanceQuery(val phoneNumber: String)

class GetAllPhoneNumbersQuery {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        return true
    }

    override fun hashCode(): Int {
        return javaClass.hashCode()
    }
}

data class GetBillingQuery(val phoneNumber: String)