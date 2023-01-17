package facades;

import entities.User;

import javax.persistence.*;

import errorhandling.IllegalAgeException;
import errorhandling.InvalidEmailException;
import errorhandling.InvalidUsernameException;
import errorhandling.UniqueException;
import security.errorhandling.AuthenticationException;

import java.util.List;

public class UserFacade {
    private static EntityManagerFactory emf;
    private static UserFacade instance;

    public static final int MINIMUM_AGE = 13;
    public static final int MAXIMUM_AGE = 120;

    public static final int MINIMUM_USERNAME_LENGTH = 3;
    public static final int MAXIMUM_USERNAME_LENGTH = 20;

    private UserFacade() {
    }

    public static UserFacade getFacade(EntityManagerFactory _emf) {
        if (instance == null) {
            emf = _emf;
            instance = new UserFacade();
        }
        return instance;
    }

    public User getVerifiedUser(String username, String password) throws AuthenticationException {
        EntityManager em = emf.createEntityManager();
        User user;
        try {
            TypedQuery<User> query = em.createQuery("SELECT u FROM User u WHERE u.username =:username ",User.class);
            query.setParameter("username",username);
            try {
                user = query.getSingleResult();
            } catch (NoResultException e) {
                throw new AuthenticationException("Invalid user name or password");
            }

            if (!user.verifyPassword(password)) {
                throw new AuthenticationException("Invalid user name or password");
            }
        } finally {
            em.close();
        }
        return user;
    }

    public User createUser(User user) throws IllegalAgeException, InvalidUsernameException, UniqueException {
        EntityManager em = emf.createEntityManager();

        if(user.getAge() < MINIMUM_AGE || user.getAge() > MAXIMUM_AGE) {
            throw new IllegalAgeException(user.getAge());
        }

        if(user.getUsername() == null || user.getUsername().equals("")) {
            throw new InvalidUsernameException("Username cannot be null or an empty string");
        }

        if(user.getUsername().length() < MINIMUM_USERNAME_LENGTH || user.getUsername().length() > MAXIMUM_USERNAME_LENGTH) {
            throw new InvalidUsernameException("Username length should be between " + MINIMUM_USERNAME_LENGTH + " and " +
                    + MAXIMUM_USERNAME_LENGTH+ " characters");
        }

        TypedQuery<Long> query = em.createQuery("SELECT count(u) FROM User u WHERE u.username =:username", Long.class);
        query.setParameter("username", user.getUsername());
        if (query.getSingleResult() > 0) {
            throw new UniqueException("Username already in use");
        }

        try {
            em.getTransaction().begin();
            em.persist(user);
            em.getTransaction().commit();
        } finally {
            em.close();
        }
        return user;
    }

    public void updateUser(User user) throws UniqueException, InvalidUsernameException, IllegalAgeException,
            InvalidEmailException {
        EntityManager em = emf.createEntityManager();

        validateUser(user);

        try {
            em.getTransaction().begin();
            em.merge(user);
            em.getTransaction().commit();
        } catch (RollbackException exception){
            throw new UniqueException(exception.getMessage());
        } finally {
            em.close();
        }
    }

    public User getUserById(int id) {
        EntityManager em = emf.createEntityManager();
        User user = em.find(User.class,id);
        em.close();
        if (user == null) {
            throw new EntityNotFoundException("User with id: "+id+" does not exist in database");
        }
        return user;
    }

    public List<User> getAllUsers() {
        EntityManager em = emf.createEntityManager();
        TypedQuery<User> query = em.createQuery("SELECT u FROM User u",User.class);
        List<User> allUsers = query.getResultList();
        em.close();
        return allUsers;
    }

    private void validateUser(User user) throws IllegalAgeException, InvalidUsernameException, InvalidEmailException {
        if(user.getUsername() == null || user.getUsername().equals("")) {
            throw new InvalidUsernameException("Username cannot be null or an empty string");
        }

        if(user.getUsername().length() < MINIMUM_USERNAME_LENGTH ||
                user.getUsername().length() > MAXIMUM_USERNAME_LENGTH) {
            throw new InvalidUsernameException("Username length should be between "
                    + MINIMUM_USERNAME_LENGTH + " and "
                    + MAXIMUM_USERNAME_LENGTH+ " characters");
        }

        if(!user.getEmail().matches("^[a-zA-Z0-9_+&*-]+" +
                "(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$")) {
            throw new InvalidEmailException(user.getEmail()+" was not a valid email");
        }

        if(user.getAge() < MINIMUM_AGE || user.getAge() > MAXIMUM_AGE) {
            throw new IllegalAgeException(user.getAge());
        }
    }

}
