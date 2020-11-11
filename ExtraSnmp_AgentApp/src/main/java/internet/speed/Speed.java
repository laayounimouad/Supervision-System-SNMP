package internet.speed;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.TimeUnit;


public class Speed extends Thread{

	  PingTest pingTest = null;
      HttpDownloadTest downloadTest = null;
      HttpUploadTest uploadTest = null;
	private static boolean stopOthers=false;
  	private static boolean stop=false;  
	public void setStop() {
		stop=true;
	}
	public boolean getStop() {
		return stop;
	}
	public void resetstop() {
		stop=false;
	}
	static int position = 0;
    static int lastPosition = 0;
    public boolean finished=false;
    public boolean ServerConnection=false;
    GetSpeedTestHostsHandler getSpeedTestHostsHandler = null;
    HashSet<String> tempBlackList;
    final DecimalFormat dec= new DecimalFormat("#.##");
    public double PingResult;
    public double DownloadResult;
    public double UploadResult;
    public boolean timeOut=false;
    
    public int dError=0;
    public int uError=0;
    public int pError=0;
    public boolean getServerConnection() {
    	return ServerConnection;
    }
    public boolean GetSpeedStatue() {
    	return finished;
    }
    public double GetPingResult() {
    	return PingResult;
    }
    public double GetDownloadResult() {
    	return DownloadResult;
    }
    public double GetUploadResult() {
		return UploadResult;
	}

    

 
            public void run() {
            	if(pingTest!=null )
            	System.out.println(pingTest.getState());
            	else System.out.println("hada nulll");
            	if(downloadTest!=null)
            		System.out.println("afasafsasdsdasdasfadfffaffdasdfdd asdownload state:"+downloadTest.getState());
            	else System.out.println("ta hada nulll");
            	if(uploadTest!=null)
            		System.out.println("sasdasdfasdfasdfasdfasdfasdfasdfasd upload "+uploadTest.getState());
            	else System.out.println("and hada nulll");
            	 tempBlackList = new HashSet<String>();
            	timeOut=false;
            	stopOthers=false;
            	 int position = 0;
                 int lastPosition = 0;
                //Restart test icin eger baglanti koparsa
                 System.out.println(getSpeedTestHostsHandler);
                if (getSpeedTestHostsHandler == null) {
                    getSpeedTestHostsHandler = new GetSpeedTestHostsHandler();
                    getSpeedTestHostsHandler.start();
                }
                //Get egcodes.speedtest hosts
                int timeCount = 600; //1min
                while (!getSpeedTestHostsHandler.isFinished()) {
                    timeCount--;
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                    }
                    if (timeCount <= 0) {
                    	timeOut=true;
                    	ServerConnection=false;
                       System.out.println("no connection");
                        getSpeedTestHostsHandler = null;
                        return;
                    }
                }
                ServerConnection=true;
                //Find closest server
                HashMap<Integer, String> mapKey = getSpeedTestHostsHandler.getMapKey();
                HashMap<Integer, List<String>> mapValue = getSpeedTestHostsHandler.getMapValue();
                double selfLat = getSpeedTestHostsHandler.getSelfLat();
                double selfLon = getSpeedTestHostsHandler.getSelfLon();
                double tmp = 19349458;
                double dist = 0.0;
                int findServerIndex = 0;
                for (int index : mapKey.keySet()) {
                    if (tempBlackList.contains(mapValue.get(index).get(5))) {
                        continue;
                    }
                    Location source = new Location("Source");
                    source.setLatitude(selfLat);
                    source.setLongitude(selfLon);

                    List<String> ls = mapValue.get(index);
                    Location dest = new Location("Dest");
                    dest.setLatitude(Double.parseDouble(ls.get(0)));
                    dest.setLongitude(Double.parseDouble(ls.get(1)));

                    double distance = source.distanceTo(dest);
                    if (tmp > distance) {
                        tmp = distance;
                        dist = distance;
                        findServerIndex = index;
                    }
                }
                String uploadAddr = mapKey.get(findServerIndex);
                final List<String> info = mapValue.get(findServerIndex);
                final double distance = dist;

                if (info == null) {
                    System.out.println("There was a problem in getting Host Location. Try again later.");
                    timeOut=true;
                	ServerConnection=false;
                    return;
                }

                System.out.println(String.format("Host Location: %s [Distance: %s km]", info.get(2), new DecimalFormat("#.##").format(distance / 1000)));

                
                final List<Double> pingRateList = new ArrayList<Double>();
                final List<Double> downloadRateList = new ArrayList<Double>();
                final List<Double> uploadRateList = new ArrayList<Double>();
                Boolean pingTestStarted = false;
                Boolean pingTestFinished = false;
                Boolean downloadTestStarted = false;
                Boolean downloadTestFinished = false;
                Boolean uploadTestStarted = false;
                Boolean uploadTestFinished = false;

                //Init Test
              pingTest =null;
              downloadTest=null;
              uploadTest=null;
              pingTest = new PingTest(info.get(6).replace(":8080", ""), 6);
              downloadTest = new HttpDownloadTest(uploadAddr.replace(uploadAddr.split("/")[uploadAddr.split("/").length - 1], ""));
              uploadTest = new HttpUploadTest(uploadAddr);
              pingTest.setName("Ping Test");
              downloadTest.setName("Download test");
              uploadTest.setName("upload test");
              downloadTest.resetStop();
              uploadTest.resetStop();

                //Tests
                while (!stop || !stopOthers) {
                	try {
                		
                    if (!pingTestStarted) {
                        pingTest.start();
                        pingTestStarted = true;
                    }
                    if (pingTestFinished && !downloadTestStarted) {
                        downloadTest.start();
                        downloadTestStarted = true;
                    }
                    if (downloadTestFinished && !uploadTestStarted) {
                    	System.out.println("333333333333333333333333333333333333333333333333333333333333333333333333333333");
                        uploadTest.start();
                        uploadTestStarted = true;
                    }
                   if(stop) {
                	   if(downloadTest.isAlive()) {
                		   System.out.println("1-trying to stoppp");
                		   downloadTest.setStop();
                		   System.out.println("1-waiting  to stoppp");
                		   TimeUnit.SECONDS.sleep(2);
                		   downloadTest.interrupt();
//                		   downloadTest.join();
                		   System.out.println("2-stoppeedddd");
                	   }
                	   if(uploadTest.isAlive()) {
                		   System.out.println("2-trying to stoppp");
                		   uploadTest.setStop();
                		   System.out.println("2-waiting  to stoppp");
//                		   TimeUnit.SECONDS.sleep(2);
//                		   downloadTest.interrupt();
                		   uploadTest.join();
//                		   System.out.println("2-stoppeedddd");
                		   
                	   }
                	   stopOthers=true;
                   }
                }catch(Exception e) {
                	e.printStackTrace();
                }
                    
                    //Ping Test
                    if (pingTestFinished) {
                        //Failure
                        System.out.println("before p test");

                        if (pingTest.getAvgRtt() == 0) {
                            System.out.println("Ping error...");
                        } else {
                            //Success
                        	PingResult=((pingTest.getAvgRtt()));
                        }
                    } 

                    //Download Test
                    if (pingTestFinished) {

                        if (downloadTestFinished) {
                            System.out.println("before d test");

                            //Failure
                            if (downloadTest.getFinalDownloadRate() == 0) {
                                System.out.println("Download error...");
                               
                            } else {
                                //Success
                            	DownloadResult=((downloadTest.getFinalDownloadRate()));
                            }
                        } else {
                            //Calc position
                            double downloadRate = downloadTest.getInstantDownloadRate();
                           downloadRateList.add(downloadRate);


                        }
                    }

                    //Upload Test
                    if (downloadTestFinished) {
                    	
                        if (uploadTestFinished) {
                            System.out.println("before u test");

                            //Failure
                            if (uploadTest.getFinalUploadRate() == 0) {
                                System.out.println("Upload error...");
                            } else {
                                //Success
                            	UploadResult=((uploadTest.getFinalUploadRate()));
                            }
                        } else {
                            //Calc position
                           double uploadRate = uploadTest.getInstantUploadRate();
                            uploadRateList.add(uploadRate);

                            //Update chart
                         

                        }
                    }

                    //Test bitti
                    if (pingTestFinished && downloadTestFinished && uploadTestFinished) {
                    	System.out.println("test finished");
                        break;
                    }

                    if (pingTest.isFinished()) {
                        pingTestFinished = true;
                    }
                    if (downloadTest.isFinished()) {
                        downloadTestFinished = true;
                    }
                    if (uploadTest.isFinished()) {
                        uploadTestFinished = true;
                        finished=true;
                        
                    }

                    if (pingTestStarted && !pingTestFinished) {
                        try {
                            Thread.sleep(500);
                        } catch (InterruptedException e) {
                        }
                    } else {
                        try {
                            Thread.sleep(300);
                        } catch (InterruptedException e) {
                        }
                    }
                }

              


            }
        }
    
    
  
