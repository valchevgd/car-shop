package services.implementations;

import org.apache.commons.codec.digest.DigestUtils;
import services.HashingService;

public class HashingServiceImpl implements HashingService {
    @Override
    public String hash(String value) {
        return DigestUtils.sha256Hex(value);
    }
}
