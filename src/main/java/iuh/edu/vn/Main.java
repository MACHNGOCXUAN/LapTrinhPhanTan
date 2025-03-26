package iuh.edu.vn;

import iuh.edu.vn.utils.PersonUtil;
import jakarta.json.*;
import net.datafaker.Faker;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Main {
    public static void main(String[] args) {

        List<iuh.fit.entities.Person> people = new ArrayList<>();
        Random rd = new Random();

        Faker faker = new Faker();
        for (int i = 0; i < 10; i++) {
            iuh.fit.entities.Person person = new iuh.fit.entities.Person();
            iuh.fit.entities.Address address = new iuh.fit.entities.Address();
            List<iuh.fit.entities.PhoneNumber> phoneNumbers = new ArrayList<iuh.fit.entities.PhoneNumber>();

            person.setFirstName(faker.name().firstName());
            person.setLastName(faker.name().lastName());
            person.setAge(faker.number().numberBetween(18, 60));

            address.setStreetAddress(faker.address().streetAddress());
            address.setCity(faker.address().city());
            address.setState(faker.address().state());
            address.setPostalCode(Integer.parseInt(faker.address().zipCode()));


            person.setAddress(address);

            int n = rd.nextInt(3);
            if(n > 0) {
                for(int j = 0; j < n; j++) {
                    phoneNumbers.add(new iuh.fit.entities.PhoneNumber(
                            faker.options().option("Fa", "Home", "Work"),
                            faker.phoneNumber().phoneNumber()
                    ));
                }
                person.setPhoneNumbers(phoneNumbers);
                people.add(person);;
            }
        }
//        people.forEach(x -> System.out.println(x));

        JsonArray ja = PersonUtil.toJson(people);
//        System.out.println(ja);

        PersonUtil.writeToFile(ja ,"json/people.json");
    }
}