import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import javax.imageio.ImageIO;
import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JFrame;

import com.github.sarxos.webcam.Webcam;
import com.github.sarxos.webcam.WebcamPanel;
import com.github.sarxos.webcam.WebcamResolution;

import java.util.*;
@SuppressWarnings("serial")

//Class that captures image from Webcam.
public class CaptureImage extends JFrame {
    private static String imageName;

    //Defining executor that can handle a thread execution.
    private Executor executor = Executors.newSingleThreadExecutor();//The Java ExecutorService is the interface which allows us to execute tasks on threads asynchronously.
    private Dimension size = WebcamResolution.QQVGA.getSize();//The Dimension class encapsulates the width and height of a component (in integer precision) in a single object-->QQVGA is the smallest dimension(176, 144)
    private List<Webcam> webcams = Webcam.getWebcams();//Function that waits for the Webcam to get discovered and then returns the lists of the webCams found.
    private List<WebcamPanel> panels = new ArrayList<WebcamPanel>();//WebcamPanel--> that extends JPanel, a part of Java Swing package, is a container that can store a group of components. The main task of JPanel is to organize components, various layouts can be set in JPanel which provide better organisation of components

    //Defining Jbuttons for Image Capturing window -->(The JButton class is used to create a labeled button that has platform independent implementation)
    private JButton btSnapMe = new JButton(new SnapMeAction());
    private JButton btStart = new JButton(new StartAction());
    private JButton btStop = new JButton(new StopAction());


    //Class that Snaps the image and saves it with the name provided at Console
    private class SnapMeAction extends AbstractAction {
        public SnapMeAction() {
            super("Snapshot");
        }
        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                for (int i = 0; i < webcams.size(); i++) {
                    Webcam webcam = webcams.get(i);
                    File file = new File(String.format("./Images/"+imageName+".jpg", i));
                    ImageIO.write(webcam.getImage(), "JPG", file);
                    System.out.format("Image for %s saved in %s \n", webcam.getName(), file);
                    ShapeDetector.detectShape(imageName);
                }
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
    }

    //A Class that starts the WebCam window image Capturing purpose by a seperate thread.
    private class StartAction extends AbstractAction implements Runnable {//Runnable-->The Runnable interface should be implemented by any class whose instances are intended to be executed by a thread.
        public StartAction() {
            super("Start");//Sending message to its Parent class.
        }
        @Override
        public void actionPerformed(ActionEvent e) {//Function that is called when the Start button is clicked on Image Campturing window.

            btStart.setEnabled(false);//Start button gets disabled
            btSnapMe.setEnabled(true);//Snap button gets enabled.
            executor.execute(this);//Thread executor starts executing this thread.
        }

        //Overiding the run method of Runnable class.
        @Override
        public void run() {
            btStop.setEnabled(true);
            for (WebcamPanel panel : panels) {
                panel.start();//Panel gets started and gets visible on the screen.
            }
        }
    }

    //Class that handles Stop button click event.
    private class StopAction extends AbstractAction {
        public StopAction() {
            super("Stop");
        }
        @Override

        //This function is triggered when stop button is clicked.
        public void actionPerformed(ActionEvent e) {
            btStart.setEnabled(true);//Start button gets enabled
            btSnapMe.setEnabled(false);//Snap button gets disabled
            btStop.setEnabled(false);//Stop button gets disabled
            for (WebcamPanel panel : panels) {
                panel.stop();//Panel is stopped
            }
        }
    }
    public CaptureImage(String imageName) {
        super("Test Snap Different Size");
        CaptureImage.imageName=imageName;

        //Starting the discovered Webcams with their respective Window sizes (Their can be more than 1 webCams I.E 1)-->USB Cam, 2)-->Builtin Cam.
        for (Webcam webcam : webcams) {
            webcam.setViewSize(size);
            WebcamPanel panel = new WebcamPanel(webcam, false);
            panels.add(panel);
        }

        // start application with disable snapshot button - we enable it when webcam is started
        btSnapMe.setEnabled(false);
        btStop.setEnabled(false);
        setLayout(new FlowLayout());
        for (WebcamPanel panel : panels) {
            add(panel);
        }

       // Adding the components(Buttons etc) in Image Capturing Window/Panel
        add(btSnapMe);
        add(btStart);
        add(btStop);
        pack();
        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}