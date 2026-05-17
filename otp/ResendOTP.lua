-- KEYS[1] = otp:meta:{challenge_id}

if redis.call("EXISTS", KEYS[1]) == 0 then
    return {err="NOT_FOUND"}
end

local attempts = tonumber(redis.call("HGET", KEYS[1], "resend_attempts_left"))

if attempts <= 0 then
    return {err="NO_RESEND_LEFT"}
end

redis.call("HINCRBY", KEYS[1], "resend_attempts_left", -1)

return {ok="RESEND_ALLOWED"}