package com.udacity.jwdnd.course1.cloudstorage.controllers;

import com.udacity.jwdnd.course1.cloudstorage.model.Credential;
import com.udacity.jwdnd.course1.cloudstorage.model.File;
import com.udacity.jwdnd.course1.cloudstorage.model.Note;
import com.udacity.jwdnd.course1.cloudstorage.model.User;
import com.udacity.jwdnd.course1.cloudstorage.services.CredentialService;
import com.udacity.jwdnd.course1.cloudstorage.services.FileService;
import com.udacity.jwdnd.course1.cloudstorage.services.NoteService;
import com.udacity.jwdnd.course1.cloudstorage.services.UserService;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping(path = "/home")
public class HomeController {

    private CredentialService credentialService;
    private NoteService noteService;
    private FileService fileService;
    private UserService userService;

    private HomeController(CredentialService credentialService, NoteService noteService, FileService fileService, UserService userService) {
        this.credentialService = credentialService;
        this.noteService = noteService;
        this.fileService = fileService;
        this.userService = userService;
    }

    @GetMapping
    public String home(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            String userName = authentication.getName();
            User user = userService.getUser(userName);
            if (user != null) {
                List<Credential> listCredentials = credentialService.getByUserId(user.getUserId());
                List<File> listFiles = fileService.getByUserId(user.getUserId());
                List<Note> listNotes = noteService.getByUserId(user.getUserId());
                model.addAttribute("listCredentials", listCredentials);
                model.addAttribute("listFiles", listFiles);
                model.addAttribute("listNotes", listNotes);
            }
        } else {
            return "redirect:/login";
        }
        if (model.containsAttribute("uploadError")) {
            model.addAttribute("uploadError", model.getAttribute("uploadError"));
        }
        if (model.containsAttribute("uploadSuccess")) {
            model.addAttribute("uploadSuccess", model.getAttribute("uploadSuccess"));
        }
        if (model.containsAttribute("credentialSuccess")) {
            model.addAttribute("credentialSuccess", model.getAttribute("credentialSuccess"));
        }
        if (model.containsAttribute("credentialError")) {
            model.addAttribute("credentialError", model.getAttribute("credentialError"));
        }
        if (model.containsAttribute("noteSuccess")) {
            model.addAttribute("noteSuccess", model.getAttribute("noteSuccess"));
        }
        if (model.containsAttribute("noteError")) {
            model.addAttribute("noteError", model.getAttribute("noteError"));
        }
        return "home";
    }
}
