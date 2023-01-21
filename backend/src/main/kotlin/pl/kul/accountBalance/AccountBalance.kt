package pl.kul.accountBalance

import org.springframework.data.annotation.Id
import pl.kul.Money

data class AccountBalance(
    @Id
    val phoneNumber: String,
    var balance: Money
)
