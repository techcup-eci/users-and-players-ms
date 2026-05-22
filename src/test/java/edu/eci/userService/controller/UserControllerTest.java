package edu.eci.userService.controller;

import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import edu.eci.userService.dto.UserDTO;
import edu.eci.userService.enums.AcademicLevel;
import edu.eci.userService.enums.ProfessorType;
import edu.eci.userService.enums.SchoolRelation;
import edu.eci.userService.services.IdentityRoleService;
import edu.eci.userService.services.UserService;

@WebMvcTest(UserController.class)
@Import(GlobalExceptionHandler.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @MockBean
    private IdentityRoleService identityRoleService;

    private ObjectMapper objectMapper;
    private UserDTO sampleDTO;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        sampleDTO = new UserDTO();
        sampleDTO.setId(1L);
        sampleDTO.setName("Juan Perez");
        sampleDTO.setEmail("juan.perez@eci.edu.co");
        sampleDTO.setBirthDate(LocalDate.of(2000, 5, 15));
        sampleDTO.setSchoolRelation(SchoolRelation.STUDENT);
        sampleDTO.setAcademicLevel(AcademicLevel.UNDERGRADUATE);
        sampleDTO.setProfessorType(ProfessorType.FULL_TIME);
        sampleDTO.setAcademicProgram("Ingenieria de Sistemas");
        sampleDTO.setSemester(5);
        sampleDTO.setIdentificationType("CC");
        sampleDTO.setIdentificationNumber(1000123456L);
        sampleDTO.setPhone(3001234567L);
    }

    // --- GET /api/users ---

    @Nested
    @DisplayName("GET /api/users")
    class GetAll {

        @Test
        @DisplayName("Debe retornar 200 con lista de usuarios")
        void shouldReturn200WithList() throws Exception {
            when(userService.getAllUsers()).thenReturn(List.of(sampleDTO));

            mockMvc.perform(get("/api/users"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$", hasSize(1)))
                    .andExpect(jsonPath("$[0].name", is("Juan Perez")))
                    .andExpect(jsonPath("$[0].email", is("juan.perez@eci.edu.co")));
        }

        @Test
        @DisplayName("Debe retornar 200 con lista vacia cuando no hay usuarios")
        void shouldReturn200WithEmptyList() throws Exception {
            when(userService.getAllUsers()).thenReturn(List.of());

            mockMvc.perform(get("/api/users"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$", hasSize(0)));
        }
    }

    // --- GET /api/users/{id} ---

    @Nested
    @DisplayName("GET /api/users/{id}")
    class GetById {

        @Test
        @DisplayName("Debe retornar 200 con el usuario cuando existe")
        void shouldReturn200WhenFound() throws Exception {
            when(userService.getUserById(1L)).thenReturn(sampleDTO);

            mockMvc.perform(get("/api/users/1"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.id", is(1)))
                    .andExpect(jsonPath("$.name", is("Juan Perez")));
        }
    }

    // --- POST /api/users/register ---

    @Nested
    @DisplayName("POST /api/users/register")
    class CreateUser {

        @Test
        @DisplayName("Debe retornar 200 con el usuario creado")
        void shouldReturn200WithCreatedUser() throws Exception {
            when(userService.createUser(any(UserDTO.class))).thenReturn(sampleDTO);

            UserDTO request = new UserDTO();
            request.setName("Juan Perez");
            request.setEmail("juan.perez@eci.edu.co");
            request.setBirthDate(LocalDate.of(2000, 5, 15));
            request.setSchoolRelation(SchoolRelation.STUDENT);
            request.setAcademicLevel(AcademicLevel.UNDERGRADUATE);
            request.setProfessorType(ProfessorType.FULL_TIME);
            request.setAcademicProgram("Ingenieria de Sistemas");
            request.setSemester(5);
            request.setIdentificationType("CC");
            request.setIdentificationNumber(1000123456L);
            request.setPhone(3001234567L);
            request.setPassword("plain_password");

            mockMvc.perform(post("/api/users/register")
                    .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.name", is("Juan Perez")))
                    .andExpect(jsonPath("$.email", is("juan.perez@eci.edu.co")));

            verify(userService).createUser(any(UserDTO.class));
        }
    }

    // --- PUT /api/users/{id} ---

    @Nested
    @DisplayName("PUT /api/users/{id}")
    class UpdateUser {

        @Test
        @DisplayName("Debe retornar 200 con el usuario actualizado")
        void shouldReturn200WithUpdatedUser() throws Exception {
            UserDTO updatedDTO = new UserDTO();
            updatedDTO.setId(1L);
            updatedDTO.setName("Juan Actualizado");
            updatedDTO.setEmail("juan.perez@eci.edu.co");

            when(userService.updateUser(eq(1L), any(UserDTO.class))).thenReturn(updatedDTO);

            mockMvc.perform(put("/api/users/1")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(updatedDTO)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.name", is("Juan Actualizado")));
        }
    }

    // --- DELETE /api/users/{id} ---

    @Nested
    @DisplayName("DELETE /api/users/{id}")
    class DeleteUser {

        @Test
        @DisplayName("Debe retornar 200 con mensaje de exito")
        void shouldReturn200WithSuccessMessage() throws Exception {
            doNothing().when(userService).deleteUser(1L);

            mockMvc.perform(delete("/api/users/1"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.message", is("User deleted successfully")));
        }

        @Test
        @DisplayName("Debe retornar 404 cuando el usuario no existe")
        void shouldReturn404WhenUserNotFound() throws Exception {
            doThrow(new NoSuchElementException("No user found with ID: 99"))
                    .when(userService).deleteUser(99L);

            mockMvc.perform(delete("/api/users/99"))
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.error", is("No user found with ID: 99")));
        }
    }
}
