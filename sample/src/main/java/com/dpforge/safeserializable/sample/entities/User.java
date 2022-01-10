package com.dpforge.safeserializable.sample.entities;

import java.io.Serializable;
import java.util.List;

public class User implements Serializable {
    long id;
    String name;
    Employment currentEmployment;
    Employment[] previousEmployments;
    List<String> phoneNumbers;
}
