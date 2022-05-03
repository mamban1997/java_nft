package com.example.crypto.data;

import com.example.crypto.security.model.User;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

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
    private boolean isHide;
    private Double instantBuyPrice;

    @ManyToOne
    @ToString.Exclude
    private User currentOwner;

    @ManyToOne
    @ToString.Exclude
    private User creator;

    @OneToMany(mappedBy = "nftEntity",cascade = {CascadeType.PERSIST, CascadeType.REFRESH, CascadeType.REMOVE})
    @ToString.Exclude
    private List<PreviousOwner> previousOwners = new java.util.ArrayList<>();




}
