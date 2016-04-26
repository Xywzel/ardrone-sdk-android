package fileops;

import android.util.Log;
import android.util.Pair;

import com.parrot.freeflight.service.DroneControlService;

import java.util.Vector;

/**
 * This can be used to run flight commands in separate thread
 * Created by xywzel on 24/04/16.
 */
public class ControlRunner extends Thread {
    volatile private boolean shouldStop;

    //private FlightController fc;

    private final Object lock = new Object();

    private FlightController flightController;

    public ControlRunner(DroneControlService dcs){
        Vector<Pair<Double, Double>> path = new Vector<Pair<Double, Double>>();

        path.add(new Pair<Double, Double>( 0.0,  0.0));
        path.add(new Pair<Double, Double>(-1.0, -1.0));
        path.add(new Pair<Double, Double>( 1.0, -2.0));
        path.add(new Pair<Double, Double>(-1.0, -3.0));
        path.add(new Pair<Double, Double>( 1.0, -4.0));
        path.add(new Pair<Double, Double>(-1.0, -5.0));
        path.add(new Pair<Double, Double>( 1.0, -6.0));
        path.add(new Pair<Double, Double>(-1.0, -7.0));
        path.add(new Pair<Double, Double>( 1.0, -8.0));
        path.add(new Pair<Double, Double>(-1.0, -9.0));
        path.add(new Pair<Double, Double>( 1.0,-10.0));

        FlightPath flightPath = new FlightPath(path);
        flightController = new FlightController(flightPath, dcs);
    }

    public ControlRunner(DroneControlService dcs, String filename){
        flightController = FlightController.fromFile(filename, dcs);
    }

    @Override
    public void run(){
        shouldStop = false;
        while(!shouldStop) {
            try {
                synchronized (lock) {
                    Thread.sleep(100);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
                shouldStop = true;
            }
            flightController.update();
            Log.d("Updating", "Updatedd");
        }
        flightController.stop();
    }

    public void end(){
        shouldStop = true;
        this.interrupt();
    }
}