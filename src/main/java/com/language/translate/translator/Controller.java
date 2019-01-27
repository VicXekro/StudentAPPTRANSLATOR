package com.language.translate.translator;


import javazoom.jl.decoder.JavaLayerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
public class Controller {

    @Autowired
    private PlayTranslatedSpeech playTranslatedSpeech;

    @RequestMapping(value ="/playConvertedSpeech", method = RequestMethod.GET)
    public void playConvertedSpeech() throws IOException, JavaLayerException {
        playTranslatedSpeech.playConvertedSpeech();
    }

}
