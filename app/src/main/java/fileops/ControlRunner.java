package fileops;

import com.parrot.freeflight.service.DroneControlService;

import java.util.Objects;

/**
 * This can be used to run flight commands in separate thread
 * Created by xywzel on 24/04/16.
 */
public class ControlRunner extends Thread {
    volatile private boolean shouldStop;

    private FlightController fc;
    private Object lock;

    public ControlRunner(DroneControlService dcs, String filename){
            fc = FlightController.fromFile(filename, dcs);
    }

    @Override
    public void run(){
        synchronized (lock) {
            shouldStop = false;
            while(!shouldStop) {
                try {
                    this.wait(50);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    shouldStop = true;
                }
                fc.update();
            }
            fc.stop();
        }
    }

    public void end(){
        shouldStop = true;
        this.interrupt();
    }
}
