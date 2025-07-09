package com.medical.schoolMedical.service;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Random;
import java.util.concurrent.*;

@Service

public class OtpService {
    private final Map<String, OtpData> otpStorage = new ConcurrentHashMap<>();
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
    private static final int OTP_LENGTH = 4;
    private static final int OTP_VALIDITY_MINUTES= 1;

    @Getter
    private static class OtpData {
        private final String otp;
        private final long expiryTime;

        public OtpData(String otp, long expiryTime) {
            this.otp = otp;
            this.expiryTime = expiryTime;
        }
    }

    private String generateRandomOtp() {
        Random random = new Random();
        StringBuilder otp = new StringBuilder();
        for(int i = 0; i < OTP_LENGTH; i++) {
            otp.append(random.nextInt(10));
        }
        return otp.toString();

    }

    public String generateOtp(String email) {
        String otp = generateRandomOtp();
        long expiryTime = System.currentTimeMillis() + OTP_VALIDITY_MINUTES*60*1000;
        otpStorage.put(email, new OtpData(otp, expiryTime));
//        Lên lịch xóa OTP sau khi hết hạn
        scheduler.schedule(()-> otpStorage.remove(email), OTP_VALIDITY_MINUTES, TimeUnit.MINUTES);
        return otp;
    }

    public boolean validateOtp(String email, String otp) {
        OtpData otpData = otpStorage.get(email);
        if(otpData == null || System.currentTimeMillis() > otpData.expiryTime) {
            return false;
        }
        return otpData.otp.equals(otp);
    }


}
