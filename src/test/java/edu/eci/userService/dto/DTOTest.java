package edu.eci.userService.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import edu.eci.userService.enums.AcademicLevel;
import edu.eci.userService.enums.JoinRequestStatus;
import edu.eci.userService.enums.ProfessorType;
import edu.eci.userService.enums.SchoolRelation;

class DTOTest {

    @Test
    @DisplayName("UserDTO: Getters and Setters")
    void userDTOTest() {
        UserDTO dto = new UserDTO();
        LocalDate birthDate = LocalDate.of(2000, 1, 1);

        dto.setId(1L);
        dto.setName("Name");
        dto.setEmail("email@test.com");
        dto.setBirthDate(birthDate);
        dto.setSchoolRelation(SchoolRelation.STUDENT);
        dto.setAcademicLevel(AcademicLevel.UNDERGRADUATE);
        dto.setProfessorType(ProfessorType.FULL_TIME);
        dto.setAcademicProgram("Program");
        dto.setSemester(1);
        dto.setIdentificationType("CC");
        dto.setIdentificationNumber(123L);
        dto.setPhone(456L);

        assertThat(dto.getId()).isEqualTo(1L);
        assertThat(dto.getName()).isEqualTo("Name");
        assertThat(dto.getEmail()).isEqualTo("email@test.com");
        assertThat(dto.getBirthDate()).isEqualTo(birthDate);
        assertThat(dto.getSchoolRelation()).isEqualTo(SchoolRelation.STUDENT);
        assertThat(dto.getAcademicLevel()).isEqualTo(AcademicLevel.UNDERGRADUATE);
        assertThat(dto.getProfessorType()).isEqualTo(ProfessorType.FULL_TIME);
        assertThat(dto.getAcademicProgram()).isEqualTo("Program");
        assertThat(dto.getSemester()).isEqualTo(1);
        assertThat(dto.getIdentificationType()).isEqualTo("CC");
        assertThat(dto.getIdentificationNumber()).isEqualTo(123L);
        assertThat(dto.getPhone()).isEqualTo(456L);
    }

    @Test
    @DisplayName("AthleticProfileDTO: Getters and Setters con UserDTO")
    void athleticProfileDTOTest() {
        AthleticProfileDTO dto = new AthleticProfileDTO();
        // ← UserDTO en lugar de UserEntity
        UserDTO userDTO = new UserDTO();
        userDTO.setId(1L);
        userDTO.setName("Test User");

        dto.setId(1L);
        dto.setDorsalNumber(10);
        dto.setNickName("Nick");
        dto.setPosition("Pos");
        dto.setLaterality("Lat");
        dto.setStature("180");
        dto.setState("Active");
        dto.setUser(userDTO); // ← UserDTO

        assertThat(dto.getId()).isEqualTo(1L);
        assertThat(dto.getDorsalNumber()).isEqualTo(10);
        assertThat(dto.getNickName()).isEqualTo("Nick");
        assertThat(dto.getPosition()).isEqualTo("Pos");
        assertThat(dto.getLaterality()).isEqualTo("Lat");
        assertThat(dto.getStature()).isEqualTo("180");
        assertThat(dto.getState()).isEqualTo("Active");
        assertThat(dto.getUser()).isEqualTo(userDTO);
        assertThat(dto.getUser().getId()).isEqualTo(1L);
    }

    @Test
    @DisplayName("UserDTO: Constructor completo")
    void userDTOFullConstructorTest() {
        LocalDate birthDate = LocalDate.of(1999, 12, 31);

        UserDTO dto = new UserDTO(
                2L,
                "Ana",
                "ana@test.com",
                birthDate,
                SchoolRelation.PROFESSOR,
                AcademicLevel.MASTER,
                ProfessorType.CHAIR,
                "Matematicas",
                3,
                "TI",
                987L,
                654L);
        dto.setPassword("secret");

        assertThat(dto.getId()).isEqualTo(2L);
        assertThat(dto.getName()).isEqualTo("Ana");
        assertThat(dto.getEmail()).isEqualTo("ana@test.com");
        assertThat(dto.getBirthDate()).isEqualTo(birthDate);
        assertThat(dto.getSchoolRelation()).isEqualTo(SchoolRelation.PROFESSOR);
        assertThat(dto.getAcademicLevel()).isEqualTo(AcademicLevel.MASTER);
        assertThat(dto.getProfessorType()).isEqualTo(ProfessorType.CHAIR);
        assertThat(dto.getAcademicProgram()).isEqualTo("Matematicas");
        assertThat(dto.getSemester()).isEqualTo(3);
        assertThat(dto.getIdentificationType()).isEqualTo("TI");
        assertThat(dto.getIdentificationNumber()).isEqualTo(987L);
        assertThat(dto.getPhone()).isEqualTo(654L);
        assertThat(dto.getPassword()).isEqualTo("secret");
    }

    @Test
    @DisplayName("AthleticProfileDTO: Constructor completo con UserDTO")
    void athleticProfileDTOFullConstructorTest() {
        // ← Constructor ahora recibe UserDTO, no UserEntity
        UserDTO userDTO = new UserDTO();
        userDTO.setId(7L);
        userDTO.setName("Test");

        AthleticProfileDTO dto = new AthleticProfileDTO(
                9,
                3L,
                userDTO, // ← UserDTO
                "Nick",
                "defensa",
                "zurdo",
                "170",
                "activo");

        assertThat(dto.getDorsalNumber()).isEqualTo(9);
        assertThat(dto.getId()).isEqualTo(3L);
        assertThat(dto.getUser()).isEqualTo(userDTO);
        assertThat(dto.getUser().getId()).isEqualTo(7L);
        assertThat(dto.getNickName()).isEqualTo("Nick");
        assertThat(dto.getPosition()).isEqualTo("defensa");
        assertThat(dto.getLaterality()).isEqualTo("zurdo");
        assertThat(dto.getStature()).isEqualTo("170");
        assertThat(dto.getState()).isEqualTo("activo");
    }

    @Test
    @DisplayName("LoginRequest: Constructor completo")
    void loginRequestFullConstructorTest() {
        LoginRequest request = new LoginRequest("mail@test.com", "pwd");

        assertThat(request.getEmail()).isEqualTo("mail@test.com");
        assertThat(request.getPassword()).isEqualTo("pwd");
    }

    @Test
    @DisplayName("JoinRequestDTO: Constructor completo y getters/setters")
    void joinRequestDTOFullCoverageTest() {
        LocalDateTime now = LocalDateTime.of(2026, 5, 21, 10, 0);
        UserDTO player = new UserDTO();
        player.setId(3L);
        player.setName("Player");

        JoinRequestDTO dto = new JoinRequestDTO(10L, player, 5L, JoinRequestStatus.PENDING, now, now);

        assertThat(dto.getId()).isEqualTo(10L);
        assertThat(dto.getPlayer()).isEqualTo(player);
        assertThat(dto.getTeamId()).isEqualTo(5L);
        assertThat(dto.getStatus()).isEqualTo(JoinRequestStatus.PENDING);
        assertThat(dto.getCreatedAt()).isEqualTo(now);
        assertThat(dto.getUpdatedAt()).isEqualTo(now);

        dto.setStatus(JoinRequestStatus.ACCEPTED);
        assertThat(dto.getStatus()).isEqualTo(JoinRequestStatus.ACCEPTED);
    }

    @Test
    @DisplayName("SendJoinRequestRequest: Constructor y accessors")
    void sendJoinRequestRequestCoverageTest() {
        SendJoinRequestRequest byConstructor = new SendJoinRequestRequest(99L);
        assertThat(byConstructor.getTeamId()).isEqualTo(99L);

        SendJoinRequestRequest bySetter = new SendJoinRequestRequest();
        bySetter.setTeamId(7L);
        assertThat(bySetter.getTeamId()).isEqualTo(7L);
    }

    @Test
    @DisplayName("RoleChangeRequest: Constructor y accessors")
    void roleChangeRequestCoverageTest() {
        RoleChangeRequest byConstructor = new RoleChangeRequest("ORGANIZER");
        assertThat(byConstructor.getRole()).isEqualTo("ORGANIZER");

        RoleChangeRequest bySetter = new RoleChangeRequest();
        bySetter.setRole("PLAYER");
        assertThat(bySetter.getRole()).isEqualTo("PLAYER");
    }
}