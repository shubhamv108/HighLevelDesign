### Approach 1
    Availability table for roomId for each date
    Distributed lock on selected roomId
Pros
    1. Easy to maintain availability and select roomId
    2. Room assigned during booking with TTl
Cons
    1. Too many rows 1M Hotel * 300 Room * 365days per year

### Approach 2
    Availability table for roomId for date range
    Required Interval splitting while updating availability complex to do
    Distributed lock on selected roomId with TTl
Pros
    1. Take lesser space compared to Approach 1
    2. Room assigned during booking
Cons
    2. Complex to update the availability while writing

### Approach 3
    Availability table on HotelId, RoomType, Date
    Distributed lock on selected HotelId::RoomType::Date with TTl for payment completion
    Requires Optimistic lock during update availability table transaction
Pros
    1. Least space compared to above approaches
Cons
    1. Room assigned during checkin

### Approach 3.1
    super(3)
    Use constraint to for count.
Pros
    1. Least space compared to above approaches
Cons
    1. Room assigned during checkin

InventoryService
InventoryCache for Availability (updated by booking workflow)

Hotel
id PK

HotelRoomTypes
id
hotelId
room_type

RoomTypeInventory
id PK
hotelId ShardKey
roomType ShardKey
roomCount
UK(hotelId, roomType)

Inventory
id PK
hotelId ShardKey
roomType Sortkey
date Sortkey
totalRooms
availableRooms
<!-- heldRooms avoid index due to low cardinality -->
<!-- bookedRooms -->
price
version
UK(hotelId, roomType, date)
    CREATE INDEX idx_available
    ON Inventory(
    hotelId,
    roomType,
    date
    )
    WHERE roomsRemaining > 0;

InventoryReservation
id PK
hotelId ShardKey
roomType Sortkey
startdate SortKey
endDate SortKey (auto cleanup rows after endDate)
roomsReserved
reservationId UK
expiryAt timestamp  (auto cleanup expired with INITIATED status)
status: INITIATED, CONFIRMED/CANCELLED
version
INDEX(hotelId, roomType, date)

ReservationService
1. Create a hold on roomCount in InventoryReservation via InventoryService
2. Initiate & Await payment. payment service sends a notification whether payment was success or not
3. Confirm/Cancel booking  by updating availability db.


Reservation
id PK
hotel_id
roomType
startdate
enddate
checkindate
checkoutdate
status

## Images
S3 + CDN