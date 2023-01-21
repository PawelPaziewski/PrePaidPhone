package pl.kul

import org.axonframework.commandhandling.CommandExecutionException
import org.axonframework.commandhandling.gateway.CommandGateway
import org.axonframework.messaging.responsetypes.ResponseTypes
import org.axonframework.queryhandling.QueryGateway
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.context.request.WebRequest
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler
import pl.kul.billing.BillingEntry
import java.math.BigDecimal
import java.time.Duration
import java.util.*

@RestController
class MainController @Autowired constructor(
    private val commandGateway: CommandGateway,
    private val queryGateway: QueryGateway
) {
    @CrossOrigin(origins = ["http://localhost:3000"])
    @GetMapping("/cardBalance")
    fun getCardBalance(@RequestParam(name = "phoneNumber", required = true) phoneNumber: String): Optional<Money> {
        return queryGateway
            .query(GetAccountBalanceQuery(phoneNumber), ResponseTypes.optionalInstanceOf(Money::class.java))
            .get()
    }
    @CrossOrigin(origins = ["http://localhost:3000"])
    @PutMapping("/buyPhoneCard")
    fun buyPrePaidCard(
        @RequestParam(required = true, name = "initialMoney") initialMoneyAmount: BigDecimal,
        @RequestParam(required = true, name = "buyerFirstName") firstName: String,
        @RequestParam(required = true, name = "buyerLastName") lastName: String
    ): String {
        return commandGateway.sendAndWait(BuyPrePaidCardCommand(initialMoneyAmount, CardOwner(firstName, lastName)))
    }
    @CrossOrigin(origins = ["http://localhost:3000"])
    @PutMapping("/makePhoneCall")
    fun makePhoneCall(
        @RequestParam(required = true, name = "callerPhoneNumber") caller: String,
        @RequestParam(required = true, name = "receiverPhoneNumber") receiver: String,
        @RequestParam(required = true, name = "callDuration") callDuration: Duration
    ) {
        commandGateway.sendAndWait<Any>(MakePhoneCallCommand(caller, receiver, callDuration))
    }
    @CrossOrigin(origins = ["http://localhost:3000"])
    @PutMapping("/topUpCard")
    fun topUpCard(
        @RequestParam(required = true, name = "phoneNumber") phoneNumber: String,
        @RequestParam(required = true, name = "amount") amount: BigDecimal
    ) {
        commandGateway.sendAndWait<Any>(TopUpCardCommand(phoneNumber, amount))
    }
    @CrossOrigin(origins = ["http://localhost:3000"])
    @PutMapping("/sendSms")
    fun sendSms(
        @RequestParam(required = true, name = "senderPhoneNumber") sender: String,
        @RequestParam(required = true, name = "receiverPhoneNumber") receiver: String,
    ) {
        commandGateway.sendAndWait<Any>(SendSmsCommand(sender, receiver))
    }
    @CrossOrigin(origins = ["http://localhost:3000"])
    @GetMapping("/billing")
    fun getBilling(@RequestParam(name = "phoneNumber", required = true) phoneNumber: String): List<BillingEntry> =
        queryGateway.query(GetBillingQuery(phoneNumber),
            ResponseTypes.multipleInstancesOf(BillingEntry::class.java)).get()
}

@ControllerAdvice
class SpringExceptionHandler : ResponseEntityExceptionHandler() {
    @CrossOrigin(origins = ["http://localhost:3000"])
    @ExceptionHandler(value = [(CommandExecutionException::class)])
    fun handleException(ex: CommandExecutionException, req: WebRequest): ResponseEntity<String> {
        if (ex.message == ValidationException::class.java.name) {
            return ResponseEntity(HttpStatus.BAD_REQUEST)
        }
        throw ex
    }
}