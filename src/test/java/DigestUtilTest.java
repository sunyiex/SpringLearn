import com.learn.common.utils.DigestUtil;

/**
 * Created by Yi on 2015/6/16.
 */
public class DigestUtilTest {
    public static void main(String[] args) {
        String m = String.valueOf(System.currentTimeMillis());
        m = "admin";
        m =   DigestUtil.Encrypt(m);
        System.out.println(m.toUpperCase());
        m =   DigestUtil.Decrypt(m);
        System.out.println(m);
    }
}
