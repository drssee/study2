package hello.jdbc.exception.basic;

import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.net.ConnectException;
import java.sql.SQLException;

/**
 * 이 클래스의 문제점 - 특정 예외에 종속적임(JDBC의 예외등)
 */
@Slf4j
public class UnCheckedAppTest {

    @Test
    void unChecked() {
        Controller controller = new Controller();
        Assertions.assertThatThrownBy(() -> controller.request())
                .isInstanceOf(RuntimeException.class);
    }

    @Test
    void printEx() {
        Controller controller = new Controller();
        try {
            controller.request();
        } catch (Exception e) {
            log.info("ex", e);
        }
    }

    static class Controller {
        Service service = new Service();

        public void request() {
            service.logic();
        }
    }

    static class Service {
        Repository repository = new Repository();
        NetworkClient networkClient = new NetworkClient();

        public void logic() {
            repository.call();
            networkClient.call();
        }

    }
    static class NetworkClient {
        public void call() {
            try {
                runNetwork();
            } catch (ConnectException e) {
                throw new RuntimeConnectException(e);
            }
        }

        public void runNetwork() throws ConnectException {
            throw new ConnectException("연결 실패");
        }
    }
    static class Repository {
        public void call() {
            try {
                runSQL();
            } catch (SQLException e) {
//                e.printStackTrace(); <- 이거는 sysout , log가 관리하게 해야함
                throw new RuntimeSQLException(e); //Throwable cause(e) 꼭 넣어줘야함, 이전 예외의 원인을 전달해야함
            }
        }

        public void runSQL() throws SQLException {
            throw new SQLException("ex");
        }
    }

    static class RuntimeConnectException extends RuntimeException {
        public RuntimeConnectException(Throwable cause) {
            super(cause);
        }
    }

    static class RuntimeSQLException extends RuntimeException {
        public RuntimeSQLException() {
        }

        public RuntimeSQLException(Throwable cause) {
            super(cause);
        }
    }
}
