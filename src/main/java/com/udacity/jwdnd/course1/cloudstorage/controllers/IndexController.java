package com.udacity.jwdnd.course1.cloudstorage.controllers;

import com.udacity.jwdnd.course1.cloudstorage.services.CredentialService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class IndexController {
    private CredentialService credentialService;

    public IndexController(CredentialService credentialService) {
        this.credentialService = credentialService;
    }

    @GetMapping(path = "/error")
    public String error() {
        return "error/404";
    }
}
