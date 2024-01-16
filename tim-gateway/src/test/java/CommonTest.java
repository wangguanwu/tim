import com.gw.tim.gateway.kit.NetAddressIsReachable;
import org.junit.Test;

/**
 * @since JDK 1.8
 */
public class CommonTest {

    @Test
    public void test() {
        boolean reachable = NetAddressIsReachable.checkAddressReachable("127.0.0.1", 11211, 1000);
        System.out.println(reachable);
    }


}
