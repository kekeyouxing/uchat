package util;

import java.util.UUID;

/**
 * @author keyouxing
 */
public class UUIDGenerator {

    public static String createUuid(){
        return UUID.randomUUID().toString().replace("-", "").toLowerCase();
    }

}
