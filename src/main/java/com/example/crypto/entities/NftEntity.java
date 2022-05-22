package com.example.crypto.entities;

import com.example.crypto.security.model.User;
import com.example.crypto.services.NftDto;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class NftEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Long id;

    private String nftName;
    private String description;
    @Column(unique = true)
    private String uniqNumber;//GUID like e02fd0e4-00fd-090A-ca30-0d00a0038ba0
    private String alias;
    private String picture;
    private LocalDateTime createDate;

    @ColumnDefault("true")
    private Boolean hidden;
    private Double instantBuyPrice;

    @ManyToOne
    @ToString.Exclude
    private User currentOwner;

    @ManyToOne
    @ToString.Exclude
    private User creator;

    @OneToMany(mappedBy = "nftEntity",cascade = {CascadeType.PERSIST, CascadeType.REFRESH, CascadeType.REMOVE, CascadeType.MERGE})
    @OnDelete(action = OnDeleteAction.CASCADE)
    @ToString.Exclude
    private List<PreviousOwnerEntity> previousOwnerEntities = new java.util.ArrayList<>();

    public NftDto getDto(){
        return NftDto.builder().alias(alias).description(description).instantBuyPrice(instantBuyPrice).Hidden(hidden).nftName(nftName).build();
    }

    public String getNumberOrAlias(){
        return alias == null ? uniqNumber : alias;
    }

    public String getStringPrice() {
        return String.format("%,.2f", getInstantBuyPrice());
    }
}
