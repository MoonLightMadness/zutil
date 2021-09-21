package app.frame.listener;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class NewButtonFollowCursorListener implements MouseMotionListener {


    private JButton jButton;

    private Frame mainFrame;

    public NewButtonFollowCursorListener(Frame mainFrame){
        this.mainFrame = mainFrame;
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        if(jButton != null){
            mainFrame.remove(jButton);
        }
        jButton = new JButton();
        jButton.setName("drage");
        jButton.setLocation(e.getPoint());
        jButton.setSize(50,50);
        mainFrame.add(jButton);
        mainFrame.revalidate();
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        if(jButton != null){
            mainFrame.remove(jButton);
        }
        jButton = new JButton();
        jButton.setName("drage");
        jButton.setLocation(e.getPoint());
        jButton.setSize(50,50);
        mainFrame.add(jButton);
        mainFrame.revalidate();
    }
}
