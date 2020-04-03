package com.example.soundlevelmeter;


import android.Manifest;
import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.MediaRecorder;
import android.os.Binder;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.RequiresApi;
import androidx.lifecycle.LifecycleService;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import com.example.soundlevelmeter.Interface.CallBackForStaticsits;
import com.example.soundlevelmeter.Interface.CallBackFromService;
import com.example.soundlevelmeter.Room.DataEvent;
import com.example.soundlevelmeter.Singleton.Singleton;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;

import java.io.IOException;

public class MyService extends LifecycleService {

    private LocationRequest locationRequest;
    private LocationCallback locationCallback;
    private MediaRecorder mRecorder;
    private Thread thread;
    public CallBackFromService callBack;
    public CallBackForStaticsits callBackForStaticsits;
    private boolean isFirstValueSpeed = true;
    private double mEMA = 0.0d;
    private final double EMA_FILTER = 0.7d;
    private int sound;
    public boolean isStopThread = false;
    private final IBinder binder = new LocalBinder();
    private long timeCorrector = 0;
    private LocationManager mLocationManager;
    private FusedLocationProviderClient fusedLocationClient;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        return Service.START_NOT_STICKY;
    }


    @Override
    public IBinder onBind(Intent intent) {
        super.onBind(intent);
        return binder;
    }


    public void startSoundMeter() {

        MutableLiveData<Boolean> mutableLiveDataStatus = Singleton.getInstance().getIsStatusWriteTrackLiveData();
        mutableLiveDataStatus.observe(this, new Observer<Boolean>() {

            @Override
            public void onChanged(Boolean aBoolean) {

                if (aBoolean) {
                    if (!Singleton.getInstance().getList().isEmpty()) {

                        timeCorrector = System.currentTimeMillis() - Singleton.getInstance().getLastTime();
                    } else {
                        timeCorrector = 0;

                    }

                }
            }
        });

        try {
            if (mRecorder == null) {
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

                if (callBack != null) {
                    callBack.callBackFromSoundMeter(sound);
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
                        //     Log.i("EEE", "Sleep");

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

    public int getSoundDb() {
        double amp = getAmplitude();
        double ampEMA = getAmplitudeEMA(amp);
        ampEMA += 40;

        double temp = (Math.log(ampEMA / 2) / Math.log(40) - 0.8d);

        int result = (int) Math.round(50d * temp);
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
        if (mEMA <= 230) {
            mEMA = mEMA + mEMA * 1.0d * (230d - mEMA) / 230d;
        }
        return mEMA;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void startSpeedometer() {

        if (fusedLocationClient == null) {
            fusedLocationClient = LocationServices.getFusedLocationProviderClient(getApplicationContext());
            locationRequest = LocationRequest.create();
            locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
            locationRequest.setInterval(1000);
            locationRequest.setFastestInterval(500);
            startLocationProvider();
        }

//        if (mLocationManager == null) {
//            mLocationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
//
//            if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) !=
//                    PackageManager.PERMISSION_GRANTED &&
//                    checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION)
//                            != PackageManager.PERMISSION_GRANTED) {
//
//                return;
//            }
//            mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0,
//                    0, mLocationListener);
//
//        }

    }

//    private LocationListener mLocationListener = new LocationListener() {

    //  private long lastTime, meanTime, currentTime;
    //  private Location lastLocation, meanLocation;
//        private Location    currentLocation;
    //   private long intervalTime;
    //  boolean isFirstTime = true;
    //    final Handler handler = new Handler();
//        int speed;
    //   private Location locationResult;


//        Runnable runnableForSoundMeter = new Runnable() {
//            @Override
//            public void run() {
//                if (callBack != null) {
//                    callBack.callBackFromSpeedometer(speed);
//                }
//            }
//        };
//
//        Runnable runnableForStatistics = new Runnable() {
//            @Override
//            public void run() {
//                if (callBackForStaticsits != null) {
//                    callBackForStaticsits.callBackForUpDataGraph();
//                }
//            }
//        };

//        private void callBackForStatistics(){
//            if (callBackForStaticsits != null) {
//                callBackForStaticsits.callBackForUpDataGraph();
//            }
//        }
//
//        private void callBackForSoundMeter(){
//            if (callBack != null) {
//                callBack.callBackFromSpeedometer(speed);
//            }
//        }


//        Thread threadLocation = new Thread() {
//
//            @Override
//            public void run() {

//                if(lastLocation == null){
//                    lastLocation = currentLocation;
//                    lastTime = System.currentTimeMillis();
//                    return;
//                }
//
//                if (meanLocation == null) {
//                    meanLocation = currentLocation;
//                    meanTime = System.currentTimeMillis();
//
//                } else {
//                    float distanceBetween = currentLocation.distanceTo(lastLocation);
//
//                    currentTime = System.currentTimeMillis();
//                    intervalTime = currentTime - lastTime;
//
//                    float speedInFloat = distanceBetween / (float) intervalTime * 3600f;
//
//                    speed = Math.round(speedInFloat);
//                    if (isFirstValueSpeed) {
//                        isFirstValueSpeed = false;
//                        return;
//                    }

//                    if (Singleton.getInstance().isStatusWriteTrack()) {
//                        writeTrack();
//                    }
//
//                    if (callBack != null && !isFirstTime) {
//                        handler.post(runnableForSoundMeter);
//                    }

//                    lastTime = currentTime;
//                    lastLocation = currentLocation;
//                    isFirstTime = false;
//                    Log.d("EEE", "Speed get Speed = " +   currentLocation.getSpeed() +  " My Speed = " + speed);
//                }
////            }
//        };

//        @Override
//        public void onLocationChanged(final Location location) {
//
//            currentLocation = location;
//          //  threadLocation.start();
//            speed = Math.round(currentLocation.getSpeed());
//
//            if (Singleton.getInstance().isStatusWriteTrack()) {
//                writeTrack();
//            }
//
//            if (callBack != null) {
//              //  handler.post(runnableForSoundMeter);
//                callBackForSoundMeter();
//            }
//            Log.d("EEE", "Speed get Speed =  " + speed);
//
//
//        }

//        @Override
//        public void onStatusChanged(String provider, int status, Bundle extras) {
//            Log.d("EEE", "onStatusChanged" + provider  + "int =" + status);
//        }
//
//        @Override
//        public void onProviderEnabled(String provider) {
//            Log.d("EEE", "onProviderEnabled");
//        }
//
//        @Override
//        public void onProviderDisabled(String provider) {
//            Log.d("EEE", "onProviderDisabled");
//        }

//        private void writeTrack() {
//            DataEvent event = new DataEvent();
//            event.setSpeed(speed);
//            int sound = getSoundDb();
//            event.setSound(sound);
//
//            event.setTime(System.currentTimeMillis() - timeCorrector);
//
//            Singleton.getInstance().addDataToList(event);
//            if (callBackForStaticsits != null) {
//              //  handler.post(runnableForStatistics);
//                callBackForStatistics();
//            }
//
//        }
//
//    };


    protected void startLocationProvider() {


        locationCallback = new LocationCallback() {

            //   private long lastTime;
            //   private long currentTime;
            //   private Location lastLocation;
            //   private Location currentLocation;
            //   private long intervalTime;
            //   boolean isFirstTime = true;
            //   final Handler handler = new Handler();
            int speed;
            LocationResult locationResult;

//            final Runnable runnableForSoundMeter = new Runnable() {
//                @Override
//                public void run() {
//                    if (callBack != null) {
//                        callBack.callBackFromSpeedometer(speed);
//                    }
//
//                }
//            };
//            final Runnable runnableForStatistics = new Runnable() {
//                @Override
//                public void run() {
//                    if (callBackForStaticsits != null) {
//                        callBackForStaticsits.callBackForUpDataGraph();
//                    }
//                }
//            };
//
//            private Thread threadLocation = new Thread() {
//
//                @Override
//                public void run() {
//
//                    currentLocation = locationResult.getLastLocation();
//                    if (lastLocation == null) {
//                        lastLocation = currentLocation;
//                        lastTime = System.currentTimeMillis();
//
//                    } else {
//                        float distanceBetween = currentLocation.distanceTo(lastLocation);
//
//                        currentTime = System.currentTimeMillis();
//                        intervalTime = currentTime - lastTime;
//
//                        float speedInFloat = distanceBetween / (float) intervalTime * 3600f;
//
//                        speed = Math.round(speedInFloat);
//                        if (isFirstValueSpeed) {
//                            isFirstValueSpeed = false;
//                            return;
//                        }
//                        Log.d("EEE", "My speed = " + speed + " getSpeed = " + currentLocation.getSpeed());
//
//                        if (Singleton.getInstance().isStatusWriteTrack()) {
//                            writeTrack();
//                        }
//
//                        if (callBack != null && !isFirstTime) {
//                            handler.post(runnableForSoundMeter);
//                        }
//
//                        lastTime = currentTime;
//                        lastLocation = currentLocation;
//                        isFirstTime = false;
//
//                    }
//                }
//            };


//
//            }
//
//            @Override
//            public void onLocationResult(final LocationResult locationResult) {
//                this.locationResult = locationResult;
//                if (locationResult == null) {
//                    return;
//                }
//                threadLocation.start();
//
//            }


            @Override
            public void onLocationResult(LocationResult locationResult) {
                super.onLocationResult(locationResult);
                if (locationResult != null) {
                    speed = Math.round(locationResult.getLastLocation().getSpeed());
                }
                if (Singleton.getInstance().isStatusWriteTrack()) {
                    writeTrack();
                }
                if (callBack != null) {
                    callBackForSoundMeter();
                }
            }

            private void callBackForStatistics() {
                if (callBackForStaticsits != null) {
                    callBackForStaticsits.callBackForUpDataGraph();
                }
            }

            private void callBackForSoundMeter() {
                if (callBack != null) {
                    callBack.callBackFromSpeedometer(speed);
                }
            }

            private void writeTrack() {
                DataEvent event = new DataEvent();
                event.setSpeed(speed);
                int sound = getSoundDb();
                event.setSound(sound);

                event.setTime(System.currentTimeMillis() - timeCorrector);

                Singleton.getInstance().addDataToList(event);
                if (callBackForStaticsits != null) {
                    //  handler.post(runnableForStatistics);
                    callBackForStatistics();
                }

            }


        };
        fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, null);
    }

    public void stopSpeedometer() {
        if (fusedLocationClient != null) {
            fusedLocationClient.removeLocationUpdates(locationCallback);
        }
        if (callBack != null) {
            callBack.callBackFromSpeedometer(0);
        }

    }


    @Override
    public void onDestroy() {
        Log.d("EEE", "onDestroy");
        super.onDestroy();
        stopSpeedometer();
        stopRecorder();
        isStopThread = true;
        Singleton.getInstance().destroy();
    }




    public class LocalBinder extends Binder {



        public void setCallBackFromService(CallBackFromService callBack) {
            MyService.this.callBack = callBack;
        }

        public void deleteCallBackFromService() {
            MyService.this.callBack = null;
        }

        public void setCallBackForStatistics(CallBackForStaticsits callBackForStaticsits) {
            MyService.this.callBackForStaticsits = callBackForStaticsits;
        }

        public void deleteCallBackForStatistics() {
            MyService.this.callBackForStaticsits = null;
        }

        public void startSoundMeter() {
            MyService.this.startSoundMeter();
        }

        @RequiresApi(api = Build.VERSION_CODES.M)
        public void startSpeedometer() {
            MyService.this.startSpeedometer();
        }

    }
}
