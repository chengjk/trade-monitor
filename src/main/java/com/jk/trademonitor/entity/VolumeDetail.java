package com.jk.trademonitor.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * created by jacky. 2019/11/22 6:02 PM
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
@Entity
@Table(name = "volume_detail")
public class VolumeDetail implements Serializable {
    @Id
    private Long id;
    private String symbol = "BTC_USDT";
    private BigDecimal volume = BigDecimal.ZERO;
    @Column(name = "ask_taker_volume")
    private BigDecimal askTakerVolume = BigDecimal.ZERO;
    @Column(name = "bid_taker_volume")
    private BigDecimal bidTakerVolume = BigDecimal.ZERO;
    private BigDecimal price = BigDecimal.ZERO;
    private String resolution = "M1";
}
