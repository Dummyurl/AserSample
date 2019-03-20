package info.pratham.asersample.utility;

import android.content.Context;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Build;
import android.widget.Toast;

public class AudioUtil {

    private static MediaRecorder mRecorder;
    private static MediaPlayer mPlayer;

    /*public static void startRecordingnew(String filePath, Chronometer chronometer) {
        try {
            mRecorder = new MediaRecorder();
            mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            mRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
            mRecorder.setOutputFile(filePath);
            mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
            mRecorder.prepare();
            mRecorder.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void stopRecordingnew(Chronometer chronometer) {
        try {
            if (mRecorder != null) {
                mRecorder.stop();
                mRecorder.release();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        mRecorder = null;
    }*/


    public static void startRecording(Context context, String filePath) {
        try {
            mRecorder = new MediaRecorder();
            mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            mRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
            mRecorder.setOutputFile(filePath);
            mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
            mRecorder.prepare();
            mRecorder.start();
            Toast.makeText(context, "Recording started", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void stopRecording(Context context) {
        try {
            if (mRecorder != null) {
                mRecorder.stop();
                mRecorder.release();
                Toast.makeText(context, "Recording stopped", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        mRecorder = null;
    }

   /* public static void pauseRecording() {
        try {
            if (mRecorder != null) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    mRecorder.pause();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void resumeRecording(Context context) {
        try {
            if (mRecorder != null) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    Toast.makeText(context, "recording resumed", Toast.LENGTH_SHORT).show();
                    mRecorder.resume();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }*/

   /* public static void playRecording(String filePath, final Activity activity) {
        try {
            if (mPlayer != null && mPlayer.isPlaying())
                mPlayer.stop();

            mPlayer = new MediaPlayer();
            mPlayer.setDataSource(filePath);
            mPlayer.prepare();
            mPlayer.start();
            mPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    stopPlayingAudio();
                    if (activity instanceof LanguageActivity)
                        ((LanguageActivity) activity).audioStopped();
                    if (activity instanceof EnglishActivity)
                        ((EnglishActivity) activity).audioStopped();
                    if (activity instanceof MathActivity)
                        ((MathActivity) activity).audioStopped();
                }
            });

        } catch (IOException e) {
            e.printStackTrace();
        }
    }*/

    public static void stopPlayingAudio() {
        try {
            if (mPlayer != null)
                mPlayer.release();
        } catch (Exception e) {
            e.printStackTrace();
        }
        mPlayer = null;
    }
}