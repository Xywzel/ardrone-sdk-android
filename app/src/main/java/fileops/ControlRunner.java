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

    private FlightPath flightPath;
    private FlightController flightController;

    public ControlRunner(DroneControlService dcs, String filename){
        //fc = FlightController.fromFile(filename, dcs);

        Vector<Pair<Double, Double>> path = new Vector<Pair<Double, Double>>();

        Pair<Double, Double> p1 = new Pair<Double, Double>(1.0,1.0);
        Pair<Double, Double> p2 = new Pair<Double, Double>(1.0,2.0);
        Pair<Double, Double> p3 = new Pair<Double, Double>(2.0,2.0);
        Pair<Double, Double> p4 = new Pair<Double, Double>(2.0,1.0);

        path.add(p1);
        path.add(p2);
        //path.add(p3);
        //path.add(p4);

        flightPath = new FlightPath(path);
        flightController = new FlightController(flightPath, dcs);

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