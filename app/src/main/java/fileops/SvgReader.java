package fileops;

import android.os.Environment;
import android.util.Pair;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Vector;

/**
 * Created by Xywzel on 20/04/16.
 * Used to parse flight coordinates from svg file
 * Use coordinates vector as input for Flight Path constructor
 */
public class SvgReader {
    public Vector<Pair<Double, Double>> coordinates;
    public SvgReader (String filename) {
        String pathLine = null;
        try {
            //File sdcard = Environment.getExternalStorageDirectory();
            File file = new File(filename);
            FileReader fr = new FileReader(file);
            BufferedReader br = new BufferedReader(fr);
            for (String line; (line = br.readLine()) != null; ){
                if (line.trim().startsWith("<path")){
                    while (!line.trim().startsWith("d=")){
                        line = br.readLine();
                    }
                    pathLine = line.trim();
                }
            }
            br.close();
            fr.close();
        } catch (FileNotFoundException e) {
            System.out.println("This here is a missing file problem, sir.");
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("We can't read next line, sir.");
            e.printStackTrace();
        }
        if (pathLine == null) {
            System.out.println("There was no path there");
        } else {
            String simplePath = pathLine.replace("d=\"M", "").replace("z", "").replace("L", "").trim();
            String[] places = simplePath.split(" ");
            coordinates = new Vector<Pair<Double, Double>>();
            for(String place : places){
                Double a = Double.parseDouble(place.trim().split(",")[0]);
                Double b = Double.parseDouble(place.trim().split(",")[1]);
                coordinates.add(new Pair<Double, Double>(a,b));
            }
        }
    }
}
