package com.udacity.jwdnd.course1.cloudstorage.controllers;

import com.udacity.jwdnd.course1.cloudstorage.model.Note;
import com.udacity.jwdnd.course1.cloudstorage.model.User;
import com.udacity.jwdnd.course1.cloudstorage.services.NoteService;
import com.udacity.jwdnd.course1.cloudstorage.services.UserService;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping(path = "/notes")
public class NoteController {

    private NoteService noteService;
    private UserService userService;

    public NoteController(NoteService noteService, UserService userService) {
        this.noteService = noteService;
        this.userService = userService;
    }

    @PostMapping(consumes = {MediaType.APPLICATION_FORM_URLENCODED_VALUE})
    public String note(Note note, RedirectAttributes redirectAttributes) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();
        User user = userService.getUser(userName);
        if (userName == null) {
            return "redirect:/home";
        }
        note.setUserId(user.getUserId());
        int rs;
        if (note.getNoteId() != null) {
            rs = noteService.updateNote(note.getNoteId(), note);
            if (rs == 0) {
                redirectAttributes.addFlashAttribute("noteError", "Update Note Fail");
            } else if (rs > 0) {
                redirectAttributes.addFlashAttribute("noteSuccess", "Update Note Successfully");
            }
        } else {
            rs = noteService.createNote(note);
            if (rs == 0) {
                redirectAttributes.addFlashAttribute("noteError", "Add Note Fail");
            } else if (rs > 0) {
                redirectAttributes.addFlashAttribute("noteSuccess", "Add Note Successfully");
            }
        }
        return "redirect:/home";
    }

    @GetMapping(path = "/delete/{id}")
    public String delete(@PathVariable Integer id, RedirectAttributes redirectAttributes) {
        int rs = noteService.deleteNote(id);
        if (rs == 0) {
            redirectAttributes.addFlashAttribute("noteError", "Delete Note Fail");
        } else if (rs > 0) {
            redirectAttributes.addFlashAttribute("noteSuccess", "Delete Note Successfully");
        }
        return "redirect:/home";
    }
}
