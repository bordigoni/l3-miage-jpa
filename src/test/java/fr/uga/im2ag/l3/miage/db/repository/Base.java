package fr.uga.im2ag.l3.miage.db.repository;

import com.github.javafaker.Faker;
import fr.uga.im2ag.l3.miage.db.repository.RepositoryFactory;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

import javax.persistence.EntityManager;
import javax.persistence.Persistence;

public abstract class Base {

    protected EntityManager entityManager;
    protected RepositoryFactory daoFactory = new RepositoryFactory();

    /**
     * Creates fresh thus empty database for each test method.
     */
    @BeforeEach
    public final void setup() {
        entityManager = Persistence.createEntityManagerFactory("TEST")
                .createEntityManager();
    }

    @AfterEach
    public final void close() {
        entityManager.close();
    }
}
