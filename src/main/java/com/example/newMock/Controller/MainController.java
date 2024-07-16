package com.example.newMock.Controller;

import com.example.newMock.Model.RequestDTO;
import com.example.newMock.Model.ResponseDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Random;

@RestController
public class MainController {

    private Logger log = LoggerFactory.getLogger(MainController.class);
    ObjectMapper mapper = new ObjectMapper();

    @PostMapping(
            value = "/info/postBalances",
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE
    )

    private Object postBalances(@RequestBody RequestDTO requestDTO) {
        try {
            char firstChar = requestDTO.getClientId().charAt(0);
            String account = requestDTO.getAccount();
            BigDecimal maxLimit;
            String currency = "";
            if (firstChar == '8') {
                maxLimit = BigDecimal.valueOf(2000.00);
                currency = "US";
            } else if (firstChar == '9') {
                maxLimit = BigDecimal.valueOf(1000.00);
                currency = "EU";
            } else {
                maxLimit = BigDecimal.valueOf(10000.00);
                currency = "RUB";
            }

            Random random = new Random();
            BigDecimal randomBalance = maxLimit.multiply(BigDecimal.valueOf(random.nextDouble())).setScale(2, RoundingMode.HALF_UP);

            ResponseDTO responseDTO = new ResponseDTO();
            responseDTO.setRqUID(requestDTO.getRqUID());
            responseDTO.setClientId(requestDTO.getClientId());
            responseDTO.setAccount(account);
            responseDTO.setCurrency(currency);
            responseDTO.setBalance(randomBalance);
            responseDTO.setMaxLimit(maxLimit);

            log.error("RequestDTO: " + mapper.writerWithDefaultPrettyPrinter().writeValueAsString(requestDTO));
            log.error("ResponseDTO: " + mapper.writerWithDefaultPrettyPrinter().writeValueAsString(responseDTO));
            return responseDTO;

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
}
