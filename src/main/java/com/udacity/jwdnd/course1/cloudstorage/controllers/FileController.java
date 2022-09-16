package com.udacity.jwdnd.course1.cloudstorage.controllers;

import com.udacity.jwdnd.course1.cloudstorage.model.File;
import com.udacity.jwdnd.course1.cloudstorage.model.User;
import com.udacity.jwdnd.course1.cloudstorage.services.FileService;
import com.udacity.jwdnd.course1.cloudstorage.services.UserService;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Controller
@RequestMapping(path = "/files")
public class FileController {
    private FileService fileService;
    private UserService userService;

    private FileController(FileService fileService, UserService userService) {
        this.fileService = fileService;
        this.userService = userService;
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public String file(@RequestParam("fileUpload") MultipartFile fileUpload, RedirectAttributes redirectAttributes) throws IOException {
        if (fileUpload.isEmpty()) {
            redirectAttributes.addFlashAttribute("uploadError", "No File to upload");
            return "redirect:/home";
        }
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();
        User user = userService.getUser(userName);
        if (fileService.getByFileName(fileUpload.getOriginalFilename(), user.getUserId()) != null) {
            redirectAttributes.addFlashAttribute("uploadError", "File Name already exists");
            return "redirect:/home";
        }
        if (userName == null) {
            return "redirect:/home";
        }
        File file = new File();
        file.setFileName(fileUpload.getOriginalFilename());
        file.setContentType(fileUpload.getContentType());
        file.setFileSize(String.valueOf(fileUpload.getSize()));
        file.setFileData(fileUpload.getBytes());
        file.setUserId(user.getUserId());
        fileService.createFile(file);
        redirectAttributes.addFlashAttribute("uploadSuccess", "Upload File Successfully");
        return "redirect:/home";
    }

    @GetMapping(path = "/delete/{id}")
    public String delete(@PathVariable Integer id, RedirectAttributes redirectAttributes) {
        int rs = fileService.deleteFile(id);
        if (rs == 0) {
            redirectAttributes.addFlashAttribute("uploadError", "Not File is deleted");
        } else if (rs > 0) {
            redirectAttributes.addFlashAttribute("uploadError", "File is deleted Successfully");
        }
        return "redirect:/home";
    }

    @GetMapping(path = "/download/{id}")
    public void download(@PathVariable Integer id, HttpServletResponse response) throws IOException {
        File file = fileService.getFile(id);
        response.setContentType("application/octet-stream");
        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=" + file.getFileName();
        response.setHeader(headerKey, headerValue);
        ServletOutputStream outputStream = response.getOutputStream();
        outputStream.write(file.getFileData());
        outputStream.close();
    }
}
