package main.java.view;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import java.awt.Dimension;

public abstract class AbstractView extends JFrame{

    protected abstract void initComponents();
    protected abstract void setupLayout();
    protected abstract void attachListeners();
    
    public AbstractView(String title, int width, int height) {
        super(title);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(new Dimension(width, height));
        setLocationRelativeTo(null);

        initComponents();
        setupLayout();
        attachListeners();

        setVisible(true);
    }

    protected void displayError(String message, String title) {
        JOptionPane.showMessageDialog(this, message, title, JOptionPane.ERROR_MESSAGE);
    }
}