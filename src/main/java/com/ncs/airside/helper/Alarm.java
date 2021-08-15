package com.ncs.airside.helper;

import com.ncs.airside.controller.SoundController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;
import java.util.Timer;
import java.util.TimerTask;

@Component
public class Alarm {

    public static float SAMPLE_RATE = 8000f;

    public Timer timer ;
    public TimerTask timerTask ;
    public boolean isAlarmTimerRunning = false;

    private static Logger logger = LoggerFactory.getLogger(Alarm.class);

    public Alarm(){


    }

    public static void tone(int hz, int msecs)
    {
        tone(hz, msecs, 1.0);
    }

    public static void tone(int hz, int msecs, double vol){
        try {
            byte[] buf = new byte[1];
            AudioFormat af =
                    new AudioFormat(
                            SAMPLE_RATE, // sampleRate
                            8,           // sampleSizeInBits
                            1,           // channels
                            true,        // signed
                            false);      // bigEndian
            SourceDataLine sdl = AudioSystem.getSourceDataLine(af);
            sdl.open(af);
            sdl.start();
            for (int i = 0; i < msecs * 8; i++) {
                double angle = i / (SAMPLE_RATE / hz) * 2.0 * Math.PI;
                buf[0] = (byte) (Math.sin(angle) * 127.0 * vol);
                sdl.write(buf, 0, 1);
            }
            sdl.drain();
            sdl.stop();
            sdl.close();
        } catch (Exception e){
            //do nothing
        }
    }

    public void onAlarm(){
        try {
            timer = new Timer();
            timerTask = new TimerTask() {
                @Override
                public void run() {
                    Alarm.tone(5000, 600);
                }
            };
        } catch (Exception ex){
            logger.error(" EXCPETION HAPPENING IN TIMER ");
        }
        isAlarmTimerRunning = true;
        timer.schedule(timerTask,1000,1000);
    }

    public void offAlarm(){
        isAlarmTimerRunning = false;
        timer.cancel();
        try {
            timer = new Timer();
            timerTask = new TimerTask() {
                @Override
                public void run() {
                    Alarm.tone(5000, 600);
                }
            };
        } catch (Exception ex){
            logger.error(" EXCPETION HAPPENING IN TIMER ");
        }
    }

}
