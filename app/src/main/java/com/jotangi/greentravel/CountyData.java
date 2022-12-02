package com.jotangi.greentravel;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

public class CountyData implements Serializable {

    @SerializedName("name")
    private String name;

    @SerializedName("districts")
    private ArrayList<DistrictData> districts;

    public String getName() {
        return name;
    }

    public ArrayList<DistrictData> getDistricts() {
        return districts;
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
