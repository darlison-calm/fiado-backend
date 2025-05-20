package com.fiado.domain.sales;
import com.fiado.domain.clients.ClientEntity;
import com.fiado.domain.clients.ClientService;
import com.fiado.domain.sales.dtos.InstallmentRequestDto;
import com.fiado.domain.sales.entities.InstallmentEntity;
import com.fiado.domain.sales.entities.SaleEntity;
import com.fiado.domain.sales.dtos.SaleCreateRequestDto;
import com.fiado.domain.sales.enums.IntallmentStatus;
import com.fiado.domain.sales.enums.SaleStatus;
import com.fiado.domain.sales.interfaces.SaleService;
import com.fiado.domain.sales.repositories.InstallmentRepository;
import com.fiado.domain.sales.repositories.SaleRepository;
import com.fiado.domain.user.entities.UserEntity;
import com.fiado.domain.user.services.UserService;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SaleServiceImpl implements SaleService {

    private final UserService userService;
    private final ClientService clientService;
    private final InstallmentRepository installmentRepository;
    private final SaleRepository saleRepository;

    @Override
    @Transactional
    public SaleEntity createSale(SaleCreateRequestDto saleDto, UUID userId) {
        BigDecimal grossAmount = saleDto.grossAmount();
        if (grossAmount == null || grossAmount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("O valor da venda deve ser maior que zero");
        }
        List<InstallmentEntity> installments = new ArrayList<>();

        List<InstallmentRequestDto> installmentsList =
                Optional.ofNullable(saleDto.installments())
                .orElseThrow(IllegalArgumentException::new);
        BigDecimal installmentSum = installmentsList.stream()
                .map(InstallmentRequestDto::value)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        if (installmentSum.compareTo(saleDto.totalAmount()) != 0) {
            throw new IllegalArgumentException("A soma das parcelas deve ser igual ao valor total da venda");
        }
        UserEntity user = userService.getUser(userId);
       ClientEntity client = clientService.getClientEntityByIdForUser(saleDto.clientId(), userId);
       SaleEntity sale = new SaleEntity();
       sale.setUser(user);
       sale.setClient(client);
       sale.setGrossAmount(saleDto.grossAmount());
       sale.setStatus(SaleStatus.PENDING);
       BigDecimal interestRate = saleDto.interestRate();
       sale.setInterestRate(interestRate);
       BigDecimal finalAmount = grossAmount;
       if (interestRate != null && interestRate.compareTo(BigDecimal.ZERO) > 0) {
           BigDecimal interestValue = grossAmount.multiply(interestRate).divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);
            finalAmount = finalAmount.add(interestValue);
       }
       sale.setTotalAmount(finalAmount);
       LocalDateTime currentDate = LocalDateTime.now();
        for (InstallmentRequestDto installmentDto: installmentsList) {
           InstallmentEntity installment = createSaleInstallment(installmentDto, sale, currentDate);
           installments.add(installment);
       };
       sale.setInstallments(installments);
       return saleRepository.save(sale);
    }

    @NotNull
    private static InstallmentEntity createSaleInstallment(InstallmentRequestDto installmentDto, SaleEntity sale, LocalDateTime currentDate) {
        InstallmentEntity installment = new InstallmentEntity();
        installment.setSale(sale);
        installment.setValue(installmentDto.value());
        LocalDateTime installmentDeadline = installmentDto.deadline();
        installment.setDeadLine(installmentDeadline);
        installment.setAmountPaid(BigDecimal.ZERO);
        if (currentDate.isAfter(installmentDeadline)){
            installment.setStatus(IntallmentStatus.DELAYED);
        } else {
            installment.setStatus(IntallmentStatus.PENDING);
        }
        return installment;
    }
}
