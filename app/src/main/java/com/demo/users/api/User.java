package com.demo.users.api;

import java.io.Serializable;

public class User implements Serializable {

    public Name name;
    public String email;
    public String phone;
    public String dob;
    public Id id;
    public Picture picture;
    public String nat;
    public Address location;

    public static class Name implements Serializable {
        public String title;
        public String first;
        public String last;
    }

    public static class Id implements Serializable {
        public String name;
        public String value;
    }
    public static class Picture implements Serializable {
        public String large;
        public String medium;
        public String thumbnail;
    }
    public static class Address implements Serializable {
        public String street;
        public String city;
        public String state;
        public String postcode;
    }
}
