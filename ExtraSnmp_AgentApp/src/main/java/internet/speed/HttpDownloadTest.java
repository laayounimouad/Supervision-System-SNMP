package internet.speed;

import java.io.InputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * @author erdigurbuz
 */
public class HttpDownloadTest extends Thread {
  	private static boolean stop=false;  
  	public void resetStop() {
  		stop=false;
  	}
	public void setStop() {
		stop=true;
	}
	public boolean getStop() {
		return stop;
	}
    public String fileURL = "";
    long startTime = 0;
    long endTime = 0;
    double downloadElapsedTime = 0;
    int downloadedByte = 0;
    double finalDownloadRate = 0.0;
    boolean finished = false;
    double instantDownloadRate = 0;
    int timeout = 15;

    HttpURLConnection httpConn = null;

    public HttpDownloadTest(String fileURL) {
        this.fileURL = fileURL;
    }

    private double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        BigDecimal bd;
        try {
            bd = new BigDecimal(value);
        } catch (Exception ex) {
            return 0.0;
        }
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

    public double getInstantDownloadRate() {
        return instantDownloadRate;
    }

    public void setInstantDownloadRate(int downloadedByte, double elapsedTime) {

        if (downloadedByte >= 0) {
            this.instantDownloadRate = round((Double) (((downloadedByte * 8) / (1000 * 1000)) / elapsedTime), 2);
        } else {
            this.instantDownloadRate = 0.0;
        }
    }

    public double getFinalDownloadRate() {
        return round(finalDownloadRate, 2);
    }

    public boolean isFinished() {
        return finished;
    }

    @Override
    public void run() {
        URL url = null;
        downloadedByte = 0;
        int responseCode = 0;
        List<String> fileUrls = new ArrayList<String>();
        fileUrls.add(fileURL + "random4000x4000.jpg");
        fileUrls.add(fileURL + "random3000x3000.jpg");
        startTime = System.currentTimeMillis();

        outer:
        	
        for (String link : fileUrls) {
        	System.out.println("inside dounw");
            try {
                url = new URL(link);
                httpConn = (HttpURLConnection) url.openConnection();
                responseCode = httpConn.getResponseCode();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            if(stop) {
            	System.out.println("entered");
            	break;
            }else System.out.println("I did not hit her");
            try {
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    byte[] buffer = new byte[10240];
                    InputStream inputStream = httpConn.getInputStream();
                    int len = 0;
                    while ((len = inputStream.read(buffer)) != -1) {
                    	if(stop) {
                    		System.out.println("2-entered");
                    		  inputStream.close();
                              httpConn.disconnect();
                    		break outer;
                        
                    	}else System.out.println("2-I did not hit her");
                        downloadedByte += len;
                        endTime = System.currentTimeMillis();
                        downloadElapsedTime = (endTime - startTime) / 1000.0;
                        setInstantDownloadRate(downloadedByte, downloadElapsedTime);
                        System.out.println("14");
                        
                        if (downloadElapsedTime >= timeout) {
                        	
                            break outer;
                        }
                    }
                    inputStream.close();
                    httpConn.disconnect();
                    System.out.println("17");
                } else {
                    System.out.println("Link not found...");
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        endTime = System.currentTimeMillis();
        downloadElapsedTime = (endTime - startTime) / 1000.0;
        finalDownloadRate = ((downloadedByte * 8) / (1000 * 1000.0)) / downloadElapsedTime;
        System.out.println("19");
        finished = true;
    }

}
