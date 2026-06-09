package ro.msg.learning.shop.entity.embeddable;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Address {

    @Column(name = "address_country")
    private String country;

    @Column(name = "address_city")
    private String city;

    @Column(name = "address_county")
    private String county;

    @Column(name = "address_street_address")
    private String streetAddress;
}
