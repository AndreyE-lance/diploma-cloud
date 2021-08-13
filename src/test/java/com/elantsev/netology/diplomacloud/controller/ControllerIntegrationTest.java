package com.elantsev.netology.diplomacloud.controller;

import com.elantsev.netology.diplomacloud.config.jwt.JwtTokenProvider;
import com.elantsev.netology.diplomacloud.model.FileInCloud;
import com.elantsev.netology.diplomacloud.repository.JpaFilesRepository;
import com.elantsev.netology.diplomacloud.service.FileService;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockServletContext;
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

}

