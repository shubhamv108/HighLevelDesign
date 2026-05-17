-- KEYS[1] = otp:meta:{challenge_id}
-- ARGV[1] = current_time (epoch seconds)

local key = KEYS[1]
local now = tonumber(ARGV[1])

if redis.call("EXISTS", key) == 0 then
    return {err="NOT_FOUND"}
end

local expires_at = tonumber(redis.call("HGET", key, "expires_at"))
if now > expires_at then
    redis.call("DEL", key)
    return {err="EXPIRED"}
end

local used = redis.call("HGET", key, "used")
if used == "true" then
    return {err="ALREADY_USED"}
end

local attempts = tonumber(redis.call("HGET", key, "verify_attempts_left"))
if attempts <= 0 then
    return {err="NO_ATTEMPTS"}
end

-- decrement attempts
redis.call("HINCRBY", key, "verify_attempts_left", -1)

return {ok="PROCEED"}