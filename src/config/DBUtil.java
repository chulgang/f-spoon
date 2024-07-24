package config;

import org.apache.commons.dbcp2.BasicDataSource;

import javax.sql.DataSource;

public class DBUtil {
    public DBUtil() {
        throw new IllegalStateException("인스턴스 오류");
    }

    private static final DataSource DATASOURCE;

    static {
        BasicDataSource basicDataSource = new BasicDataSource();

        basicDataSource.setDriverClassName("org.mariadb.jdbc.Driver");

        basicDataSource.setUrl("jdbc:mariadb://localhost:3308/gui");
        basicDataSource.setUsername("gui");
        basicDataSource.setPassword("java");

        basicDataSource.setInitialSize(5);
        basicDataSource.setMaxTotal(5);
        basicDataSource.setMaxIdle(5);
        basicDataSource.setMinIdle(5);

        basicDataSource.setValidationQuery("SELECT 1");
        basicDataSource.setTestOnBorrow(true);

        DATASOURCE = basicDataSource;
    }

    public static DataSource getDataSource() {
        return DATASOURCE;
    }
}
