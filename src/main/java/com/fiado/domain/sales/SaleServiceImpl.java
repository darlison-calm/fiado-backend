package com.fiado.domain.sales;

import com.fiado.domain.clients.ClientEntity;
import com.fiado.domain.clients.ClientService;
import com.fiado.domain.sales.dtos.InstallmentRequestDto;
import com.fiado.domain.sales.entities.InstallmentEntity;
import com.fiado.domain.sales.entities.SaleEntity;
import com.fiado.domain.sales.dtos.CreateSaleRequestDto;
import com.fiado.domain.sales.enums.InstallmentStatus;
import com.fiado.domain.sales.enums.SaleStatus;
import com.fiado.domain.sales.interfaces.SaleService;
import com.fiado.domain.sales.repositories.SaleRepository;
import com.fiado.domain.user.entities.UserEntity;
import com.fiado.domain.user.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SaleServiceImpl implements SaleService {

    private final UserService userService;
    private final ClientService clientService;
    private final SaleRepository saleRepository;

    @Override
    @Transactional
    public SaleEntity createSale(CreateSaleRequestDto saleDto, UUID userId) {
        UserEntity user = userService.getUser(userId);
        ClientEntity client = clientService.getClientEntityByIdForUser(saleDto.clientId(), userId);

        if (saleDto.totalAmount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("O valor da venda deve ser maior que zero");
        }

        if (saleDto.grossAmount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("O valor da venda deve ser maior que zero");
        }

        List<InstallmentRequestDto> installmentsList = saleDto.installments();
        validateInstallments(installmentsList);

        BigDecimal finalAmount = calculateFinalAmount(saleDto.grossAmount(), saleDto.interestRate());
        validateInstallmentsSum(installmentsList, finalAmount);
        SaleEntity sale = buildSaleEntity(saleDto, user, client, finalAmount);
        List<InstallmentEntity> installments = createInstallments(installmentsList, sale);
        sale.setInstallments(installments);
        return saleRepository.save(sale);
    }
    private List<InstallmentEntity> createInstallments(List<InstallmentRequestDto> installmentsList, SaleEntity sale) {
        LocalDateTime currentDate = LocalDateTime.now();
        return installmentsList.stream()
                .map(in -> createInstallment(in, sale, currentDate))
                .toList();
    }

    private SaleEntity buildSaleEntity(CreateSaleRequestDto saleDto, UserEntity user, ClientEntity client, BigDecimal totalAmount) {
        SaleEntity sale = new SaleEntity();
        sale.setUser(user);
        sale.setClient(client);
        sale.setGrossAmount(saleDto.grossAmount());
        sale.setStatus(SaleStatus.PENDING);
        sale.setInterestRate(saleDto.interestRate());
        sale.setDescription(saleDto.description());
        sale.setTotalAmount(totalAmount);
        return sale;
    }

    private InstallmentEntity createInstallment(InstallmentRequestDto dto, SaleEntity sale, LocalDateTime now) {
        InstallmentEntity installment = new InstallmentEntity();
        installment.setSale(sale);
        installment.setValue(dto.value());
        installment.setDeadLine(dto.deadline());
        installment.setAmountPaid(BigDecimal.ZERO);
        installment.setStatus(now.isAfter(dto.deadline()) ? InstallmentStatus.DELAYED : InstallmentStatus.PENDING);
        return installment;
    }

    private void validateInstallments(List<InstallmentRequestDto> installmentsDto) {
        // Verifica se a lista de parcelas existe
        List<InstallmentRequestDto> installments = Optional.ofNullable(installmentsDto)
                .orElseThrow(() -> new IllegalArgumentException("É necessário informar pelo menos uma parcela"));

        // Valida cada parcela individualmente
        for (int i = 0; i < installments.size(); i++) {
            InstallmentRequestDto installment = installments.get(i);
            validateInstallment(installment, i + 1);
        }
    }

    private void validateInstallment(InstallmentRequestDto installment, int installmentNumber) {
        if (installment == null) {
            throw new IllegalArgumentException("A parcela " + installmentNumber + " não pode ser nula");
        }

        // Validação do valor da parcela
        BigDecimal value = installment.value();
        if (value == null) {
            throw new IllegalArgumentException("O valor da parcela " + installmentNumber + " é obrigatório");
        }

        if (value.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("O valor da parcela " + installmentNumber + " deve ser maior que zero");
        }

        // Validação da data de vencimento
        LocalDateTime deadline = installment.deadline();
        if (deadline == null) {
            throw new IllegalArgumentException("A data de vencimento da parcela " + installmentNumber + " é obrigatória");
        }
    }

    private void validateInstallmentsSum(List<InstallmentRequestDto> installmentsList, BigDecimal expectedTotal) {
        BigDecimal installmentSum = installmentsList.stream()
                .map(InstallmentRequestDto::value)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        if (installmentSum.compareTo(expectedTotal) != 0) {
            throw new IllegalArgumentException(
                    "A soma das parcelas " + installmentSum + " deve ser igual ao valor total da venda " + expectedTotal);
        }
    }

    private BigDecimal calculateFinalAmount(BigDecimal grossAmount, BigDecimal interestRate) {
        BigDecimal finalAmount = grossAmount;

        if (interestRate != null && interestRate.compareTo(BigDecimal.ZERO) > 0) {
            BigDecimal interestValue = grossAmount.multiply(interestRate)
                    .divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);
            finalAmount = finalAmount.add(interestValue);
        }

        return finalAmount;
    }
}
