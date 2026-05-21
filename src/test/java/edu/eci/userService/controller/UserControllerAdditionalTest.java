package edu.eci.userService.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.eci.userService.dto.UserDTO;
import edu.eci.userService.enums.UserRoleEnum;
import edu.eci.userService.exceptions.InvalidCredentialsException;
import edu.eci.userService.services.UserService;
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

import java.time.LocalDate;

import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(UserController.class)
@Import(GlobalExceptionHandler.class)
@DisplayName("UserController - casos adicionales")
class UserControllerAdditionalTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    private ObjectMapper objectMapper;
    private UserDTO sampleDTO;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        objectMapper.findAndRegisterModules();

        sampleDTO = new UserDTO();
        sampleDTO.setId(1L);
        sampleDTO.setName("Juan Perez");
        sampleDTO.setEmail("juan@gmail.com");
        sampleDTO.setBirthDate(LocalDate.of(2000, 5, 15));
        sampleDTO.setRole(UserRoleEnum.STUDENT);
        sampleDTO.setRelationship("student");
        sampleDTO.setAcademicProgram("Ingenieria de Sistemas");
        sampleDTO.setSemester(5);
        sampleDTO.setIdentificationType("CC");
        sampleDTO.setIdentificationNumber(1000123456L);
        sampleDTO.setPhone(3001234567L);
        sampleDTO.setSystemRole("PLAYER");
    }


    @Nested
    @DisplayName("POST /api/users/login")
    class Login {

        @Test
        @DisplayName("Debe retornar 200 cuando las credenciales son válidas")
        void shouldReturn200WithValidCredentials() throws Exception {
            when(userService.authenticate("juan@gmail.com", "password123")).thenReturn(sampleDTO);

            mockMvc.perform(post("/api/users/login")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content("{\"email\": \"juan@gmail.com\", \"password\": \"password123\"}"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.email", is("juan@gmail.com")));
        }

        @Test
        @DisplayName("Debe retornar 401 cuando las credenciales son inválidas")
        void shouldReturn401WithInvalidCredentials() throws Exception {
            when(userService.authenticate("juan@gmail.com", "wrong"))
                    .thenThrow(new InvalidCredentialsException("Invalid credentials"));

            mockMvc.perform(post("/api/users/login")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content("{\"email\": \"juan@gmail.com\", \"password\": \"wrong\"}"))
                    .andExpect(status().isUnauthorized());
        }

        @Test
        @DisplayName("Debe retornar 400 cuando los datos son inválidos")
        void shouldReturn400WithMissingData() throws Exception {
            when(userService.authenticate("", ""))
                    .thenThrow(new IllegalArgumentException("Email and password are required"));

            mockMvc.perform(post("/api/users/login")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content("{\"email\": \"\", \"password\": \"\"}"))
                    .andExpect(status().isBadRequest());
        }
    }


    @Nested
    @DisplayName("POST /api/users/validate-jerseys")
    class ValidateJerseys {

        @Test
        @DisplayName("Debe retornar 200 con valid=true para lista de jugadores")
        void shouldReturn200WithValidTrue() throws Exception {
            mockMvc.perform(post("/api/users/validate-jerseys")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content("{\"playerIds\": [1, 2, 3]}"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.valid", is(true)));
        }

        @Test
        @DisplayName("Debe retornar 200 con lista vacía de playerIds")
        void shouldReturn200WithEmptyPlayerIds() throws Exception {
            mockMvc.perform(post("/api/users/validate-jerseys")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content("{\"playerIds\": []}"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.valid", is(true)));
        }

        @Test
        @DisplayName("Debe retornar 200 sin campo playerIds (usa default vacío)")
        void shouldReturn200WithoutPlayerIds() throws Exception {
            mockMvc.perform(post("/api/users/validate-jerseys")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content("{}"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.valid", is(true)));
        }
    }


    @Nested
    @DisplayName("POST /api/users/validate-programs")
    class ValidatePrograms {

        @Test
        @DisplayName("Debe retornar 200 con valid=true para lista de jugadores")
        void shouldReturn200WithValidTrue() throws Exception {
            mockMvc.perform(post("/api/users/validate-programs")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content("{\"playerIds\": [1, 2, 3]}"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.valid", is(true)));
        }

        @Test
        @DisplayName("Debe retornar 200 sin campo playerIds (usa default vacío)")
        void shouldReturn200WithoutPlayerIds() throws Exception {
            mockMvc.perform(post("/api/users/validate-programs")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content("{}"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.valid", is(true)));
        }
    }


    @Nested
    @DisplayName("PUT /api/users/{id}/system-role")
    class UpdateSystemRole {

        @Test
        @DisplayName("Debe retornar 200 cuando el systemRole es válido")
        void shouldReturn200WithValidSystemRole() throws Exception {
            doNothing().when(userService).updateSystemRole(1L, "CAPTAIN");

            mockMvc.perform(put("/api/users/1/system-role")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content("{\"systemRole\": \"CAPTAIN\"}"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.message", is("System role updated successfully")))
                    .andExpect(jsonPath("$.systemRole", is("CAPTAIN")));
        }

        @Test
        @DisplayName("Debe retornar error cuando systemRole es vacío")
        void shouldReturnErrorWhenSystemRoleIsEmpty() throws Exception {
            mockMvc.perform(put("/api/users/1/system-role")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content("{\"systemRole\": \"\"}"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.error").exists());
        }

        @Test
        @DisplayName("Debe retornar error cuando falta el campo systemRole")
        void shouldReturnErrorWhenSystemRoleFieldMissing() throws Exception {
            mockMvc.perform(put("/api/users/1/system-role")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content("{}"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.error").exists());
        }
    }


    @Nested
    @DisplayName("GET /api/users/{id} — edge cases")
    class GetByIdEdgeCases {

        @Test
        @DisplayName("Debe retornar 200 cuando getUserById retorna null")
        void shouldReturn200WhenUserNotFound() throws Exception {
            when(userService.getUserById(99L)).thenReturn(null);

            mockMvc.perform(get("/api/users/99"))
                    .andExpect(status().isOk());
        }
    }
}
