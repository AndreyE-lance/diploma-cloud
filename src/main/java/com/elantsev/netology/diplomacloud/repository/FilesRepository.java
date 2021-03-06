package com.elantsev.netology.diplomacloud.repository;


import com.elantsev.netology.diplomacloud.model.FileInCloud;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.List;

@Repository
@Deprecated
public class FilesRepository {
    private final JdbcTemplate jdbcTemplate;
    final static String SEP = File.separator;


    public FilesRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<FileInCloud> getFilesList(int limit, String tableName) {
        String queryStr = "SELECT filename, sizeKB AS size FROM " + tableName + " WHERE deleted = false FETCH FIRST " + limit + " ROWS ONLY";
        List result = jdbcTemplate.query(queryStr, new BeanPropertyRowMapper(FileInCloud.class));
        return result;
    }

    public String deleteFile(String tableName, String fileName) {
        String queryStr = "SELECT filename FROM " + tableName + " WHERE deleted = false AND filename=" + '\'' + fileName + '\'';
        List res = jdbcTemplate.queryForList(queryStr, String.class);
        if (res.size() == 0) return "No File";
        queryStr = "UPDATE " + tableName + " SET deleted=true, deletedate = NOW() WHERE filename = ?";
        int result = jdbcTemplate.update(queryStr, fileName);
        if (result == 1)
            return "OK";
        else
            return "Fail";

    }

    public String getFullFileName(String fileName, String tableName) {
        String queryStr = "SELECT CONCAT(path, filename)  FROM " + tableName + " WHERE filename=" + '\'' + fileName + '\'';
        List result = jdbcTemplate.queryForList(queryStr, String.class);
        return result.get(0).toString();
    }


    public String renameFile(String tableName, String fileName, String newFileName, String userName) {
        String queryStr = "SELECT filename FROM " + tableName + " WHERE filename = ?";
        List result = jdbcTemplate.queryForList(queryStr, newFileName);
        if (result.size() != 0) {
            return "Already in use";
        } else {
            queryStr = "SELECT filename FROM " + tableName + " WHERE filename = ?";
            result = jdbcTemplate.queryForList(queryStr, fileName);
            if (result.size() == 0) {
                return "No File";
            }

            StringBuilder fullPath = new StringBuilder("cloud" + SEP + "users" + SEP)
                    .append(userName);
            File file = new File(fullPath.toString() + getFullFileName(fileName, tableName)
                    .replace("\\", SEP));
            if (file.renameTo(new File(fullPath.append(SEP).append(newFileName).toString()))) {
                queryStr = "UPDATE " + tableName + " SET filename='" + newFileName + "' WHERE filename = ?";
                int ret = jdbcTemplate.update(queryStr, fileName);
            } else return "Fail";
        }
        return "OK";
    }

    public String uploadFile(String tableName, String path, String fileName, MultipartFile file) {

        if (!file.isEmpty()) {
            try {
                byte[] bytes = file.getBytes();
                BufferedOutputStream stream =
                        new BufferedOutputStream(new FileOutputStream(new File(path + fileName)));
                stream.write(bytes);
                stream.close();
                String queryStr = "INSERT INTO " + tableName + "(path, filename, sizeKB, loaddate, deleted) VALUES ('\\',?, ?, NOW(), FALSE)";
                int ret = jdbcTemplate.update(queryStr, fileName, bytes.length);//new Object[]{fileName,bytes.length} ????????
                return "OK";
            } catch (Exception e) {
                return "Fail => " + e.getMessage();
            }
        } else {
            return "File is empty";
        }
    }
}


