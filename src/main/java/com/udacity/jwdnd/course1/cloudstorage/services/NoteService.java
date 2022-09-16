package com.udacity.jwdnd.course1.cloudstorage.services;

import com.udacity.jwdnd.course1.cloudstorage.mapper.NoteMapper;
import com.udacity.jwdnd.course1.cloudstorage.model.Note;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NoteService {
    private NoteMapper noteMapper;

    public NoteService(NoteMapper noteMapper) {
        this.noteMapper = noteMapper;
    }

    public int createNote(Note note) {
        return noteMapper.insert(new Note(null, note.getNoteTitle(), note.getNoteDescription(), note.getUserId()));
    }

    public int updateNote(Integer noteId, Note note) {
        return noteMapper.update(noteId, note);
    }

    public Note getNote(Integer noteId) {
        return noteMapper.getNote(noteId);
    }

    public int deleteNote(Integer noteId) {
        return noteMapper.delete(noteId);
    }

    public List<Note> getByUserId(Integer userId) {
        return noteMapper.getByUserId(userId);
    }
}
