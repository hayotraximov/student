package uz.pdp.appjparelationships.payload;

import lombok.Data;

@Data
public class StudentDTO {
//    private Integer id;
    private String firstName;
    private String lastName;
    private String city;//Toshkent
    private String district;//Mirobod
    private String street;//U.Nosir ko'chasi
    private Integer groupid;
    private Integer[] subjectsId;
}
