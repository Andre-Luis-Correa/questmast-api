package com.questmast.questmast.core.admin.controller;

import com.questmast.questmast.core.address.domain.entity.Address;
import com.questmast.questmast.core.address.service.AddressService;
import com.questmast.questmast.core.admin.domain.dto.AdminDTO;
import com.questmast.questmast.core.admin.domain.dto.AdminFormDTO;
import com.questmast.questmast.core.admin.service.AdminService;
import com.questmast.questmast.core.city.domain.City;
import com.questmast.questmast.core.city.service.CityService;
import com.questmast.questmast.core.contact.email.domain.entity.Email;
import com.questmast.questmast.core.contact.email.repository.EmailRepository;
import com.questmast.questmast.core.contact.email.service.EmailService;
import com.questmast.questmast.core.contact.phone.domain.model.Phone;
import com.questmast.questmast.core.contact.phone.service.PhoneService;
import com.questmast.questmast.core.gender.domain.Gender;
import com.questmast.questmast.core.gender.service.GenderService;
import com.questmast.questmast.core.neighborhood.domain.Neighborhood;
import com.questmast.questmast.core.neighborhood.service.NeighborhoodService;
import com.questmast.questmast.core.street.domain.Street;
import com.questmast.questmast.core.street.service.StreetService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
@CrossOrigin
public class AdminController {

    private final AdminService adminService;
    private final GenderService genderService;
    private final AddressService addressService;
    private final StreetService streetService;
    private final NeighborhoodService neighborhoodService;
    private final CityService cityService;
    private final PhoneService phoneService;
    private final EmailService emailService;

    @PostMapping
    public ResponseEntity<AdminDTO> create(@RequestBody AdminFormDTO adminFormDTO) {
        Gender gender = genderService.findByAcronym(adminFormDTO.genderAcronym());
        Street street = streetService.findById(adminFormDTO.specificAddressFormDTO().streetId());
        Neighborhood neighborhood = neighborhoodService.findById(adminFormDTO.specificAddressFormDTO().neighborhoodId());
        City city = cityService.findById(adminFormDTO.specificAddressFormDTO().cityId());
        Address address = addressService.create(adminFormDTO.specificAddressFormDTO(), street, neighborhood, city);
        List<Phone> phoneList = phoneService.generateValidPhoneList(adminFormDTO.phoneList());
        List<Email> emailList = emailService.generateValidEmailList(adminFormDTO.emailList());

        AdminDTO adminDTO = adminService.create(adminFormDTO, gender, address, phoneList, emailList);

        return ResponseEntity.status(HttpStatus.OK).body(adminDTO);
    }
}
