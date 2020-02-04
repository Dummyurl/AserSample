package info.pratham.asersample.utility;

import android.content.Context;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.cloud.speech.v1.RecognitionAudio;
import com.google.cloud.speech.v1.RecognitionConfig;
import com.google.cloud.speech.v1.RecognizeResponse;
import com.google.cloud.speech.v1.SpeechClient;
import com.google.cloud.speech.v1.SpeechRecognitionAlternative;
import com.google.cloud.speech.v1.SpeechRecognitionResult;
import com.google.protobuf.ByteString;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import info.pratham.asersample.R;
import info.pratham.asersample.database.modalClasses.QuestionStructure;
import info.pratham.asersample.interfaces.RecordPrepairListner;

public class AudioUtil {

    private static MediaRecorder mRecorder;
    private static MediaPlayer mPlayer;
    private static ImageView audioController;
    private static Context context;
    private static String currentRecordingFile;
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


    public static void startRecording(RecordPrepairListner recordPrepairListner, String filePath, @NonNull Context context, QuestionStructure questionStructure, String level, boolean isAttemptedQue, RecyclerView.Adapter recyclerViewAdapter) {
        try {
            stopPlayingAudio();
            mRecorder = new MediaRecorder();
            mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            mRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
            mRecorder.setOutputFile(filePath);
            mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_WB);
            mRecorder.setAudioSamplingRate(16000);
            mRecorder.prepare();
            currentRecordingFile = filePath;
            mRecorder.start();
            Toast.makeText(context, "Recording started", Toast.LENGTH_SHORT).show();
            recordPrepairListner.onRecordingStarted(context, questionStructure, level, isAttemptedQue, recyclerViewAdapter);
            // Toast.makeText(context, "Recording started", Toast.LENGTH_SHORT).show();
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
                if (currentRecordingFile != null && !currentRecordingFile.isEmpty())
                    getTranscriptionAndCheckIfCorrectlySpokenOrNot(currentRecordingFile);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        mRecorder = null;
    }

    private static void getTranscriptionAndCheckIfCorrectlySpokenOrNot(String currentRecordingFile) {
        try {
            // Instantiates a client
            SpeechClient speech = SpeechClient.create();

            // The path to the audio file to transcribe
//        String fileName = "./resources/audio.raw";

            File file = new File(currentRecordingFile);
            int size = (int) file.length();
            byte[] bytes = new byte[size];
            try {
                BufferedInputStream buf = new BufferedInputStream(new FileInputStream(file));
                buf.read(bytes, 0, bytes.length);
                buf.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            ByteString audioBytes = ByteString.copyFrom(bytes);

            // Builds the sync recognize request
            RecognitionConfig config = RecognitionConfig.newBuilder()
                    .setEncoding(RecognitionConfig.AudioEncoding.LINEAR16)
                    .setSampleRateHertz(16000)
                    .setLanguageCode("en-US")
                    .build();
            RecognitionAudio audio = RecognitionAudio.newBuilder()
                    .setContent(audioBytes)
                    .build();

            // Performs speech recognition on the audio file
            RecognizeResponse response = speech.recognize(config, audio);
            List<SpeechRecognitionResult> results = response.getResultsList();

            for (SpeechRecognitionResult result: results) {
                // There can be several alternative transcripts for a given chunk of speech. Just use the
                // first (most likely) one here.
                SpeechRecognitionAlternative alternative = result.getAlternativesList().get(0);
                System.out.printf("Transcription: %s%n", alternative.getTranscript());
            }
            speech.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
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

    public static void playRecording(String filePath, ImageView audio, Context mcontext) {
        try {
            if (mPlayer != null && mPlayer.isPlaying())
                stopPlayingAudio();
            // mPlayer.stop();
            mPlayer = new MediaPlayer();
            mPlayer.setDataSource(filePath);
            mPlayer.prepare();
            mPlayer.start();
            audioController = audio;
            context = mcontext;
            audioController.setImageDrawable(context.getDrawable(R.drawable.ic_stop));
            mPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    stopPlayingAudio();
                   /* if (activity instanceof LanguageActivity)
                        ((LanguageActivity) activity).audioStopped();
                    if (activity instanceof EnglishActivity)
                        ((EnglishActivity) activity).audioStopped();
                    if (activity instanceof MathActivity)
                        ((MathActivity) activity).audioStopped();*/
                }
            });

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void stopPlayingAudio() {
        try {
            if (mPlayer != null) {
                mPlayer.release();
                audioController.setImageDrawable(context.getDrawable(R.drawable.ic_play_button));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        mPlayer = null;
    }
}