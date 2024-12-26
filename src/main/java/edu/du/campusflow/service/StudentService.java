package edu.du.campusflow.service;

import edu.du.campusflow.entity.CommonCode;
import edu.du.campusflow.entity.CommonCodeGroup;
import edu.du.campusflow.entity.Student;
import edu.du.campusflow.repository.CommonCodeGroupRepository;
import edu.du.campusflow.repository.CommonCodeRepository;
import edu.du.campusflow.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class StudentService {

    @Autowired
    private PasswordEncoder passwordEncoder;
    private final StudentRepository studentRepository;
    private final CommonCodeRepository commonCodeRepository;
    private final CommonCodeGroupRepository commonCodeGroupRepository;

    // 전체 학생 목록 조회
    public List<Student> getAllStudents() {
        return studentRepository.findAll();
    }

    // 페이징된 학생 목록 조회
    public Page<Student> getStudentList(Pageable pageable) {
        return studentRepository.findAll(pageable);
    }

    // ID로 학생 조회
    public Student getStudentById(Long id) {
        return studentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 학생이 존재하지 않습니다. ID: " + id));
    }

    // 학생 등록
    @Transactional
    public Student createStudent(Student student) {
        return studentRepository.save(student);
    }

    // 학생 정보 수정
    @Transactional
    public Student updateStudent(Long id, Student studentDetails) {
        Student student = getStudentById(id);

        // 필요한 필드 업데이트
        if (studentDetails.getDept() != null) {
            student.setDept(studentDetails.getDept());
        }
        if (studentDetails.getGrade() != null) {
            student.setGrade(studentDetails.getGrade());
        }
        if (studentDetails.getAdmissionDate() != null) {
            student.setAdmissionDate(studentDetails.getAdmissionDate());
        }
        if (studentDetails.getGraduationDate() != null) {
            student.setGraduationDate(studentDetails.getGraduationDate());
        }
        if (studentDetails.getAcademicStatus() != null) {
            student.setAcademicStatus(studentDetails.getAcademicStatus());
        }

        return studentRepository.save(student);
    }

    // 학생 삭제
    @Transactional
    public void deleteStudent(Long id) {
        Student student = getStudentById(id);
        studentRepository.delete(student);
    }

    // 더미 데이터 생성
    @Transactional
    public void createDummyStudents() {
        // 기존에 저장된 성별 코드와 학적 상태 코드 가져오기
        CommonCodeGroup gender = commonCodeGroupRepository.findByGroupCode("GENDER");
        CommonCodeGroup academicStatus = commonCodeGroupRepository.findByGroupCode("ACADEMIC_STATUS");
        CommonCode maleGender = commonCodeRepository.findByCodeValue("MALE");
        CommonCode femaleGender = commonCodeRepository.findByCodeValue("FEMALE");
        CommonCode activeStatus = commonCodeRepository.findByCodeValue("ACTIVE");
        CommonCode leaveStatus = commonCodeRepository.findByCodeValue("LEAVE");

        List<Student> dummyStudents = List.of(
                Student.builder()
                        .name("김철수")
                        .password(passwordEncoder.encode("std1234"))
                        .email("student1@du.ac.kr")
                        .tel("01011111111")
                        .address("서울시 강남구")
                        .birthDate(LocalDate.of(2000, 1, 15))
                        .deptId(1L)
                        .grade(1)
                        .admissionDate(LocalDate.of(2023, 3, 2))
                        .isActive(true)
                        .gender(maleGender)
                        .academicStatusCode(activeStatus)
                        .createAt(LocalDateTime.now())
                        .updateAt(LocalDateTime.now())
                        .imageFileId(1L)
                        .build(),

                Student.builder()
                        .name("이영희")
                        .password(passwordEncoder.encode("std1234"))
                        .email("student2@du.ac.kr")
                        .tel("01022222222")
                        .address("서울시 서초구")
                        .birthDate(LocalDate.of(1999, 5, 20))
                        .deptId(1L)
                        .grade(2)
                        .admissionDate(LocalDate.of(2022, 3, 2))
                        .isActive(true)
                        .gender(femaleGender)
                        .academicStatusCode(activeStatus)
                        .createAt(LocalDateTime.now())
                        .updateAt(LocalDateTime.now())
                        .imageFileId(2L)
                        .build(),

                Student.builder()
                        .name("박민수")
                        .password(passwordEncoder.encode("std1234"))
                        .email("student3@du.ac.kr")
                        .tel("01033333333")
                        .address("서울시 송파구")
                        .birthDate(LocalDate.of(2001, 8, 10))
                        .deptId(2L)
                        .grade(1)
                        .admissionDate(LocalDate.of(2023, 3, 2))
                        .isActive(true)
                        .gender(maleGender)
                        .academicStatusCode(activeStatus)
                        .createAt(LocalDateTime.now())
                        .updateAt(LocalDateTime.now())
                        .imageFileId(3L)
                        .build(),

                Student.builder()
                        .name("정다희")
                        .password(passwordEncoder.encode("std1234"))
                        .email("student4@du.ac.kr")
                        .tel("01044444444")
                        .address("서울시 마포구")
                        .birthDate(LocalDate.of(2000, 12, 25))
                        .deptId(2L)
                        .grade(2)
                        .admissionDate(LocalDate.of(2022, 3, 2))
                        .isActive(true)
                        .gender(femaleGender)
                        .academicStatusCode(leaveStatus)
                        .createAt(LocalDateTime.now())
                        .updateAt(LocalDateTime.now())
                        .imageFileId(4L)
                        .build(),

                Student.builder()
                        .name("최준호")
                        .password(passwordEncoder.encode("std1234"))
                        .email("student5@du.ac.kr")
                        .tel("01055555555")
                        .address("서울시 영등포구")
                        .birthDate(LocalDate.of(1999, 3, 30))
                        .deptId(3L)
                        .grade(3)
                        .admissionDate(LocalDate.of(2021, 3, 2))
                        .isActive(true)
                        .gender(maleGender)
                        .academicStatusCode(activeStatus)
                        .createAt(LocalDateTime.now())
                        .updateAt(LocalDateTime.now())
                        .imageFileId(5L)
                        .build(),

                Student.builder()
                        .name("강지영")
                        .password(passwordEncoder.encode("std1234"))
                        .email("student6@du.ac.kr")
                        .tel("01066666666")
                        .address("서울시 강서구")
                        .birthDate(LocalDate.of(2000, 7, 7))
                        .deptId(3L)
                        .grade(2)
                        .admissionDate(LocalDate.of(2022, 3, 2))
                        .isActive(true)
                        .gender(femaleGender)
                        .academicStatusCode(activeStatus)
                        .createAt(LocalDateTime.now())
                        .updateAt(LocalDateTime.now())
                        .imageFileId(6L)
                        .build(),

                Student.builder()
                        .name("윤성민")
                        .password(passwordEncoder.encode("std1234"))
                        .email("student7@du.ac.kr")
                        .tel("01077777777")
                        .address("서울시 동작구")
                        .birthDate(LocalDate.of(2001, 4, 15))
                        .deptId(4L)
                        .grade(1)
                        .admissionDate(LocalDate.of(2023, 3, 2))
                        .isActive(true)
                        .gender(maleGender)
                        .academicStatusCode(activeStatus)
                        .createAt(LocalDateTime.now())
                        .updateAt(LocalDateTime.now())
                        .imageFileId(7L)
                        .build(),

                Student.builder()
                        .name("한소희")
                        .password(passwordEncoder.encode("std1234"))
                        .email("student8@du.ac.kr")
                        .tel("01088888888")
                        .address("서울시 관악구")
                        .birthDate(LocalDate.of(2000, 9, 20))
                        .deptId(4L)
                        .grade(2)
                        .admissionDate(LocalDate.of(2022, 3, 2))
                        .isActive(true)
                        .gender(femaleGender)
                        .academicStatusCode(leaveStatus)
                        .createAt(LocalDateTime.now())
                        .updateAt(LocalDateTime.now())
                        .imageFileId(8L)
                        .build(),

                Student.builder()
                        .name("임재현")
                        .password(passwordEncoder.encode("std1234"))
                        .email("student9@du.ac.kr")
                        .tel("01099999999")
                        .address("서울시 성북구")
                        .birthDate(LocalDate.of(1999, 11, 11))
                        .deptId(5L)
                        .grade(3)
                        .admissionDate(LocalDate.of(2021, 3, 2))
                        .isActive(true)
                        .gender(maleGender)
                        .academicStatusCode(activeStatus)
                        .createAt(LocalDateTime.now())
                        .updateAt(LocalDateTime.now())
                        .imageFileId(9L)
                        .build(),

                Student.builder()
                        .name("오민지")
                        .password(passwordEncoder.encode("std1234"))
                        .email("student10@du.ac.kr")
                        .tel("01000000000")
                        .address("서울시 노원구")
                        .birthDate(LocalDate.of(2000, 2, 28))
                        .deptId(5L)
                        .grade(2)
                        .admissionDate(LocalDate.of(2022, 3, 2))
                        .isActive(true)
                        .gender(femaleGender)
                        .academicStatusCode(activeStatus)
                        .createAt(LocalDateTime.now())
                        .updateAt(LocalDateTime.now())
                        .imageFileId(10L)
                        .build()
        );

        studentRepository.saveAll(dummyStudents);
    }
} 