package callisto.quotermvp.estatedetails.mvp;

import callisto.quotermvp.realm.Helper;
import callisto.quotermvp.realm.model.Estate;

public class EstateDetailsModel {
    private long estateId;

    public EstateDetailsModel(long estateId) {
        this.estateId = estateId;
    }

    public Estate getEstate() {
        return Helper.getInstance().get(estateId);
    }

    void storeInRealm(String address, String city, double lat, double lng, String owner) {
//        Estate estate = Helper.getInstance().get(estateId);

        Estate estate = new Estate();

        estate.setId(estateId);

        estate.setAddress(address);

        estate.setCity(city);

        estate.setLatitude(lat);

        estate.setLongitude(lng);

        estate.setOwner(owner);

        Helper.getInstance().save(estate);
    }
}
