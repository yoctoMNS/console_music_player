import java.io.File;
import java.net.URL;
import java.util.Random;
 
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;

import java.io.IOException;
import java.net.MalformedURLException;

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

public class Player {
    private String dirPath = "./sounds";
    private String extension = ".mp3";
    private File[] files;
    private Random rd;
    private ProcessBuilder process = new ProcessBuilder("cmd", "/c", "cls");
    private int select;
    private Clip playSound;
    private int length;


    public Player() {
        loadFiles(dirPath);
        rd = new Random();
    }


    private void loadFiles(String path) {
        var dir = new File(path);
        files = dir.listFiles();
    }

    
    private void selectFile() {
        select = rd.nextInt(files.length);

        try (AudioInputStream ais = AudioSystem.getAudioInputStream(files[select])) {
            AudioFormat af = ais.getFormat();
            long size = ais.getFrameLength();
            length = (int)(size / af.getSampleRate());
            DataLine.Info dataLine = new DataLine.Info(Clip.class,af);
            playSound = (Clip)AudioSystem.getLine(dataLine);
            playSound.open(ais);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (UnsupportedAudioFileException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (LineUnavailableException e) {
            e.printStackTrace();
        } finally {
            System.err.println("読み込んだファイル: " + files[select].getName());
        }
    }


    private void draw() {
        try {
            process.inheritIO().start().waitFor();
            System.out.println("今流れている曲");
            System.out.println(files[select].getName());
        } catch (InterruptedException e) {
        } catch (IOException e) {
        }
    }


    private void play() {
        playSound.start();
        playSound.close();
    }


    public void run() {
        while (true) {
            selectFile();
            play();
            draw();
            try {
                Thread.sleep(length*1000);
            } catch (InterruptedException e) {
            }
        }
    }
}
