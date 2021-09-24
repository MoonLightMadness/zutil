package app.game;

/**
 * @ClassName : app.game.ProgressBar
 * @Description :
 * @Date 2021-09-24 08:56:53
 * @Author ZhangHL
 */
public class ProgressBar {

    public static void display(int total){
        System.out.print("\n");
        for (int i = 0; i < total; i++) {
            float rate = ((float) i/(total-1));
            int percent = (int) (rate * 100);
            // \033[1A 光标上移一行
            System.out.print(messageHandler("\033[1A[{}{}]{}%",
                    getSymbols(percent,'*'),getSymbols((100-percent),' '),percent));
        }
    }

    private static String getSymbols(int num,char symbol){
        StringBuilder stringBuilder =new StringBuilder();
        for (int i = 0; i < num; i++) {
            stringBuilder.append(symbol);
        }
        return stringBuilder.toString();
    }

    private static String messageHandler(String msg, Object... args) {
        StringBuilder logBuilder = new StringBuilder();
        for (int i = 0; i < args.length; i++) {
            int pointer = msg.indexOf('{');
            if (pointer != -1) {
                logBuilder.append(msg, 0, pointer);
                logBuilder.append(args[i].toString());
                msg = msg.substring(pointer + 2);
            }
        }
        logBuilder.append(msg).append("\n");
        return logBuilder.toString();
    }

    public static void main(String[] args) {
        ProgressBar.display(100000);
    }
}
