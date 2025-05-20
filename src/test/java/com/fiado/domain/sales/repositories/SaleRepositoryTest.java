package com.fiado.domain.sales.repositories;

import com.fiado.domain.sales.entities.SaleEntity;
import com.fiado.domain.sales.enums.SaleStatus;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
public class SaleRepositoryTest {

    @Autowired
    private SaleRepository saleRepository;

    @Test
    public void testCreateAndFindSale() {
        SaleEntity sale = new SaleEntity();
        sale.setGrossAmount(new BigDecimal("100.00"));
        sale.setTotalAmount(new BigDecimal("105.00"));
        sale.setInterestRate(new BigDecimal("5.00"));
        sale.setStatus(SaleStatus.PENDING);

        // Salvar no banco (teste com banco Postgres teste via Docker)
        SaleEntity saved = saleRepository.save(sale);

        // Buscar pelo ID
        SaleEntity found = saleRepository.findById(saved.getId()).orElse(null);

        assertThat(found).isNotNull();
        assertThat(found.getGrossAmount()).isEqualByComparingTo("100.00");
    }
}
