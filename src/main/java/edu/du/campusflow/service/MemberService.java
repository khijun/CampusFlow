package edu.du.campusflow.service;

import edu.du.campusflow.entity.CommonCode;
import edu.du.campusflow.entity.Member;
import edu.du.campusflow.enums.AcademicStatus;
import edu.du.campusflow.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;



    public Member findByMemberId(Long memberId) {
        return memberRepository.findById(memberId).orElseThrow();
    };
    // AcademicStatus에 따라 Member 목록을 찾는 메서드
    public List<Member> findByAcademicStatus(CommonCode academicStatus) {
        return memberRepository.findByAcademicStatus(academicStatus); // AcademicStatus로 Member 찾기
    }
    // 모든 학생을 반환하는 메서드
    public List<Member> findAll() {
        return memberRepository.findAll();
    }

}
