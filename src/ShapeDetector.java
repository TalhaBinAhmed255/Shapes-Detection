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
    private static MatOfPoint2f approx;//MatOfPoint2f-->Field inherited from class org.opencv.core.Mat
    private static String nameOfImage;

    private static Imgcodecs Imgcodecs;//Used to read image from buffer.
    private static  Mat image=null;
    private static List<MatOfPoint> contours;//Will store Matrices of points where the curves will get detected

    //Defining Matrices to store different transformation of Image.
    private static  Mat hierarchey;
    private static  Mat gray;
    private static Mat binary;

    //Gets the final approximated Curves count and predicts the shape
    private static void takeApproxPolyCurvesDecideShape(int count){
        if(count>2 && count<10)
        {
            switch(count) {
                case 3:
                    System.out.println("The image  : "+nameOfImage+" is a Triangle. ");
                    break;
                case 4:
                    float ar;//This working is done to differentiate between Rectangle and square.
                    Rect rect = null;
                    rect= Imgproc.boundingRect(approx);//The boundingRect() function of OpenCV is used to draw an approximate rectangle around the binary image. This function is used mainly to highlight the region of interest after obtaining contours from an image.
                    ar = rect.width / rect.height;
                    if(ar >= 0.95 && ar <= 1.05)//if the ratio of width over height lies between this range then the shape must be a square.
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
        else if(count==-1){//Logic that varifies that Image does not exsists in the directory
            System.out.println("The image  : "+nameOfImage+" does not Exists in the directory. ");
        }
        else{
            System.out.println("The image  : "+nameOfImage+" is either not of a Proper Quality or it's not a Geometric-Shape");
        }
    }
    private static int getApproximatePolygonalCurves(){
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);//Function used to load the Libraries whose name or path is specified as parameter
        Imgcodecs = new Imgcodecs();
        image = Imgcodecs.imread("./Images/"+nameOfImage+".jpg", 4);//The function reads an image from the specified buffer in the memory. If the buffer is too short or contains invalid data, the empty matrix/image is returned.
        if(!image.isContinuous()) {
            return -1;
        }

        //Performing initializations
        contours = new ArrayList<>();
        hierarchey = new Mat();
        gray = new Mat(image.rows(), image.cols(), image.type());//Defining matrix to store gray scale image
        Imgproc.cvtColor(image, gray, Imgproc.COLOR_BGR2GRAY);//converts the iamge to Grayscale and stores in gray
        binary = new Mat(image.rows(), image.cols(), image.type(), new Scalar(0));//Defining matrix to store binary form of image image

        Imgproc.threshold(gray, binary, 100, 255, Imgproc.THRESH_BINARY_INV);//Finding the threshold in the iamge and storing it in binary matrix
        Imgproc.findContours(binary, contours, hierarchey, Imgproc.RETR_TREE,//Finding the countours (Contours can be explained simply as a curve joining all the continuous points (along the boundary), having same color or intensity. The contours are a useful tool for shape analysis and object detection and recognition)
                Imgproc.CHAIN_APPROX_SIMPLE);

        MatOfPoint matOfPoint= contours.get(0);
        MatOfPoint2f matOfPoint2f = new MatOfPoint2f(matOfPoint.toArray());//converting matOfPoint to MatOfPoint2f
        double peri = Imgproc.arcLength(matOfPoint2f, true);//Finding the arc lengths for approximation accuracy
        approx = new MatOfPoint2f();

        //Attempt to fit the contour to the best polygon
        //input: matOfPoint2f, which is the contour found earlier
        //output: approx, which is the MatOfPoint2f that holds the new polygon that has less vertices
        //peri, it smooths out the edges using the third parameter as its approximation accuracy
        //final parameter determines whether the new approximation must be closed (true=closed)
        Imgproc.approxPolyDP(matOfPoint2f, approx, 0.02 * peri, true);
        int count = (int) approx.total();//Return the final approximated curves.
        return count;
    }
    public static void detectShape(String nameOfImage){
        ShapeDetector.nameOfImage=nameOfImage;
        try{
            int approxPolyCurves=getApproximatePolygonalCurves();//t\This returns the final approximated curves.
            takeApproxPolyCurvesDecideShape(approxPolyCurves);
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }
}