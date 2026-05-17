-- KEYS[1] = otp:meta:{challenge_id}

if redis.call("EXISTS", KEYS[1]) == 1 then
    redis.call("DEL", KEYS[1])
    return {ok="VERIFIED"}
end

return {err="NOT_FOUND"}