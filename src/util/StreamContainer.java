package util;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.ResultSet;
import java.sql.SQLException;

public class StreamContainer {
    public static FileInputStream initialize(String filePath) {
        try {
            return new FileInputStream(filePath);
        } catch (FileNotFoundException fe) {
            fe.printStackTrace();
        }

        return null;
    }

    public static void close(InputStream is) {
        try {
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void close(ResultSet rs) {
        try {
            rs.close();
        } catch (SQLException se) {
            se.printStackTrace();
        }
    }
}
