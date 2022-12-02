package com.jotangi.greentravel;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class PersonalData implements Serializable {

    @SerializedName("account")
    private String account;
    @SerializedName("accountType")
    private String accountType;
    @SerializedName("tel")
    private String tel;
    @SerializedName("name")
    private String name;
    @SerializedName("email")
    private String email;
    @SerializedName("address")
    private String address;
    @SerializedName("birthday")
    private String birthday;
    @SerializedName("imageName")
    private String imageName;
    @SerializedName("mobileType")
    private String mobileType;

    @SerializedName("sex")
    private String sex;
    @SerializedName("city")
    private String city;
    @SerializedName("region")
    private String region;

    @SerializedName("point")
    private String point;
    @SerializedName("cmdImageFile")
    private String cmdImageFile;


    @SerializedName("referrerPhone")
    private String referrerPhone;
    @SerializedName("referrerCount")
    private String referrerCount;


    public String getReferrerPhone() {
        return referrerPhone;
    }

    public String getReferrerCount() {
        return referrerCount;
    }

    public String getPoint() {
        return point;
    }

    public String getCmdImageFile() {
        return cmdImageFile;
    }

    public String getSex() {
        return sex;
    }

    public String getCity() {
        return city;
    }

    public String getRegion() {
        return region;
    }

    public String getAccount() {
        return account;
    }

    public String getAccountType() {
        return accountType;
    }

    public String getTel() {
        return tel;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getAddress() {
        return address;
    }

    public String getBirthday() {
        return birthday;
    }

    public String getImageName() {
        return imageName;
    }

    public String getMobileType() {
        return mobileType;
    }
}
