package exception;

import javax.swing.*;

public class GlobalExceptionHandler {
    public static void throwCheckedException(Exception exception) {
        try {
            throw exception;
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage(), "잘못된 접근", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void throwRuntimeException(RuntimeException runtimeException) {
        JOptionPane.showMessageDialog(null, runtimeException.getMessage(), "잘못된 접근", JOptionPane.ERROR_MESSAGE);
    }
}
