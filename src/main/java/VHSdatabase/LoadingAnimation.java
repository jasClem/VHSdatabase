/**
 * // VHS Database // - Jason Clemons
 *
 * Loading Animation - Plays loading animations
 *
 */
package VHSdatabase;



public class LoadingAnimation {

//region [// Animation variables //]

    private String lastLine = "";
    //String variable for blank string

    private byte animationFrame;
    //Byte variable for changing animations

    private static byte loadStage;
    //Byte variable for changing loading stages

//endregion



    public static void main() throws InterruptedException {

        LoadingAnimation load = new LoadingAnimation();
        //LoadingAnimation variable for loading

        switch (loadStage) {
            case 1:
                for (int i = 0; i < 17; i++) {
                    load.animator("Loading " +VHSDatabase.APP_TITLE);
                    Thread.sleep(400);
                    //Loading stage

                }
                break;
            default:
                for (int i = 0; i < 17; i++) {
                    load.animator("Creating " +VHSDatabase.APP_TITLE);
                    Thread.sleep(400);
                    //Creating stage

                }
                loadStage = 0;
        }
        loadStage++;
        //Loading cycle/run animations
    }



    public void printer(String line) {
        //Line printer (Prints animation frames)

        if (lastLine.length() > line.length()) {
            String temp = "";

            for (int i = 0; i < lastLine.length(); i++) {
                temp += " ";
            }

            if (temp.length() > 1)
                System.out.print("\r" + temp);
            //Clear the previous lines
        }

        System.out.print("\r" + line);
        lastLine = line;
        //Print next line
    }



    public void animator(String line) {
        switch (animationFrame) {
            //Animation frames (get sent to printer)

            case 1:
                printer(line+" [ \\ ] ");
                break;
                //Frame #2

            case 2:
                printer(line+" [ | ] ");
                break;
                //Frame #3

            case 3:
                printer(line+" [ / ] ");
                break;
                //Frame #4

            default:
                printer(line+" [ - ] ");
                animationFrame = 0;
                //Frame #1/reset

        }
        animationFrame++;
        //Animation cycle
    }
}