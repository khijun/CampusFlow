package edu.du.campusflow.entity;

import lombok.*;

import javax.persistence.*;
import java.util.List;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Table(name = "common_code_group")
public class CommonCodeGroup {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "group_id")
    private Long groupId;

    @Column(name = "group_code", length = 50)
    private String groupCode;

    @Column(name = "group_name", length = 100)
    private String groupName;

    @Column(name = "group_description", length = 100)
    private String groupDescription;

    @OneToMany(mappedBy = "codeGroup", fetch = FetchType.EAGER)
    @ToString.Exclude
    private List<CommonCode> commonCodes;
} 