package fileops;

import android.util.Pair;

import com.parrot.freeflight.service.DroneControlService;

/**
 * Created by xywzel on 24/04/16.
 */
public class FlightController {
    private FlightPath path;
    private Pair<Double, Double> currentPlace;
    private Pair<Double, Double> targetPlace;
    private DroneControlService dcs;
    public double time;

    private static double epsilon = 1.0;

    public FlightController(FlightPath path, DroneControlService dcs){
        this.path = path;
        this.currentPlace = new Pair(0.0, 0.0);
        this.targetPlace = path.nextCoordinate();
        this.time = 0.0f;
        this.dcs = dcs;
    }

    public static FlightController fromFile(String filename, DroneControlService dcs){
        SvgReader svg = new SvgReader(filename);
        FlightPath path = new FlightPath(svg.coordinates);
        return new FlightController(path, dcs);
    }

    public void update() {
        if (time == 0.0f) time = (double) System.currentTimeMillis() / 1000.0;
        if (distanceToCurrentTarget() < epsilon) this.targetPlace = path.nextCoordinate();

        if (targetPlace.first - currentPlace.first > epsilon) dcs.moveForward(0.5f);
        else if (targetPlace.first - currentPlace.first < -epsilon) dcs.moveBackward(0.5f);
        if (targetPlace.second - currentPlace.second > epsilon) dcs.moveRight(0.5f);
        else if (targetPlace.second - currentPlace.second < -epsilon) dcs.moveLeft(0.5f);
    }

    private double distanceToCurrentTarget(){
        double dy = targetPlace.first - currentPlace.first;
        double dx = targetPlace.second - currentPlace.second;
        return Math.sqrt(dy * dy + dx * dx);
    }
}
