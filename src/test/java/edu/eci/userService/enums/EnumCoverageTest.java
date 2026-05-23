package edu.eci.userService.enums;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;


@DisplayName("Enum Coverage Tests")
class EnumCoverageTest {


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
            SchoolRelation.TEACHER,
            SchoolRelation.GRADUATE,
            SchoolRelation.STAFF,
            SchoolRelation.FAMILY
        );
    }

    @Test
    @DisplayName("SchoolRelation: toString debe retornar el nombre")
    void schoolRelationToString() {
        assertThat(SchoolRelation.STUDENT.toString()).isEqualTo("STUDENT");
        assertThat(SchoolRelation.PROFESSOR.toString()).isEqualTo("PROFESSOR");
        assertThat(SchoolRelation.TEACHER.toString()).isEqualTo("TEACHER");
        assertThat(SchoolRelation.GRADUATE.toString()).isEqualTo("GRADUATE");
        assertThat(SchoolRelation.STAFF.toString()).isEqualTo("STAFF");
        assertThat(SchoolRelation.FAMILY.toString()).isEqualTo("FAMILY");
    }

    @Test
    @DisplayName("SchoolRelation: valueOf debe funcionar correctamente")
    void schoolRelationValueOf() {
        assertThat(SchoolRelation.valueOf("STUDENT")).isEqualTo(SchoolRelation.STUDENT);
        assertThat(SchoolRelation.valueOf("PROFESSOR")).isEqualTo(SchoolRelation.PROFESSOR);
        assertThat(SchoolRelation.valueOf("TEACHER")).isEqualTo(SchoolRelation.TEACHER);
        assertThat(SchoolRelation.valueOf("GRADUATE")).isEqualTo(SchoolRelation.GRADUATE);
        assertThat(SchoolRelation.valueOf("STAFF")).isEqualTo(SchoolRelation.STAFF);
        assertThat(SchoolRelation.valueOf("FAMILY")).isEqualTo(SchoolRelation.FAMILY);
    }


    @Test
    @DisplayName("AcademicLevel: debe contener los valores esperados")
    void academicLevelValues() {
        assertThat(AcademicLevel.values()).containsExactlyInAnyOrder(
            AcademicLevel.UNDERGRADUATE,
            AcademicLevel.POSTGRADUATE,
            AcademicLevel.MASTER
        );
    }

    @Test
    @DisplayName("AcademicLevel: toString debe retornar el nombre")
    void academicLevelToString() {
        assertThat(AcademicLevel.UNDERGRADUATE.toString()).isEqualTo("UNDERGRADUATE");
        assertThat(AcademicLevel.POSTGRADUATE.toString()).isEqualTo("POSTGRADUATE");
        assertThat(AcademicLevel.MASTER.toString()).isEqualTo("MASTER");
    }

    @Test
    @DisplayName("AcademicLevel: valueOf debe funcionar correctamente")
    void academicLevelValueOf() {
        assertThat(AcademicLevel.valueOf("MASTER")).isEqualTo(AcademicLevel.MASTER);
        assertThat(AcademicLevel.valueOf("POSTGRADUATE")).isEqualTo(AcademicLevel.POSTGRADUATE);
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
