# FR
Login - OAuth, Registration
Search
Book miltiple seats
cancel booking all at once

# NFR
Consistent for booking - no duplicates
Avaibale for booking
low latency for login/booking

APIS
POST /users {
name
}

PUT /users/{id} {
name
}

PATCH /users/{id}/status {
status
}

GET /search?source={}&destination={}&date={} -> [
{
trainId
routeId
}
]

GET /bookings/availability?routeId=?&sourceSequqnceNo=?&destinationSequenceNo=? -> {
avaibility
}

POST /bookings {
routeId
sourceStatsionId
destinationStationId
seatCount
} -> 2xx {
id
}

GET /bookings/{id} -> 200, 404 {
source
destination
status
routeId
trainId
}

DELETE /bookings/{id} -> 204, 410 {
status
}



Entities

Train
Long id;

Route
id;
trainId;

RouteStation
Long id;
Integer sequence;
DayOfWeek dayOfWeek;
Route route; // routeId (FK)
Station station; // stationId (FK)
// UK (sequence, station)

Station
id
name

class TrainSeat {
id;
seatNumber;
trainId;

/**
* Select * from RouteStation as s join RouteStation as d on s.routeId = d.routeId
* where s.dayOfWeek in [] and s.stationId = ? and d.stationId = ?
  */

RunningStatus
id;
routeId
startDate;
status;

RouteStationAvailability
Long id;
RunningStatus runningStatus; // runningStatusId
int routeStationSequence;
int availability;

// Redis entry  runningStatus::routeStationSequence -> availability

// minimum of route availability from source to destination in route gives the avilability


Booking
seatNumbers[]
Long userId
// routeId
source
destination
status


