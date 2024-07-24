package util;

import static javax.swing.JFileChooser.APPROVE_OPTION;
import static javax.swing.JOptionPane.YES_OPTION;

public class DialogChooser {
    public static boolean isApproved(int choice) {
        return choice == APPROVE_OPTION;
    }

    public static boolean isYes(int answer) {
        return answer == YES_OPTION;
    }
}
