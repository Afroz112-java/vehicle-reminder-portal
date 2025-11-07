package net.konic.vehicle.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class Userdto {
    private Long id;
    private String name;
    private String email;
    private String phone;
    private String role;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
