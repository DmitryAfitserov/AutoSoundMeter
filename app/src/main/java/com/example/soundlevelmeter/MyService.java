package com.example.soundlevelmeter;


import android.app.Service;
import android.content.Intent;
import android.location.Location;
import android.media.MediaRecorder;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

import com.example.soundlevelmeter.Interface.CallBackForStaticsits;
import com.example.soundlevelmeter.Interface.CallBackFromService;
import com.example.soundlevelmeter.Singleton.DataEvent;
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
    private MediaRecorder mRecorder;
    private Thread thread;
    public CallBackFromService callBack;
    public CallBackForStaticsits callBackForStaticsits;
    private double mEMA = 0.0d;
    private final double EMA_FILTER = 0.6d;
    private int sound;
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
                //   Log.d("EEE", "noise = " + sound);
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
        //  Log.d("EEE", "soundDb() : " + "  amp -  " + amp );
        int result = (int) Math.round(20 * Math.log10(amp / 5d));
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

            final Runnable runnableForSoundMeter = new Runnable() {
                @Override
                public void run() {
                    callBack.callBackFromSpeedometer(speed);
                }
            };
            final Runnable runnableForStatistics = new Runnable() {
                @Override
                public void run() {
                    callBackForStaticsits.callBackForUpDataGraph();
                }
            };

            private Thread threadLocation = new Thread() {

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

                        float speedInFloat = distanceBetween / (float) intervalTime * 3600f;

                        speed = Math.round(speedInFloat);

                        if (Singleton.getInstance().isStatusWriteTrack()) {
                            writeTrack();
                        }

                        if (callBack != null && !isFirstTime) {
                            handler.post(runnableForSoundMeter);
                        }

                        lastTime = currentTime;
                        lastLocation = currentLocation;
                        isFirstTime = false;

                    }
                }
            };

            private void writeTrack() {
                DataEvent event = new DataEvent();
                event.setSpeed(speed);
                int sound = getSoundDb();
                event.setSound(sound);
                event.setTime(System.currentTimeMillis());

                Singleton.getInstance().addDataToList(event);
                if (callBackForStaticsits != null) {
                    handler.post(runnableForStatistics);
                }

            }

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
        if (fusedLocationClient != null) {
            fusedLocationClient.removeLocationUpdates(locationCallback);
        }
        callBack.callBackFromSpeedometer(0);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        stopSpeedometer();
        stopRecorder();
        isStopThread = true;
        Singleton.getInstance().destroy();
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

        public void setCallBackForStatistics(CallBackForStaticsits callBackForStaticsits) {
            MyService.this.callBackForStaticsits = callBackForStaticsits;

        }


    }
}
