package com.demo.users.api;

public class User {

    public Name name;
    public String email;
    public String phone;
    public Id id;
    public Picture picture;
    public String nat;

    public static class Name {
        public String title;
        public String first;
        public String last;
    }

    public static class Id {
        public String name;
        public String value;
    }
    public static class Picture {
        public String large;
        public String medium;
        public String thumbnail;
    }
}
