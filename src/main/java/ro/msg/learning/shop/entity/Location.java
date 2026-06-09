package ro.msg.learning.shop.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ro.msg.learning.shop.entity.base.AbstractBaseEntity;
import ro.msg.learning.shop.entity.embeddable.Address;

@Entity
@Table(name = "locations")
@Getter
@Setter
@NoArgsConstructor
public class Location extends AbstractBaseEntity {

    @Column(name = "name", nullable = false)
    private String name;

    @Embedded
    private Address address;
}
