package com.jotangi.greentravel.ui.account;

public class AccountTradeRecord {
    private String clinicName;
    private String tradeTime;
    private String cost;

    public AccountTradeRecord(String clinicName, String tradeTime, String cost) {
        this.clinicName = clinicName;
        this.tradeTime = tradeTime;
        this.cost = cost;
    }

    public String getClinicName() {
        return clinicName;
    }

    public String getTradeTime() {
        return tradeTime;
    }

    public String getCost() {
        return cost;
    }

    public void setClinicName(String clinicName) {
        this.clinicName = clinicName;
    }

    public void setTradeTime(String tradeTime) {
        this.tradeTime = tradeTime;
    }

    public void setCost(String cost) {
        this.cost = cost;
    }
}
