package org.model.request;

import lombok.*;

@NoArgsConstructor
@Builder
@Data
public class Customer {

    String id;
    String name;
    String phone_number;
    boolean sms_sent;

    public Customer(String id, String name, String phone_number) {
        this.id = id;
        this.name = name;
        this.phone_number = phone_number;
    }

    public Customer(String id, String name, String phone_number, boolean sms_sent) {
        this.id = id;
        this.name = name;
        this.phone_number = phone_number;
        this.sms_sent = sms_sent;
    }

    public Customer(String id, String phone_number) {
        this.id = id;
        this.phone_number = phone_number;
    }
}
