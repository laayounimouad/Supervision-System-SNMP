package internet.speed;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.HashMap;

/**
 * @author erdigurbuz
 */
public class PingTest extends Thread {

    HashMap<String, Object> result = new HashMap<String, Object>();
    String server = "";
    int count;
    double instantRtt = 0;
    double avgRtt = 0.0;
    boolean finished = false;
    boolean started = false;
    public boolean error = false;
    public PingTest(String serverIpAddress, int pingTryCount) {
        this.server = serverIpAddress;
        this.count = pingTryCount;
    }

    public double getAvgRtt() {
        return avgRtt;
    }

    public double getInstantRtt() {
        return instantRtt;
    }

    public boolean isFinished() {
        return finished;
    }

    @Override
    public void run() {
        try {
            ProcessBuilder ps = new ProcessBuilder("ping", "-n ", count+"" ,this.server);
            ps.redirectErrorStream(true);
            Process pr = ps.start();

            BufferedReader in = new BufferedReader(new InputStreamReader(pr.getInputStream()));
            String line;
            String output = "";
            while ((line = in.readLine()) != null) {
                if (line.contains("Reply"))   {
                    instantRtt = Double.parseDouble(line.split(" ")[line.split(" ").length - 2].replace("time=", "").replace("ms", ""));
                }
                if (line.contains("Minimum")) {
                	
                    avgRtt = Double.parseDouble(line.split(" ")[6].replace(",", "").replace("ms", ""));
                    break;
                }
            }
            pr.waitFor();
            in.close();

        } catch (Exception e) {
            finished = true;

        	error = true;
            e.printStackTrace();
        }

        finished = true;
    }

}