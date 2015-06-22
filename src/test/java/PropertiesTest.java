import com.learn.common.utils.PropertyUtil;

/**
 * Created by Yi on 2015/6/17.
 */
public class PropertiesTest {
    public static void main(String[] args) {
        String url =  PropertyUtil.getFilterURL("UserURL");
        String[] notFilter = url.split(",");
        for(int i=0; i<notFilter.length; ++i){
            System.out.println(notFilter[i]);
        }
    }
}
