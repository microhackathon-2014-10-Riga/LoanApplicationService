package com.ofg.microservice

import com.ofg.infrastructure.web.resttemplate.fluent.ServiceRestClient
import groovy.transform.ToString
import groovy.transform.TypeChecked
import groovy.util.logging.Slf4j
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

import static org.springframework.web.bind.annotation.RequestMethod.POST

@Slf4j
@TypeChecked
@RestController
@RequestMapping('/api/loanApplication')
class NewLoanController {
    private final ServiceRestClient serviceRestClient

    @Autowired
    NewLoanController(ServiceRestClient serviceRestClient) {
        this.serviceRestClient = serviceRestClient
    }

    @RequestMapping(method = POST)
    void apply(@RequestBody LoanApplication applicationForNewLoan) {
        log.debug("NEW APPLICATION RECEIVED FROM UI: $applicationForNewLoan")
        send(applicationForNewLoan, "fraud-detection-service", "/api/loanApplication/$applicationForNewLoan.loanId")
        send(applicationForNewLoan, "reporting-service", "/api/loan")
    }

    private HttpStatus send(Object stuffToSend, String serviceAlias, String url) {
        HttpStatus clientServiceResponseStatus = serviceRestClient.forService(serviceAlias).
                post().
                onUrl(url).
                body(stuffToSend).
                withHeaders().
                contentType(MediaType.APPLICATION_JSON).
                andExecuteFor().aResponseEntity().ofType(Object).statusCode
        return clientServiceResponseStatus
    }
}


@TypeChecked
@ToString
class LoanApplication {
    String firstName
    String lastName
    Integer age
    String jobPosition
    BigDecimal amount
    String loanId
}