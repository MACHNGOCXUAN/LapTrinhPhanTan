package iuh.edu.vn.utils;

import iuh.fit.entities.Address;
import iuh.fit.entities.Person;
import jakarta.json.*;
import jakarta.json.stream.JsonGenerator;

import javax.print.attribute.standard.JobSheets;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PersonUtil {
    public static void main(String[] args) {
        Person person = fromJson("json/person.json");
        System.out.println(person);
    }

    private static Person fromJson(String fileName){
        Person person = null;
        try(JsonReader reader = Json.createReader(new FileReader(fileName))){
            JsonObject jo = reader.readObject();
            if (jo != null){
                person = new Person();
                String fName = jo.getString("firstName");
                person.setFirstName(fName);
                person.setLastName(jo.getString("lastName"));
                person.setAge(jo.getInt("age"));

                JsonObject joAddr = jo.getJsonObject("address");
                if(joAddr != null){
                    iuh.fit.entities.Address address = new Address();
                    address.setStreetAddress(joAddr.getString("streetAddress"));
                    address.setCity(joAddr.getString("city"));
                    address.setState(joAddr.getString("state"));
                    address.setPostalCode(joAddr.getInt("postalCode"));

                    person.setAddress(address);
                }

                JsonArray jaPhones = jo.getJsonArray("phoneNumbers");
                if(jaPhones != null){
                    List<iuh.fit.entities.PhoneNumber> phoneNumbers = jaPhones.stream()
                            .map(jv -> (JsonObject) jv)
                            .map(joPhone -> {
                                iuh.fit.entities.PhoneNumber phoneNumber = new iuh.fit.entities.PhoneNumber();
                                phoneNumber.setType(joPhone.getString("type"));
                                phoneNumber.setNumber(joPhone.getString("number"));
                                return phoneNumber;
                            }).toList();
                    person.setPhoneNumbers(phoneNumbers);
                }
            }
        }catch (Exception ex){
            ex.printStackTrace();
        }

        return person;
    }

    public static JsonArray toJson(List<iuh.fit.entities.Person> people) {
        JsonArrayBuilder jaBuilder = Json.createArrayBuilder();
        JsonObjectBuilder joBuilder = Json.createObjectBuilder();
        people.stream()
                .map(person -> {
                    joBuilder.add("firstName", person.getFirstName())
                            .add("lastName", person.getLastName())
                            .add("age", person.getAge())
                            .add("address", toJson(person.getAddress()));

                    if(person.getPhoneNumbers() != null)
                        joBuilder.add("phoneNumbers", to2Json(person.getPhoneNumbers()));

                    return joBuilder.build(); //JsonObject
                })//List<JSonObject>
                .forEach(jo -> jaBuilder.add(jo));

        return jaBuilder.build();
    }

    public static void writeToFile(JsonArray ja, String fileName) {
        // Định dạng
        Map<String, Boolean> conf = new HashMap<>();
        conf.put(JsonGenerator.PRETTY_PRINTING, true);

        JsonWriterFactory factory = Json.createWriterFactory(conf);

        try(JsonWriter writer = factory.createWriter(new FileWriter(fileName))) {
            writer.writeArray(ja);
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static JsonObject toJson(Address address) {
        JsonObjectBuilder joBuilder = Json.createObjectBuilder();
        return joBuilder
                .add("streetAddress", address.getStreetAddress())
                .add("city", address.getCity())
                .add("state", address.getState())
                .add("postalCode", address.getPostalCode()).build();
    }

    private static JsonArray to2Json(List<iuh.fit.entities.PhoneNumber> phoneNumbers) {
        JsonObjectBuilder joBuilder = Json.createObjectBuilder();
        JsonArrayBuilder jaBuilder = Json.createArrayBuilder();
        phoneNumbers.stream()
                .map(phoneNumber -> {
                    return joBuilder.add("type", phoneNumber.getType())
                            .add("number", phoneNumber.getNumber())
                            .build();
                })
                .forEach(jo -> jaBuilder.add(jo));

        return jaBuilder.build();
    }
}
