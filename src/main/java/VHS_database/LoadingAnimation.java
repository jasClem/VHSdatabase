//Loading Animation - Jason

package VHS_database;



public class LoadingAnimation {

//region [// Animation variables //]

    private String lastLine = "";
    //String variable for blank string

    private byte anim;
    //Byte variable for changing animations

    private static byte loadStage;
    //Byte variable for changing loading stages

//endregion



    public void print(String line) {
        if (lastLine.length() > line.length()) {
            String temp = "";

            for (int i = 0; i < lastLine.length(); i++) {
                temp += " ";
            }

            if (temp.length() > 1)
                System.out.print("\r" + temp);
            //Clear the previous line
        }

        System.out.print("\r" + line);
        lastLine = line;
        //Print next line
    }



    public void animate(String line) {
        switch (anim) {
            case 1:
                print(line+" [ \\ ] ");
                break;
            case 2:
                print(line+" [ | ] ");
                break;
            case 3:
                print(line+" [ / ] ");
                break;
            default:
                print(line+" [ - ] ");
                anim = 0;
        }
        anim++;
        //Animation cycle
    }



    public static void main() throws InterruptedException {

        LoadingAnimation load = new LoadingAnimation();

        switch (loadStage) {
            case 1:
                for (int i = 0; i < 17; i++) {
                    load.animate("Loading " +VHSDatabase.APP_TITLE);
                    Thread.sleep(400);

                }
                break;
            default:
                for (int i = 0; i < 17; i++) {
                    load.animate("Creating " +VHSDatabase.APP_TITLE);
                    Thread.sleep(400);

                }
                loadStage = 0;
        }
        loadStage++;
        //Loading cycle/run animations
    }
}