package com.alpharun.jack.alpharun.Utilities;

import android.location.Location;

/**
 * Created by Jack on 16/03/2017.
 */

public class RunCalculations {

    //Function for calculating the distnace in meters between two points. Need to investiage if this is good
    //Just copied from SO.
    public static double distFrom(Location prevLoc, Location currLoc) {

        //Get current coordinates
        double lat1 = prevLoc.getLatitude();
        double lng1 = prevLoc.getLongitude();
        double lat2 = currLoc.getLatitude();
        double lng2 = currLoc.getLongitude();


        double earthRadius = 6371000; //meters
        double dLat = Math.toRadians(lat2-lat1);
        double dLng = Math.toRadians(lng2-lng1);
        double a = Math.sin(dLat/2) * Math.sin(dLat/2) +
                Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                        Math.sin(dLng/2) * Math.sin(dLng/2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
        float dist = (float) (earthRadius * c);

        return dist;
    }

    //Function for calculating the pace of the runner based on a previous coordinate and their
    //current coordinate
    //EDIT: Might just be easier to calculate the pace of a current run based on the distance travelled
    //Divited by the time of the current run considering these values are kept track of during the run.
    public static double getPaceFromLocation(int distance, long time){
        //Pace in s/Km
        double pace = time/distance;

        //Divide by 60 to get Min/Km
        return pace / 60;
    }
}
