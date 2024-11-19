package org.example.datn_website_supershoes.service;

import org.example.datn_website_supershoes.Enum.Status;
import org.example.datn_website_supershoes.dto.request.PayBillRequest;
import org.example.datn_website_supershoes.dto.response.BillResponse;
import org.example.datn_website_supershoes.dto.response.PayBillOrderResponse;
import org.example.datn_website_supershoes.model.*;
import org.example.datn_website_supershoes.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class BillByEmployeeService {

    @Autowired
    private BillByEmployeeRepository billByEmployeeRepository;
    @Autowired
    private BillRepository billRepository;
    @Autowired
    private BillDetailRepository billDetailRepository;
    @Autowired
    private VoucherRepository voucherRepository;
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private AccountVoucherRepository accountVoucherRepository;
    @Autowired
    private PayBillRepository payBillRepository;
    @Autowired
    private PayBillService PayBillService;
    @Autowired
    private CartService cartService;
    @Autowired
    private CartRepository cartRepository;
    @Autowired
    private ProductDetailRepository productDetailRepository;
    @Autowired
    private CartDetailRepository cartDetailRepository;
    //Lấy tối đa 5 phần tử
    private static final int MAX_DISPLAY_BILLS = 5;

    //Lấy danh sách hóa đơn hiển thị và hóa đơn chờ
    public Map<String, List<String>> getDisplayAndWaitingBills() {
        Long idEmployees = 2L;
        //Lấy tất cả hóa đơn
        List<String> allBills = billByEmployeeRepository.findCodeBillWaitingForPayment(idEmployees, Status.WAITING_FOR_PAYMENT.toString());
        Map<String, List<String>> response = new HashMap<>();
        //Hóa đơn hiển thị
        List<String> displayBills;
        //Hóa đơn chờ
        List<String> waitingBills = new ArrayList<>();
        //Nếu allBills lớn hơn 5 phần tử
        if (allBills.size() > MAX_DISPLAY_BILLS) {
            displayBills = new ArrayList<>(allBills.subList(allBills.size() - 5, allBills.size()));
            waitingBills = allBills.subList(0, allBills.size() - 5);
        } else {
            displayBills = new ArrayList<>(allBills.subList(0, Math.min(MAX_DISPLAY_BILLS, allBills.size())));
        }
        response.put("displayBills", displayBills);
        response.put("waitingBills", waitingBills);

        return response;
    }

    public Map<String, List<String>> sortDisplayBills(List<String> displayBills, List<String> selectills) {
        // Khởi tạo map phản hồi
        Map<String, List<String>> response = new HashMap<>();

        Long idEmployees = 2L; // ID của nhân viên (có thể thay đổi nếu cần)

        // Lấy danh sách tất cả hóa đơn đang chờ thanh toán
        List<String> allBills = billByEmployeeRepository.findCodeBillWaitingForPayment(idEmployees, Status.WAITING_FOR_PAYMENT.toString());

        // Kiểm tra xem số lượng bill trong selectills có vượt quá 5 không
        if (selectills != null && selectills.size() > 5) {
            throw new RuntimeException("Số lượng hóa đơn chọn hiển thị vượt quá 5 hóa đơn");
        }

        // Kiểm tra nếu bất kỳ hóa đơn nào trong selectills đã nằm trong displayBills
        if (selectills != null && displayBills != null) {
            List<String> duplicateInDisplay = selectills.stream()
                    .filter(displayBills::contains) // Lọc các hóa đơn đã có trong displayBills
                    .collect(Collectors.toList());

            // Nếu có bất kỳ hóa đơn nào trong selectills đã nằm trong displayBills, ném ngoại lệ
            if (!duplicateInDisplay.isEmpty()) {
                throw new RuntimeException("Các hóa đơn sau đã nằm trong hóa đơn hiển thị: " + duplicateInDisplay);
            }
        }

        // Kiểm tra xem các hóa đơn trong selectills có tồn tại trong allBills hay không
        if (selectills != null) {
            List<String> missingInAllBills = selectills.stream()
                    .filter(billCode -> !allBills.contains(billCode)) // Lọc các hóa đơn không tồn tại trong allBills
                    .collect(Collectors.toList());

            // Nếu có hóa đơn nào không tồn tại trong allBills, ném ngoại lệ
            if (!missingInAllBills.isEmpty()) {
                throw new RuntimeException("Các hóa đơn sau không tồn tại trong hệ thống: " + missingInAllBills);
            }
        }

        // Kiểm tra các hóa đơn bị trùng lặp trong selectills
        if (selectills != null) {
            Set<String> uniqueSelectBills = new HashSet<>();
            List<String> duplicateInSelectills = selectills.stream()
                    .filter(billCode -> !uniqueSelectBills.add(billCode)) // Nếu hóa đơn đã tồn tại trong uniqueSelectBills thì thêm vào duplicateInSelectills
                    .collect(Collectors.toList());

            // Nếu có bất kỳ hóa đơn nào bị trùng lặp trong selectills, ném ngoại lệ
            if (!duplicateInSelectills.isEmpty()) {
                throw new RuntimeException("Các hóa đơn sau bị trùng lặp trong danh sách chọn: " + duplicateInSelectills);
            }
        }

        // Kiểm tra xem hóa đơn nào trong displayBills không tồn tại trong allBills
        if (displayBills != null) {
            List<String> missingInDisplayBills = displayBills.stream()
                    .filter(billCode -> !allBills.contains(billCode)) // Lọc ra các hóa đơn không tồn tại trong allBills
                    .collect(Collectors.toList());

            // Nếu có bất kỳ hóa đơn nào không tồn tại trong allBills, ném ngoại lệ
            if (!missingInDisplayBills.isEmpty()) {
                throw new RuntimeException("Các hóa đơn sau không tồn tại trong hệ thống: " + missingInDisplayBills);
            }
        }

        // Kiểm tra các hóa đơn bị trùng lặp trong displayBills
        if (displayBills != null) {
            Set<String> uniqueDisplayBills = new HashSet<>();
            List<String> duplicateInDisplayBills = displayBills.stream()
                    .filter(billCode -> !uniqueDisplayBills.add(billCode)) // Nếu hóa đơn đã tồn tại trong uniqueDisplayBills thì thêm vào duplicateInDisplayBills
                    .collect(Collectors.toList());

            // Nếu có bất kỳ hóa đơn nào bị trùng lặp trong displayBills, ném ngoại lệ
            if (!duplicateInDisplayBills.isEmpty()) {
                throw new RuntimeException("Các hóa đơn sau bị trùng lặp trong danh sách hiển thị: " + duplicateInDisplayBills);
            }
        }

        // Khởi tạo danh sách hóa đơn chờ
        List<String> waitingBills;

        // Nếu số hóa đơn lớn hơn hoặc bằng MAX_DISPLAY_BILLS
        if (allBills.size() >= MAX_DISPLAY_BILLS) {
            // Kiểm tra xem danh sách displayBills có hợp lệ hay không
            if (selectills != null && displayBills != null && displayBills.size() == MAX_DISPLAY_BILLS) {
                // Thêm mã hóa đơn mới vào danh sách displayBills
                displayBills.addAll(selectills);
                // Lấy danh sách hóa đơn cần xóa
                List<String> removeBills = new ArrayList<>(displayBills.subList(0, selectills.size()));
                // Xóa hóa đơn trong danh sách displayBills (FIFO)
                displayBills.removeAll(removeBills);
            } else {
                // Nếu displayBills không hợp lệ, chỉ cần giữ lại hóa đơn đã có
                displayBills = new ArrayList<>(allBills.subList(Math.max(0, allBills.size() - MAX_DISPLAY_BILLS), allBills.size()));
            }

            // Cập nhật danh sách waitingBills bằng cách loại bỏ các hóa đơn đã hiển thị
            List<String> finalDisplayBills = displayBills;
            waitingBills = allBills.stream()
                    .filter(number -> !finalDisplayBills.contains(number)) // Chỉ giữ lại các phần tử không có trong displayBills
                    .collect(Collectors.toList());
        } else {
            // Nếu không đủ hóa đơn, hiển thị toàn bộ hóa đơn hiện có
            displayBills = new ArrayList<>(allBills);
            waitingBills = new ArrayList<>();
        }

        response.put("displayBills", displayBills);
        response.put("waitingBills", waitingBills);

        return response;
    }

    public Map<String, List<String>> createBillByEmployee(List<String> displayBills) {
        // Tạo hóa đơn mới và lưu vào cơ sở dữ liệu
        Bill bill = billByEmployeeRepository.save(convertBill());

        // Khởi tạo map phản hồi
        Map<String, List<String>> response = new HashMap<>();

        Long idEmployees = 2L; // ID của nhân viên (có thể thay đổi nếu cần)

        // Lấy danh sách tất cả hóa đơn đang chờ thanh toán
        List<String> allBills = billByEmployeeRepository.findCodeBillWaitingForPayment(idEmployees, Status.WAITING_FOR_PAYMENT.toString());

        // Kiểm tra xem hóa đơn nào trong displayBills không tồn tại trong allBills
        if (displayBills != null) {
            // Kiểm tra các hóa đơn bị trùng lặp trong displayBills
            Set<String> uniqueBills = new HashSet<>();
            List<String> duplicateBills = displayBills.stream()
                    .filter(billCode -> !uniqueBills.add(billCode)) // Nếu hóa đơn đã tồn tại trong uniqueBills thì thêm vào duplicateBills
                    .collect(Collectors.toList());

            // Nếu có bất kỳ hóa đơn nào bị trùng, ném ngoại lệ
            if (!duplicateBills.isEmpty()) {
                throw new RuntimeException("Các hóa đơn sau bị trùng lặp: " + duplicateBills);
            }

            List<String> missingBills = displayBills.stream()
                    .filter(billCode -> !allBills.contains(billCode)) // Lọc ra các hóa đơn không tồn tại trong allBills
                    .collect(Collectors.toList());

            // Nếu có bất kỳ hóa đơn nào không tồn tại, ném ngoại lệ
            if (!missingBills.isEmpty()) {
                throw new RuntimeException("Các hóa đơn sau không tồn tại: " + missingBills);
            }
        }

        // Khởi tạo danh sách hóa đơn chờ
        List<String> waitingBills;

        // Nếu số hóa đơn lớn hơn hoặc bằng MAX_DISPLAY_BILLS
        if (allBills.size() >= MAX_DISPLAY_BILLS) {
            // Kiểm tra xem danh sách displayBills có hợp lệ hay không
            if (displayBills != null && displayBills.size() == MAX_DISPLAY_BILLS) {
                // Thêm mã hóa đơn mới vào danh sách displayBills
                displayBills.add(bill.getCodeBill());
                // Xóa hóa đơn đầu tiên trong danh sách displayBills (FIFO)
                displayBills.remove(0);
            } else {
                // Nếu displayBills không hợp lệ, chỉ cần giữ lại hóa đơn đã có
                displayBills = new ArrayList<>(allBills.subList(Math.max(0, allBills.size() - MAX_DISPLAY_BILLS), allBills.size()));
            }

            // Cập nhật danh sách waitingBills bằng cách loại bỏ các hóa đơn đã hiển thị
            List<String> finalDisplayBills = displayBills;
            waitingBills = allBills.stream()
                    .filter(number -> !finalDisplayBills.contains(number)) // Chỉ giữ lại các phần tử không có trong displayBills
                    .collect(Collectors.toList());
        } else {
            // Nếu không đủ hóa đơn, hiển thị toàn bộ hóa đơn hiện có
            displayBills = new ArrayList<>(allBills);
            waitingBills = new ArrayList<>();
        }

        // Cập nhật phản hồi
        response.put("displayBills", displayBills);
        response.put("waitingBills", waitingBills);

        return response;
    }

    public BillResponse findBillResponseByCodeBill(String codeBill) {
        Optional<BillResponse> billResponse = billRepository.findBillResponseByCodeBill(codeBill);
        if (!billResponse.isPresent()) {
            throw new RuntimeException("Hóa đơn không còn tồn tại");
        }
        return billResponse.get();
    }

    public static String generateRandomCode() {
        Random random = new Random();
        // Tạo một số ngẫu nhiên có 6 chữ số
        int randomNumber = 100000 + random.nextInt(900000); // Số ngẫu nhiên từ 100000 đến 999999
        // Kết hợp với tiền tố HD
        return "HD" + randomNumber;
    }

    public Bill convertBill() {
        //get lấy id của người dùng từ đăng nhập
        // vd lấy id của người đăng nhập đó bằng 2
        Long id = 2L;
        Account employee = accountRepository.findById(id).get();
        UUID uuid = UUID.randomUUID();
        String generatedCode = this.generateRandomCode() + "-" + uuid;
        Bill bill = Bill.builder()
                .employees(employee)
                .codeBill(generatedCode)
                .type(2)
                .build();
        bill.setCreatedBy(employee.getName());
        bill.setUpdatedBy(employee.getName());
        bill.setStatus(Status.WAITING_FOR_PAYMENT.toString());
        return bill;
    }

    public void payBillByEmployee(
            String codeBill,
            boolean delivery,
            boolean postpaid,
            String codeVoucher,
            Long idAccount,
            String name,
            String phoneNumber,
            String address,
            String note
    ) {
        try {
            Optional<Bill> billOptional = billRepository.findByCodeBill(codeBill);
            if (billOptional.isEmpty()) {
                throw new RuntimeException("Hóa đơn " + codeBill + " không tồn tại");
            }

            Bill bill = billOptional.get(); // Lấy giá trị bill để sử dụng sau này

            boolean checkVoucher = false;
            boolean checkAccountVoucher = false;
            Voucher voucher = null;
            AccountVoucher accountVoucher = null;

            if (delivery) {
                if (name == null || name.isBlank()) {
                    throw new RuntimeException("Không được để trống tên người nhận");
                } else if (phoneNumber == null || phoneNumber.isBlank()) {
                    throw new RuntimeException("Không được để trống số điện thoại người nhận");
                } else if (address == null || address.isBlank()) {
                    throw new RuntimeException("Không được để trống địa chỉ nhận hàng");
                }
                if (note != null && !note.isBlank()) {
                    bill.setNote(note);
                }
                bill.setNameCustomer(name);
                bill.setPhoneNumber(phoneNumber);
                bill.setAddress(address);
            }

            if (idAccount != null) {
                Optional<Account> accountOptional = accountRepository.findById(idAccount);
                if (accountOptional.isEmpty()) {
                    throw new RuntimeException("Tài khoản với id là " + idAccount + " không tồn tại");
                }
                bill.setCustomer(accountOptional.get());
            }

            List<BillDetail> listBillDetail = billDetailRepository.findByIdBill(bill.getId());
            if (listBillDetail == null || listBillDetail.isEmpty()) {
                throw new RuntimeException("Chưa có sản phẩm nào trong giỏ hàng");
            }

            BigDecimal totalMerchandise = listBillDetail.stream()
                    .map(billDetail -> billDetail.getPriceDiscount()
                            .multiply(BigDecimal.valueOf(billDetail.getQuantity()))
                            .setScale(2, RoundingMode.HALF_UP))
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            bill.setTotalMerchandise(totalMerchandise.setScale(2, RoundingMode.HALF_UP));

            BigDecimal priceDiscount = BigDecimal.ZERO;
            BigDecimal totalAmount = totalMerchandise;

            if (codeVoucher != null && !codeVoucher.isBlank()) {
                Optional<Voucher> voucherOptional = voucherRepository.findByCodeVoucher(codeVoucher);
                if (voucherOptional.isPresent()) {
                    voucher = voucherOptional.get();

                    if (voucher.getQuantity() <= 0) {
                        throw new RuntimeException("Đã hết phiếu giảm giá");
                    }
                    if (totalMerchandise.compareTo(voucher.getMinBillValue()) < 0) {
                        throw new RuntimeException("Hóa đơn không đủ điều kiện sử dụng phiếu giảm giá");
                    }

                    BigDecimal priceSale = totalMerchandise.multiply(BigDecimal.valueOf(voucher.getValue() / 100.0))
                            .setScale(2, RoundingMode.HALF_UP);
                    BigDecimal maximumDiscount = voucher.getMaximumDiscount().max(BigDecimal.ZERO);

                    priceDiscount = priceSale.compareTo(maximumDiscount) <= 0 ? priceSale : maximumDiscount;
                    totalAmount = totalMerchandise.subtract(priceDiscount);

                    if (voucher.getIsPrivate()) {
                        if (idAccount == null) {
                            throw new RuntimeException("Bạn không đủ điều kiện sử dụng voucher: " + voucher.getCodeVoucher());
                        }
                        Optional<AccountVoucher> accountVoucherOptional = accountVoucherRepository
                                .findAccountVoucherByIdAccountAndidVoucher(idAccount, voucher.getId());
                        if (accountVoucherOptional.isEmpty()) {
                            throw new RuntimeException("Bạn không đủ điều kiện sử dụng voucher: " + voucher.getCodeVoucher());
                        }
                        accountVoucher = accountVoucherOptional.get();
                        accountVoucher.setStatus(Status.INACTIVE.toString());
                        checkAccountVoucher = true;
                    }

                    voucher.setQuantity(voucher.getQuantity() - 1);
                    if (voucher.getQuantity() == 0) {
                        voucher.setStatus(Status.FINISHED.toString());
                    }
                    checkVoucher = true;
                    bill.setVoucher(voucher);
                }
            }

            bill.setPriceDiscount(priceDiscount);
            bill.setTotalAmount(totalAmount.setScale(2, RoundingMode.HALF_UP));

            BigDecimal totalPaid = payBillRepository.findByCodeBill(codeBill).stream()
                    .map(PayBillOrderResponse::getAmount)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            if (postpaid && !delivery) {
                throw new RuntimeException("Chức năng trả sau chỉ áp dụng khi giao hàng");
            } else if (postpaid) {
                PayBillRequest payBillRequest = PayBillRequest.builder()
                        .amount(totalAmount.subtract(totalPaid))
                        .codeBill(codeBill)
                        .type(1)
                        .build();
                PayBillService.createPayBill(payBillRequest, 2, Status.WAITING_FOR_PAYMENT.toString());
                bill.setStatus(Status.SHIPPED.toString());
            } else if (totalPaid.compareTo(totalAmount) < 0) {
                throw new RuntimeException("Vui lòng thanh toán đủ số tiền trước khi thanh toán hóa đơn");
            } else {
                bill.setStatus(delivery ? Status.SHIPPED.toString() : Status.COMPLETED.toString());
            }

            if (checkVoucher) {
                voucherRepository.save(voucher);
            }
            if (checkAccountVoucher) {
                accountVoucherRepository.save(accountVoucher);
            }
            billRepository.save(bill);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    @Transactional
    public void payBillOnline(
            String codeVoucher,
            Long idAccount,
            String name,
            String phoneNumber,
            String address,
            String note
    ) {
        UUID uuid = UUID.randomUUID();
        String generatedCode = this.generateRandomCode() + "-" + uuid;

        Account account = accountRepository.findById(idAccount)
                .orElseThrow(() -> new RuntimeException("Account not found!"));

        Cart cart = cartService.getCartByAccountId(idAccount);
        List<CartDetail> cartDetails = cart.getCartDetails();

        Voucher voucher = voucherRepository.findByCodeVoucher(codeVoucher)
                .orElse(null);

        Bill bill = Bill.builder()
                .customer(account)
                .codeBill(generatedCode)
                .nameCustomer(name)
                .phoneNumber(phoneNumber)
                .address(address)
                .note(note)
                .type(1)
                .build();

        bill.setCreatedBy(account.getName());
        bill.setUpdatedBy(account.getName());
        bill.setStatus(Status.PENDING.toString());

        if (voucher != null && voucher.getIsPrivate()) {
            bill.setVoucher(voucher);
        }

        // Lưu Bill trước
        billRepository.save(bill);

        try {
            for (CartDetail detail : cartDetails) {
                Optional<ProductDetail> productDetailOptional = productDetailRepository
                        .findByIdAndAndStatus(detail.getProductDetail().getId(), Status.ACTIVE.toString());

                if (productDetailOptional.isEmpty()) {
                    throw new RuntimeException("Id " + detail.getProductDetail().getId() + " không tồn tại hoặc trạng thái không hợp lệ.");
                }

                ProductDetail productDetail = productDetailOptional.get();

                BillDetail billDetail = new BillDetail();
                billDetail.setProductDetail(productDetail);
                billDetail.setBill(bill);
                billDetail.setQuantity(detail.getQuantity());
                billDetail.setStatus(Status.PENDING.toString());
                billDetail.setPriceDiscount(productDetail.getPrice());

                // Lưu từng BillDetail
                billDetailRepository.save(billDetail);
            }

            // Xóa tất cả CartDetail liên quan sau khi xử lý thành công
            cartRepository.delete(cart);


        } catch (RuntimeException e) {
            // Rollback transaction nếu có lỗi
            throw e;
        }
    }


}
