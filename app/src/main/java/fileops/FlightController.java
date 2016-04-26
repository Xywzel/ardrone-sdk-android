package fileops;

import android.util.Pair;

import com.parrot.freeflight.service.DroneControlService;

/**
 * This class handles flying to coordinates by reading path.
 * Created by xywzel on 24/04/16.
 */
public class FlightController {
    private FlightPath path;
    private Pair<Double, Double> currentPlace;
    private Pair<Double, Double> targetPlace;
    private DroneControlService dcs;
    private Pair<Double, Double> currentSpeed;
    public double time;
    public double yawTime;

    private static double epsilon = 1.0;

    public FlightController(FlightPath path, DroneControlService dcs){
        this.path = path;
        this.currentPlace = new Pair<Double, Double>(0.0, 0.0);
        this.currentSpeed = new Pair<Double, Double>(0.0, 0.0);
        this.targetPlace = path.nextCoordinate();
        this.time = 0.0f;
        this.dcs = dcs;
        this.yawTime = 0.0f;
    }

    public static FlightController fromFile(String filename, DroneControlService dcs){
        SvgReader svg = new SvgReader(filename);
        FlightPath path = new FlightPath(svg.coordinates);
        return new FlightController(path, dcs);
    }

    public void update() {
        if (time == 0.0) {
            time = (double) System.currentTimeMillis() / 1000.0;
        } else {
            double currentTime = (double) System.currentTimeMillis() / 1000.0;
            double deltaTime = currentTime - time;
            time = currentTime;
            currentPlace = new Pair<Double, Double>(
                    currentPlace.first + 10 * deltaTime * currentSpeed.first,
                    currentPlace.second + 10 * deltaTime * currentSpeed.second);
        }
        if (distanceToCurrentTarget() < epsilon)
        {
            this.targetPlace = path.nextCoordinate();
            dcs.setYaw(1);
            yawTime = (double) System.currentTimeMillis() / 1000;

        }
        Pair<Double, Double> newSpeed = getTargetSpeed();
        dcs.setProgressiveCommandEnabled(true);
        dcs.setProgressiveCommandCombinedYawEnabled(true);
        if ((double) System.currentTimeMillis() / 1000 > yawTime + 0.5) {
            dcs.setYaw(0);
            if (newSpeed.first > 0.0) dcs.moveRight(newSpeed.first.floatValue());
            if (newSpeed.first < 0.0) dcs.moveLeft(newSpeed.first.floatValue());
            if (newSpeed.second > 0.0) dcs.moveBackward(newSpeed.second.floatValue());
            if (newSpeed.second < 0.0) dcs.moveForward(newSpeed.second.floatValue());
        }
        currentSpeed = newSpeed;
    }

    public void stop(){
        double currentTime = (double) System.currentTimeMillis() / 1000.0;
        double deltaTime = currentTime - time;
        time = currentTime;
        currentPlace = new Pair<Double, Double>(
                currentPlace.first + 0.1 * deltaTime * currentSpeed.first,
                currentPlace.second + 0.1 * deltaTime * currentSpeed.second);
        time = 0.0;
        currentSpeed = new Pair<Double, Double>(0.0, 0.0);
        dcs.moveRight(0.0f);
        dcs.moveForward(0.0f);
    }

    private double distanceToCurrentTarget(){
        double dx = targetPlace.first - currentPlace.first;
        double dy = targetPlace.second - currentPlace.second;
        return Math.sqrt(dx * dx + dy * dy);
    }

    private Pair<Double, Double> getTargetSpeed(){
        double dx = targetPlace.first - currentPlace.first;
        double dy = targetPlace.second - currentPlace.second;
        double scale = Math.max(Math.abs(dx), Math.abs(dy));
        return new Pair<Double, Double>(dx / scale, dy / scale);
    }
}