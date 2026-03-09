package obssolution.task1.util;

import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.IdentifierGenerator;

import java.io.Serializable;

public class OrderNumberGenerator implements IdentifierGenerator {

    @Override
    public Serializable generate(SharedSessionContractImplementor session, Object o) {
        return session.doReturningWork(connection -> {
            try (var statement = connection.prepareStatement("SELECT nextval('order_no_seq')")) {
                var rs = statement.executeQuery();
                if (rs.next()) {
                    return "O" + rs.getLong(1);
                }
                throw new RuntimeException("Sequence tidak ditemukan");
            }
        });
    }


}
