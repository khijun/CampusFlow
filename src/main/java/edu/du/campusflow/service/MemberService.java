package edu.du.campusflow.service;

import edu.du.campusflow.dto.MemberDTO;
import edu.du.campusflow.entity.CommonCode;
import edu.du.campusflow.entity.CommonCode;
import edu.du.campusflow.entity.Member;
import edu.du.campusflow.repository.CommonCodeGroupRepository;
import edu.du.campusflow.repository.CommonCodeRepository;
import edu.du.campusflow.enums.AcademicStatus;
import edu.du.campusflow.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;
    private final CommonCodeGroupRepository commonCodeGroupRepository;
    private final CommonCodeRepository commonCodeRepository;



    public Member findByMemberId(Long memberId) {
        return memberRepository.findById(memberId).orElseThrow();
    };

    public List<MemberDTO> findAllMemberDTOs(List<CommonCode> searchMemberType){
        List<Member> members = memberRepository.findAllWithDetails(searchMemberType);
        return members.stream().map(MemberDTO::fromEntity).collect(Collectors.toList());
    }

    public List<MemberDTO> findAllMemberDTOs(){
        return findAllMemberDTOs(commonCodeGroupRepository.findByGroupCode("MEMBERTYPE").getCommonCodes());
    }

    public List<MemberDTO> findAllMemberDTOs(CommonCode searchMemberType){
        return findAllMemberDTOs(Collections.singletonList(searchMemberType));
    }

    public List<MemberDTO> findAllMemberDTOs(Long typeId) {
        return typeId==null||typeId==0L?findAllMemberDTOs():findAllMemberDTOs(commonCodeRepository.findById(typeId).orElse(null));
    }

    // AcademicStatus에 따라 Member 목록을 찾는 메서드
    public List<Member> findByAcademicStatus(CommonCode academicStatus) {
        return memberRepository.findByAcademicStatus(academicStatus); // AcademicStatus로 Member 찾기
    }

    // 모든 학생을 반환하는 메서드
    public List<Member> findAll() {
        return memberRepository.findAll();
    }

}
