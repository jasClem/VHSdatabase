//Loading Animation - Jason

package VHS_database;

public class LoadingAnimation {

    private String lastLine = "";

    public void print(String line) {
        //clear the last line if longer
        if (lastLine.length() > line.length()) {
            String temp = "";
            for (int i = 0; i < lastLine.length(); i++) {
                temp += " ";
            }
            if (temp.length() > 1)
                System.out.print("\r" + temp);
        }
        System.out.print("\r" + line);
        lastLine = line;
    }

    private byte anim;

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
    }

    private static byte loadStage;

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

    }
}