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
        double deltaTime = 0.0;
        if (time == 0.0) {
            time = (double) System.currentTimeMillis() / 1000.0;
        } else {
            double currentTime = (double) System.currentTimeMillis() / 1000.0;
            deltaTime = currentTime - time;
            time = currentTime;
            currentPlace = new Pair<Double, Double>(
                    currentPlace.first + 10.0 * deltaTime * currentSpeed.first,
                    currentPlace.second + 10.0 * deltaTime * currentSpeed.second);
        }
        double epsilon = 9.0 * deltaTime * Math.sqrt(currentSpeed.first*currentSpeed.first + currentSpeed.second*currentSpeed.second);
        if (distanceToCurrentTarget() < epsilon) this.targetPlace = path.nextCoordinate();

        Pair<Double, Double> newSpeed = getTargetSpeed();
        dcs.setProgressiveCommandEnabled(true);
        dcs.setProgressiveCommandCombinedYawEnabled(true);
        dcs.moveRight(newSpeed.first.floatValue());
        dcs.moveBackward(newSpeed.second.floatValue());
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
        return new Pair<Double, Double>(0.5 * dx / scale, 0.5 * dy / scale);
    }
}
