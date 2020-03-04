package com.example.soundlevelmeter;


import android.app.Service;
import android.content.Intent;
import android.location.Location;
import android.media.MediaRecorder;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

import com.example.soundlevelmeter.Interface.CallBackFromService;
import com.example.soundlevelmeter.Singleton.Singleton;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;

import java.io.IOException;


public class MyService extends Service {

    private FusedLocationProviderClient fusedLocationClient;
    private LocationRequest locationRequest;
    private LocationCallback locationCallback;
    MediaRecorder mRecorder;
    Thread thread;
    public CallBackFromService callBack;
    private double mEMA = 0.0d;
    private final double EMA_FILTER = 0.6d;
    private double sound;
    public boolean isStopThread = false;
    private final IBinder binder = new LocalBinder();

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return android.app.Service.START_NOT_STICKY;
    }


    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }


    public void startSoundMeter() {

        try {
            if (mRecorder == null) {
                Log.d("EEE", "create mRecorder ");
                mRecorder = new MediaRecorder();
                mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
                mRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
                mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
                mRecorder.setOutputFile("/dev/null");
                mRecorder.prepare();
                mRecorder.start();
                startGetDataMediaRecoder();
            }
        } catch (IOException e) {
            mRecorder = null;
            e.printStackTrace();
            Log.d("EEE", "IOException e.printStackTrace(); " + e.toString());
        } catch (RuntimeException e) {
            mRecorder = null;
            e.printStackTrace();
            Log.d("EEE", "RuntimeException e.printStackTrace(); " + e.toString());
        }
    }


    private void startGetDataMediaRecoder() {

        final Handler handler = new Handler();
        final Runnable runnable = new Runnable() {
            @Override
            public void run() {
                Log.d("EEE", "noise = " + sound);
                if (callBack != null) {
                    callBack.callBackFromSoundMeter((int) sound);
                }
            }
        };
        thread = new Thread() {
            @Override
            public void run() {

                while (thread != null) {
                    try {
                        if (isStopThread) {
                            return;
                        }
                        sound = getSoundDb();
                        handler.post(runnable);
                        Thread.sleep(500);
                        if (isStopThread) {
                            return;
                        }
                        Log.i("EEE", "Sleep");

                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                }
            }
        };
        thread.start();
    }

    public void stopRecorder() {
        if (mRecorder != null) {
            mRecorder.stop();
            mRecorder.release();
            mRecorder = null;
        }
    }

    public double getSoundDb() {
        double amp = getAmplitude();
        double ampEMA = getAmplitudeEMA(amp);
        //  Log.d("EEE", "soundDb() : " + "  amp -  " + amp );
        double result = 20 * Math.log10(amp / 5d);
        if (result < 0) {
            return 0;
        }
        return result;
    }


    private double getAmplitude() {
        if (mRecorder != null) {
            //   Log.d("EEE", "getAmplitude " + mRecorder.getMaxAmplitude());
            return mRecorder.getMaxAmplitude();
        } else
            return 0;

    }

    private double getAmplitudeEMA(double amp) {

        mEMA = EMA_FILTER * amp + (1.0 - EMA_FILTER) * mEMA;

        return mEMA;
    }

    public void startSpeedometer() {
        if (fusedLocationClient == null) {
            fusedLocationClient = LocationServices.getFusedLocationProviderClient(getApplicationContext());
            locationRequest = LocationRequest.create();
            locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
            locationRequest.setInterval(100); // 0,1 seconds
            //  locationRequest.setFastestInterval(5 * 10); // 0,05 seconds
        }

        startLocationProvider();
    }


    protected void startLocationProvider() {


        locationCallback = new LocationCallback() {

            private long lastTime;
            private long currentTime;
            private Location lastLocation;
            private Location currentLocation;
            private long intervalTime;
            boolean isFirstTime = true;
            final Handler handler = new Handler();
            int speed;
            LocationResult locationResult;

            final Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    callBack.callBackFromSpeedometer(speed);
                }
            };

            public Thread threadLocation = new Thread() {

                @Override
                public void run() {

                    currentLocation = locationResult.getLastLocation();
                    if (lastLocation == null) {
                        Log.d("EEE", "lastLocation == null");
                        lastLocation = currentLocation;
                        lastTime = System.currentTimeMillis();

                    } else {

                        float distanceBetween = currentLocation.distanceTo(lastLocation);

                        currentTime = System.currentTimeMillis();
                        intervalTime = currentTime - lastTime;

                        double latitude = currentLocation.getLatitude();
                        double longitude = currentLocation.getLongitude();
                        String msg = "New Latitude: " + latitude + "New Longitude: " + longitude;

                        float speedInFloat = distanceBetween / (float) intervalTime * 3600f;

                        speed = Math.round(speedInFloat);

                        Log.d("EEE", "lmsg   " + msg + "  getSpeed " + currentLocation.getSpeed() + "  distance  -  " + distanceBetween +
                                "  interval Time   - " + intervalTime + " speed -  " + speedInFloat + " speed -  " + speed);

                        if (callBack != null && !isFirstTime) {
                            handler.post(runnable);

                        }

                        lastTime = currentTime;
                        lastLocation = currentLocation;
                        isFirstTime = false;

                    }
                    return;
                }
            };

            @Override
            public void onLocationResult(final LocationResult locationResult) {
                this.locationResult = locationResult;
                if (locationResult == null) {
                    return;
                }
                threadLocation.start();

            }
        };
        fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, null);
    }

    public void stopSpeedometer() {
        fusedLocationClient.removeLocationUpdates(locationCallback);
        callBack.callBackFromSpeedometer(0);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        stopSpeedometer();
        stopRecorder();
        isStopThread = true;
        Singleton.getInstance().setStatusSpeedometer(false);
        Singleton.getInstance().setStatusService(false);
        Singleton.getInstance().setStatusSoundMeter(false);
        Singleton.getInstance().setStatusWriteTrack(false);
        Log.d("EEE", "onDestroy   MyService ");
    }


    @Override
    public boolean onUnbind(Intent intent) {

        //  fusedLocationClient.removeLocationUpdates(locationCallback);
        Log.d("EEE", "onUnbind in   MyService ");
        return super.onUnbind(intent);
    }


    public class LocalBinder extends Binder {

        public MyService getService() {
            return MyService.this;
        }

        public void setCallBackFromService(CallBackFromService callBack) {
            MyService.this.callBack = callBack;

        }


    }
}
