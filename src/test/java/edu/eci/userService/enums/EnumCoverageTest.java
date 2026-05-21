package edu.eci.userService.enums;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;


@DisplayName("Enum Coverage Tests")
class EnumCoverageTest {


    @Test
    @DisplayName("UserRoleEnum: debe contener los valores esperados")
    void userRoleEnumValues() {
        assertThat(UserRoleEnum.values()).containsExactlyInAnyOrder(
            UserRoleEnum.STUDENT,
            UserRoleEnum.TEACHER,
            UserRoleEnum.STAFF,
            UserRoleEnum.GRADUATE,
            UserRoleEnum.OTHER
        );
    }

    @Test
    @DisplayName("UserRoleEnum: valueOf debe funcionar correctamente")
    void userRoleEnumValueOf() {
        assertThat(UserRoleEnum.valueOf("STUDENT")).isEqualTo(UserRoleEnum.STUDENT);
        assertThat(UserRoleEnum.valueOf("TEACHER")).isEqualTo(UserRoleEnum.TEACHER);
        assertThat(UserRoleEnum.valueOf("STAFF")).isEqualTo(UserRoleEnum.STAFF);
        assertThat(UserRoleEnum.valueOf("GRADUATE")).isEqualTo(UserRoleEnum.GRADUATE);
        assertThat(UserRoleEnum.valueOf("OTHER")).isEqualTo(UserRoleEnum.OTHER);
    }


    @Test
    @DisplayName("JoinRequestStatus: debe contener exactamente tres valores")
    void joinRequestStatusValues() {
        assertThat(JoinRequestStatus.values()).containsExactlyInAnyOrder(
            JoinRequestStatus.PENDING,
            JoinRequestStatus.ACCEPTED,
            JoinRequestStatus.REJECTED
        );
    }

    @Test
    @DisplayName("JoinRequestStatus: valueOf debe funcionar correctamente")
    void joinRequestStatusValueOf() {
        assertThat(JoinRequestStatus.valueOf("PENDING")).isEqualTo(JoinRequestStatus.PENDING);
        assertThat(JoinRequestStatus.valueOf("ACCEPTED")).isEqualTo(JoinRequestStatus.ACCEPTED);
        assertThat(JoinRequestStatus.valueOf("REJECTED")).isEqualTo(JoinRequestStatus.REJECTED);
    }


    @Test
    @DisplayName("UserStatus: debe contener ACTIVE e INACTIVE")
    void userStatusValues() {
        assertThat(UserStatus.values()).containsExactlyInAnyOrder(
            UserStatus.ACTIVE,
            UserStatus.INACTIVE
        );
    }

    @Test
    @DisplayName("UserStatus: toString debe retornar el nombre")
    void userStatusToString() {
        assertThat(UserStatus.ACTIVE.toString()).isEqualTo("ACTIVE");
        assertThat(UserStatus.INACTIVE.toString()).isEqualTo("INACTIVE");
    }

    @Test
    @DisplayName("UserStatus: valueOf debe funcionar correctamente")
    void userStatusValueOf() {
        assertThat(UserStatus.valueOf("ACTIVE")).isEqualTo(UserStatus.ACTIVE);
        assertThat(UserStatus.valueOf("INACTIVE")).isEqualTo(UserStatus.INACTIVE);
    }


    @Test
    @DisplayName("IdentificationType: debe contener los valores esperados")
    void identificationTypeValues() {
        assertThat(IdentificationType.values()).containsExactlyInAnyOrder(
            IdentificationType.CC,
            IdentificationType.TI,
            IdentificationType.PP,
            IdentificationType.CE,
            IdentificationType.OTRO
        );
    }

    @Test
    @DisplayName("IdentificationType: toString debe retornar el nombre")
    void identificationTypeToString() {
        assertThat(IdentificationType.CC.toString()).isEqualTo("CC");
        assertThat(IdentificationType.TI.toString()).isEqualTo("TI");
        assertThat(IdentificationType.PP.toString()).isEqualTo("PP");
        assertThat(IdentificationType.CE.toString()).isEqualTo("CE");
        assertThat(IdentificationType.OTRO.toString()).isEqualTo("OTRO");
    }

    @Test
    @DisplayName("IdentificationType: valueOf debe funcionar correctamente")
    void identificationTypeValueOf() {
        assertThat(IdentificationType.valueOf("CC")).isEqualTo(IdentificationType.CC);
        assertThat(IdentificationType.valueOf("TI")).isEqualTo(IdentificationType.TI);
        assertThat(IdentificationType.valueOf("PP")).isEqualTo(IdentificationType.PP);
        assertThat(IdentificationType.valueOf("CE")).isEqualTo(IdentificationType.CE);
        assertThat(IdentificationType.valueOf("OTRO")).isEqualTo(IdentificationType.OTRO);
    }


    @Test
    @DisplayName("UserRole: debe contener los valores esperados")
    void userRoleValues() {
        assertThat(UserRole.values()).containsExactlyInAnyOrder(
            UserRole.STUDENT,
            UserRole.GRADUATE,
            UserRole.PROFESSOR,
            UserRole.ADMINISTRATIVE_STAFF,
            UserRole.FAMILY_MEMBER,
            UserRole.ADMINISTRATOR,
            UserRole.ORGANIZER
        );
    }

    @Test
    @DisplayName("UserRole: toString debe retornar el nombre")
    void userRoleToString() {
        assertThat(UserRole.STUDENT.toString()).isEqualTo("STUDENT");
        assertThat(UserRole.GRADUATE.toString()).isEqualTo("GRADUATE");
        assertThat(UserRole.PROFESSOR.toString()).isEqualTo("PROFESSOR");
        assertThat(UserRole.ADMINISTRATIVE_STAFF.toString()).isEqualTo("ADMINISTRATIVE_STAFF");
        assertThat(UserRole.FAMILY_MEMBER.toString()).isEqualTo("FAMILY_MEMBER");
        assertThat(UserRole.ADMINISTRATOR.toString()).isEqualTo("ADMINISTRATOR");
        assertThat(UserRole.ORGANIZER.toString()).isEqualTo("ORGANIZER");
    }

    @Test
    @DisplayName("UserRole: valueOf debe funcionar correctamente")
    void userRoleValueOf() {
        assertThat(UserRole.valueOf("STUDENT")).isEqualTo(UserRole.STUDENT);
        assertThat(UserRole.valueOf("ORGANIZER")).isEqualTo(UserRole.ORGANIZER);
    }


    @Test
    @DisplayName("ProfileStatus: debe contener ACTIVE e INACTIVE")
    void profileStatusValues() {
        assertThat(ProfileStatus.values()).containsExactlyInAnyOrder(
            ProfileStatus.ACTIVE,
            ProfileStatus.INACTIVE
        );
    }

    @Test
    @DisplayName("ProfileStatus: toString debe retornar el nombre")
    void profileStatusToString() {
        assertThat(ProfileStatus.ACTIVE.toString()).isEqualTo("ACTIVE");
        assertThat(ProfileStatus.INACTIVE.toString()).isEqualTo("INACTIVE");
    }

    @Test
    @DisplayName("ProfileStatus: valueOf debe funcionar correctamente")
    void profileStatusValueOf() {
        assertThat(ProfileStatus.valueOf("ACTIVE")).isEqualTo(ProfileStatus.ACTIVE);
        assertThat(ProfileStatus.valueOf("INACTIVE")).isEqualTo(ProfileStatus.INACTIVE);
    }


    @Test
    @DisplayName("SchoolRelation: debe contener los valores esperados")
    void schoolRelationValues() {
        assertThat(SchoolRelation.values()).containsExactlyInAnyOrder(
            SchoolRelation.STUDENT,
            SchoolRelation.PROFESSOR,
            SchoolRelation.GRADUATE,
            SchoolRelation.GUEST,
            SchoolRelation.ADMINISTRATIVE_STAFF,
            SchoolRelation.FAMILY_MEMBER
        );
    }

    @Test
    @DisplayName("SchoolRelation: toString debe retornar el nombre")
    void schoolRelationToString() {
        assertThat(SchoolRelation.STUDENT.toString()).isEqualTo("STUDENT");
        assertThat(SchoolRelation.GUEST.toString()).isEqualTo("GUEST");
        assertThat(SchoolRelation.PROFESSOR.toString()).isEqualTo("PROFESSOR");
    }

    @Test
    @DisplayName("SchoolRelation: valueOf debe funcionar correctamente")
    void schoolRelationValueOf() {
        assertThat(SchoolRelation.valueOf("STUDENT")).isEqualTo(SchoolRelation.STUDENT);
        assertThat(SchoolRelation.valueOf("GUEST")).isEqualTo(SchoolRelation.GUEST);
    }


    @Test
    @DisplayName("AcademicLevel: debe contener los valores esperados")
    void academicLevelValues() {
        assertThat(AcademicLevel.values()).containsExactlyInAnyOrder(
            AcademicLevel.UNDERGRADUATE,
            AcademicLevel.SPECIALIZATION,
            AcademicLevel.MASTER,
            AcademicLevel.DOCTORATE
        );
    }

    @Test
    @DisplayName("AcademicLevel: toString debe retornar el nombre")
    void academicLevelToString() {
        assertThat(AcademicLevel.UNDERGRADUATE.toString()).isEqualTo("UNDERGRADUATE");
        assertThat(AcademicLevel.SPECIALIZATION.toString()).isEqualTo("SPECIALIZATION");
        assertThat(AcademicLevel.MASTER.toString()).isEqualTo("MASTER");
        assertThat(AcademicLevel.DOCTORATE.toString()).isEqualTo("DOCTORATE");
    }

    @Test
    @DisplayName("AcademicLevel: valueOf debe funcionar correctamente")
    void academicLevelValueOf() {
        assertThat(AcademicLevel.valueOf("MASTER")).isEqualTo(AcademicLevel.MASTER);
        assertThat(AcademicLevel.valueOf("DOCTORATE")).isEqualTo(AcademicLevel.DOCTORATE);
    }


    @Test
    @DisplayName("LateralityType: debe contener los valores esperados")
    void lateralityTypeValues() {
        assertThat(LateralityType.values()).containsExactlyInAnyOrder(
            LateralityType.LEFT,
            LateralityType.RIGHT,
            LateralityType.AMBIDEXTROUS
        );
    }

    @Test
    @DisplayName("LateralityType: toString debe retornar el nombre")
    void lateralityTypeToString() {
        assertThat(LateralityType.LEFT.toString()).isEqualTo("LEFT");
        assertThat(LateralityType.RIGHT.toString()).isEqualTo("RIGHT");
        assertThat(LateralityType.AMBIDEXTROUS.toString()).isEqualTo("AMBIDEXTROUS");
    }

    @Test
    @DisplayName("LateralityType: valueOf debe funcionar correctamente")
    void lateralityTypeValueOf() {
        assertThat(LateralityType.valueOf("LEFT")).isEqualTo(LateralityType.LEFT);
        assertThat(LateralityType.valueOf("RIGHT")).isEqualTo(LateralityType.RIGHT);
        assertThat(LateralityType.valueOf("AMBIDEXTROUS")).isEqualTo(LateralityType.AMBIDEXTROUS);
    }
}
