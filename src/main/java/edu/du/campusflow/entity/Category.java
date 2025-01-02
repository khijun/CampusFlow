package edu.du.campusflow.entity;

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
    private Long categoryId;


    @Column(name = "category_name", length = 50)
    private String categoryName;

    @ManyToOne
    @JoinColumn(name = "file_id")
    private FileInfo fileInfo;

    @Column(name = "category_url", length = 200)
    private String categoryUrl;

    @ManyToOne
    @JoinColumn(name = "parent_id", nullable = true)
    private Category parent;

    @OneToMany(mappedBy = "parent")
    private List<Category> children;

    @OneToMany(mappedBy = "category")
    private List<CategoryOrder> categoryOrders;
}