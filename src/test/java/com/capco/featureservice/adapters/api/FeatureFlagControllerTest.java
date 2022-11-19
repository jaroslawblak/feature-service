package com.capco.featureservice.adapters.api;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.capco.featureservice.adapters.api.dto.FeatureFlagResponse;
import com.capco.featureservice.adapters.api.dto.FullFeatureFlagResponse;
import com.capco.featureservice.application.service.FeatureFlagService;
import com.capco.featureservice.config.security.Permission;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
class FeatureFlagControllerTest {

    private static final String jsonRequest = "{\"name\" : \"test\", \"permissions\": [\"BASIC\"]}";

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private FeatureFlagService service;

    @Test
    public void testWithoutLogin() throws Exception {
        //get
        mockMvc.perform(get("/v1/feature-flags")).andExpect(status().isUnauthorized());
        mockMvc.perform(get("/v1/feature-flags/1")).andExpect(status().isUnauthorized());

        //post
        mockMvc.perform(post("/v1/feature-flags")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isUnauthorized());

        mockMvc.perform(put("/v1/feature-flags/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(username = "user", authorities = "ROLE_USER")
    public void testWithoutValidPermission() throws Exception {

        //get
        mockMvc.perform(get("/v1/feature-flags")).andExpect(status().isForbidden());
        mockMvc.perform(get("/v1/feature-flags/1")).andExpect(status().isForbidden());

        //post
        mockMvc.perform(post("/v1/feature-flags")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isForbidden());

        //put
        mockMvc.perform(put("/v1/feature-flags/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "admin", password = "admin", authorities = "ROLE_ADMIN")
    public void testWithValidPermission() throws Exception {
        when(service.findAll())
                .thenReturn(List.of(new FullFeatureFlagResponse(
                        new FeatureFlagResponse(1L, "testName", true), Set.of(Permission.BASIC))));
        //get
        mockMvc.perform(get("/v1/feature-flags")).andExpect(status().isOk());
        mockMvc.perform(get("/v1/feature-flags/1")).andExpect(status().isOk());

        //post
        mockMvc.perform(post("/v1/feature-flags")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isOk());

        //put
        mockMvc.perform(put("/v1/feature-flags/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isOk());

    }
}
