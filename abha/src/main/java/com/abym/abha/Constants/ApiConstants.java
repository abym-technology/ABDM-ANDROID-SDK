package com.abym.abha.Constants;

public interface ApiConstants {

    public String BASE_URL_DEV = "http://13.234.94.247:8094";
    public String BASE_URL_LIVE = "http://13.234.94.247:8094";
    public String BASEURL_WEBSERVIC = "/api/v1/abha/";
    public String BASEURL_UAT = ApiConstants.BASE_URL_DEV + ApiConstants.BASEURL_WEBSERVIC;
    public String BASEURL_LIVE = ApiConstants.BASE_URL_LIVE + ApiConstants.BASEURL_WEBSERVIC;
    public String GENERATE_AADHAR_OTP = "send-aadhar-otp";
    public String RESEND_AADHAR_OTP = "resend-aadhar-otp";
    public String VERIFY_AADHAR_OTP = "verify-aadhar-otp";
    public String MOBILE_OTP = "send-mobile-otp";
    public String VERIFY_MOBILE_OTP = "verify-mobile-otp";
    public String CHECK_PHR_AVAIL = "check-healthid";
    public String CREATE_ABHA = "create-new-abha";
    public String ABHA_CARD = "account/png/card";
    public String ABHA_PROFILE = "account/profile";
}
