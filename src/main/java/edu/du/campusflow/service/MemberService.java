package edu.du.campusflow.service;

import edu.du.campusflow.dto.MemberDTO;
import edu.du.campusflow.entity.CommonCode;
import edu.du.campusflow.entity.Member;
import edu.du.campusflow.repository.CommonCodeGroupRepository;
import edu.du.campusflow.repository.CommonCodeRepository;
import edu.du.campusflow.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
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

    // 공통코드 아이디를 받아 멤버 타입으로서 필터링해 반환하는 메서드. null 이거나 0이면 모든 값을 반환하는 메서드
    public List<MemberDTO> findAllMemberDTOs(Long typeId, Boolean isActive) {
        if(typeId == null) typeId = 0L;
        if(isActive == null) isActive = true ;
        return typeId==0L?findAllMemberDTOs(new ArrayList<>(), isActive):
                findAllMemberDTOs(Collections.singletonList(typeId), isActive);
    }

    // AcademicStatus에 따라 Member 목록을 찾는 메서드
    public List<Member> findByAcademicStatus(CommonCode academicStatus) {
        return memberRepository.findByAcademicStatus(academicStatus); // AcademicStatus로 Member 찾기
    }

    public List<Member> findAllMembers(List<Long> typeIds, Boolean isActive) {
        if(isActive==null)  isActive=true;
        return typeIds==null||typeIds.isEmpty()?memberRepository.findAllWithDetails(isActive):memberRepository.findAllWithDetailsByIds(typeIds, isActive);
    }

    // 멤버 타입 공통 코드의 리스트를 받아 조건에 맞는 값을 반환하는 메서드
    public List<MemberDTO> findAllMemberDTOs(List<Long> typeIds, Boolean isActive){
        return MemberDTO.fromEntityList(findAllMembers(typeIds, isActive));
    }

    // 모든 학생을 반환하는 메서드
    public List<Member> findAll() {
        return memberRepository.findAll();
    }

}
