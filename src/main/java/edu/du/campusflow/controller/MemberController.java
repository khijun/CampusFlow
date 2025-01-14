package edu.du.campusflow.controller;

import edu.du.campusflow.dto.MemberDTO;
import edu.du.campusflow.entity.CommonCode;
import edu.du.campusflow.repository.CommonCodeGroupRepository;
import edu.du.campusflow.service.CommonCodeService;
import edu.du.campusflow.service.MemberCreationService;
import edu.du.campusflow.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class MemberController {
    private final MemberCreationService memberCreationService;
    private final MemberService memberService;
    private final CommonCodeGroupRepository commonCodeGroupRepository;
    private final CommonCodeService commonCodeService;

    @GetMapping("/iframe/member/select_member")
    public String selectMember(Model model, @RequestParam(required = false, name = "memberType") Long typeId
    , @RequestParam(required = false, name = "isActive") Boolean isActive) {
        List<MemberDTO> memberDTOList = MemberDTO.fromEntityList(memberService.findAllByMemberType(typeId));
        List<CommonCode> typeList = commonCodeGroupRepository.findByGroupCode("MEMBERTYPE").getCommonCodes();

        model.addAttribute("selectedTypeId", typeId);
        model.addAttribute("memberDTOList", memberDTOList);
        model.addAttribute("typeList", typeList);
        return "view/iframe/member/select_member";
    }

    @GetMapping("/api/members")
    public ResponseEntity<?> getMembers(@RequestParam(required = false, name = "memberType") Long typeId
    , @RequestParam(required = false, name = "isActive") Boolean isActive) {
        return ResponseEntity.ok(MemberDTO.fromEntityList(memberService.findAllByMemberType(typeId)));
    }
}
