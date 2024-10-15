package com.colak.springtutorial.upload.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

@WebMvcTest(FileUploadController.class)
class FileUploadControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void testFileUpload() throws Exception {
        MockMultipartFile file = new MockMultipartFile(
                "file",
                "testFile.txt",
                MediaType.TEXT_PLAIN_VALUE,
                "This is a test file".getBytes()
        );

        mockMvc.perform(MockMvcRequestBuilders.multipart("/upload")
                        .file(file))
                .andExpect(status().isOk())
                .andExpect(content().string("File uploaded successfully: testFile.txt"));
    }
}
