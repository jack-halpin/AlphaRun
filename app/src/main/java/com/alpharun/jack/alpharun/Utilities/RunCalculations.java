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
    public static double getPaceFromLocation(Location prevLoc, Location currLoc){
        //Get current coordinates
        double lat1 = prevLoc.getLatitude();
        double lng1 = prevLoc.getLongitude();
        double lat2 = currLoc.getLatitude();
        double lng2 = currLoc.getLongitude();

        //Get the distance in meters between the two points
        double distance = distFrom(prevLoc, currLoc);

        //Get the time different
        double timeDifference = currLoc.getTime() - prevLoc.getTime();

        //Pace is given as average speed which is distance/time
        double pace = distance/timeDifference; //m/s
        return pace;
    }
}
