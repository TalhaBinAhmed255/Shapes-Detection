import java.util.Scanner;

public class Driver {
    private static Scanner sc;
    static void initializeScannerObject(){
        sc= new Scanner(System.in);
    }
    static void runMenu(){
        String input;
        String  imageName;
        System.out.println("Choose from the options : ");
        System.out.println("***Press 1 for detecting from Existing Images***");
        System.out.println("***Press 2 for detection on a New Image***");
        while(true){
            input= sc.nextLine();
            if(input.equals("1")){
                System.out.println("Enter Name of the Existing Image : ");
                imageName= sc.nextLine();
                ShapeDetector.detectShape(imageName);
                break;
            }
            else if(input.equals("2")){
                System.out.println("Enter Name For the Image : ");
                imageName= sc.nextLine();
                new CaptureImage(imageName);
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
