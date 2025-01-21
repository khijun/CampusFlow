package edu.du.campusflow.service;

import edu.du.campusflow.define.MemberIdPosition;
import edu.du.campusflow.dto.*;
import edu.du.campusflow.entity.CommonCode;
import edu.du.campusflow.entity.Member;
import edu.du.campusflow.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;
    private final DeptService deptService;
    private final CommonCodeService commonCodeService;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private CommonCodeGroupService commonCodeGroupService;


    public Member findByMemberId(Long memberId) {
        return memberRepository.findById(memberId).orElse(null);
    }

    // AcademicStatus에 따라 Member 목록을 찾는 메서드
    public List<Member> findByAcademicStatus(CommonCode academicStatus) {
        return memberRepository.findByAcademicStatus(academicStatus); // AcademicStatus로 Member 찾기
    }

    public List<Member> findAllWithFilter(MemberSearchFilter filter) {
        if (filter != null) {
            if (filter.getIsActive() == null) filter.setIsActive(true);
            if (filter.getMemberType() != null && filter.getMemberType() == 0L) filter.setMemberType(null);
            if (filter.getName() != null) filter.setName('%' + filter.getName() + '%');
            if (filter.getTel() != null) filter.setTel('%' + filter.getTel() + '%');
        } else {
            filter = MemberSearchFilter.builder().build();
        }
        return memberRepository.findAllWithFilter(filter);
    }

    // 모든 학생을 반환하는 메서드
    public List<Member> findAll() {
        return memberRepository.findAll();
    }

    // ----------------------------------------------------------------

    public MemberDTO updateMember(Member member, MemberUpdateDTO memberUpdateDTO) throws RuntimeException {
        if (!checkPassword(memberUpdateDTO.getCurrentPassword(), member.getPassword())) {
            throw new RuntimeException("비밀번호가 일치하지 않습니다");
        }
        member.setName(memberUpdateDTO.getName());
        member.setTel(memberUpdateDTO.getTel());
        member.setAddress(memberUpdateDTO.getAddress());
        member.setBirthDate(memberUpdateDTO.getBirthDate());
        member.setEmail(memberUpdateDTO.getEmail());
        boolean passwordEncoding = false;
        if (!(memberUpdateDTO.getNewPassword()==null||memberUpdateDTO.getNewPassword().isEmpty())){
            member.setPassword(memberUpdateDTO.getNewPassword());
            passwordEncoding = true;
        }
        return MemberDTO.fromEntity(updateMember(member, passwordEncoding));
    }

    public MemberDTO updateMember(MemberDTO memberDTO) {
        // 멤버 아이디를 사용하여 기존 Member 객체 가져오기
        Member member = findByMemberId(memberDTO.getMemberId());

        // MemberDTO의 내용을 Member 객체에 설정
        // 아이디
        // 학과
        // 이름
        member.setName(memberDTO.getName());
        // 전번
        member.setTel(memberDTO.getTel());
        // 주소
        member.setAddress(memberDTO.getAddress());
        // 생년월일
        member.setBirthDate(memberDTO.getBirthDate());
        // 계정상태
        member.setIsActive(memberDTO.getIsActive());
        // 생성일
        // 수정일
        // 이메일
        member.setEmail(memberDTO.getEmail());
        // 파일정보
        // 성별
        // 학적상태
        // 학년
        // 회원구분
        // 입학날짜
        // 졸업날짜


        // 입학, 퇴직 날짜 설정
        member.setStartDate(memberDTO.getStartDate());
        member.setEndDate(memberDTO.getEndDate());

        return MemberDTO.fromEntity(updateMember(member, false));
    }

    public Member updateMember(Member member, boolean passwordEncoding) {
        member.setUpdateAt(LocalDateTime.now());
        if (passwordEncoding) {
            member.setPassword(passwordEncoder.encode(member.getPassword()));
        }
        return memberRepository.save(member);
    }

    // ----------------------------------------------------------------

    public Member addMember(MemberCreateDTO dto, Long deptId, Boolean isActive, Long academicStatusId, Long memberTypeId, LocalDate startDate) throws RuntimeException {

        // 생년월일을 비밀번호로 설정. 생일입력값이 없을 시에 랜덤한 문자열로 비밀번호 작성
        String pw = dto.getBirthday() == null ?
                PasswordGenerator.generateRandomPassword(6) :
                dto.getBirthday().format(DateTimeFormatter.ofPattern("yyMMdd"));
        // 입력값이 없으면 true
        isActive = isActive == null ? true : isActive;
        CommonCode gender = commonCodeService.findById(dto.getGenderId());

        if (memberTypeId == null)
            throw new RuntimeException("멤버 타입이 지정되지 않음");
        CommonCode memberType = commonCodeService.findById(memberTypeId);

        CommonCode academicStatus = null;
        CommonCode grade = null;
        if (memberType.getCodeValue().equals("STUDENT")){
            academicStatus = academicStatusId == null ? commonCodeService.findByCodeGroupAndCodeValue("ACADEMICSTATUS", "ENROLLED") : commonCodeService.findById(academicStatusId);
            grade = commonCodeService.findByCodeGroupAndCodeValue("GRADE", "GRADE_1");
        }

        Member member = Member.builder()
                .memberId(createMemberId(deptId, memberType))
                .dept(deptService.findById(deptId))
                .password(pw)
                .name(dto.getName())
                .tel(dto.getTel())
                .address(dto.getAddress())
                .birthDate(dto.getBirthday())
                .isActive(isActive)
                .createAt(LocalDateTime.now())
                .updateAt(LocalDateTime.now())
                .email(dto.getEmail())
                .fileInfo(null)
                .gender(gender)
                .academicStatus(academicStatus)
                // 학년 그룹에서 1학년을 갖고옴
                .grade(grade)
                // 멤버타입 그룹에서 학생을 갖고옴
                .memberType(memberType)
                .startDate(startDate)
                .endDate(null)
                .build();
        return addMember(member);
    }

    @Transactional
    public List<Member> addMembers(MemberCreateRequestDTO dto) throws RuntimeException {
        return dto.getMemberDTOs().stream().map(cDto -> {
            // dto의 값을 각각 addMember한 후 리스트로 만들어 반환
            return addMember(cDto, dto.getDeptId(), dto.getIsActive(), dto.getAcademicStatusId(), dto.getMemberTypeId(), dto.getStartDate());
        }).collect(Collectors.toList());
    }

    public Long createMemberId(Long deptId, CommonCode memberType) {
        if (deptId == null) throw new RuntimeException("학과 아이디가 비어있습니다");
        int year = LocalDate.now().getYear() % 100;
        switch (memberType.getCodeValue()) {
            case "STUDENT":
                Integer studentMaxNo = memberRepository.getMaxStudentNoFromDeptAndYear(deptId, year, memberType.getCodeId());
                if (studentMaxNo == null) studentMaxNo = 0;
                return (year * MemberIdPosition.YEAR_POSITION) +
                        (deptId * MemberIdPosition.STUDENT_DEPT_POSITION) +
                        (studentMaxNo + 1);
            case "PROFESSOR":
                Integer professorMaxNo = memberRepository.getMaxProfessorNoFromDept(deptId, memberType.getCodeId());
                if (professorMaxNo == null) professorMaxNo = 0;
                return (deptId * MemberIdPosition.PROFESSOR_DEPT_POSITION) +
                        (professorMaxNo + 1);
            case "STAFF":
                Integer staffMaxNo = memberRepository.getMaxStaffNoFromDept(deptId, memberType.getCodeId());
                if (staffMaxNo == null) staffMaxNo = 0;
                return (MemberIdPosition.STAFF_NUMBER * MemberIdPosition.STAFF_NO_POSITION) +
                        (deptId * MemberIdPosition.STAFF_DEPT_POSITION) +
                        (staffMaxNo + 1);
            default:
                throw new RuntimeException("멤버 타입이 올바르지 않음");
        }
    }

    public Member addMember(Member member) {
        member.setPassword(passwordEncoder.encode(member.getPassword()));
        try {
            return memberRepository.save(member);
        } catch (Exception e) {
            return null;
        }
    }

    // ----------------------------------------------------------------

    public boolean checkPassword(String rawPassword, String encodedPassword) {
        return passwordEncoder.matches(rawPassword, encodedPassword);
    }

}
