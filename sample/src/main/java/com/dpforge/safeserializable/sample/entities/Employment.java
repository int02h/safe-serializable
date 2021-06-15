package com.dpforge.safeserializable.sample.entities;

import java.io.Serializable;
import java.util.Date;

public class Employment implements Serializable {
    String jobTitle;
    Date startData;
    Date endDate;
    Employer employer;
}
