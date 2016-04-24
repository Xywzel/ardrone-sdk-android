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
    public Vector<Pair<Double, Double>> normalizedCoordinates;
    private int currentIndex;
    public FlightPath(Vector<Pair<Double, Double>> coordinates){
        currentIndex = 0;
        double maxDist = 0.0f;
        Vector<Pair<Double, Double>> centralCoordinates = new Vector<Pair<Double, Double>>();
        for(Pair<Double, Double> coord : coordinates) {
            Double x = coord.first - coordinates.elementAt(0).first;
            Double y = coord.second - coordinates.elementAt(0).second;
            if(x * x + y * y > maxDist){
                maxDist = x * x + y * y;
            }
            centralCoordinates.add(new Pair<Double, Double>(x,y));
        }
        normalizedCoordinates = new Vector<Pair<Double, Double>>();
        Double factor = 100.0 / maxDist;
        for (Pair<Double, Double> coord : centralCoordinates){
            normalizedCoordinates.add(new Pair<Double, Double>(coord.first * factor, coord.second * factor));
        }
    }
    public Pair<Double, Double> nextCoordinate(){
        Pair<Double, Double> next = normalizedCoordinates.elementAt(currentIndex);
        currentIndex = (currentIndex + 1) % normalizedCoordinates.size();
        return next;
    }
}