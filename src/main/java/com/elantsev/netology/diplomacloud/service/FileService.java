package com.elantsev.netology.diplomacloud.service;

import com.elantsev.netology.diplomacloud.config.jwt.JwtTokenProvider;
import com.elantsev.netology.diplomacloud.model.FileInCloud;
import com.elantsev.netology.diplomacloud.repository.FilesRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.List;

@Service
public class FileService {
    final static String SEP = File.separator;
    private final FilesRepository filesRepository;
    private final JwtTokenProvider jwtTokenProvider;

    public FileService(FilesRepository filesRepository, JwtTokenProvider jwtTokenProvider) {
        this.filesRepository = filesRepository;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    public List<FileInCloud> getFilesList(int limit, String token){
        String tableName = getTableName(jwtTokenProvider.getUserName(token));
        return filesRepository.getFilesList(limit,tableName);
    }

    public String downloadFile(String filename, String token){
        String username = jwtTokenProvider.getUserName(token);
        String path = filesRepository.getFullFileName(filename,getTableName(username));
        StringBuilder fullPath = new StringBuilder("cloud"+SEP+"users"+SEP)
                .append(username)
                .append(path.replace("\\", SEP));

        return String.valueOf(fullPath);
    }

    public String deleteFile(String fileName, String token){
        String username = jwtTokenProvider.getUserName(token);
        return filesRepository.deleteFile(getTableName(username), fileName);
    }

    public String renameFile(String fileName, String newFileName, String token) {
        String userName = jwtTokenProvider.getUserName(token);
        return filesRepository.renameFile(getTableName(userName), fileName, newFileName);
    }

    public String uploadFile(String fileName, MultipartFile file, String token) {
        String userName = jwtTokenProvider.getUserName(token);
        StringBuilder fullPath = new StringBuilder("cloud"+SEP+"users"+SEP)
                .append(userName)
                .append(SEP);
        return filesRepository.uploadFile(getTableName(userName), fullPath.toString(),fileName,file);
    }

    private String getTableName(String username){
        return username.replace('@','_').replace('.','_');
    }

}
