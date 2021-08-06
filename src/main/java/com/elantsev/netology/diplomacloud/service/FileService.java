package com.elantsev.netology.diplomacloud.service;

import com.elantsev.netology.diplomacloud.config.jwt.JwtTokenProvider;
import com.elantsev.netology.diplomacloud.exception.*;
import com.elantsev.netology.diplomacloud.model.FileInCloud;
import com.elantsev.netology.diplomacloud.repository.FilesRepository;
import com.elantsev.netology.diplomacloud.utils.CloudLogger;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.List;

@Service
public class FileService {
    final static String SEP = File.separator;
    private final FilesRepository filesRepository;
    private final JwtTokenProvider jwtTokenProvider;

    private final CloudLogger cloudLogger;

    public FileService(FilesRepository filesRepository, JwtTokenProvider jwtTokenProvider, CloudLogger cloudLogger) {
        this.filesRepository = filesRepository;
        this.jwtTokenProvider = jwtTokenProvider;
        this.cloudLogger = cloudLogger;
    }

    public List<FileInCloud> getFilesList(int limit, String token){
        if (limit <= 0) {
            cloudLogger.logError("Invalid files limit: "+limit);
            throw new ErrorInputData("Service said: Error input data!");
        }
        String tableName;
        try {
            String userName = jwtTokenProvider.getUserName(token);
            tableName = getTableName(userName);
        } catch (Exception e) {
            cloudLogger.logError(String.format("Method 'getFilesList' thrown exception %s for unauthorized user", e.toString()));
            throw new ErrorUnauthorized("Service said: Unauthorized error!");
        }
        try {
            return filesRepository.getFilesList(limit, tableName);
        } catch (Exception e) {
            cloudLogger.logError(String.format("Method 'getFilesList' thrown exception %s for user %s",e.toString(),jwtTokenProvider.getUserName(token)));
            throw new ErrorGettingFileList("Service said: Error getting file list!");
        }

    }

    public String downloadFile(String fileName, String token) {
        String userName = getUserName(token);

        String path = filesRepository.getFullFileName(fileName, getTableName(userName));
        if(path==null) {
            cloudLogger.logError(String.format("Method 'downloadFile' generated exception ErrorInputData. Path was null. File: %s User: %s",fileName, userName));
            throw new ErrorInputData("Service said: Error input data!");
        }
        StringBuilder fullPath = new StringBuilder("cloud" + SEP + "users" + SEP)
                .append(userName)
                .append(path.replace("\\", SEP));
        cloudLogger.logInfo(String.format("Method 'downloadFile' completed successfully. File: %s User: %s",fileName, userName));
        return String.valueOf(fullPath);
    }

    public String deleteFile(String fileName, String token) {
        String userName = getUserName(token);
        String result;
        try {
            result = filesRepository.deleteFile(getTableName(userName), fileName);
        }catch (Exception e){
            cloudLogger.logError(String.format("Method 'deleteFile' thrown exception %s File: %s User: %s",e.toString(), fileName,userName));
            throw new ErrorDeleteFile("Service said: Error delete file!");
        }
        switch (result) {
            case "No File":
                cloudLogger.logError(String.format("Method 'deleteFile' generated exception ErrorInputData File: %s User: %s",fileName,userName));
                throw new ErrorInputData("Service said: Error input data!");
            case "Fail":
                cloudLogger.logError(String.format("Method 'deleteFile' generated exception ErrorDeleteFile File: %s User: %s",fileName,userName));
                throw new ErrorDeleteFile("Service said: Error delete file!");
        }
        cloudLogger.logInfo(String.format("Method 'deleteFile' completed successfully. File: %s User: %s",fileName, userName));
        return result;
    }

    public String renameFile(String fileName, String newFileName, String token) {
        String userName = getUserName(token);
        String result;
        try{
            result = filesRepository.renameFile(getTableName(userName), fileName, newFileName, userName);
        }catch(Exception e){
            cloudLogger.logError(String.format("Method 'renameFile' thrown exception %s File: %s to file %s User: %s", e.toString(), fileName, newFileName, userName));
            throw new ErrorUploadFile("Service said: Rename error!");
        }
        if(!result.equals("OK")) {
            cloudLogger.logError(String.format("Method 'renameFile' generated exception ErrorInputData File: %s to file %s User: %s", fileName, newFileName, userName));
            throw new ErrorInputData("Service said: " + result);
        }
        cloudLogger.logInfo(String.format("Method 'renameFile' completed successfully. File: %s to file %s User: %s", fileName, newFileName, userName));
        return result;
    }

    public String uploadFile(String fileName, MultipartFile file, String token) {
        String userName = getUserName(token);
        String result;
        try {
            StringBuilder fullPath = new StringBuilder("cloud" + SEP + "users" + SEP)
                    .append(userName)
                    .append(SEP);
            result = filesRepository.uploadFile(getTableName(userName), fullPath.toString(), fileName, file);
        } catch (Exception e) {
            cloudLogger.logError(String.format("Method 'uploadFile' thrown exception %s File: %s User: %s", e.toString(), fileName, userName));
            throw new ErrorInputData("Service said: Error input data!");
        }
        cloudLogger.logInfo(String.format("Method 'uploadFile' completed successfully. File: %s  User: %s", fileName,  userName));
        return result;
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

    private String getTableName(String username) {
        return username.replace('@', '_').replace('.', '_');
    }


}
