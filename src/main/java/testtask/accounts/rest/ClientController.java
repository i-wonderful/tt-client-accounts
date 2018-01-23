/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package testtask.accounts.rest;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import testtask.accounts.dto.AccountDto;
import testtask.accounts.dto.ClientDto;

/**
 *
 * @author Strannica
 */
@RestController
@RequestMapping(value = "/client")
public class ClientController {

    @GetMapping(value = "/go")
    public String hello() {
        return "I'm client controller!";
    }

    @GetMapping(value = "/current")
    public ClientDto get() {

        List<AccountDto> accounts = new ArrayList<>();
        accounts.add(new AccountDto(new BigDecimal(1233.44)));
        accounts.add(new AccountDto(new BigDecimal(45566.55)));

        ClientDto cl = new ClientDto("Doooooo", "Goooo", accounts);

        return cl;
    }
}
