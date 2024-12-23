package edu.du.academic_management_system.service;

import edu.du.academic_management_system.entity.CommonCode;
import edu.du.academic_management_system.entity.Professor;
import edu.du.academic_management_system.repository.CommonCodeRepository;
import edu.du.academic_management_system.repository.ProfessorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProfessorService {
    
    private final ProfessorRepository professorRepository;
    private final CommonCodeRepository commonCodeRepository;

    @Transactional
    public void createDummyProfessors() {
        // 기존에 저장된 성별 코드 가져오기
        CommonCode maleGender = commonCodeRepository.findByCodeValue("MALE");
        CommonCode femaleGender = commonCodeRepository.findByCodeValue("FEMALE");

        List<Professor> dummyProfessors = List.of(
            Professor.builder()
                .name("김교수")
                .password("prof1234")
                .email("prof1@du.ac.kr")
                .tel("01012341001")
                .address("서울시 서대문구")
                .birthDate(LocalDate.of(1970, 3, 15))
                .deptId(1L)
                .hireDate(LocalDate.of(2010, 3, 1))
                .isActive(true)
                .gender(maleGender)
                .createAt(LocalDateTime.now())
                .updateAt(LocalDateTime.now())
                .imageFileId(11L)
                .build(),

            Professor.builder()
                .name("이교수")
                .password("prof1234")
                .email("prof2@du.ac.kr")
                .tel("01012341002")
                .address("서울시 마포구")
                .birthDate(LocalDate.of(1975, 5, 20))
                .deptId(1L)
                .hireDate(LocalDate.of(2012, 3, 1))
                .isActive(true)
                .gender(femaleGender)
                .createAt(LocalDateTime.now())
                .updateAt(LocalDateTime.now())
                .imageFileId(12L)
                .build(),

            Professor.builder()
                .name("박교수")
                .password("prof1234")
                .email("prof3@du.ac.kr")
                .tel("01012341003")
                .address("서울시 강남구")
                .birthDate(LocalDate.of(1968, 8, 10))
                .deptId(2L)
                .hireDate(LocalDate.of(2008, 9, 1))
                .isActive(true)
                .gender(maleGender)
                .createAt(LocalDateTime.now())
                .updateAt(LocalDateTime.now())
                .imageFileId(13L)
                .build(),

            Professor.builder()
                .name("정교수")
                .password("prof1234")
                .email("prof4@du.ac.kr")
                .tel("01012341004")
                .address("서울시 송파구")
                .birthDate(LocalDate.of(1972, 11, 25))
                .deptId(2L)
                .hireDate(LocalDate.of(2011, 3, 1))
                .isActive(true)
                .gender(femaleGender)
                .createAt(LocalDateTime.now())
                .updateAt(LocalDateTime.now())
                .imageFileId(14L)
                .build(),

            Professor.builder()
                .name("최교수")
                .password("prof1234")
                .email("prof5@du.ac.kr")
                .tel("01012341005")
                .address("서울시 용산구")
                .birthDate(LocalDate.of(1965, 4, 5))
                .deptId(3L)
                .hireDate(LocalDate.of(2005, 3, 1))
                .isActive(true)
                .gender(maleGender)
                .createAt(LocalDateTime.now())
                .updateAt(LocalDateTime.now())
                .imageFileId(15L)
                .build(),

            Professor.builder()
                .name("강교수")
                .password("prof1234")
                .email("prof6@du.ac.kr")
                .tel("01012341006")
                .address("서울시 강서구")
                .birthDate(LocalDate.of(1973, 7, 15))
                .deptId(3L)
                .hireDate(LocalDate.of(2013, 9, 1))
                .isActive(true)
                .gender(femaleGender)
                .createAt(LocalDateTime.now())
                .updateAt(LocalDateTime.now())
                .imageFileId(16L)
                .build(),

            Professor.builder()
                .name("윤교수")
                .password("prof1234")
                .email("prof7@du.ac.kr")
                .tel("01012341007")
                .address("서울시 영등포구")
                .birthDate(LocalDate.of(1969, 9, 20))
                .deptId(4L)
                .hireDate(LocalDate.of(2009, 3, 1))
                .isActive(true)
                .gender(maleGender)
                .createAt(LocalDateTime.now())
                .updateAt(LocalDateTime.now())
                .imageFileId(17L)
                .build(),

            Professor.builder()
                .name("한교수")
                .password("prof1234")
                .email("prof8@du.ac.kr")
                .tel("01012341008")
                .address("서울시 동작구")
                .birthDate(LocalDate.of(1971, 12, 3))
                .deptId(4L)
                .hireDate(LocalDate.of(2010, 9, 1))
                .isActive(true)
                .gender(femaleGender)
                .createAt(LocalDateTime.now())
                .updateAt(LocalDateTime.now())
                .imageFileId(18L)
                .build(),

            Professor.builder()
                .name("임교수")
                .password("prof1234")
                .email("prof9@du.ac.kr")
                .tel("01012341009")
                .address("서울시 관악구")
                .birthDate(LocalDate.of(1967, 2, 28))
                .deptId(5L)
                .hireDate(LocalDate.of(2007, 3, 1))
                .isActive(true)
                .gender(maleGender)
                .createAt(LocalDateTime.now())
                .updateAt(LocalDateTime.now())
                .imageFileId(19L)
                .build(),

            Professor.builder()
                .name("오교수")
                .password("prof1234")
                .email("prof10@du.ac.kr")
                .tel("01012341010")
                .address("서울시 성북구")
                .birthDate(LocalDate.of(1974, 6, 15))
                .deptId(5L)
                .hireDate(LocalDate.of(2014, 3, 1))
                .isActive(true)
                .gender(femaleGender)
                .createAt(LocalDateTime.now())
                .updateAt(LocalDateTime.now())
                .imageFileId(20L)
                .build()
        );

        professorRepository.saveAll(dummyProfessors);
    }
} 