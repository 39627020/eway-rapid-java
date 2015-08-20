package com.eway.payment.rapid.sdk.beans.internal;

import org.codehaus.jackson.annotate.JsonProperty;

public class BeagleVerification {

    @JsonProperty("Email")
    private String email;
    
    @JsonProperty("Phone")
    private String phone;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
