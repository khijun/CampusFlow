package edu.du.campusflow.service;

import edu.du.campusflow.entity.CommonCode;
import edu.du.campusflow.entity.Member;
import edu.du.campusflow.repository.CommonCodeGroupRepository;
import edu.du.campusflow.repository.CommonCodeRepository;
import edu.du.campusflow.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;
    private final CommonCodeGroupRepository commonCodeGroupRepository;
    private final CommonCodeRepository commonCodeRepository;



    public Member findByMemberId(Long memberId) {
        return memberRepository.findById(memberId).orElse(null);
    };

    // AcademicStatus에 따라 Member 목록을 찾는 메서드
    public List<Member> findByAcademicStatus(CommonCode academicStatus) {
        return memberRepository.findByAcademicStatus(academicStatus); // AcademicStatus로 Member 찾기
    }

    public List<Member> findAllWithFilter(
            Long memberType, Boolean isActive, Long deptId, String name, String tel,
            LocalDate birthStart, LocalDate birthEnd, LocalDateTime createAtStart, LocalDateTime createAtEnd,
            Long academicStatus, Long grade, LocalDate startDateStart, LocalDate startDateEnd,
            LocalDate endDateStart, LocalDate endDateEnd) {
        if(isActive==null)  isActive=true;
        if(memberType!=null&&memberType==0L) memberType=null;
        if(name!=null) name = '%'+name+'%';
        if(tel!=null) tel = '%'+tel+'%';
        return memberRepository.findAllWithFilter(memberType, isActive, deptId, name, tel, birthStart, birthEnd, createAtStart, createAtEnd, academicStatus, grade, startDateStart, startDateEnd,
                endDateStart, endDateEnd);
    }

    public List<Member> findAllByMemberType(Long memberType) {
        return findAllWithFilter(memberType, null, null, null, null, null, null,null,null,null,null,null,null,null,null);
    }

    // 모든 학생을 반환하는 메서드
    public List<Member> findAll() {
        return memberRepository.findAll();
    }

}
