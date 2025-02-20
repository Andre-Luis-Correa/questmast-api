package com.questmast.questmast.core.authentication;

import com.questmast.questmast.core.address.domain.entity.Address;
import com.questmast.questmast.core.address.service.AddressService;
import com.questmast.questmast.core.admin.domain.dto.AdminDTO;
import com.questmast.questmast.core.admin.service.AdminService;
import com.questmast.questmast.core.authentication.user.domain.dto.UserFormDTO;
import com.questmast.questmast.core.authentication.user.service.UserDetailsServiceImpl;
import com.questmast.questmast.core.city.domain.City;
import com.questmast.questmast.core.city.service.CityService;
import com.questmast.questmast.core.contact.email.domain.entity.Email;
import com.questmast.questmast.core.contact.email.service.EmailService;
import com.questmast.questmast.core.contact.phone.domain.model.Phone;
import com.questmast.questmast.core.contact.phone.service.PhoneService;
import com.questmast.questmast.core.enums.PersonRole;
import com.questmast.questmast.core.gender.domain.Gender;
import com.questmast.questmast.core.gender.service.GenderService;
import com.questmast.questmast.core.neighborhood.domain.Neighborhood;
import com.questmast.questmast.core.neighborhood.service.NeighborhoodService;
import com.questmast.questmast.core.street.domain.Street;
import com.questmast.questmast.core.street.service.StreetService;
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
    private final StreetService streetService;
    private final NeighborhoodService neighborhoodService;
    private final CityService cityService;
    private final PhoneService phoneService;
    private final EmailService emailService;

    @PostMapping
    public String authenticate(Authentication authentication) {
        return authenticationService.authenticate(authentication);
    }

    @PostMapping("/register")
    public ResponseEntity<AdminDTO> create(@RequestBody UserFormDTO userFormDTO) {
        Gender gender = genderService.findByAcronym(userFormDTO.genderAcronym());
        Street street = streetService.findById(userFormDTO.specificAddressFormDTO().streetId());
        Neighborhood neighborhood = neighborhoodService.findById(userFormDTO.specificAddressFormDTO().neighborhoodId());
        City city = cityService.findById(userFormDTO.specificAddressFormDTO().cityId());
        Address address = addressService.create(userFormDTO.specificAddressFormDTO(), street, neighborhood, city);
        List<Phone> phoneList = phoneService.generateValidPhoneList(userFormDTO.phoneList());
        List<Email> emailList = emailService.generateValidEmailList(userFormDTO.emailList());

        if(userFormDTO.personRole().equals(PersonRole.ROLE_ADMIN)) {
            adminService.create(userFormDTO, gender, address, phoneList, emailList);
            userDetailsService.create(userFormDTO);
        }

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
