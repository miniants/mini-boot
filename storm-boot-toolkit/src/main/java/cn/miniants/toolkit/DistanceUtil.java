package cn.miniants.toolkit;

/**
 * @author guoqianyou
 * @date 2022/6/16 12:25
 */
public class DistanceUtil {
    private static final double EARTH_RADIUS = 6371000;//赤道半径(单位m)

    private static double rad(double d) {
        return d * Math.PI / 180.0;
    }

    public static double GetDistance(double lon1, double lat1, double lon2, double lat2) {
        double radLat1 = rad(lat1);
        double radLat2 = rad(lat2);
        double a = radLat1 - radLat2;
        double b = rad(lon1) - rad(lon2);
        double s = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a / 2), 2) + Math.cos(radLat1) * Math.cos(radLat2) * Math.pow(Math.sin(b / 2), 2)));
        s = s * EARTH_RADIUS;
        s = Math.round(s * 10000) / 10000;
        return s;
    }
}
