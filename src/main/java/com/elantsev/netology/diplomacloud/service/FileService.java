package com.elantsev.netology.diplomacloud.service;

import com.elantsev.netology.diplomacloud.config.jwt.JwtTokenProvider;
import com.elantsev.netology.diplomacloud.exception.*;
import com.elantsev.netology.diplomacloud.model.FileInCloud;
import com.elantsev.netology.diplomacloud.repository.FilesRepository;
import com.elantsev.netology.diplomacloud.repository.JpaFilesRepository;
import com.elantsev.netology.diplomacloud.utils.CloudLogger;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.List;

@Service
public class FileService {
    final static String SEP = File.separator;
    private final JwtTokenProvider jwtTokenProvider;

    private final JpaFilesRepository jpaFilesRepository;

    private final CloudLogger cloudLogger;

    public FileService( JwtTokenProvider jwtTokenProvider, JpaFilesRepository jpaFilesRepository, CloudLogger cloudLogger) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.jpaFilesRepository = jpaFilesRepository;
        this.cloudLogger = cloudLogger;
    }

    public List<FileInCloud> getFilesList(int limit, String token) {
        if (limit <= 0) {
            cloudLogger.logError("Invalid files limit: " + limit);
            throw new ErrorInputData("Service said: Error input data!");
        }

        String userName;
        try {
            userName = jwtTokenProvider.getUserName(token);

        } catch (Exception e) {
            cloudLogger.logError(String.format("Method 'getFilesList' thrown exception %s for unauthorized user", e.toString()));
            throw new ErrorUnauthorized("Service said: Unauthorized error!");
        }
        try {
            List<FileInCloud> result = jpaFilesRepository.getFilesByUsernameAndDeletedIsFalse(userName);
            return result.subList(0, Math.min(limit, result.size()));
        } catch (Exception e) {
            cloudLogger.logError(String.format("Method 'getFilesList' thrown exception %s for user %s", e.toString(), jwtTokenProvider.getUserName(token)));
            throw new ErrorGettingFileList("Service said: Error getting file list!");
        }

    }

    public String downloadFile(String fileName, String token) {
        String userName = getUserName(token);
        String path = SEP + jpaFilesRepository.getFileInCloudByFilenameAndAndUsername(fileName, userName).getFilename();

        if (path == null) {
            cloudLogger.logError(String.format("Method 'downloadFile' generated exception ErrorInputData. Path was null. File: %s User: %s", fileName, userName));
            throw new ErrorInputData("Service said: Error input data!");
        }

        StringBuilder fullPath = new StringBuilder("cloud" + SEP + "users" + SEP)
                .append(userName)
                .append(path);
        cloudLogger.logInfo(String.format("Method 'downloadFile' completed successfully. File: %s User: %s", fileName, userName));
        return String.valueOf(fullPath);
    }

    public String deleteFile(String fileName, String token) {
        String userName = getUserName(token);
        int result;
        try {
            result = jpaFilesRepository.setDelete(fileName, userName);
        } catch (Exception e) {
            cloudLogger.logError(String.format("Method 'deleteFile' thrown exception %s File: %s User: %s", e.toString(), fileName, userName));
            throw new ErrorDeleteFile("Service said: Error delete file!");
        }
        if (result == 0) {
            cloudLogger.logError(String.format("Method 'deleteFile' generated exception ErrorInputData File: %s User: %s", fileName, userName));
            throw new ErrorInputData("Service said: Error input data!");
        }
        cloudLogger.logInfo(String.format("Method 'deleteFile' completed successfully. File: %s User: %s", fileName, userName));
        return "OK";
    }

    public String renameFile(String fileName, String newFileName, String token) {
        String userName = getUserName(token);
        int result;
        try {
            result = jpaFilesRepository.setNewName(userName, fileName, newFileName);
        } catch (Exception e) {
            cloudLogger.logError(String.format("Method 'renameFile' thrown exception %s File: %s to file %s User: %s",
                    e.toString(), fileName, newFileName, userName));
            throw new ErrorUploadFile("Service said: Rename error!");
        }
        if (result==0) {
            cloudLogger.logError(String.format("Method 'renameFile' generated exception ErrorInputData File: %s to file %s User: %s",
                    fileName, newFileName, userName));
            throw new ErrorInputData("Service said: " + result);
        }
        File oldFile = new File(getFullPath(fileName, userName));
        if(!oldFile.renameTo(new File(getFullPath(newFileName, userName)))){
            result = jpaFilesRepository.setNewName(userName, newFileName, fileName);
            throw new ErrorUploadFile("Service said: Rename error!");
        }

        cloudLogger.logInfo(String.format("Method 'renameFile' completed successfully. File: %s to file %s User: %s",
                fileName, newFileName, userName));
        return "OK";
    }


    public String uploadFile(String fileName, MultipartFile file, String token) {
        String userName = getUserName(token);
        int result;

        if (!file.isEmpty()) {
            try {
                byte[] bytes = file.getBytes();
                BufferedOutputStream stream =
                        new BufferedOutputStream(new FileOutputStream(new File(getFullPath(fileName,userName))));
                stream.write(bytes);
                stream.close();
                result = jpaFilesRepository.insertNewFile(userName,fileName,file.getSize());

        } catch (Exception e) {
            cloudLogger.logError(String.format("Method 'uploadFile' thrown exception %s File: %s User: %s", e.toString(), fileName, userName));
            throw new ErrorInputData("Service said: Error input data!");
        }
        }
        cloudLogger.logInfo(String.format("Method 'uploadFile' completed successfully. File: %s  User: %s", fileName, userName));
        return "OK";
    }


    private String getFullPath(String fileName, String userName) {
        StringBuilder newFilePath = new StringBuilder("cloud" + SEP + "users" + SEP)
                .append(userName)
                .append(SEP)
                .append(fileName);
        return newFilePath.toString();
    }


    private String getUserName(String token) {
        String userName;
        try {
            userName = jwtTokenProvider.getUserName(token);
        } catch (Exception e) {
            cloudLogger.logError(String.format("Method 'getUserName' thrown exception %s", e.toString()));
            throw new ErrorUnauthorized("Service said: Unauthorized error!");
        }
        return userName;
    }

}
