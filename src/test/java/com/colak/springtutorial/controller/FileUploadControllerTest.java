package com.colak.springtutorial.controller;

import com.colak.springtutorial.service.FileStorageService;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@WebMvcTest(FileUploadController.class)
@Import(FileStorageService.class)
class FileUploadControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @Order(1)
    void testUploadFile() throws Exception {
        MockMultipartFile file = new MockMultipartFile(
                "file",
                "testFile.txt",
                MediaType.TEXT_PLAIN_VALUE,
                "This is a test file".getBytes()
        );

        mockMvc.perform(MockMvcRequestBuilders.multipart("/upload")
                        .file(file))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("File uploaded successfully: testFile.txt"));
    }

    @Test
    @Order(2)
    void testDeleteFile() throws Exception {
        String fileName = "testFile.txt";
        mockMvc.perform(MockMvcRequestBuilders.delete("/delete/{fileName}", fileName)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("File deleted successfully: " + fileName));
    }

    @Test
    @Order(3)
    void testDeleteFile_NotFound() throws Exception {
        String fileName = "nonExistentFile.txt";

        mockMvc.perform(MockMvcRequestBuilders.delete("/delete/{fileName}", fileName)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.content().string("File not found: " + fileName));
    }
}
