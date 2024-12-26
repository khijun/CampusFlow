package edu.du.campusflow.service;

import edu.du.campusflow.entity.CommonCode;
import edu.du.campusflow.entity.Dept;
import edu.du.campusflow.entity.Professor;
import edu.du.campusflow.repository.CommonCodeRepository;
import edu.du.campusflow.repository.DeptRepository;
import edu.du.campusflow.repository.FileInfoRepository;
import edu.du.campusflow.repository.ProfessorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProfessorService {

    @Autowired
    private PasswordEncoder passwordEncoder;
    
    private final ProfessorRepository professorRepository;
    private final CommonCodeRepository commonCodeRepository;
    private final FileInfoRepository fileInfoRepository;
    private final DeptRepository deptRepository;

    @Transactional
    public void createDummyProfessors() {
        // 기존에 저장된 성별 코드 가져오기
        CommonCode maleGender = commonCodeRepository.findByCodeValue("MALE");
        CommonCode femaleGender = commonCodeRepository.findByCodeValue("FEMALE");
        Dept computer = deptRepository.findById(1L).orElse(null);
        Dept mechanical = deptRepository.findById(2L).orElse(null);
        Dept electrical = deptRepository.findById(3L).orElse(null);
        Dept civil = deptRepository.findById(4L).orElse(null);
        Dept chemical = deptRepository.findById(5L).orElse(null);
        Dept business = deptRepository.findById(6L).orElse(null);
        Dept economics = deptRepository.findById(7L).orElse(null);
        Dept psychology = deptRepository.findById(8L).orElse(null);
        Dept english = deptRepository.findById(9L).orElse(null);


        List<Professor> dummyProfessors = List.of(
            Professor.builder()
                .name("김교수")
                .password(passwordEncoder.encode("prof1234"))
                .email("prof1@du.ac.kr")
                .tel("01012341001")
                .address("서울시 서대문구")
                .birthDate(LocalDate.of(1970, 3, 15))
                .dept(deptRepository.findById(1L).orElse(null))
                .hireDate(LocalDate.of(2010, 3, 1))
                .isActive(true)
                .gender(maleGender)
                .createAt(LocalDateTime.now())
                .updateAt(LocalDateTime.now())
                .file(fileInfoRepository.findById(11L).orElse(null))
                .build(),

            Professor.builder()
                .name("이교수")
                .password(passwordEncoder.encode("prof1234"))
                .email("prof2@du.ac.kr")
                .tel("01012341002")
                .address("서울시 마포구")
                .birthDate(LocalDate.of(1975, 5, 20))
                .dept(deptRepository.findById(1L).orElse(null))
                .hireDate(LocalDate.of(2012, 3, 1))
                .isActive(true)
                .gender(femaleGender)
                .createAt(LocalDateTime.now())
                .updateAt(LocalDateTime.now())
                .file(fileInfoRepository.findById(12L).orElse(null))
                .build(),

            Professor.builder()
                .name("박교수")
                .password(passwordEncoder.encode("prof1234"))
                .email("prof3@du.ac.kr")
                .tel("01012341003")
                .address("서울시 강남구")
                .birthDate(LocalDate.of(1968, 8, 10))
                .dept(deptRepository.findById(2L).orElse(null))
                .hireDate(LocalDate.of(2008, 9, 1))
                .isActive(true)
                .gender(maleGender)
                .createAt(LocalDateTime.now())
                .updateAt(LocalDateTime.now())
                .file(fileInfoRepository.findById(13L).orElse(null))
                .build(),

            Professor.builder()
                .name("정교수")
                .password(passwordEncoder.encode("prof1234"))
                .email("prof4@du.ac.kr")
                .tel("01012341004")
                .address("서울시 송파구")
                .birthDate(LocalDate.of(1972, 11, 25))
                .dept(deptRepository.findById(2L).orElse(null))
                .hireDate(LocalDate.of(2011, 3, 1))
                .isActive(true)
                .gender(femaleGender)
                .createAt(LocalDateTime.now())
                .updateAt(LocalDateTime.now())
                .file(fileInfoRepository.findById(14L).orElse(null))
                .build(),

            Professor.builder()
                .name("최교수")
                .password(passwordEncoder.encode("prof1234"))
                .email("prof5@du.ac.kr")
                .tel("01012341005")
                .address("서울시 용산구")
                .birthDate(LocalDate.of(1965, 4, 5))
                .dept(deptRepository.findById(3L).orElse(null))
                .hireDate(LocalDate.of(2005, 3, 1))
                .isActive(true)
                .gender(maleGender)
                .createAt(LocalDateTime.now())
                .updateAt(LocalDateTime.now())
                .file(fileInfoRepository.findById(15L).orElse(null))
                .build(),

            Professor.builder()
                .name("강교수")
                .password(passwordEncoder.encode("prof1234"))
                .email("prof6@du.ac.kr")
                .tel("01012341006")
                .address("서울시 강서구")
                .birthDate(LocalDate.of(1973, 7, 15))
                .dept(deptRepository.findById(3L).orElse(null))
                .hireDate(LocalDate.of(2013, 9, 1))
                .isActive(true)
                .gender(femaleGender)
                .createAt(LocalDateTime.now())
                .updateAt(LocalDateTime.now())
                .file(fileInfoRepository.findById(16L).orElse(null))
                .build(),

            Professor.builder()
                .name("윤교수")
                .password(passwordEncoder.encode("prof1234"))
                .email("prof7@du.ac.kr")
                .tel("01012341007")
                .address("서울시 영등포구")
                .birthDate(LocalDate.of(1969, 9, 20))
                .dept(deptRepository.findById(4L).orElse(null))
                .hireDate(LocalDate.of(2009, 3, 1))
                .isActive(true)
                .gender(maleGender)
                .createAt(LocalDateTime.now())
                .updateAt(LocalDateTime.now())
                .file(fileInfoRepository.findById(17L).orElse(null))
                .build(),

            Professor.builder()
                .name("한교수")
                .password(passwordEncoder.encode("prof1234"))
                .email("prof8@du.ac.kr")
                .tel("01012341008")
                .address("서울시 동작구")
                .birthDate(LocalDate.of(1971, 12, 3))
                .dept(deptRepository.findById(4L).orElse(null))
                .hireDate(LocalDate.of(2010, 9, 1))
                .isActive(true)
                .gender(femaleGender)
                .createAt(LocalDateTime.now())
                .updateAt(LocalDateTime.now())
                .file(fileInfoRepository.findById(18L).orElse(null))
                .build(),

            Professor.builder()
                .name("임교수")
                .password(passwordEncoder.encode("prof1234"))
                .email("prof9@du.ac.kr")
                .tel("01012341009")
                .address("서울시 관악구")
                .birthDate(LocalDate.of(1967, 2, 28))
                .dept(deptRepository.findById(5L).orElse(null))
                .hireDate(LocalDate.of(2007, 3, 1))
                .isActive(true)
                .gender(maleGender)
                .createAt(LocalDateTime.now())
                .updateAt(LocalDateTime.now())
                .file(fileInfoRepository.findById(19L).orElse(null))
                .build(),

            Professor.builder()
                .name("오교수")
                .password(passwordEncoder.encode("prof1234"))
                .email("prof10@du.ac.kr")
                .tel("01012341010")
                .address("서울시 성북구")
                .birthDate(LocalDate.of(1974, 6, 15))
                .dept(deptRepository.findById(5L).orElse(null))
                .hireDate(LocalDate.of(2014, 3, 1))
                .isActive(true)
                .gender(femaleGender)
                .createAt(LocalDateTime.now())
                .updateAt(LocalDateTime.now())
                .file(fileInfoRepository.findById(20L).orElse(null))
                .build()
        );

        professorRepository.saveAll(dummyProfessors);
    }
} 