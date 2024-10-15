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
@Service
public class VoucherService {

    @Autowired
    private VoucherRepository voucherRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private AccountVoucherRepository accountVoucherRepository;

    private String generateVoucherCode() {
        int length = 6 + (int) (Math.random() * 3);
        return RandomStringUtils.randomAlphanumeric(length).toUpperCase();
    }

    public long countVouchers(Specification<Voucher> spec) {
        return voucherRepository.count(spec);
    }

    // Create Voucher Service
    public Voucher createVoucher(VoucherRequest voucherRequest, Long userId) {
        if (voucherRequest.getIsPrivate() && (voucherRequest.getAccountIds() == null || voucherRequest.getAccountIds().isEmpty())) {
            throw new IllegalArgumentException("For private vouchers, account IDs must be provided.");
        }

        Voucher voucher = convertVoucherRequestDTO(voucherRequest);
        voucher.setCodeVoucher(generateVoucherCode());
        Date currentDate = new Date();

        // Set the creator details
        Account creator = accountRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        voucher.setCreatedBy(creator.getName());


        // Set the voucher status based on start/end date
        updateVoucherStatus(voucher, currentDate);

        Voucher savedVoucher = voucherRepository.save(voucher);

        // Associate accounts with private vouchers
        if (voucher.getIsPrivate()) {
            saveAccountVouchers(savedVoucher, voucherRequest.getAccountIds());
        }

        return savedVoucher;
    }

    // Associate accounts with private vouchers
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
        // Fetch the voucher by ID
        Voucher voucher = voucherRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Voucher not found"));

        // Ensure the voucher is not expired
        if (voucher.getStatus().equals(Status.EXPIRED.toString())) {
            throw new RuntimeException("Cannot update an expired voucher.");
        }

        // Fetch the user who is updating the voucher
        Account updater = accountRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Preserve fields that should not be overwritten
        String[] ignoredProperties = {"id", "createdAt", "createdBy", "status", "codeVoucher"};
        BeanUtils.copyProperties(voucherRequest, voucher, ignoredProperties);
        voucher.setUpdatedBy(updater.getName());

        // Handle the change of voucher type
        if (!voucherRequest.getIsPrivate() && voucher.getIsPrivate()) {
            // Voucher type changed from private to public
            System.out.println("Changing voucher from private to public. Deleting account associations...");
            accountVoucherRepository.deleteByVoucherId(id); // Remove all account associations immediately
            voucher.setIsPrivate(false); // Set the voucher as public
            voucherRequest.setAccountIds(null); // Set accountIds to null since it's no longer private
        } else if (voucherRequest.getIsPrivate()) {
            // Voucher remains private, update the account associations
            accountVoucherRepository.deleteByVoucherId(id); // Remove old associations
            saveAccountVouchers(voucher, voucherRequest.getAccountIds()); // Save new associations
        }

        // Save the updated voucher details
        Voucher updatedVoucher = voucherRepository.save(voucher);

        return updatedVoucher;
    }


    // Get Voucher by ID with detailed response
    public VoucherResponse getVoucherById(Long id) {
        Voucher voucher = voucherRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Voucher not found"));

        // Restrict access to expired vouchers
        if ("EXPIRED".equals(voucher.getStatus())) {
            throw new RuntimeException("Cannot view expired voucher details.");
        }

        // Convert the voucher entity to a VoucherResponse DTO
        VoucherResponse voucherResponse = convertToVoucherResponse(voucher);

        // If the voucher is private, populate the accountIds
        if (voucher.getIsPrivate()) {
            List<Long> accountIds = accountVoucherRepository.findAccountIdsByVoucherId(id);
            voucherResponse.setAccountIds(accountIds);
        }

        return voucherResponse;
    }


    // Delete (expire) voucher
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

    // End voucher early
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

    // Reactivate voucher
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

    // Scheduled task to expire vouchers
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

    // Update voucher status based on start and end dates
    private void updateVoucherStatus(Voucher voucher, Date currentDate) {
        if (voucher.getStartAt().after(currentDate)) {
            voucher.setStatus(Status.UPCOMING.toString());
        } else if (voucher.getStartAt().before(currentDate) && voucher.getEndAt().after(currentDate)) {
            voucher.setStatus(Status.ONGOING.toString());
        } else {
            voucher.setStatus(Status.EXPIRED.toString());
        }
    }

    // Convert voucher to response DTO
    private VoucherResponse convertToVoucherResponse(Voucher voucher) {
        VoucherResponse response = new VoucherResponse();
        BeanUtils.copyProperties(voucher, response);
        return response;
    }

    // Convert request DTO to voucher entity
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
