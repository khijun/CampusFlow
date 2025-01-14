package edu.du.campusflow.service;

import edu.du.campusflow.dto.MemberSearchFilter;
import edu.du.campusflow.entity.CommonCode;
import edu.du.campusflow.entity.Member;
import edu.du.campusflow.repository.CommonCodeGroupRepository;
import edu.du.campusflow.repository.CommonCodeRepository;
import edu.du.campusflow.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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

    public List<Member> findAllWithFilter(MemberSearchFilter filter) {
        if(filter!=null) {
            if (filter.getIsActive() == null) filter.setIsActive(true);
            if (filter.getMemberType() != null && filter.getMemberType() == 0L) filter.setMemberType(null);
            if (filter.getName() != null) filter.setName('%' + filter.getName() + '%');
            if (filter.getTel() != null) filter.setTel('%' + filter.getTel() + '%');
        }else{
            filter = MemberSearchFilter.builder().build();
        }
        return memberRepository.findAllWithFilter(filter);
    }

//    public List<Member> findAllByMemberType(Long memberType) {
//        return findAllWithFilter();
//    }

    // 모든 학생을 반환하는 메서드
    public List<Member> findAll() {
        return memberRepository.findAll();
    }

}
