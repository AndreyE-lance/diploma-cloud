package com.elantsev.netology.diplomacloud.controller;

import com.elantsev.netology.diplomacloud.config.jwt.JwtTokenProvider;
import com.elantsev.netology.diplomacloud.repository.FilesRepository;
import org.junit.Assert;
import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockServletContext;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import javax.servlet.ServletContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
public class ControllerIntegrationTest {

    @Autowired
    private WebApplicationContext webApplicationContext;
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private JwtTokenProvider jwtTokenProvider;

    @MockBean
    private FilesRepository filesRepository;




    @Test
    public void ControllerTest() {
        ServletContext servletContext = webApplicationContext.getServletContext();
        Assert.assertNotNull(servletContext);
        Assert.assertTrue(servletContext instanceof MockServletContext);
        Assert.assertNotNull(webApplicationContext.getBean(CloudController.class));
    }

    @Test
    public void testListUnauthorizedThrow() throws Exception{
        mockMvc.perform(get("/list")
                .param("limit","3")
                .header("auth-token","Bearer "))
                .andExpect(status().isUnauthorized());
    }

    @Test
    public void testListBadRequestThrow() throws Exception{
        mockMvc.perform(get("/list")
                .param("limit","-3")
                .header("auth-token","Bearer test"))
                .andExpect(status().isBadRequest());
    }

   /* @Test
    public void testListIntServerErrorThrow() throws Exception{
        Mockito.when(jwtTokenProvider.getUserName(Mockito.any()))
                .thenReturn("max@cloud.com");
        Mockito.when(filesRepository.getFilesList(3,"max_cloud_com"))
                .thenThrow(new Exception());
        mockMvc.perform(get("/list")
                .param("limit","3")
                .header("auth-token","Bearer test"))
                .andExpect(status().isInternalServerError());
    }*/

}

