// In Driver class

import javax.swing.SwingUtilities;

import gui.AdminPanel;

public class Driver {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new AdminPanel(200));        
    }
}
