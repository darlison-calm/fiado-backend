package com.fiado.domain.sales.services;

import com.fiado.domain.clients.ClientEntity;
import com.fiado.domain.clients.ClientService;
import com.fiado.domain.sales.SaleServiceImpl;
import com.fiado.domain.sales.dtos.CreateSaleRequestDto;
import com.fiado.domain.sales.dtos.InstallmentRequestDto;
import com.fiado.domain.sales.entities.InstallmentEntity;
import com.fiado.domain.sales.entities.SaleEntity;
import com.fiado.domain.sales.enums.InstallmentStatus;
import com.fiado.domain.sales.repositories.InstallmentRepository;
import com.fiado.domain.sales.repositories.SaleRepository;
import com.fiado.domain.user.entities.UserEntity;
import com.fiado.domain.user.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static com.fiado.domain.sales.enums.SaleStatus.PENDING;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@DisplayName("SaleServiceImpl Unit Tests")
@ExtendWith(MockitoExtension.class)
class SaleServiceImplTest {

    private static final Logger log = LoggerFactory.getLogger(SaleServiceImplTest.class);

    @Mock private UserService userService;
    @Mock private ClientService clientService;
    @Mock private SaleRepository saleRepository;
    @Mock private InstallmentRepository installmentRepository;

    @InjectMocks private SaleServiceImpl saleService;

    private UUID userId;

    @BeforeEach
    void setUp() {
        userId = UUID.randomUUID();

        UserEntity dummyUser = new UserEntity();
        dummyUser.setId(userId);
        lenient().when(userService.getUser(userId)).thenReturn(dummyUser);

        ClientEntity dummyClient = new ClientEntity();
        dummyClient.setId(1L);
        lenient().when(clientService.getClientEntityByIdForUser(eq(1L), eq(userId))).thenReturn(dummyClient);

        lenient().when(saleRepository.save(any(SaleEntity.class)))
                .thenAnswer(invocation -> {
                    SaleEntity sale = invocation.getArgument(0);
                    sale.setId(100L);
                    return sale;
                });
    }


    @Test
    @DisplayName("Should throw exception when grossAmount is zero")
    void createSale_shouldFail_whenGrossAmountIsZero() {
        BigDecimal gross = BigDecimal.ZERO;
        List<InstallmentRequestDto> installments = List.of(
                new InstallmentRequestDto(BigDecimal.valueOf(500), LocalDateTime.now().plusDays(1))
        );
        CreateSaleRequestDto dto = new CreateSaleRequestDto(
                1L, gross, BigDecimal.ZERO, BigDecimal.ZERO, "Invalid sale", installments
        );

        try {
            saleService.createSale(dto, userId);
        } catch (IllegalArgumentException ex) {
            assertThat(ex.getMessage()).contains("O valor da venda deve ser maior que zero");
        }
    }

    @Test
    @DisplayName("Should throw exception when installment total does not match totalAmount")
    void createSale_shouldFail_whenInstallmentSumMismatch() {
        BigDecimal gross = BigDecimal.valueOf(1000);
        BigDecimal total = BigDecimal.valueOf(1050);
        List<InstallmentRequestDto> installments = List.of(
                new InstallmentRequestDto(BigDecimal.valueOf(500), LocalDateTime.now().plusDays(1)),
                new InstallmentRequestDto(BigDecimal.valueOf(400), LocalDateTime.now().plusDays(2))
        );
        CreateSaleRequestDto dto = new CreateSaleRequestDto(
                1L, gross, BigDecimal.valueOf(5), total, "Mismatch test", installments
        );
        BigDecimal sum = installments.stream().map(InstallmentRequestDto::value).reduce(BigDecimal.ZERO, (BigDecimal::add));

        try {
            saleService.createSale(dto, userId);
        } catch (IllegalArgumentException ex) {
            assertThat(ex.getMessage()).contains("A soma das parcelas " + sum + " deve ser igual ao valor total da venda " + total);
        }
    }

    @Test
    @DisplayName("Should mark installment as DELAYED if deadline is before now")
    void createSale_shouldMarkInstallmentAsDelayed() {
        BigDecimal gross = BigDecimal.valueOf(500);
        BigDecimal total = gross;
        List<InstallmentRequestDto> installments = List.of(
                new InstallmentRequestDto(BigDecimal.valueOf(500), LocalDateTime.now().minusDays(1))
        );
        CreateSaleRequestDto dto = new CreateSaleRequestDto(
                1L, gross, BigDecimal.ZERO, total, "Overdue installment", installments
        );

        SaleEntity result = saleService.createSale(dto, userId);

        assertThat(result.getInstallments()).hasSize(1);
        InstallmentEntity inst = result.getInstallments().get(0);
        assertThat(inst.getStatus()).isEqualTo(InstallmentStatus.DELAYED);
    }

    @Test
    @DisplayName("Should create a sale without interest and 3 installments of 500 each")
    void createSale_noInterest_threeInstallments() {
        BigDecimal gross = BigDecimal.valueOf(1500);
        BigDecimal interestRate = BigDecimal.ZERO;
        BigDecimal total = gross;
        List<InstallmentRequestDto> installments = List.of(
                new InstallmentRequestDto(BigDecimal.valueOf(500), LocalDateTime.now().plusDays(1)),
                new InstallmentRequestDto(BigDecimal.valueOf(500), LocalDateTime.now().plusDays(2)),
                new InstallmentRequestDto(BigDecimal.valueOf(500), LocalDateTime.now().plusDays(3))
        );
        CreateSaleRequestDto dto = new CreateSaleRequestDto(
                1L, gross, interestRate, total, "Test sale", installments
        );

        SaleEntity result = saleService.createSale(dto, userId);

        log.info("Created sale ID: {}", result.getId());
        log.info("Installments: {}", result.getInstallments().size());
        log.debug("Sale Total: {}, Gross: {}, Interest: {}", result.getTotalAmount(), result.getGrossAmount(), result.getInterestRate());

        assertThat(result.getId()).isEqualTo(100L);
        assertThat(result.getGrossAmount()).isEqualByComparingTo(gross);
        assertThat(result.getTotalAmount()).isEqualByComparingTo(total);
        assertThat(result.getStatus()).isEqualTo(PENDING);
        assertThat(result.getInstallments()).hasSize(3);

        result.getInstallments().forEach(inst -> {
            log.debug("Installment value: {} | Deadline: {} | Status: {}", inst.getValue(), inst.getDeadLine(), inst.getStatus());
        });

        verify(saleRepository).save(any());
    }

    @Test
    @DisplayName("Should create a sale with 10% interest and split total into 3 installments")
    void createSale_withInterest_threeInstallments() {
        BigDecimal gross = BigDecimal.valueOf(1000);
        BigDecimal interestRate = BigDecimal.valueOf(10);
        BigDecimal expectedInterest = gross.multiply(interestRate).divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);
        BigDecimal total = gross.add(expectedInterest);
        List<InstallmentRequestDto> installments = List.of(
                new InstallmentRequestDto(total.divide(BigDecimal.valueOf(3), 2, RoundingMode.HALF_UP), LocalDateTime.now().plusDays(1)),
                new InstallmentRequestDto(total.divide(BigDecimal.valueOf(3), 2, RoundingMode.HALF_UP), LocalDateTime.now().plusDays(2)),
                new InstallmentRequestDto(total.subtract(total.divide(BigDecimal.valueOf(3), 2, RoundingMode.HALF_UP).multiply(BigDecimal.valueOf(2))), LocalDateTime.now().plusDays(3))
        );
        CreateSaleRequestDto dto = new CreateSaleRequestDto(
                1L, gross, interestRate, total, "Sale with interest", installments
        );

        SaleEntity result = saleService.createSale(dto, userId);

        log.info("Created sale with interest. Gross: {}, Interest: {}, Total: {}", gross, interestRate, total);
        log.info("Installments created: {}", result.getInstallments().size());

        assertThat(result.getGrossAmount()).isEqualByComparingTo(gross);
        assertThat(result.getInterestRate()).isEqualByComparingTo(interestRate);
        assertThat(result.getTotalAmount()).isEqualByComparingTo(total);

        assertThat(result.getInstallments()).hasSize(3);
        BigDecimal sum = result.getInstallments().stream()
                .map(InstallmentEntity::getValue)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        assertThat(sum).isEqualByComparingTo(total);
    }
}
