import ballerina/log;
//import ballerina/os;
import ballerina/sql;
import ballerina/time;
import ballerinax/mysql;
import ballerinax/mysql.driver as _;

mysql:Client mysqlEp;

function init() returns error? {
    //int dbPort = check int:fromString(dbPortStr);
    log:printInfo("DB INFO: ", host = DB_HOST, user = DB_USERNAME, password = DB_PASSWORD, database = DB_NAME, port = DB_PORT);

    mysqlEp = check new (host = DB_HOST, user = DB_USERNAME, password = DB_PASSWORD, database = DB_NAME, port = DB_PORT);
}

# This function provides the available room types for a given date range and guest capacity
#
# + checkinDate - checkin date
# + checkoutDate - checkout date
# + guestCapacity - guest capacity
# + return - returns the available room types
function getAvailableRoomTypes(string checkinDate, string checkoutDate, int guestCapacity) returns RoomTypeData[]|error {
    time:Utc userCheckinUTC = check time:utcFromString(checkinDate);
    time:Utc userCheckoutUTC = check time:utcFromString(checkoutDate);
    sql:ParameterizedQuery sqlQuery = `SELECT rt.*
        FROM room_type rt
        WHERE rt.guest_capacity >= ${guestCapacity}
        AND EXISTS (
            SELECT r.*
            FROM room r
            WHERE r.type_id = rt.id
            AND NOT EXISTS (
                SELECT res.*
                FROM reservation res
                WHERE res.room_number = r.number
                AND res.checkin_date < ${userCheckinUTC}
                AND res.checkout_date > ${userCheckoutUTC}
            )
        )`;
    stream<RoomTypeData, sql:Error?> infoStream = mysqlEp->query(sqlQuery);

    RoomTypeData[] roomTypes = [];

    error? e = infoStream.forEach(function(RoomTypeData roomTypeData) {
        log:printInfo("RoomType: ", name= roomTypeData.name, capacity= roomTypeData.guest_capacity, price= roomTypeData.price);
        roomTypes.push(roomTypeData);
    });
    
    if e is error {
        log:printError("Error processing stream", 'error = e);
        return e;
    }
    return roomTypes;
}

configurable string DB_HOST = ?; //os:getEnv("DB_HOST");
configurable string DB_USERNAME = ?; //os:getEnv("DB_USERNAME");
configurable string DB_PASSWORD = ?; //os:getEnv("DB_PASSWORD");
configurable string DB_NAME =?; //os:getEnv("DB_NAME");
configurable int DB_PORT = ?;//os:getEnv("DB_PORT");

