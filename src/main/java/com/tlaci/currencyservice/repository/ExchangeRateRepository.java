package com.tlaci.currencyservice.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import com.tlaci.currencyservice.entity.ExchangeRate;
import com.tlaci.currencyservice.entity.ExchangeType;

public interface ExchangeRateRepository extends CrudRepository<ExchangeRate, Long> {

    Optional<ExchangeRate> findByTypeAndFromAndTo(ExchangeType type, String from, String to);

    List<ExchangeRate> findByTypeAndFrom(ExchangeType type, String from);
}
