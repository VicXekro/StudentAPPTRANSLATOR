package com.language.translate.translator;

import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.auth.DefaultAWSCredentialsProviderChain;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.polly.model.OutputFormat;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.Bucket;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.translate.AmazonTranslate;
import com.amazonaws.services.translate.AmazonTranslateClient;
import com.amazonaws.services.translate.model.TranslateTextRequest;
import com.amazonaws.services.translate.model.TranslateTextResult;
import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.advanced.AdvancedPlayer;
import javazoom.jl.player.advanced.PlaybackEvent;
import javazoom.jl.player.advanced.PlaybackListener;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

@Service
public class PlayTranslatedSpeech {

    public void playConvertedSpeech() throws IOException, JavaLayerException {

        BasicAWSCredentials awsCreds = new BasicAWSCredentials("AKIAJ3XIFCWCHCYVAACA", "IOwBgcyt59RDI7EPZMct+gTBLapizQwHLwId4tb9");
        AmazonS3 s3Client = AmazonS3ClientBuilder.standard()
                .withRegion("us-east-1")
                .withCredentials(new AWSStaticCredentialsProvider(awsCreds))
                .build();

        S3Object x = s3Client.getObject("ksutextstorage","testfile.txt");

        BufferedReader reader = new BufferedReader(new InputStreamReader(x.getObjectContent()));

        String line;
        while((line = reader.readLine()) != null) {

            String convertedText = translateText(line);

            playTheSpeech(convertedText);
        }


    }


    private String translateText(String text){




            String REGION = "us-east-1";

            AWSCredentialsProvider awsCreds = DefaultAWSCredentialsProviderChain.getInstance();

            AmazonTranslate translate = AmazonTranslateClient.builder()
                    .withCredentials(awsCreds)
                    .withRegion(REGION).build();

            TranslateTextRequest request = new TranslateTextRequest()
                    .withText(text)
                    .withSourceLanguageCode("en")
                    .withTargetLanguageCode("es");
            TranslateTextResult result  = translate.translateText(request);

            return result.getTranslatedText();

    }

    private void playTheSpeech(String text) throws IOException, JavaLayerException {

        //create the test class
        Polly helloWorld = new Polly(Region.getRegion(Regions.US_EAST_1));
        //get the audio stream
        InputStream speechStream = helloWorld.synthesize(text, OutputFormat.Mp3);

        //create an MP3 player
        AdvancedPlayer player = new AdvancedPlayer(speechStream,
                javazoom.jl.player.FactoryRegistry.systemRegistry().createAudioDevice());

        player.setPlayBackListener(new PlaybackListener() {
            @Override
            public void playbackStarted(PlaybackEvent evt) {
                System.out.println("Playback started");
                System.out.println(text);
            }

            @Override
            public void playbackFinished(PlaybackEvent evt) {
                System.out.println("Playback finished");
            }
        });


        // play it!
        player.play();
    }


}
