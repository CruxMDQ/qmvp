package callisto.quotermvp.tools;

import java.util.List;

import callisto.quotermvp.firebase.model.Chamber;
import callisto.quotermvp.firebase.model.RealEstate;
import callisto.quotermvp.realm.model.Estate;

public class Events {
    static public class AddMarkerEvent {
        public AddMarkerEvent() {}
    }

    static public class OnMapReadyEvent {
        public OnMapReadyEvent() {}
    }

    static public class GeocodingRequestEvent {
        final String address;
        final String city;

        public GeocodingRequestEvent(String address, String city) {
            this.address = address;
            this.city = city;
        }

        public String getAddress() {
            return address;
        }

        public String getCity() {
            return city;
        }
    }

    static public class EstateDetailsQueried {
        final Estate estate;

        public EstateDetailsQueried(Estate estate) {
            this.estate = estate;
        }

        public Estate getEstate() {
            return estate;
        }
    }

    static public class RealEstateDetailsQueried {
        final RealEstate estate;

        public RealEstateDetailsQueried(RealEstate estate) {
            this.estate = estate;
        }

        public RealEstate getEstate() {
            return estate;
        }
    }

    static public class EstatePictureCaptureEvent {
        public EstatePictureCaptureEvent() { }
    }

    static public class RoomsListRequestedEvent {
        public RoomsListRequestedEvent() { }
    }

    static public class RoomPictureCaptureEvent {
        public RoomPictureCaptureEvent() { }
    }

    static public class RoomCreationRequestedEvent {
        public RoomCreationRequestedEvent() { }
    }

//    static public class RoomEditionRequestedEvent {
//        public long estateId;
//        public long roomId;
//
//        public RoomEditionRequestedEvent(long estateId, long roomId) {
//            this.estateId = estateId;
//            this.roomId = roomId;
//        }
//    }

    static public class EstatesRetrievedFromFirebaseEvent {
        public List<RealEstate> estates;

        public EstatesRetrievedFromFirebaseEvent(List<RealEstate> result) {
            this.estates = result;
        }
    }

    static public class QueriedEstateRetrievedEvent {
        public RealEstate estate;

        public QueriedEstateRetrievedEvent(RealEstate result) {
            this.estate = result;
        }
    }

    static public class RoomListRetrievedFromFirebaseEvent {
        public final List<Chamber> rooms;

        public RoomListRetrievedFromFirebaseEvent(List<Chamber> rooms) {
            this.rooms = rooms;
        }
    }

    static public class RoomRetrievedFromFirebaseEvent {
        public final Chamber room;

        public RoomRetrievedFromFirebaseEvent(Chamber chamber) {
            this.room = chamber;
        }
    }

    static public class KeyCreatedForNewRoom {
        public String estateId;
        public String roomId;

        public KeyCreatedForNewRoom(String estateId, String roomId) {
            this.estateId = estateId;
            this.roomId = roomId;
        }
    }
}
