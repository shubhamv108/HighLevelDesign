package otp;

import redis.clients.jedis.Jedis;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;

public class OtpService {

    private static final String HMAC_ALGO = "HmacSHA256";
    private static final int TIME_STEP = 30; // seconds

    public static long getTimeWindow(long currentEpochSeconds) {
        return currentEpochSeconds / TIME_STEP;
    }

    public static String generateOtp(
            String userSecret,
            String userId,
            String purpose,
            String challengeId,
            long currentEpochSeconds) throws Exception {
        long timeWindow = getTimeWindow(currentEpochSeconds);

        String data = userId + ":" + purpose + ":" + challengeId + ":" + timeWindow;

        byte[] hmac = hmacSha256(userSecret, data);

        String base62 = base62Encode(hmac);

        // Take last 6 digits (or 4–8 as you want)
        int otpLength = 6;
        return base62.substring(Math.max(0, base62.length() - otpLength));
    }

    public static int generateNumericOtp(
            String userSecret,
            String userId,
            String purpose,
            String challengeId,
            long currentEpochSeconds) throws Exception {

        long timeWindow = getTimeWindow(currentEpochSeconds);
        String data = userId + ":" + purpose + ":" + challengeId + ":" + timeWindow;
        byte[] hmac = hmacSha256(userSecret, data);

        // RFC 6238-style dynamic truncation
        int offset = hmac[hmac.length - 1] & 0x0F;
        int code = ((hmac[offset]     & 0x7F) << 24)
                | ((hmac[offset + 1] & 0xFF) << 16)
                | ((hmac[offset + 2] & 0xFF) << 8)
                |  (hmac[offset + 3] & 0xFF);

        int OTP_LENGTH = 6;                        // change to 4–8 as needed
        int otp = code % (int) Math.pow(10, OTP_LENGTH);

        // Zero-pad to ensure consistent length (e.g. 000412 not 412)
        return Integer.valueOf(String.format("%0" + OTP_LENGTH + "d", otp));
    }

    private static byte[] hmacSha256(String key, String data) throws Exception {
        Mac mac = Mac.getInstance(HMAC_ALGO);
        SecretKeySpec keySpec = new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), HMAC_ALGO);
        mac.init(keySpec);
        return mac.doFinal(data.getBytes(StandardCharsets.UTF_8));
    }

    // Simple Base62 encoding
    private static final String BASE62 = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";

    private static String base62Encode(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            int val = b & 0xFF;
            sb.append(BASE62.charAt(val % 62));
        }
        return sb.toString();
    }

    public boolean verifyOtp(
            String userSecret,
            String userId,
            String purpose,
            String challengeId,
            String inputOtp,
            Jedis jedis
                            ) throws Exception {

        String key = "otp:meta:" + challengeId;

        // 1. Run Lua (check + decrement)
        Object luaResult = jedis.evalsha(VERIFY_LUA_SHA,
                                         1,
                                         key,
                                         String.valueOf(System.currentTimeMillis() / 1000)
                                        );

        if (luaResult.toString().contains("err")) {
            return false;
        }

        // 2. Recompute OTP (check T and T-1)
        long now = System.currentTimeMillis() / 1000;

        String otpNow = OtpGenerator.generateOtp(userSecret, userId, purpose, challengeId, now);
        String otpPrev = OtpGenerator.generateOtp(userSecret, userId, purpose, challengeId, now - 30);

        boolean match = inputOtp.equals(otpNow) || inputOtp.equals(otpPrev);

        if (match) {
            jedis.evalsha(SUCCESS_LUA_SHA, 1, key);
            return true;
        }

        return false;
    }

    private static final String RESEND_LUA_SHA = "your_loaded_lua_sha"; // preload script

    public boolean resendOtp(
            String userSecret,
            String userId,
            String purpose,
            String challengeId,
            Jedis jedis
                            ) throws Exception {

        String key = "otp:meta:" + challengeId;
        long now = System.currentTimeMillis() / 1000;

        // 1. Run Lua script (atomic validation + decrement)
        Object result = jedis.evalsha(
                RESEND_LUA_SHA,
                1,
                key,
                String.valueOf(now)
                                     );

        String res = result.toString();

        if (res.contains("NOT_FOUND") || res.contains("EXPIRED") || res.contains("NO_RESEND_LEFT")) {
            return false;
        }

        // 2. Fetch metadata (only what we need)
        String storedUserId = jedis.hget(key, "user_id");
        String storedPurpose = jedis.hget(key, "purpose");

        if (!userId.equals(storedUserId) || !purpose.equals(storedPurpose)) {
            return false; // safety check
        }

        // 3. Recompute OTP (same logic as generation)
        String otp = OtpGenerator.generateOtp(
                userSecret,
                userId,
                purpose,
                challengeId,
                now
                                             );

        // 4. Send OTP (plug your SMS provider here)
        sendSms(userId, otp);

        return true;
    }

    private void sendSms(String phone, String otp) {
        // integrate with provider like Twilio
        System.out.println("Sending OTP " + otp + " to " + phone);
    }
}