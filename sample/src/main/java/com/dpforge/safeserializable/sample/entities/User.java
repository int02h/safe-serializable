package com.dpforge.safeserializable.sample.entities;

import java.io.Serializable;

public class User implements Serializable {
    long id;
    String name;
    Employment currentEmployment;
    Employment[] previousEmployments;
}
