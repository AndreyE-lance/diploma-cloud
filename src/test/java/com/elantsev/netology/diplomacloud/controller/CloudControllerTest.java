package com.elantsev.netology.diplomacloud.controller;

import com.elantsev.netology.diplomacloud.dto.Login;
import com.elantsev.netology.diplomacloud.dto.NewFileName;
import com.elantsev.netology.diplomacloud.dto.Token;
import com.elantsev.netology.diplomacloud.model.FileInCloud;
import com.elantsev.netology.diplomacloud.service.AuthService;
import com.elantsev.netology.diplomacloud.service.FileService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;


import java.util.ArrayList;
import java.util.List;



public class CloudControllerTest {
    AuthService mockAuthService = Mockito.mock(AuthService.class);
    FileService mockFileService = Mockito.mock(FileService.class);
    Login testlogin = new Login("Username", "password");

    @Test
    public void testLogin(){
        Mockito.when(mockAuthService.getToken(testlogin))
                .thenReturn("Bearer testToken");
        ResponseEntity<Token> actual =
                new CloudController(mockAuthService,mockFileService)
                        .login(testlogin);
        ResponseEntity<Token> expected = new ResponseEntity<Token>(new Token("Bearer testToken"), HttpStatus.OK);
        Assertions.assertEquals(actual, expected);
    }



    @Test
    public void testLogout(){
        Mockito.when(mockAuthService.removeToken("test"))
                .thenReturn(true);
        ResponseEntity<String> actual =
                new CloudController(mockAuthService,mockFileService)
                        .logout("test");
        ResponseEntity<Token> expected =
                new ResponseEntity<>( HttpStatus.OK);
        Assertions.assertEquals(actual, expected);
    }

   /* @Test
    public void testList() throws Exception {
        List<FileInCloud> testList = new  ArrayList<>();
        testList.add(new FileInCloud());
        testList.add(new FileInCloud());
        testList.add(new FileInCloud());
        Mockito.when(mockFileService.getFilesList(3, "test"))
                .thenReturn(testList);
        ResponseEntity<List<FileInCloud>> actual =
                new CloudController(mockAuthService,mockFileService)
                            .list(  "Bearer test",3);
        ResponseEntity<List<FileInCloud>> expected =
                new ResponseEntity<>(testList,HttpStatus.OK);
        Assertions.assertEquals(actual,expected);
    }*/

    @Test
    public void testDelete() throws Exception{
        Mockito.when(mockFileService.deleteFile("test.jpg", "test"))
                   .thenReturn("OK");
        ResponseEntity<String> actual =
                new CloudController(mockAuthService,mockFileService)
                        .delete(  "Bearer test","test.jpg");
        ResponseEntity<String> expected =
                new ResponseEntity<>("Delete success",HttpStatus.OK);
        Assertions.assertEquals(actual,expected);
    }

    @Test
    public void testRename() throws Exception{
        NewFileName newFileName = new NewFileName("newTest.jpg");
        Mockito.when(mockFileService.renameFile("test.jpg","newTest.jpg","test"))
                .thenReturn("OK");
        ResponseEntity<String> actual = new CloudController(mockAuthService,mockFileService)
                .rename(  "Bearer test","test.jpg", newFileName);
        ResponseEntity<String> expected =
                new ResponseEntity<>("Rename success",HttpStatus.OK);
        Assertions.assertEquals(actual,expected);

    }

    @Test
    public void testUpload() throws Exception{
        MultipartFile multipartFile = new MockMultipartFile("test.jpg", "test".getBytes());
        Mockito.when(mockFileService.uploadFile("test.jpg",multipartFile,"test"))
                .thenReturn("OK");
        ResponseEntity<String> actual = new CloudController(mockAuthService,mockFileService)
                .upload("Bearer test", "test.jpg", multipartFile);
        ResponseEntity<String> expected =
                new ResponseEntity<>("Upload success", HttpStatus.OK);
        Assertions.assertEquals(actual,expected);

    }



}
