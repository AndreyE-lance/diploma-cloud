package com.elantsev.netology.diplomacloud.controller;

import com.elantsev.netology.diplomacloud.dto.Login;
import com.elantsev.netology.diplomacloud.dto.NewFileName;
import com.elantsev.netology.diplomacloud.dto.Token;
import com.elantsev.netology.diplomacloud.model.FileInCloud;
import com.elantsev.netology.diplomacloud.service.AuthService;
import com.elantsev.netology.diplomacloud.service.FileService;
import com.elantsev.netology.diplomacloud.utils.MediaTypeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletContext;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@RestController
@RequestMapping("/")
public class CloudController {

    private final AuthService authService;
    private final FileService fileService;

    @Autowired
    private ServletContext servletContext;

    public CloudController(AuthService authService, FileService fileService) {
        this.authService = authService;
        this.fileService = fileService;
    }

    @PostMapping("/login")
    public ResponseEntity<Token> login(@RequestBody Login login){
        String res = authService.getToken(login);
        System.out.println(login);
        return new ResponseEntity<>( new Token(res), HttpStatus.OK);
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(@RequestHeader("auth-token") String authToken) {
        final Boolean isRemove = authService.removeToken(authToken);
        return new ResponseEntity<>("Success logout", HttpStatus.OK);
    }

    @GetMapping("/list")
    public ResponseEntity<List<FileInCloud>> list(@RequestHeader("auth-token") String authToken,
                                                  @RequestParam("limit") int limit){
        List<FileInCloud> list = fileService.getFilesList(limit, authToken.substring(7));
        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    @GetMapping("/file")
    public ResponseEntity<Resource> download(@RequestHeader("auth-token") String authToken,
                                             @RequestParam("filename") String fileName) throws IOException {
        MediaType mediaType = MediaTypeUtils.getMediaTypeForFileName(this.servletContext, fileName);
        File file = new File(fileService.downloadFile(fileName,authToken.substring(7)));
        System.out.println(file.length());
        Path path = Paths.get(file.getAbsolutePath());
        ByteArrayResource resource = new ByteArrayResource(Files.readAllBytes(path));
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=" + file.getName())
                .contentType(mediaType)
                .contentLength(file.length())
                .body(resource);

    }

    @DeleteMapping("/file")
    public ResponseEntity<String> delete(@RequestHeader("auth-token") String authToken,
                                         @RequestParam("filename") String fileName) {
        String result =fileService.deleteFile(fileName, authToken.substring(7));
        if (result.equals("OK")) {
            return new ResponseEntity<>("Delete success", HttpStatus.OK);
        } else {
            return new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/file")
    public ResponseEntity<String> rename(@RequestHeader("auth-token") String authToken,
                                         @RequestParam("filename") String fileName,
                                         @RequestBody NewFileName newFileName){
        String result = fileService.renameFile(fileName, newFileName.getFilename(), authToken.substring(7));
        if (result.equals("OK")) {
            return new ResponseEntity<>("Rename success", HttpStatus.OK);
        } else {
            return new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/file")
    public ResponseEntity<String> upload(@RequestHeader("auth-token") String authToken,
                                                     @RequestParam("filename") String fileName, MultipartFile file){
        String result = fileService.uploadFile(fileName,file,authToken.substring(7));
        if (result.equals("OK")) {
            return new ResponseEntity<>("Upload success", HttpStatus.OK);
        } else {
            return new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);
        }
    }



}
