//package mt.endearments.service;
//
//import lombok.AccessLevel;
//import lombok.RequiredArgsConstructor;
//import lombok.experimental.FieldDefaults;
//import mt.endearments.model.User;
//import org.springframework.data.redis.core.RedisTemplate;
//import org.springframework.stereotype.Service;
//
//import java.time.Duration;
//import java.util.HashMap;
//import java.util.Map;
//import java.util.Set;
//
//@Service
//@RequiredArgsConstructor
//@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
//public class RedisTokenService {
//    RedisTemplate<String, Object> redisTemplate;
//
//    public void saveToken(User user, String accessToken, int duration) {
//        String key = buildKey(user.getId(), accessToken);
//
//        Map<String, Object> tokenData = new HashMap<>();
//        tokenData.put("revoked", false);
//        tokenData.put("expired", false);
//
//        redisTemplate.opsForHash().putAll(key, tokenData);
//        redisTemplate.expire(key, Duration.ofMinutes(duration));
//    }
//
//    public void revokeAllUserTokens(Long userId) {
//        Set<String> keys = redisTemplate.keys("token:" + userId + ":*");
//        if (!keys.isEmpty()) {
//            for (String key : keys) {
//                redisTemplate.opsForHash().put(key, "revoked", true);
//                redisTemplate.opsForHash().put(key, "expired", true);
//            }
//        }
//    }
//
//    public boolean isTokenValid(Long userId, String token) {
//        String key = buildKey(userId, token);
//        Boolean revoked = (Boolean) redisTemplate.opsForHash().get(key, "revoked");
//        Boolean expired = (Boolean) redisTemplate.opsForHash().get(key, "expired");
//
//        return Boolean.FALSE.equals(revoked) && Boolean.FALSE.equals(expired);
//    }
//
//    private String buildKey(Long userId, String token) {
//        return "token:" + userId + ":" + token;
//    }
//}

package mt.endearments.service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import mt.endearments.model.User;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class RedisTokenService {

    RedisTemplate<String, Object> redisTemplate;

    /**
     * Lưu access token vào Redis với TTL
     */
    public void saveToken(User user, String accessToken, int durationMinutes) {
        String key = buildKey(user.getId(), accessToken);

        Map<String, Object> tokenData = new HashMap<>();
        tokenData.put("revoked", false);
        tokenData.put("expired", false);

        redisTemplate.opsForHash().putAll(key, tokenData);
        redisTemplate.expire(key, Duration.ofMinutes(durationMinutes));
    }

    /**
     * Revoke toàn bộ token của user (đăng xuất tất cả device)
     */
    public void revokeAllUserTokens(Long userId) {
        Set<String> keys = redisTemplate.keys("token:" + userId + ":*");

        if (keys == null || keys.isEmpty()) {
            return;
        }

        for (String key : keys) {
            redisTemplate.opsForHash().put(key, "revoked", true);
            redisTemplate.opsForHash().put(key, "expired", true);
        }
    }

    /**
     * Kiểm tra token còn hợp lệ không
     */
    public boolean isTokenValid(Long userId, String token) {
        String key = buildKey(userId, token);

        if (!redisTemplate.hasKey(key)) {
            // Token không tồn tại trong Redis (hết hạn hoặc chưa lưu)
            return false;
        }

        Object revokedObj = redisTemplate.opsForHash().get(key, "revoked");
        Object expiredObj = redisTemplate.opsForHash().get(key, "expired");

        boolean revoked = revokedObj instanceof Boolean && (Boolean) revokedObj;
        boolean expired = expiredObj instanceof Boolean && (Boolean) expiredObj;

        return !revoked && !expired;
    }

    private String buildKey(Long userId, String token) {
        return "token:" + userId + ":" + token;
    }
}
