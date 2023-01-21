package pl.kul.billing

import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface BillingRepository : CrudRepository<BillingEntry, String> {
    fun findAllByPhoneNumber(phoneNumber: String): List<BillingEntry>
}