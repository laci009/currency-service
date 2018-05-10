package com.tlaci.currencyservice.entity;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "EXCHANGE_RATE",
        indexes = {@Index(name = "INDEX_TYPE_FROM_TO", columnList = "TYPE,FROM_CURRENCY,TO_CURRENCY", unique = true)
})
public class ExchangeRate {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_EXCHANGE_RATE")
    @SequenceGenerator(name = "SEQ_EXCHANGE_RATE", sequenceName = "SEQ_EXCHANGE_RATE")
    private Long id;

    @Column(name = "type")
    @Enumerated(EnumType.STRING)
    private ExchangeType type;

    @Column(name = "from_currency")
    private String from;

    @Column(name = "to_currency")
    private String to;

    @Column(name = "RATE", precision = 19, scale = 7)
    private BigDecimal rate;

    public ExchangeRate() {
    }

    public ExchangeRate(ExchangeType type, String from, String to, BigDecimal rate) {
        this.type = type;
        this.from = from;
        this.to = to;
        this.rate = rate;
    }

    public Long getId() {
        return id;
    }

    public ExchangeType getType() {
        return type;
    }

    public String getFrom() {
        return from;
    }

    public String getTo() {
        return to;
    }

    public BigDecimal getRate() {
        return rate;
    }

    @Override
    public String toString() {
        return "ExchangeRate{" +
                "id=" + id +
                ", type='" + type + '\'' +
                ", from='" + from + '\'' +
                ", to='" + to + '\'' +
                ", rate=" + rate +
                '}';
    }
}
