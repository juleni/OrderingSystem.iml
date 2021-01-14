package sk.juleni.schema;
import org.h2.api.Trigger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class ProductInsertTrigger implements Trigger {

    @Override
    public void init(Connection connection, String s, String s1, String s2, boolean b, int i) throws SQLException {
        // initialize the trigger object is necessary
    }

    @Override
    public void fire(Connection connection, Object[] objects, Object[] objects1) throws SQLException {
        PreparedStatement pstmtUpdate = connection.prepareStatement("UPDATE PRODUCT SET PRODUCT_DESC='aaa'");
        pstmtUpdate.executeUpdate();
    }

    @Override
    public void close() throws SQLException {

    }

    @Override
    public void remove() throws SQLException {

    }
}
