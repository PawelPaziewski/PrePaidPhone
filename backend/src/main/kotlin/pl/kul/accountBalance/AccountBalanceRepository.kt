package pl.kul.accountBalance

import org.springframework.data.repository.CrudRepository

interface AccountBalanceRepository : CrudRepository<AccountBalance, String>