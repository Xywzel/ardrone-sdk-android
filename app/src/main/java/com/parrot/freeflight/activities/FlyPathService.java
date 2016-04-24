package com.parrot.freeflight.activities;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;
import android.util.Pair;

import com.parrot.freeflight.service.DroneControlService;

import java.util.Vector;

import fileops.FlightController;
import fileops.FlightPath;

public class FlyPathService extends Service implements Runnable{

    private final IBinder binder = new LocalBinder();

    private DroneControlService droneControlService;

    private FlightPath flightPath;
    private FlightController flightController;

    private boolean flightPathRunning;

    public FlyPathService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    public class LocalBinder extends Binder
    {
        public FlyPathService getService() {
            return FlyPathService.this;
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();

        flightPathRunning = false;

        Vector<Pair<Double, Double>> path = new Vector<Pair<Double, Double>>();

        Pair<Double, Double> p1 = new Pair<Double, Double>(1.0,2.0);
        Pair<Double, Double> p2 = new Pair<Double, Double>(2.0,2.0);
        Pair<Double, Double> p3 = new Pair<Double, Double>(3.0,2.0);
        Pair<Double, Double> p4 = new Pair<Double, Double>(3.0,3.0);

        path.add(p1);
        path.add(p2);
        path.add(p3);
        path.add(p4);

        flightPath = new FlightPath(path);
        flightController = new FlightController(flightPath, droneControlService);
    }

    public void startFlight() {
        flightPathRunning = true;
    }

    public void stopFlight() {
        flightPathRunning = false;
    }

    public void setDroneControlService(DroneControlService dcs) {
        droneControlService = dcs;
    }

    public void run() {
        Log.d("flight running", "runningg");

        while(flightPathRunning) {

            Log.d("flightPathUpdate", "updating");

            try {
                this.wait(50);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            flightController.update();
            Log.d("flightPathUpdate", "updated");
        }

    }
}
