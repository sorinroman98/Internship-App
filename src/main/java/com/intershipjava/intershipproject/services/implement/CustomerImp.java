package com.intershipjava.intershipproject.services.implement;

import com.intershipjava.intershipproject.constants.Constant;
import com.intershipjava.intershipproject.dto.CustomerDto;
import com.intershipjava.intershipproject.exceptions.CustomerExceptions;
import com.intershipjava.intershipproject.model.Customer;
import com.intershipjava.intershipproject.model.VerificationToken;
import com.intershipjava.intershipproject.repository.CustomerRepository;
import com.intershipjava.intershipproject.repository.VerificationTokenRepository;
import com.intershipjava.intershipproject.services.CustomerService;
import com.intershipjava.intershipproject.services.EmailService;
import com.intershipjava.intershipproject.services.validator.ValidatorService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Calendar;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class CustomerImp implements CustomerService {

    private final CustomerRepository customerRepository;
    private final VerificationTokenRepository verificationTokenRepository;
    private final ValidatorService validator;
    private final VerificationTokenImpl verificationTokenService;
    private final EmailService emailService;


    @Override

    public boolean setValidEmail(String email) {
        return validator.isValidEmailAddress(email);
    }

    @Transactional
    Customer save(Customer customer){
       return customerRepository.save(customer);
    }
    @Override
    public boolean setValidName(String name) throws CustomerExceptions {
        return validator.isValidName(name);
    }

    @Override
    @Transactional
    public CustomerDto saveCustomer(Customer customer) {
        if (customer == null) {
            throw new CustomerExceptions(Constant.errorMessages.INVALID_INPUT);
        }

        if(customerRepository.findCustomerByEmail(customer.getEmail()) != null){

            throw new CustomerExceptions(customer.getEmail());
        }

        validator.isValidName(customer.getName());
        validator.isValidEmailAddress(customer.getEmail());

        customer.setPassword(encryptPassword(customer.getPassword()));
        customer.setRole("user");
        customer.setEnabled(false);
        save(customer);
       final String token = UUID.randomUUID().toString();
       createVerificationTokenForUser(customer, token);
       emailService.sendVerificationToken(token, customer);
        return customerToDto(customer);
    }

        @Override
        public CustomerDto verifyCustomer(CustomerDto customerDto){
        customerDto.setPassword(encryptPassword(customerDto.getPassword()));
        Customer customer = customerRepository.getCustomerByEmailAndPassword(customerDto.getEmail(),customerDto.getPassword());
      if( customer == null){
          throw new CustomerExceptions(Constant.errorMessages.INVALID_CREDENTIALS);

      }else if(customer.isEnabled()){
          return customerToDto(customer) ;

      }else {
          throw new CustomerExceptions(Constant.errorMessages.INACTIVE_ACCOUNT);

      }

    }

    @Override
    public void createVerificationTokenForUser(final Customer user, final String token) {
        final VerificationToken myToken = new VerificationToken(token, user);
        verificationTokenRepository.save(myToken);
    }

    @Override
    @Transactional
    public boolean resendVerificationToken(final String existingVerificationToken) {
        VerificationToken vToken = verificationTokenRepository.findByToken(existingVerificationToken);
        if(vToken != null) {
            vToken.updateToken(UUID.randomUUID().toString());
            vToken = verificationTokenRepository.save(vToken);
            emailService.sendVerificationToken(vToken.getToken(), vToken.getCustomer());
            return true;
        }
        return false;
    }

    @Override
    public String validateVerificationToken(String token) {
        final VerificationToken verificationToken = verificationTokenRepository.findByToken(token);
        if (verificationToken == null) {
            return Constant.AppConstants.TOKEN_INVALID;
        }

        final Customer customer = verificationToken.getCustomer();
        final Calendar cal = Calendar.getInstance();
        if ((verificationToken.getExpiryDate().getTime() - cal.getTime().getTime()) <= 0) {
            return Constant.AppConstants.TOKEN_EXPIRED;
        }

        customer.setEnabled(true);
        verificationTokenRepository.delete(verificationToken);
        customerRepository.save(customer);
        return Constant.AppConstants.TOKEN_VALID;
    }


    @Override
    public List<Customer> listAllCustomers() {
        if (customerRepository.findAll().isEmpty()) {
            throw new CustomerExceptions(Constant.errorMessages.EMPTY_CUSTOMER_LIST);
        }
        return customerRepository.findAll();
    }

    @Override
    public Customer customerDtoToCustomer(CustomerDto customerDto) {
        if (customerDto == null) {
            throw new CustomerExceptions(Constant.errorMessages.INVALID_OBJECT);
        }
        Customer customerResponse = new Customer();
        customerResponse.setEmail(customerDto.getEmail());
        customerResponse.setName(customerDto.getName());
        customerResponse.setPassword(customerDto.getPassword());
        customerResponse.setRole(customerDto.getRole());
        customerResponse.setEnabled(customerDto.isEnabled());
        return customerResponse;
    }

    @Override
    public CustomerDto customerToDto(Customer customer) {
        if (customer == null) {
            throw new CustomerExceptions(Constant.errorMessages.INVALID_OBJECT);
        }
        CustomerDto customerDto = new CustomerDto();
        customerDto.setEmail(customer.getEmail());
        customerDto.setName(customer.getName());
        customerDto.setPassword(customer.getPassword());
        customerDto.setRole(customer.getRole());
        customerDto.setEnabled(customer.isEnabled());

        return customerDto;
    }

    @Override
    public String encryptPassword (String password){
        if (password.isEmpty()){
            throw new CustomerExceptions(Constant.errorMessages.EMPTY_PASSWORD);
        }
        String encryptedpassword = null;
        try
        {
            MessageDigest m = MessageDigest.getInstance("MD5");

            m.update(password.getBytes());

            byte[] bytes = m.digest();

            StringBuilder s = new StringBuilder();
            for(int i=0; i< bytes.length ;i++)
            {
                s.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
            }

            encryptedpassword = s.toString();
        }
        catch (NoSuchAlgorithmException e)
        {
            throw new CustomerExceptions(Constant.errorMessages.FAILED_TO_ENCRYPT);
        }

        return encryptedpassword;
    }
}
