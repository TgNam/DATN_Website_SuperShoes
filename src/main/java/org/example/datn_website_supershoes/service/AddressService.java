package org.example.datn_website_supershoes.service;

import org.example.datn_website_supershoes.Enum.Status;
import org.example.datn_website_supershoes.dto.request.AddressRequest;
import org.example.datn_website_supershoes.dto.response.AddressResponse;
import org.example.datn_website_supershoes.model.Account;
import org.example.datn_website_supershoes.model.Address;
import org.example.datn_website_supershoes.repository.AccountRepository;
import org.example.datn_website_supershoes.repository.AddressRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AddressService {

    @Autowired
    AddressRepository addressRepository;

    @Autowired
    AccountRepository accountRepository;
    public List<AddressResponse> listAddressResponseByidAccount(Long idAccount){
        return addressRepository.listAddressResponseByidAccount(idAccount);
    }
    public  Address findAddressById(Long idAddress){
        Address address = addressRepository.findById(idAddress)
                .orElseThrow(() -> new RuntimeException("Địa chỉ không tồn tại!"));
        return address;
    }
    // Thêm mới một đối tượng Address
    public AddressResponse createAddress(AddressRequest addressRequest) {
        // Bước 1: Lấy danh sách các địa chỉ hiện tại của tài khoản
        List<AddressResponse> existingAddresses = addressRepository.listAddressResponseByidAccount(addressRequest.getIdAccount());

        // Bước 2: Chuyển đổi AddressRequest thành đối tượng Address
        Address address = convertAddressRequesDTO(addressRequest);

        // Bước 3: Kiểm tra danh sách hiện có
        if (existingAddresses.isEmpty()) {
            // Nếu không có địa chỉ nào trong danh sách, set type = 1 cho địa chỉ mới
            address.setType(1);
        } else {
            // Nếu đã có địa chỉ, set type = 0 cho địa chỉ mới
            address.setType(0);
        }

        // Bước 4: Lưu địa chỉ mới vào cơ sở dữ liệu
        Address addressRe =addressRepository.save(address);
        AddressResponse addressResponse = AddressResponse.builder()
                .id(addressRe.getId())
                .codeCity(addressRe.getCodeCity())
                .codeDistrict(addressRe.getCodeDistrict())
                .codeWard(addressRe.getCodeWard())
                .address(addressRe.getAddress())
                .idAccount(addressRe.getAccount().getId())
                .type(addressRe.getType())
                .status(addressRe.getStatus())
                .build();
        return addressResponse;
    }

    public AddressResponse updateAddress(Long idAddress, AddressRequest addressRequest){
        Address address = addressRepository.findById(idAddress)
                .orElseThrow(() -> new RuntimeException("Địa chỉ không tồn tại!"));
        address.setAddress(addressRequest.getAddress());
        address.setCodeCity(addressRequest.getCodeCity());
        address.setCodeDistrict(addressRequest.getCodeDistrict());
        address.setCodeWard(addressRequest.getCodeWard());
        Address addressRe =addressRepository.save(address);
        AddressResponse addressResponse = AddressResponse.builder()
                .id(addressRe.getId())
                .codeCity(addressRe.getCodeCity())
                .codeDistrict(addressRe.getCodeDistrict())
                .codeWard(addressRe.getCodeWard())
                .address(addressRe.getAddress())
                .idAccount(addressRe.getAccount().getId())
                .type(addressRe.getType())
                .status(addressRe.getStatus())
                .build();
        return addressResponse;
    }

    // Phương thức cập nhật địa chỉ mặc định
    public AddressResponse updateAddressType(Long id) {
        // Bước 1: Lấy thông tin của đối tượng cần sửa (đối tượng A)
        Address addressA = addressRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Địa chỉ không tồn tại!"));

        // Bước 2: Kiểm tra xem đã có đối tượng nào khác với type = 1 trong cùng tài khoản (đối tượng B)
        List<AddressResponse> existingType1Addresses = addressRepository.listAddressResponseByidAccountAndType(addressA.getAccount().getId());

        // Nếu tồn tại đối tượng B (type = 1) và đối tượng A sẽ được cập nhật thành type = 1
        if (!existingType1Addresses.isEmpty() && addressA.getType() != 1) {
            // Bước 3: Lấy đối tượng B
            AddressResponse addressBResponse = existingType1Addresses.get(0);  // Giả sử chỉ có một đối tượng B (type = 1)
            Address addressB = addressRepository.findById(addressBResponse.getId())
                    .orElseThrow(() -> new RuntimeException("Lỗi sủa địa chỉ mặc định!"));

            // Bước 4: Sửa đối tượng B thành type = 0
            addressB.setType(0);
            addressRepository.save(addressB);
        }

        // Bước 5: Sửa đối tượng A thành type = 1
        addressA.setType(1);

        // Bước 6: Lưu lại đối tượng A đã sửa
        Address address = addressRepository.save(addressA);
                AddressResponse addressResponse = AddressResponse.builder()
                        .id(address.getId())
                        .codeCity(address.getCodeCity())
                        .codeDistrict(address.getCodeDistrict())
                        .codeWard(address.getCodeWard())
                        .address(address.getAddress())
                        .idAccount(address.getAccount().getId())
                        .type(address.getType())
                        .status(address.getStatus())
                        .build();
        return addressResponse;
    }



    public AddressResponse deleteAddress(Long id) {
        // Bước 1: Kiểm tra địa chỉ có tồn tại không
        Address address = addressRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Địa chỉ không tồn tại!"));

        // Bước 2: Xóa địa chỉ
        addressRepository.delete(address);

        // Bước 3: Kiểm tra xem địa chỉ đã được xóa chưa
        if (addressRepository.findById(id).isEmpty()) {
            // Bước 4: Lấy tất cả các địa chỉ còn lại theo tài khoản
            List<AddressResponse> remainingAddresses = addressRepository.listAddressResponseByidAccount(address.getAccount().getId());

            // Bước 5: Nếu còn địa chỉ và không có địa chỉ nào có type = 1, chuyển địa chỉ đầu tiên thành type = 1
            if (!remainingAddresses.isEmpty()) {
                boolean hasType1 = remainingAddresses.stream().anyMatch(addr -> addr.getType() == 1);

                // Nếu không có địa chỉ nào có type = 1
                if (!hasType1) {
                    AddressResponse firstAddress = remainingAddresses.get(0);

                    // Tìm lại đối tượng trong DB để chỉnh sửa
                    Address firstAddressEntity = addressRepository.findById(firstAddress.getId())
                            .orElseThrow(() -> new RuntimeException("Không tìm thấy địa chỉ đầu tiên!"));

                    // Chuyển type thành 1
                    firstAddressEntity.setType(1);

                    // Lưu thay đổi
                    addressRepository.save(firstAddressEntity);
                }
            }
        } else {
            throw new RuntimeException("Lỗi: Địa chỉ chưa được xóa!");
        }

        // Tạo response cho địa chỉ đã xóa
        AddressResponse addressResponse = AddressResponse.builder()
                .id(address.getId())
                .codeCity(address.getCodeCity())
                .codeDistrict(address.getCodeDistrict())
                .codeWard(address.getCodeWard())
                .address(address.getAddress())
                .idAccount(address.getAccount().getId())
                .type(address.getType())
                .status(address.getStatus())
                .build();
        return addressResponse;
    }



    public Address convertAddressRequesDTO(AddressRequest addressRequest){
        Account account = accountRepository.findById(addressRequest.getIdAccount()).get();
        Address address = Address.builder()
                .codeCity(addressRequest.getCodeCity())
                .codeDistrict(addressRequest.getCodeDistrict())
                .codeWard(addressRequest.getCodeWard())
                .address(addressRequest.getAddress())
                .account(account)
                .build();
        address.setStatus(Status.ACTIVE.toString());
        return address;
    }


}
