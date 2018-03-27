package kz.samgau.services;

import com.google.gson.Gson;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import kz.samgau.entities.Item;
import kz.samgau.entities.RootObject;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.io.File;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RestClient implements Runnable {

    private Map getProperties() {
        Map prop = new HashMap<String, String>();
        String path = "jdbc:sqlite:" + System.getProperty("user.home") + File.separator + "Desktop" + File.separator + "contract.db";
        prop.put("hibernate.connection.url", path);
        return prop;
    }

    private Map properties = getProperties();

    private EntityManagerFactory emf = Persistence.createEntityManagerFactory("rest", properties);

    private Gson gson = new Gson();

    private String getJson(String next_page) { //метод который выгружает данные с веб-сервиса

        //выгружаем данные с веб-сервиса
        Client client = Client.create();
        WebResource webResource = client.resource("https://ows.goszakup.gov.kz" + next_page);
        ClientResponse response = null;

        try {
            response = webResource.accept("application/json")
                    .header("Authorization", "Bearer 1b75e3fc135e35daa1cb4dfee98a3e2a")
                    .get(ClientResponse.class);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        if (response == null) {
            return next_page;
        } else if (response.getStatus() != 200) {
            throw new RuntimeException("Failed : HTTP error code : "
                    + response.getStatus());
        }

        //записываем в БД
        EntityManager entityManager = emf.createEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();

        try {
            String jsonString = response.getEntity(String.class);
            jsonString = jsonString.replaceAll("(\"ref_amendm_agreem_justif_id\":)\\[(.*?)\\]", "$1\"$2\"");
            RootObject rootObject = gson.fromJson(jsonString, RootObject.class); //парсим JSON в Java объект
            List<Item> items = rootObject.getItems();
            for (Item item : items) {
                entityManager.persist(item);
            }
            transaction.commit();

            next_page = rootObject.getNext_page();

        } catch (Exception e) {
            System.out.println(e.getMessage());
            transaction.rollback();
            return null;
        } finally {
            entityManager.close();
        }
        return next_page;
    }

    public void run() {
        long start = System.currentTimeMillis();

        String page = "/contract";
        while (page != null || !page.isEmpty()) {
            System.out.println(new Date(System.currentTimeMillis()) + "    " + page);
            try {
                page = getJson(page);
                System.out.println("Saved!");
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
            if (page == null || page.isEmpty()) {
                break;
            }
        }
        System.out.println(System.currentTimeMillis() - start + "ms");
    }
}
