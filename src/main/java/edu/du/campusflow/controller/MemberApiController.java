package edu.du.campusflow.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.du.campusflow.dto.MemberCreateRequestDTO;
import edu.du.campusflow.dto.MemberDTO;
import edu.du.campusflow.dto.MemberSearchFilter;
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
    @GetMapping
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
}
