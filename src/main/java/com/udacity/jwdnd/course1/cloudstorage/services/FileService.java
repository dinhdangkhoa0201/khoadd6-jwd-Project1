package com.udacity.jwdnd.course1.cloudstorage.services;

import com.udacity.jwdnd.course1.cloudstorage.mapper.FileMapper;
import com.udacity.jwdnd.course1.cloudstorage.model.File;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FileService {
    private FileMapper fileMapper;

    public FileService(FileMapper fileMapper) {
        this.fileMapper = fileMapper;
    }

    public int createFile(File file) {
        return fileMapper.insert(new File(null, file.getFileName(), file.getContentType(), file.getFileSize(), file.getFileData(), file.getUserId()));
    }

    public File getFile(Integer fileId) {
        return fileMapper.getFile(fileId);
    }

    public int deleteFile(Integer fileId) {
        return fileMapper.delete(fileId);
    }

    public List<File> getByUserId(Integer userId) {
        return fileMapper.getByUserId(userId);
    }

    public File getByFileName(String fileName, Integer userId) {
        return fileMapper.getByFileName(fileName, userId);
    }
}
