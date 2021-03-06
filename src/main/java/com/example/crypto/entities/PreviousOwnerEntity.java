package com.example.crypto.entities;

import com.example.crypto.security.model.User;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "previous_owner")
public class PreviousOwnerEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    private LocalDateTime startOfOwnership;
    private LocalDateTime finishOfOwnership;

    private Double purchasePrice;//Цена покупки
    private Double sellingPrice;//Цена продажи

    private Integer number;

    @ManyToOne(cascade = {CascadeType.PERSIST})
    @JoinColumn(name = "nft_entity_id")
    private NftEntity nftEntity;

    @ManyToOne
    @ToString.Exclude
    private User previousOwner;
}