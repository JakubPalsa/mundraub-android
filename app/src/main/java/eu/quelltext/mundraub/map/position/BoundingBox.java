package eu.quelltext.mundraub.map.position;

import java.util.List;

import eu.quelltext.mundraub.common.Helper;
import eu.quelltext.mundraub.error.Logger;
import eu.quelltext.mundraub.map.PlantsCache;

public class BoundingBox {

    protected static Logger.Log log = Logger.newFor("BoundingBox");

    private final double north;
    private final double south;
    private final double east;
    private final double west;

    protected BoundingBox(double north, double east, double south, double west) {
        this.north = getLatitude(north); // latitude
        this.south = getLatitude(south); // latitude
        this.east = getLongitude(east); // longitude
        this.west = getLongitude(west); // longitude
    }

    public static BoundingBox fromWestSouthEastNorthArray(String[] bbox) {
        return new BoundingBox(
                Double.parseDouble(bbox[3]),
                Double.parseDouble(bbox[2]),
                Double.parseDouble(bbox[1]),
                Double.parseDouble(bbox[0]));
    }

    public static BoundingBox fromPositionAndRadius(IPosition position, double distanceInMeters) {
        return RadiusBoundingBox.fromPositionAndRadius(position, distanceInMeters);
    }

    public static double getLongitude(double longitude) {
        longitude = (longitude + 180) % 360 - 180;
        while (longitude < -180) {
            longitude += 360;
        }
        return longitude;
    }

    public static double getLatitude(double latitude) {
        if (latitude > 90 || latitude < -90) {
            log.e("Invalid latitude", latitude + "");
        }
        return latitude;
    }

    public String asSqlQueryString(String columnLongitude, String columnLatitude) {
        return  " ( " + columnLongitude + " < " + east + (east < west ? " or " : " and ") + columnLongitude + " > " + west + " ) " +
                " and " +
                " ( " + columnLatitude + " > " + south + " and " + columnLatitude + " < " + north + ")";
    }

    public List<PlantsCache.Marker> selectPositionsInsideAfterSQLQuery(List<PlantsCache.Marker> positions) {
        return positions;
    }

    public static double distanceInMetersBetween(IPosition p1, IPosition p2) {
        return Helper.distanceInMetersBetween(p1.getLongitude(), p1.getLatitude(), p2.getLongitude(), p2.getLatitude());
    }
}