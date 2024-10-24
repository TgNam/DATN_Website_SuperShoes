package org.example.datn_website_supershoes.service;

import jakarta.transaction.Transactional;
import org.apache.commons.lang3.RandomStringUtils;
import org.example.datn_website_supershoes.Enum.Status;
import org.example.datn_website_supershoes.dto.request.VoucherRequest;
import org.example.datn_website_supershoes.dto.response.VoucherResponse;
import org.example.datn_website_supershoes.model.Account;
import org.example.datn_website_supershoes.model.AccountVoucher;
import org.example.datn_website_supershoes.model.Voucher;
import org.example.datn_website_supershoes.repository.AccountRepository;
import org.example.datn_website_supershoes.repository.AccountVoucherRepository;
import org.example.datn_website_supershoes.repository.VoucherRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class VoucherService {

    @Autowired
    private VoucherRepository voucherRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private AccountVoucherRepository accountVoucherRepository;

    private static final Logger logger = LoggerFactory.getLogger(VoucherService.class);

    private String generateVoucherCode() {
        int length = 6 + (int) (Math.random() * 3);
        return RandomStringUtils.randomAlphanumeric(length).toUpperCase();
    }

    @Transactional
    @Scheduled(cron = "0 * * * * *")  // Runs every minute to check for status updates
    public void updateVoucherStatuses() {
        List<Voucher> vouchers = voucherRepository.findAll();
        Date now = new Date();

        for (Voucher voucher : vouchers) {
            String oldStatus = voucher.getStatus();

            if (voucher.getStartAt().after(now)) {
                voucher.setStatus(Status.UPCOMING.toString());
            } else if (voucher.getStartAt().before(now) && voucher.getEndAt().after(now)) {
                voucher.setStatus(Status.ONGOING.toString());
            } else if (voucher.getEndAt().before(now)) {
                voucher.setStatus(Status.EXPIRED.toString());
            }

            if (!oldStatus.equals(voucher.getStatus())) {
                logger.info("Voucher {} status changed from {} to {}", voucher.getCodeVoucher(), oldStatus, voucher.getStatus());
            }

            voucherRepository.save(voucher);
        }
    }



    public long countVouchers(Specification<Voucher> spec) {
        return voucherRepository.count(spec);
    }


    public Voucher createVoucher(VoucherRequest voucherRequest, Long userId) {
        if (voucherRequest.getIsPrivate() && (voucherRequest.getAccountIds() == null || voucherRequest.getAccountIds().isEmpty())) {
            throw new IllegalArgumentException("For private vouchers, account IDs must be provided.");
        }

        Voucher voucher = convertVoucherRequestDTO(voucherRequest);
        voucher.setCodeVoucher(generateVoucherCode());
        Date currentDate = new Date();


        Account creator = accountRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        voucher.setCreatedBy(creator.getName());


        updateVoucherStatus(voucher, currentDate);

        Voucher savedVoucher = voucherRepository.save(voucher);


        if (voucher.getIsPrivate()) {
            saveAccountVouchers(savedVoucher, voucherRequest.getAccountIds());
        }

        return savedVoucher;
    }


    private void saveAccountVouchers(Voucher voucher, List<Long> accountIds) {
        accountIds.forEach(accountId -> {
            Account account = accountRepository.findById(accountId)
                    .orElseThrow(() -> new RuntimeException("Account not found: " + accountId));
            AccountVoucher accountVoucher = new AccountVoucher();
            accountVoucher.setVoucher(voucher);
            accountVoucher.setAccount(account);
            accountVoucherRepository.save(accountVoucher);
        });
    }

    @Transactional
    public Voucher updateVoucher(Long id, VoucherRequest voucherRequest, Long userId) {

        Voucher voucher = voucherRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Voucher not found"));


        if (voucher.getStatus().equals(Status.EXPIRED.toString())) {
            throw new RuntimeException("Cannot update an expired voucher.");
        }


        Account updater = accountRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));


        String[] ignoredProperties = {"id", "createdAt", "createdBy", "status", "codeVoucher"};
        BeanUtils.copyProperties(voucherRequest, voucher, ignoredProperties);
        voucher.setUpdatedBy(updater.getName());


        if (!voucherRequest.getIsPrivate() && voucher.getIsPrivate()) {

            System.out.println("Changing voucher from private to public. Deleting account associations...");
            accountVoucherRepository.deleteByVoucherId(id);
            voucher.setIsPrivate(false);
            voucherRequest.setAccountIds(null);
        } else if (voucherRequest.getIsPrivate()) {

            accountVoucherRepository.deleteByVoucherId(id);
            saveAccountVouchers(voucher, voucherRequest.getAccountIds());
        }


        Voucher updatedVoucher = voucherRepository.save(voucher);

        return updatedVoucher;
    }

    public VoucherResponse getVoucherById(Long id) {
        Voucher voucher = voucherRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Voucher not found"));
//        if ("EXPIRED".equals(voucher.getStatus())) {
//            throw new RuntimeException("Cannot view expired voucher details.");
//        }
        VoucherResponse voucherResponse = convertToVoucherResponse(voucher);
        if (voucher.getIsPrivate()) {
            List<Long> accountIds = accountVoucherRepository.findAccountIdsByVoucherId(id);
            voucherResponse.setAccountIds(accountIds);
        }
        return voucherResponse;
    }

    public Voucher deleteVoucher(Long id) {
        Voucher voucher = voucherRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Voucher not found"));

        if (Status.EXPIRED.toString().equals(voucher.getStatus())) {
            throw new RuntimeException("Cannot delete an expired voucher.");
        }

        voucher.setStatus(Status.EXPIRED.toString());
        accountVoucherRepository.updateStatusByVoucherId(id, "INACTIVE");
        return voucherRepository.save(voucher);
    }

    public Voucher endVoucherEarly(Long id, Long userId) {
        Voucher voucher = voucherRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Voucher not found"));

        Account updater = accountRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        voucher.setStatus(Status.ENDED_EARLY.toString());
        voucher.setUpdatedBy(updater.getName());
        accountVoucherRepository.updateStatusByVoucherId(id, "INACTIVE");
        return voucherRepository.save(voucher);
    }


    public Voucher reactivateVoucher(Long id, Long userId) {
        Voucher voucher = voucherRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Voucher not found"));

        if (!Status.ENDED_EARLY.toString().equals(voucher.getStatus())) {
            throw new RuntimeException("Only vouchers with 'Ended Early' status can be reactivated.");
        }

        Account updater = accountRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Date currentDate = new Date();
        updateVoucherStatus(voucher, currentDate);
        voucher.setUpdatedBy(updater.getName());
        accountVoucherRepository.updateStatusByVoucherId(id, "ACTIVE");

        return voucherRepository.save(voucher);
    }


    @Scheduled(cron = "0 0 0 * * *")
    public void checkAndExpireVouchers() {
        List<Voucher> vouchers = voucherRepository.findAll();
        Date currentDate = new Date();

        vouchers.forEach(voucher -> {
            if (voucher.getEndAt().before(currentDate) && !Status.EXPIRED.toString().equals(voucher.getStatus())) {
                voucher.setStatus(Status.EXPIRED.toString());
                voucherRepository.save(voucher);
            }
        });
    }


    private void updateVoucherStatus(Voucher voucher, Date currentDate) {
        if (voucher.getStartAt().after(currentDate)) {
            voucher.setStatus(Status.UPCOMING.toString());
        } else if (voucher.getStartAt().before(currentDate) && voucher.getEndAt().after(currentDate)) {
            voucher.setStatus(Status.ONGOING.toString());
        } else {
            voucher.setStatus(Status.EXPIRED.toString());
        }
    }


    private VoucherResponse convertToVoucherResponse(Voucher voucher) {
        VoucherResponse response = new VoucherResponse();
        BeanUtils.copyProperties(voucher, response);
        return response;
    }


    private Voucher convertVoucherRequestDTO(VoucherRequest voucherRequest) {
        return Voucher.builder()
                .codeVoucher(voucherRequest.getCodeVoucher())
                .name(voucherRequest.getName())
                .note(voucherRequest.getNote())
                .value(voucherRequest.getValue())
                .quantity(voucherRequest.getQuantity())
                .maximumDiscount(voucherRequest.getMaximumDiscount())
                .type(voucherRequest.getType())
                .minBillValue(voucherRequest.getMinBillValue())
                .isPrivate(voucherRequest.getIsPrivate())
                .startAt(voucherRequest.getStartAt())
                .endAt(voucherRequest.getEndAt())
                .build();
    }

    public Page<VoucherResponse> getVouchers(Specification<Voucher> spec, Pageable pageable) {
        return voucherRepository.findAll(spec, pageable).map(this::convertToVoucherResponse);
    }
}
