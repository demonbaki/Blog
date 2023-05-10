package it.cgmconsulting.myblog.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode
public class Category {
    @Id
    @EqualsAndHashCode.Include
    @Column( length = 50)
    private String categoryName;

    private boolean visible;

    public Category(String categoryName) {
        this.categoryName = categoryName;
    }
}
