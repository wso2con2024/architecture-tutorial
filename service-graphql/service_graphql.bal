import ballerina/graphql;
import ballerina/log;

service class RoomType {
    private final readonly & RoomTypeData roomTypeData;

    function init(RoomTypeData roomTypeData) {
        self.roomTypeData = roomTypeData.cloneReadOnly();
    }

    resource function get id() returns int {
        return self.roomTypeData.id;
    }

    resource function get name() returns string {
        return self.roomTypeData.name;
    }


    resource function get guestCapacity() returns int {
        return self.roomTypeData?.guest_capacity;
    }

    resource function get price() returns decimal {
        return self.roomTypeData.price;
    }
}

@graphql:ServiceConfig {
    cors: {
        allowOrigins: ["*"],
        allowHeaders: ["Content-Type"]
    },
    graphiql: {
        enabled: true
    }
}
service /roomgql on new graphql:Listener(9090) {

    resource function get roomTypes(string checkinDate, string checkoutDate, int guestCapacity) returns RoomType[]|error {
        log:printInfo("searching for drug description:", checkinDate = checkinDate, checkoutDate = checkoutDate, guestCapacity = guestCapacity);
        RoomTypeData[]|error availableRoomTypes = getAvailableRoomTypes(checkinDate, checkoutDate, guestCapacity);
        RoomType[] roomTypes = [];
        if (availableRoomTypes is RoomTypeData[]) {
            // Convert RoomTypeData array to RoomType array if needed
            foreach var roomTypeData in availableRoomTypes {
                RoomType roomType = new RoomType(roomTypeData);
                roomTypes.push(roomType);
            }   
            return roomTypes;
        } 

        return roomTypes;
    }

}

