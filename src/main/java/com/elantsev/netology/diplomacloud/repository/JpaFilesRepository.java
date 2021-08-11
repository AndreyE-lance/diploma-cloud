package com.elantsev.netology.diplomacloud.repository;

import com.elantsev.netology.diplomacloud.model.FileInCloud;
import org.hibernate.annotations.SQLInsert;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;



@Repository
public interface JpaFilesRepository extends JpaRepository<FileInCloud,String> {

    List<FileInCloud> getFilesByUsernameAndDeletedIsFalse(String name);

    FileInCloud getFileInCloudByFilenameAndAndUsername(String filename, String username);

    @Modifying
    @Transactional
    @Query("update FileInCloud fic set fic.deleted = true, fic.deleteddate=current_timestamp where fic.filename = :filename and fic.username=:username")
    int setDelete(@Param("filename") String filename, @Param("username") String username);

    @Modifying
    @Transactional
    @Query("update FileInCloud fic set fic.filename = :newfilename where fic.filename = :filename and fic.username=:username")
    int setNewName(@Param("username") String username, @Param("filename") String filename, @Param("newfilename") String newfilename);

    @Modifying
    @Transactional
    @Query(value = "insert into files(filename, username, size, loaddate, deleted) VALUES (:filename, :username, :filesize, current_timestamp, false)", nativeQuery = true)
    int insertNewFile(@Param("username") String username, @Param("filename") String filename, @Param("filesize") long filesilze);

}

