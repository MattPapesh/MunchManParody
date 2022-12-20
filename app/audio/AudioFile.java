package app.audio;

import java.io.File;
import java.io.IOException;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

import fundamentals.Constants;

/**
 * Used as a data structure, AudioFile takes in a audio file name upon instantiation in order to make use of the Clip class. Moreover, 
 * using AudioFile methods, the name of the audio file associated with each AudioFile instance can be returned while also returning 
 * a Clip instance to get an audio clip of the audio file; this can be used to easily obtain and play audio clips from audio files.
 * 
 * @see
 * Note: AudioFile will only function apropriately if the audio file in question is a WAV file type, and if it is
 * located within the "assets/audio/" root directory; in the "audio" folder.  
 */
public class AudioFile 
{
    private AudioInputStream audio_input_stream = null;
    private Clip audio_clip = null; 
    private String file_name = "";

    /**
     * Used as a data structure, AudioFile takes in a audio file name upon instantiation in order to make use of the Clip class. Moreover, 
     * using AudioFile methods, the name of the audio file associated with each AudioFile instance can be returned while also returning 
     * a Clip instance to get an audio clip of the audio file; this can be used to easily obtain and play audio clips from audio files.
     * 
     * @see
     * Note: AudioFile will only function apropriately if the audio file in question is a WAV file type, and if it is
     * located within the "assets/audio/" root directory; in the "audio" folder. 
     */
    protected AudioFile(String file_name)
    {
        try
        {   
            this.file_name = file_name;
            audio_input_stream = AudioSystem.getAudioInputStream(new File(Constants.FILE_ROOT_DIRECTORIES.AUDIO_ROOT_DIRECTORY + file_name));
            audio_clip = (Clip)AudioSystem.getLine(new DataLine.Info(Clip.class, audio_input_stream.getFormat()));
        
            audio_clip.open(audio_input_stream); 
        }
        catch(UnsupportedAudioFileException e)
        {
            System.err.println("AudioFile.java: Caught exeception! Audio file type is not a recognizable type!");
        }
        catch(IOException e)
        {
            System.err.println("AudioFile.java: Caught exeception! Could not find the requested audio file!");
        }
        catch(LineUnavailableException e)
        {
            System.err.println("AudioFile.java: Caught LineUnavailableException! ");
        }
        catch(NullPointerException e){} 
    }

    @Override
    protected void finalize() throws Throwable 
    {
        if(audio_clip != null)
        {
            audio_clip.close();
        }    
    }

    /**
     * @return A Clip instance based on the AudioFile instance's associated audio file. 
     */
    protected Clip getAudioClip()
    {
        return audio_clip;
    }
    
    /**
     * @return The name of the audio file associated with this AudioFile instance. 
     */
    protected String getFileName()
    {
        return file_name;
    }
}
