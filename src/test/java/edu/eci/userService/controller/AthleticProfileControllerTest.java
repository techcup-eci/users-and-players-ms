package edu.eci.userService.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.eci.userService.dto.AthleticProfileDTO;
import edu.eci.userService.entities.UserEntity;
import edu.eci.userService.services.AthleticProfileService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AthleticProfileController.class)
@Import(GlobalExceptionHandler.class)
class AthleticProfileControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AthleticProfileService athleticProfileService;

    private ObjectMapper objectMapper;
    private AthleticProfileDTO sampleDTO;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();

        UserEntity user = new UserEntity();
        user.setId(1L);
        user.setName("Juan Pérez");

        sampleDTO = new AthleticProfileDTO();
        sampleDTO.setId(1L);
        sampleDTO.setDorsalNumber(10);
        sampleDTO.setNickName("Juancho");
        sampleDTO.setPosition("delantero");
        sampleDTO.setLaterality("diestro");
        sampleDTO.setStature("175cm");
        sampleDTO.setState("activo");
        sampleDTO.setUser(user);
    }


    @Nested
    @DisplayName("GET /api/athletic-profiles")
    class GetAll {

        @Test
        @DisplayName("Debe retornar 200 con lista de perfiles")
        void shouldReturn200WithList() throws Exception {
            when(athleticProfileService.getAllAthleticProfiles()).thenReturn(List.of(sampleDTO));

            mockMvc.perform(get("/api/athletic-profiles"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$", hasSize(1)))
                    .andExpect(jsonPath("$[0].nickName", is("Juancho")))
                    .andExpect(jsonPath("$[0].position", is("delantero")));
        }

        @Test
        @DisplayName("Debe retornar 200 con lista vacía")
        void shouldReturn200WithEmptyList() throws Exception {
            when(athleticProfileService.getAllAthleticProfiles()).thenReturn(List.of());

            mockMvc.perform(get("/api/athletic-profiles"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$", hasSize(0)));
        }
    }


    @Nested
    @DisplayName("GET /api/athletic-profiles/{UserId}")
    class GetByUserId {

        @Test
        @DisplayName("Debe retornar 200 con el perfil cuando existe")
        void shouldReturn200WhenFound() throws Exception {
            when(athleticProfileService.getAthleticProfilesByUserId(1L)).thenReturn(sampleDTO);

            mockMvc.perform(get("/api/athletic-profiles/1"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.id", is(1)))
                    .andExpect(jsonPath("$.nickName", is("Juancho")));
        }
    }


    @Nested
    @DisplayName("POST /api/athletic-profiles")
    class CreateProfile {

        @Test
        @DisplayName("Debe retornar 200 con el perfil creado")
        void shouldReturn200WithCreatedProfile() throws Exception {
            when(athleticProfileService.createAthleticProfile(any(AthleticProfileDTO.class)))
                    .thenReturn(sampleDTO);

            String body = """
                    {
                        "dorsalNumber": 10,
                        "email": "juan.perez@eci.edu.co",
                        "position": "delantero",
                        "laterality": "diestro",
                        "stature": "175cm",
                        "state": "activo"
                    }
                    """;

            mockMvc.perform(post("/api/athletic-profiles")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(body))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.nickName", is("Juancho")));

            verify(athleticProfileService).createAthleticProfile(any(AthleticProfileDTO.class));
        }
    }


    @Nested
    @DisplayName("PUT /api/athletic-profiles/{UserId}")
    class UpdateProfile {

        @Test
        @DisplayName("Debe retornar 200 con el perfil actualizado")
        void shouldReturn200WithUpdatedProfile() throws Exception {
            AthleticProfileDTO updatedDTO = new AthleticProfileDTO();
            updatedDTO.setId(1L);
            updatedDTO.setNickName("Juancho2");
            updatedDTO.setPosition("volante");

            when(athleticProfileService.updateAthleticProfile(eq(1L), any(AthleticProfileDTO.class)))
                    .thenReturn(updatedDTO);

            String body = """
                    {
                        "dorsalNumber": 7,
                        "email": "juan.perez@eci.edu.co",
                        "position": "volante",
                        "laterality": "zurdo",
                        "stature": "180cm",
                        "state": "activo"
                    }
                    """;

            mockMvc.perform(put("/api/athletic-profiles/1")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(body))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.nickName", is("Juancho2")));
        }
    }


    @Nested
    @DisplayName("DELETE /api/athletic-profiles/{UserId}")
    class DeleteProfile {

        @Test
        @DisplayName("Debe retornar 200 con mensaje de éxito")
        void shouldReturn200WithSuccessMessage() throws Exception {
            doNothing().when(athleticProfileService).deleteAthleticProfile(1L);

            mockMvc.perform(delete("/api/athletic-profiles/1"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.message", is("User deleted successfully")));
        }

        @Test
        @DisplayName("Debe retornar 404 cuando el perfil no existe")
        void shouldReturn404WhenProfileNotFound() throws Exception {
            doThrow(new java.util.NoSuchElementException("Athletic profile not found"))
                    .when(athleticProfileService)
                    .deleteAthleticProfile(99L);

            mockMvc.perform(delete("/api/athletic-profiles/99"))
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.error", is("Athletic profile not found")));
        }
    }
}
