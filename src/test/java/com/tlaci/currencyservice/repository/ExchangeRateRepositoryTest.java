package com.tlaci.currencyservice.repository;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.hamcrest.Matchers;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import com.tlaci.currencyservice.entity.ExchangeRate;
import com.tlaci.currencyservice.entity.ExchangeType;

@RunWith(SpringRunner.class)
@DataJpaTest
public class ExchangeRateRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;
    @Autowired
    private ExchangeRateRepository exchangeRateRepository;

    @Test
    public void testFindByTypeAndFrom_withResult() {
        entityManager.persist(new ExchangeRate(ExchangeType.BUY, "JPY", "EUR", BigDecimal.valueOf(100.2)));
        entityManager.persist(new ExchangeRate(ExchangeType.BUY, "JPY", "GBP", BigDecimal.valueOf(130.12)));

        List<ExchangeRate> exchangeRates = exchangeRateRepository.findByTypeAndFrom(ExchangeType.BUY, "JPY");
        assertFalse(exchangeRates.isEmpty());
        assertEquals(2, exchangeRates.size());
    }

    @Test
    public void testFindByTypeAndFrom_withoutResult() {
        List<ExchangeRate> exchangeRates = exchangeRateRepository.findByTypeAndFrom(ExchangeType.BUY, "JPY");
        assertTrue(exchangeRates.isEmpty());
    }

    @Test
    public void testFindByTypeAndFromAndTo_withResult() {
        entityManager.persist(new ExchangeRate(ExchangeType.BUY, "JPY", "GBP", BigDecimal.valueOf(130.12)));

        Optional<ExchangeRate> exchangeRate = exchangeRateRepository.findByTypeAndFromAndTo(ExchangeType.BUY, "JPY", "GBP");
        assertTrue(exchangeRate.isPresent());
        ExchangeRate exchangeRateValue = exchangeRate.get();
        assertEquals("JPY", exchangeRateValue.getFrom());
        assertEquals("GBP", exchangeRateValue.getTo());
        assertEquals(ExchangeType.BUY, exchangeRateValue.getType());
        assertThat(BigDecimal.valueOf(130.12), Matchers.comparesEqualTo(exchangeRateValue.getRate()));
    }

    @Test
    public void testFindByTypeAndFromAndTo_withoutResult() {
        Optional<ExchangeRate> exchangeRate = exchangeRateRepository.findByTypeAndFromAndTo(ExchangeType.BUY, "JPY", "GBP");
        assertFalse(exchangeRate.isPresent());
    }
}
