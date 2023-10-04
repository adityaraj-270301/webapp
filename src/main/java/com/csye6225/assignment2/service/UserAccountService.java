package com.csye6225.assignment2.service;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.csye6225.assignment2.model.UserAccount;
import com.csye6225.assignment2.repository.UserAccountRepository;

@Service
public class UserAccountService {

    @Autowired
    private UserAccountRepository userAccountRepository;

    public UserAccountService(UserAccountRepository userAccountRepository) {
        this.userAccountRepository = userAccountRepository;
    }

    public UserAccountService() {

    }

    public List<UserAccount> loadUsersFromCSV(String csvFilePath) throws IOException {

        CSVParser rows = CSVParser.parse(new FileReader(csvFilePath), CSVFormat.DEFAULT);
        List<UserAccount> userAccounts = new ArrayList<>();
        int c = 0;
        for (CSVRecord row : rows) {
            if (c == 0) {
                c++;
                continue;
            }
            UserAccount userAccount = new UserAccount();
            userAccount.setId(UUID.randomUUID());
            userAccount.setFirstName(row.get(0));
            userAccount.setLastName(row.get(1));
            userAccount.setEmail(row.get(2));
            userAccount.setPassword(new BCryptPasswordEncoder().encode(row.get(3)));
            userAccount.setAccountCreated(new Date());
            userAccount.setAccountUpdated(new Date());

            userAccounts.add(userAccount);

            // }

        }

        return userAccounts;
    }

    public void saveAll(List<UserAccount> userAccounts) {
        userAccountRepository.saveAll(userAccounts);
    }

    public boolean authenticateUser(String email, String password) {
        Optional<UserAccount> userOptional = userAccountRepository.findByEmail(email);
        if (userOptional.isPresent()) {
            UserAccount user = userOptional.get();
            // Use BCryptPasswordEncoder to check if the provided password matches the
            // hashed password
            return new BCryptPasswordEncoder().matches(password, user.getPassword());
        }
        return false;
    }

}
