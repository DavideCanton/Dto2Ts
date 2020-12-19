package come.mi.pare;

import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class Person {
    String name;
    String surname;
    int age;
    boolean admin;
    Date birthDate; // o LocalDateTime, forse usano quello
    PersonAddress mainAddress;
    List<PersonAddress> otherAddresses;
    int[] numeri;

}
