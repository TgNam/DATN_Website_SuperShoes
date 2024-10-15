package org.example.datn_website_supershoes.controller.REST;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Test3 {
    public static void main(String[] args) {
        List<Integer> allBills = new ArrayList<>(Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10));
        List<Integer> displayBills = new ArrayList<>(Arrays.asList(3, 2, 5, 1, 7));
        List<Integer> newDataList = new ArrayList<>(Arrays.asList(11,12));
        List<Integer> remo = new ArrayList<>(displayBills.subList(0, newDataList.size()));
        displayBills.addAll(newDataList);
        displayBills.removeAll(remo);
        List<Integer> waitingBills = allBills.stream()
                .filter(number -> !displayBills.contains(number)) // Chỉ giữ lại các phần tử không có trong resultList
                .collect(Collectors.toList());
        System.out.println("Danh sách kết quả: " + displayBills); // Kết quả: [2, 5, 1, 7, 11]
        System.out.println("Các phần tử còn lại: " + waitingBills); // Kết quả: [4, 6, 8, 9, 10, 3]
    }
}

