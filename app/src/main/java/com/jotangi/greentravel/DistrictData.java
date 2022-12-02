package com.jotangi.greentravel;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class DistrictData implements Serializable {

    public DistrictData(String name) {
        this.name = name;
    }

    //    @SerializedName("zip")
//    private String zip;

    @SerializedName("name")
    private String name;


//    public String getZip() {
//        return zip;
//    }

    public String getName() {
        return name;
    }

    /**
     * Pay attention here, you have to override the toString method as the
     * ArrayAdapter will reads the toString of the given object for the name
     *
     * @return contact_name
     */
    @Override
    public String toString() {
        return name;
    }
}
