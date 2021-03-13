package com.maccarons.prova.dto;

import lombok.*;

import java.time.*;
import java.util.*;

@Getter
@Setter
public class ApiePersonModel {
    private UUID uidperson;
    private String codpersontype;
    private String desname;
    private String dessurname;
    private LocalDateTime birthDate;
    private List<String> codes;
    private List<PrseAddressModel> addresses;
}
