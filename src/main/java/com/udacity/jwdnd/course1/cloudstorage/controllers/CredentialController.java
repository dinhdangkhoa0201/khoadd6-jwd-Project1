package com.udacity.jwdnd.course1.cloudstorage.controllers;

import com.udacity.jwdnd.course1.cloudstorage.model.Credential;
import com.udacity.jwdnd.course1.cloudstorage.model.User;
import com.udacity.jwdnd.course1.cloudstorage.services.CredentialService;
import com.udacity.jwdnd.course1.cloudstorage.services.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping(path = "/credentials")
public class CredentialController {
    private CredentialService credentialService;
    private UserService userService;

    public CredentialController(CredentialService credentialService, UserService userService) {
        this.credentialService = credentialService;
        this.userService = userService;
    }

    @PostMapping
    public String credential(Credential credential, RedirectAttributes redirectAttributes) {
        if (!userService.isUserNameAvailable(credential.getUserName())) {
            redirectAttributes.addFlashAttribute("credentialError", "The username already exists.");
            return "redirect:/home";
        }
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();
        User user = userService.getUser(userName);
        if (userName == null) {
            return "redirect:/home";
        }
        int rs = 0;
        credential.setUserId(user.getUserId());
        if (credential.getCredentialId() != null) {
            rs = credentialService.updateCredential(credential.getCredentialId(), credential);
            if (rs == 0) {
                redirectAttributes.addFlashAttribute("credentialError", "Update Credential Fail");
            } else if (rs > 0) {
                redirectAttributes.addFlashAttribute("credentialSuccess", "Update Credential Successfully");
            }
        } else {
            rs = credentialService.createCredential(credential);
            if (rs == 0) {
                redirectAttributes.addFlashAttribute("credentialError", "Add Credential Fail");
            } else if (rs > 0) {
                redirectAttributes.addFlashAttribute("credentialSuccess", "Add Credential Successfully");
            }
        }

        return "redirect:/home";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Integer id, RedirectAttributes redirectAttributes) {
        int rs = credentialService.deleteCredential(id);
        if (rs == 0) {
            redirectAttributes.addFlashAttribute("credentialError", "Delete Credential Fail");
        } else if (rs > 0) {
            redirectAttributes.addFlashAttribute("credentialSuccess", "Delete Credential Successfully");
        }
        return "redirect:/home";
    }
}
