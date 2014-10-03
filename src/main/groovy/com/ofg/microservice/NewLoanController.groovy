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
@RequestMapping('/application')
class NewLoanController {
    private final ServiceRestClient serviceRestClient

    @Autowired
    NewLoanController(ServiceRestClient serviceRestClient) {
        this.serviceRestClient = serviceRestClient
    }

    @RequestMapping(method = POST)
    void apply(@RequestBody LoanApplication applicationForNewLoan) {
        log.debug("NEW APPLICATION RECEIVED FROM UI: $applicationForNewLoan")

        //send stuff to reporting service
        //send stuff to FRED
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
    BigDecimal amount
    String loanId
}
