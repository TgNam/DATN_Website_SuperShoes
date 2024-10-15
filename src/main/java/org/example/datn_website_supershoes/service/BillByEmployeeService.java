package org.example.datn_website_supershoes.service;

import org.example.datn_website_supershoes.Enum.Status;
import org.example.datn_website_supershoes.dto.request.BillRequest;
import org.example.datn_website_supershoes.dto.request.SizeRequest;
import org.example.datn_website_supershoes.model.Account;
import org.example.datn_website_supershoes.model.Bill;
import org.example.datn_website_supershoes.model.BillDetail;
import org.example.datn_website_supershoes.model.Size;
import org.example.datn_website_supershoes.repository.AccountRepository;
import org.example.datn_website_supershoes.repository.BillByEmployeeRepository;
import org.example.datn_website_supershoes.repository.BillDetailByEmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class BillByEmployeeService {

    @Autowired
    BillByEmployeeRepository billByEmployeeRepository;

    @Autowired
    AccountRepository accountRepository;
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
        if(allBills.size()>MAX_DISPLAY_BILLS){
            displayBills = new ArrayList<>(allBills.subList(allBills.size() - 5,allBills.size()));
            waitingBills = allBills.subList(0,allBills.size() - 5);
        }else{
            displayBills = new ArrayList<>(allBills.subList(0,Math.min(MAX_DISPLAY_BILLS,allBills.size())));
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
}
