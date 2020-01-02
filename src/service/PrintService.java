package service;

import model.Account;

import java.util.List;

public class PrintService {
    public PrintService() {

    }

    public void printReport(List<Account> accountList) {
        accountList.stream().forEach( a -> System.out.println(a));
    }
}
