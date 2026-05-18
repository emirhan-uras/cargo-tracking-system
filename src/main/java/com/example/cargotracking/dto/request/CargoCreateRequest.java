package com.example.cargotracking.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.time.LocalDateTime;

@Data
public class CargoCreateRequest {

    @NotBlank(message = "Alıcı adı soyadı boş bırakılamaz")
    private String receiverName;

    @NotBlank(message = "Alıcı telefonu boş bırakılamaz")
    private String receiverPhone;

    @NotBlank(message = "Lütfen şehir seçiniz")
    private String city;

    @NotBlank(message = "Lütfen ilçe seçiniz")
    private String district;

    @NotBlank(message = "Açık adres (Sokak, bina no, daire) alanı boş bırakılamaz")
    private String streetAndDetails;

    @NotNull(message = "Kurye çağırma saati boş bırakılamaz")
    private LocalDateTime pickupTime;

    @NotNull(message = "Lütfen paketi teslim edeceğiniz çıkış şubesini seçiniz")
    private Long currentBranchId;

    private Long receiverUserId;
}