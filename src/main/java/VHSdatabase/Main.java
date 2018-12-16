//Main Program - Jason
//Run Me

package VHSdatabase;



public class Main {

    public static void main(String args[]) throws InterruptedException {

        System.out.println("\n");
        LoadingAnimation.main();
        //Play creation animation

        VHSDatabase db = new VHSDatabase();
        //Start VHS database GUI

        System.out.print("\r"+VHSDatabase.APP_TITLE+" successfully created\n");
        //Display successful creation message

        System.out.println("\n");
        LoadingAnimation.main();
        //Play loading animation

        VHSDatabaseGUI gui = new VHSDatabaseGUI(db);
        //Start VHS database GUI

        System.out.print("\r"+VHSDatabase.APP_TITLE+" successfully loaded\n");
        //Display successful load message
    }
}
