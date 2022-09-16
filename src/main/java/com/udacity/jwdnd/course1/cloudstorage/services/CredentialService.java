package com.udacity.jwdnd.course1.cloudstorage.services;

import com.udacity.jwdnd.course1.cloudstorage.mapper.CredentialMapper;
import com.udacity.jwdnd.course1.cloudstorage.model.Credential;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.Base64;
import java.util.List;

@Service
public class CredentialService {
    private CredentialMapper credentialMapper;
    private HashService hashService;

    public CredentialService(CredentialMapper credentialMapper, HashService hashService) {
        this.credentialMapper = credentialMapper;
        this.hashService = hashService;
    }

    public int createCredential(Credential credential) {
        return credentialMapper.insert(new Credential(null, credential.getUrl(), credential.getUserName(), credential.getKey(), credential.getPassword(), credential.getUserId()));
    }

    public int updateCredential(Integer credentialId, Credential credential) {
        return credentialMapper.update(credentialId, new Credential(credential.getCredentialId(), credential.getUrl(), credential.getUserName(), credential.getKey(), credential.getPassword(), credential.getUserId()));
    }

    public Credential getCredential(Integer credentialId) {
        return credentialMapper.getCredential(credentialId);
    }

    public int deleteCredential(Integer credentialId) {
        return credentialMapper.delete(credentialId);
    }

    public List<Credential> getByUserId(Integer userId) {
        return credentialMapper.getByUserId(userId);
    }
}
