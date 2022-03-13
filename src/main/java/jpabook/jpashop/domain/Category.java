package jpabook.jpashop.domain;

import jpabook.jpashop.domain.item.Item;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
public class Category {
    @Id
    @GeneratedValue
    @Column(name ="category_id")
    private Long id;

    private String name;

    @ManyToMany
    @JoinTable(name="category_item",
        joinColumns = @JoinColumn(name="category_id"),//중간테이블에있는카테고리
        inverseJoinColumns = @JoinColumn(name = "item_id")//JoinTable 아이템쪽으로 들어가는
    )
    private List<Item> items = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private Category parent; //카테고리 부모

    @OneToMany(mappedBy = "parent")
    private List<Category> child = new ArrayList<>(); //여러개 가질 수 있다다

    //==연관관계 메서드==//
    public void addChildCategory(Category child) {
        this.child.add(child);
        child.setParent(this);
    }

}