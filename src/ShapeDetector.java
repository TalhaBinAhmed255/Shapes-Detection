import org.opencv.core.*;
import org.opencv.imgproc.Imgproc;
import java.util.ArrayList;
import java.util.List;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Scalar;
import org.opencv.imgcodecs.Imgcodecs;


public  class ShapeDetector {
    private static MatOfPoint2f approx;
    private static String nameOfImage;

    private static Imgcodecs Imgcodecs;
    private static  Mat image=null;
    private static List<MatOfPoint> contours;
    private static  Mat hierarchey;
    private static  Mat gray;
    private static Mat binary;


    private static void takeApproxPolyCurvesDecideShape(int count){
        if(count>2 && count<10)
        {
            switch(count) {
                case 3:
                    System.out.println("The image  : "+nameOfImage+" is a Triangle. ");
                    break;
                case 4:
                    float ar;
                    Rect rect = null;
                    rect= Imgproc.boundingRect(approx);
                    ar = rect.width / rect.height;
                    if(ar >= 0.95 && ar <= 1.05)
                    {
                        System.out.println("The image  : "+nameOfImage+" is a Square. ");
                    }
                    else
                    {
                        System.out.println("The image  : "+nameOfImage+" is a Rectangle. ");
                    }
                    break;
                case 5:
                    System.out.println("The image  : "+nameOfImage+" is a Pentagon. ");
                    break;
                default:
                    System.out.println("The image  : "+nameOfImage+" is a Circle. ");
            }
        }
        else if(count==-1){
            System.out.println("The image  : "+nameOfImage+" does not Exists in the directory. ");
        }
        else{
            System.out.println("The image  : "+nameOfImage+" is either not of a Proper Quality or it's not a Geometric-Shape");
        }
    }
    private static int getApproximatePolygonalCurves(){
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        Imgcodecs = new Imgcodecs();
        image = Imgcodecs.imread("./Images/"+nameOfImage+".jpg", 4);
        if(!image.isContinuous()) {
            return -1;
        }
        contours = new ArrayList<>();
        hierarchey = new Mat();
        gray = new Mat(image.rows(), image.cols(), image.type());
        Imgproc.cvtColor(image, gray, Imgproc.COLOR_BGR2GRAY);
        binary = new Mat(image.rows(), image.cols(), image.type(), new Scalar(0));

        Imgproc.threshold(gray, binary, 100, 255, Imgproc.THRESH_BINARY_INV);
        Imgproc.findContours(binary, contours, hierarchey, Imgproc.RETR_TREE,
                Imgproc.CHAIN_APPROX_SIMPLE);

        MatOfPoint matOfPoint= contours.get(0);
        MatOfPoint2f matOfPoint2f = new MatOfPoint2f(matOfPoint.toArray());
        double peri = Imgproc.arcLength(matOfPoint2f, true);
        approx = new MatOfPoint2f();
        Imgproc.approxPolyDP(matOfPoint2f, approx, 0.02 * peri, true);
        int count = (int) approx.total();
        return count;
    }
    public static void detectShape(String nameOfImage){
        ShapeDetector.nameOfImage=nameOfImage;
        try{
            int approxPolyCurves=getApproximatePolygonalCurves();
            takeApproxPolyCurvesDecideShape(approxPolyCurves);
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }
}