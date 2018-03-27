package kz.samgau;

import kz.samgau.services.RestClient;
import kz.samgau.utils.TrustCertificate;

public class Main {

    public static void main(String[] args) {
        try {
            TrustCertificate certificate = new TrustCertificate();
            certificate.doTrustToCertificates();
            Thread thread = new Thread(new RestClient());
            thread.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
