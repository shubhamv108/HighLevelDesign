```
CONSTRAINT no_overlapping_seat_booking
EXCLUDE USING gist (
  trainId WITH =,
  trainInstanceDate WITH =,
  train_coach_id WITH =,
  seatNumber WITH =,
  journey_range WITH &&
)
WHERE (status = 'BOOKED');
```