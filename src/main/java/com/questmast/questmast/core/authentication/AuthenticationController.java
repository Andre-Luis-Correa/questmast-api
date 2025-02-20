package com.questmast.questmast.core.authentication;

import com.questmast.questmast.common.exception.domain.CPFNotValidException;
import com.questmast.questmast.core.address.address.domain.entity.Address;
import com.questmast.questmast.core.address.address.service.AddressService;
import com.questmast.questmast.core.admin.domain.dto.AdminDTO;
import com.questmast.questmast.core.admin.service.AdminService;
import com.questmast.questmast.core.authentication.user.domain.dto.UserFormDTO;
import com.questmast.questmast.core.authentication.user.service.UserDetailsServiceImpl;
import com.questmast.questmast.core.address.city.domain.City;
import com.questmast.questmast.core.address.city.service.CityService;
import com.questmast.questmast.core.contact.email.domain.entity.Email;
import com.questmast.questmast.core.contact.email.service.EmailService;
import com.questmast.questmast.core.contact.phone.domain.model.Phone;
import com.questmast.questmast.core.contact.phone.service.PhoneService;
import com.questmast.questmast.core.enums.PersonRole;
import com.questmast.questmast.core.gender.domain.Gender;
import com.questmast.questmast.core.gender.service.GenderService;
import com.questmast.questmast.core.address.neighborhood.domain.Neighborhood;
import com.questmast.questmast.core.address.neighborhood.service.NeighborhoodService;
import com.questmast.questmast.core.address.street.domain.Street;
import com.questmast.questmast.core.address.street.service.StreetService;
import com.questmast.questmast.core.person.cpf.domain.CPF;
import com.questmast.questmast.core.person.cpf.service.CPFService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/authentication")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService authenticationService;
    private final UserDetailsServiceImpl userDetailsService;
    private final AdminService adminService;
    private final GenderService genderService;
    private final AddressService addressService;
    private final PhoneService phoneService;
    private final EmailService emailService;
    private final CPFService cpfService;

    @PostMapping
    public String authenticate(Authentication authentication) {
        return authenticationService.authenticate(authentication);
    }

    @GetMapping
    public ResponseEntity<Boolean> email(String email) throws Exception {
        return ResponseEntity.ok().body(emailService.isEmailValid(email));
    }

    @PostMapping("/register")
    public ResponseEntity<AdminDTO> create(@RequestBody UserFormDTO userFormDTO) throws Exception {
        CPF cpf = cpfService.getValidCPF(userFormDTO.cpf());
        Gender gender = genderService.findByAcronym(userFormDTO.genderAcronym());
        Address address = addressService.create(userFormDTO.specificAddressFormDTO());
        Email mainEmail = emailService.getValidEmail(userFormDTO.mainEmail());
        Email reocveryEmail = emailService.getValidEmail(userFormDTO.recoveryEmail());
        List<Phone> phoneList = phoneService.generateValidPhoneList(userFormDTO.phoneList());

        if(userFormDTO.personRole().equals(PersonRole.ROLE_ADMIN)) {
            adminService.create(userFormDTO, gender, address, mainEmail, reocveryEmail, phoneList);
            userDetailsService.create(userFormDTO);
        }

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
