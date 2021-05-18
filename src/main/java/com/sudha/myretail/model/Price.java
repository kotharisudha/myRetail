package com.sudha.myretail.model;

public class Price {
    private String value;
    private String currencyCode;

    public Price() {
    }

    public Price(String value, String currencyCode) {
        this.value = value;
        this.currencyCode = currencyCode;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }
}
