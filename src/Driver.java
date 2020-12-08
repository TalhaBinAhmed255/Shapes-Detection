import java.util.Scanner;

//Application driver class.
public class Driver {

    //Input from console reader object.
    private static Scanner sc;

    static void initializeScannerObject(){
        sc= new Scanner(System.in);
    }

    //Displaying and handling menu on console.
    static void runMenu(){
        String input;
        String  imageName;

        //Displaying options on console.
        System.out.println("Choose from the options : ");
        System.out.println("***Press 1 for detecting from Existing Images***");
        System.out.println("***Press 2 for detection on a New Image***");

        //Loop that goes on running until the user selects from provided options I.E(1,2,).
        while(true){
            input= sc.nextLine();
            if(input.equals("1")){
                System.out.println("Enter Name of the Existing Image : ");
                imageName= sc.nextLine();//Reading image name from console
                ShapeDetector.detectShape(imageName);//Passing the image name to shape Detector object.
                break;
            }
            else if(input.equals("2")){
                System.out.println("Enter Name For the Image : ");
                imageName= sc.nextLine();
                new CaptureImage(imageName);//Capturing and storing a new Image with the provided imageName
                break;
            }
            else{
                System.out.println("***[Enter A Valid Input]***");
            }
        }
    }
    public static void main(String[] args) {
        System.out.println("--:Application Started:--");
        initializeScannerObject();
        runMenu();
    }
}
