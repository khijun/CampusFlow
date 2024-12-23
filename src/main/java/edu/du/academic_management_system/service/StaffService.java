package edu.du.academic_management_system.service;

import edu.du.academic_management_system.entity.CommonCode;
import edu.du.academic_management_system.entity.Staff;
import edu.du.academic_management_system.repository.CommonCodeRepository;
import edu.du.academic_management_system.repository.StaffRepository;
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
public class StaffService {

    @Autowired
    private PasswordEncoder passwordEncoder;
    private final StaffRepository staffRepository;
    private final CommonCodeRepository commonCodeRepository;

    @Transactional
    public void createDummyStaffs() {
        // 기존에 저장된 성별 코드 가져오기
        CommonCode maleGender = commonCodeRepository.findByCodeValue("MALE");
        CommonCode femaleGender = commonCodeRepository.findByCodeValue("FEMALE");

        List<Staff> dummyStaffs = List.of(
            Staff.builder()
                .name("김직원")
                .password(passwordEncoder.encode("staff1234"))
                .email("staff1@du.ac.kr")
                .tel("01056781001")
                .address("서울시 중구")
                .birthDate(LocalDate.of(1980, 3, 15))
                .deptId(1L)
                .hireDate(LocalDate.of(2015, 3, 1))
                .isActive(true)
                .gender(maleGender)
                .createAt(LocalDateTime.now())
                .updateAt(LocalDateTime.now())
                .imageFileId(21L)
                .build(),

            Staff.builder()
                .name("이직원")
                .password(passwordEncoder.encode("staff1234"))
                .email("staff2@du.ac.kr")
                .tel("01056781002")
                .address("서울시 종로구")
                .birthDate(LocalDate.of(1985, 5, 20))
                .deptId(1L)
                .hireDate(LocalDate.of(2016, 3, 1))
                .isActive(true)
                .gender(femaleGender)
                .createAt(LocalDateTime.now())
                .updateAt(LocalDateTime.now())
                .imageFileId(22L)
                .build(),

            Staff.builder()
                .name("박직원")
                .password(passwordEncoder.encode("staff1234"))
                .email("staff3@du.ac.kr")
                .tel("01056781003")
                .address("서울시 용산구")
                .birthDate(LocalDate.of(1982, 8, 10))
                .deptId(2L)
                .hireDate(LocalDate.of(2015, 9, 1))
                .isActive(true)
                .gender(maleGender)
                .createAt(LocalDateTime.now())
                .updateAt(LocalDateTime.now())
                .imageFileId(23L)
                .build(),

            Staff.builder()
                .name("정직원")
                .password(passwordEncoder.encode("staff1234"))
                .email("staff4@du.ac.kr")
                .tel("01056781004")
                .address("서울시 성동구")
                .birthDate(LocalDate.of(1987, 11, 25))
                .deptId(2L)
                .hireDate(LocalDate.of(2017, 3, 1))
                .isActive(true)
                .gender(femaleGender)
                .createAt(LocalDateTime.now())
                .updateAt(LocalDateTime.now())
                .imageFileId(24L)
                .build(),

            Staff.builder()
                .name("최직원")
                .password(passwordEncoder.encode("staff1234"))
                .email("staff5@du.ac.kr")
                .tel("01056781005")
                .address("서울시 광진구")
                .birthDate(LocalDate.of(1983, 4, 5))
                .deptId(3L)
                .hireDate(LocalDate.of(2016, 3, 1))
                .isActive(true)
                .gender(maleGender)
                .createAt(LocalDateTime.now())
                .updateAt(LocalDateTime.now())
                .imageFileId(25L)
                .build(),

            Staff.builder()
                .name("강직원")
                .password(passwordEncoder.encode("staff1234"))
                .email("staff6@du.ac.kr")
                .tel("01056781006")
                .address("서울시 동대문구")
                .birthDate(LocalDate.of(1986, 7, 15))
                .deptId(3L)
                .hireDate(LocalDate.of(2017, 9, 1))
                .isActive(true)
                .gender(femaleGender)
                .createAt(LocalDateTime.now())
                .updateAt(LocalDateTime.now())
                .imageFileId(26L)
                .build(),

            Staff.builder()
                .name("윤직원")
                .password(passwordEncoder.encode("staff1234"))
                .email("staff7@du.ac.kr")
                .tel("01056781007")
                .address("서울시 중랑구")
                .birthDate(LocalDate.of(1984, 9, 20))
                .deptId(4L)
                .hireDate(LocalDate.of(2016, 9, 1))
                .isActive(true)
                .gender(maleGender)
                .createAt(LocalDateTime.now())
                .updateAt(LocalDateTime.now())
                .imageFileId(27L)
                .build(),

            Staff.builder()
                .name("한직원")
                .password(passwordEncoder.encode("staff1234"))
                .email("staff8@du.ac.kr")
                .tel("01056781008")
                .address("서울시 성북구")
                .birthDate(LocalDate.of(1988, 12, 3))
                .deptId(4L)
                .hireDate(LocalDate.of(2018, 3, 1))
                .isActive(true)
                .gender(femaleGender)
                .createAt(LocalDateTime.now())
                .updateAt(LocalDateTime.now())
                .imageFileId(28L)
                .build(),

            Staff.builder()
                .name("임직원")
                .password(passwordEncoder.encode("staff1234"))
                .email("staff9@du.ac.kr")
                .tel("01056781009")
                .address("서울시 강북구")
                .birthDate(LocalDate.of(1981, 2, 28))
                .deptId(5L)
                .hireDate(LocalDate.of(2015, 3, 1))
                .isActive(true)
                .gender(maleGender)
                .createAt(LocalDateTime.now())
                .updateAt(LocalDateTime.now())
                .imageFileId(29L)
                .build(),

            Staff.builder()
                .name("오직원")
                .password(passwordEncoder.encode("staff1234"))
                .email("staff10@du.ac.kr")
                .tel("01056781010")
                .address("서울시 도봉구")
                .birthDate(LocalDate.of(1989, 6, 15))
                .deptId(5L)
                .hireDate(LocalDate.of(2019, 3, 1))
                .isActive(true)
                .gender(femaleGender)
                .createAt(LocalDateTime.now())
                .updateAt(LocalDateTime.now())
                .imageFileId(30L)
                .build()
        );

        staffRepository.saveAll(dummyStaffs);
    }
} 