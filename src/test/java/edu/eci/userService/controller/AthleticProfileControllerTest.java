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

/**
 * Tests de integración de la capa web para {@link AthleticProfileController}.
 */
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

    // ── GET /AthleticProfile ─────────────────────────────────────────────────

    @Nested
    @DisplayName("GET /AthleticProfile")
    class GetAll {

        @Test
        @DisplayName("Debe retornar 200 con lista de perfiles")
        void shouldReturn200WithList() throws Exception {
            when(athleticProfileService.getAllAthleticProfiles()).thenReturn(List.of(sampleDTO));

            mockMvc.perform(get("/AthleticProfile"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$", hasSize(1)))
                    .andExpect(jsonPath("$[0].nickName", is("Juancho")))
                    .andExpect(jsonPath("$[0].position", is("delantero")));
        }

        @Test
        @DisplayName("Debe retornar 200 con lista vacía")
        void shouldReturn200WithEmptyList() throws Exception {
            when(athleticProfileService.getAllAthleticProfiles()).thenReturn(List.of());

            mockMvc.perform(get("/AthleticProfile"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$", hasSize(0)));
        }
    }

    // ── GET /AthleticProfile/{UserId} ────────────────────────────────────────

    @Nested
    @DisplayName("GET /AthleticProfile/{UserId}")
    class GetByUserId {

        @Test
        @DisplayName("Debe retornar 200 con el perfil cuando existe")
        void shouldReturn200WhenFound() throws Exception {
            when(athleticProfileService.getAthleticProfilesByUserId(1L)).thenReturn(sampleDTO);

            mockMvc.perform(get("/AthleticProfile/1"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.id", is(1)))
                    .andExpect(jsonPath("$.nickName", is("Juancho")));
        }
    }

    // ── POST /AthleticProfile ────────────────────────────────────────────────

    @Nested
    @DisplayName("POST /AthleticProfile")
    class CreateProfile {

        @Test
        @DisplayName("Debe retornar 200 con el perfil creado")
        void shouldReturn200WithCreatedProfile() throws Exception {
            when(athleticProfileService.createAthleticProfile(any(AthleticProfileDTO.class)))
                    .thenReturn(sampleDTO);

            // Construimos el request body usando el record del controller
            String body = """
                    {
                        "dorsalNumber": 10,
                        "id": 1,
                        "nickName": "Juancho",
                        "position": "delantero",
                        "laterality": "diestro",
                        "stature": "175cm",
                        "state": "activo",
                        "user": { "id": 1, "name": "Juan Pérez" }
                    }
                    """;

            mockMvc.perform(post("/AthleticProfile")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(body))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.nickName", is("Juancho")));

            verify(athleticProfileService).createAthleticProfile(any(AthleticProfileDTO.class));
        }
    }

    // ── PUT /AthleticProfile/{UserId} ────────────────────────────────────────

    @Nested
    @DisplayName("PUT /AthleticProfile/{UserId}")
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
                        "id": 1,
                        "nickName": "Juancho2",
                        "position": "volante",
                        "laterality": "zurdo",
                        "stature": "180cm",
                        "state": "activo"
                    }
                    """;

            mockMvc.perform(put("/AthleticProfile/1")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(body))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.nickName", is("Juancho2")));
        }
    }

    // ── DELETE /AthleticProfile/{UserId} ─────────────────────────────────────

    @Nested
    @DisplayName("DELETE /AthleticProfile/{UserId}")
    class DeleteProfile {

        @Test
        @DisplayName("Debe retornar 200 con mensaje de éxito")
        void shouldReturn200WithSuccessMessage() throws Exception {
            doNothing().when(athleticProfileService).deleteAthleticProfile(1L);

            mockMvc.perform(delete("/AthleticProfile/1"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.message", is("User deleted successfully")));
        }

        @Test
        void shouldReturn404WhenProfileNotFound() throws Exception {

            doThrow(new IllegalArgumentException("Athletic profile not found"))
                    .when(athleticProfileService)
                    .deleteAthleticProfile(99L);

            mockMvc.perform(delete("/AthleticProfile/99"))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.error", is("Athletic profile not found")));
        }
    }
}
