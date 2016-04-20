package fileops;

import android.util.Pair;

import java.util.Vector;

/**
 * Created by xywzel on 20/04/16.
 * This class represents path that can be flight
 * It takes vector of float pairs, normalizes them
 * and has iterator that can be used to get next pair of coordinates
 */
public class FlightPath {
    public Vector<Pair<Float, Float>> normalizedCoordinates;
    private int currentIndex;
    public FlightPath(Vector<Pair<Float, Float>> coordinates){
        currentIndex = 0;
        float maxDist = 0.0f;
        Vector<Pair<Float, Float>> centralCoordinates = new Vector();
        for(Pair<Float, Float> coord : coordinates) {
            Float x = coord.first - coordinates.elementAt(0).first;
            Float y = coord.second - coordinates.elementAt(0).second;
            if(x * x + y * y > maxDist){
                maxDist = x * x + y * y;
            }
            centralCoordinates.add(new Pair<Float, Float>(x,y));
        }
        normalizedCoordinates = new Vector();
        Float factor = 100.0f / maxDist;
        for (Pair<Float, Float> coord : centralCoordinates){
            normalizedCoordinates.add(new Pair<Float, Float>(coord.first * factor, coord.second * factor));
        }
    }
    public Pair<Float, Float> nextCoordinate(){
        currentIndex++;
        return normalizedCoordinates.elementAt(currentIndex - 1);
    }
}
