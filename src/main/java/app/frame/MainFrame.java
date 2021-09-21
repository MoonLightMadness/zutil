package app.frame;

import app.frame.listener.NewButtonFollowCursorListener;
import org.junit.Test;

import javax.swing.*;
import javax.swing.plaf.basic.DefaultMenuLayout;
import java.awt.*;
import java.awt.event.MouseListener;

public class MainFrame {

    private static JFrame frame= null;


    /**
     * 启动frame
     * @param title 标题
     * @return
     * @author zhl
     * @date 2021-09-21 08:23
     * @version V1.0
     */
    public void showFrame(String title){
        changeLook();
        initializeFrame(title);
        SwingUtilities.invokeLater(() -> frame.setVisible(true));
    }

    private void initializeFrame(String title){
        frame = new JFrame(title);
        frame.setSize(1440,900);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new FlowLayout());
        //test
        //JButton jButton = new JButton("drag");
        NewButtonFollowCursorListener newButtonFollowCursorListener = new NewButtonFollowCursorListener(frame);
        frame.addMouseMotionListener(newButtonFollowCursorListener);
        //frame.add(jButton);
    }

    private void changeLook(){
        //更改外观
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        MainFrame mainFrame = new MainFrame();
        mainFrame.showFrame("test1");
    }

}
