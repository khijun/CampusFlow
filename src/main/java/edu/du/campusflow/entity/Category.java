package edu.du.campusflow.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.*;

import javax.persistence.*;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@ToString
@AllArgsConstructor
@Entity
@Table(name = "category")
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "category_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonBackReference
    @ToString.Exclude
    @JoinColumn(name = "parent_id")
    private Category parent;

    @Column(name = "category_name", length = 50)
    private String name;

    @ManyToOne
    @JoinColumn(name = "category_image")
    private FileInfo image;

    @Column(name = "category_url", length = 200)
    private String url;

    @Column(name = "is_active")
    private Boolean isActive;

    @ManyToOne
    @JoinColumn(name = "member_type")
    private CommonCode memberType;

    @Column(name = "order_no")
    private Integer orderNo;

    @OneToMany(mappedBy = "parent")
    @OrderBy("orderNo asc")
    private List<Category> children;

}
