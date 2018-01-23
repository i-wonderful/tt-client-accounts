/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package testtask.accounts.rest;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
