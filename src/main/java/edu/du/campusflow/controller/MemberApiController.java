package edu.du.campusflow.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.du.campusflow.dto.MemberCreateRequestDTO;
import edu.du.campusflow.dto.MemberDTO;
import edu.du.campusflow.dto.MemberSearchFilter;
import edu.du.campusflow.dto.MemberUpdateDTO;
import edu.du.campusflow.service.AuthService;
import edu.du.campusflow.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/member")
public class MemberApiController {
    private final MemberService memberService;
    private final AuthService authService;

    @GetMapping("/all")
    public List<MemberDTO> getMembers(@RequestParam(required = false, name = "filter") String searchURL){
        ObjectMapper objectMapper = new ObjectMapper();
        MemberSearchFilter filter;
        try{
            filter = objectMapper.readValue(searchURL, MemberSearchFilter.class);
        }catch (Exception e){
            filter = MemberSearchFilter.builder().build();
        }
        return MemberDTO.fromEntityList(memberService.findAllWithFilter(filter));
    }
    @GetMapping("/me")
    public MemberDTO getMember(){
        return MemberDTO.fromEntity(authService.getCurrentMember());
    }

    @GetMapping("/{id}")
    public MemberDTO getMemberById(@PathVariable Long id){
        return MemberDTO.fromEntity(memberService.findByMemberId(id));
    }
    @PostMapping
    public ResponseEntity<?> addMembers(@RequestBody MemberCreateRequestDTO dto){
        System.out.println(dto);
        try {
            memberService.addMembers(dto);
        } catch (RuntimeException e) {
            System.out.println("예외발생");
            return ResponseEntity.badRequest().body(e.getMessage());
        }

        return ResponseEntity.ok().build();
    }
    @PutMapping
    public ResponseEntity<?> updateMember(@RequestBody MemberUpdateDTO memberUpdateDTO) {
        // 기존 멤버 업데이트
        try{
            MemberDTO updatedMember = memberService.updateMember(authService.getCurrentMember(), memberUpdateDTO);
            return ResponseEntity.ok(updatedMember);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
