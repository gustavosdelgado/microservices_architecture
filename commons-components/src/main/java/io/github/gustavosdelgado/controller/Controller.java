package io.github.gustavosdelgado.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

/**
 * The <code>Controller</code> is an interface created to enforce a
 * standarization on
 * controller
 * classes.
 * <p/>
 * 
 * @author Gustavo Delgado
 */
public interface Controller<R extends WebRequest, S extends WebResponse> {

    /**
     * Entity creation API endpoint. Must pass a JWT token in order to authorise the
     * request.
     * 
     * @param token
     *                JWT token containing authorization data.
     * @param request
     *                the request data.
     * @return
     *         must return the created entity.
     */
    public ResponseEntity<S> create(String token, R request);

    /**
     * Entity query API endpoint. Must pass a JWT token in order to authorise the
     * request.
     * 
     * @param token
     *                 JWT token containing authorization data.
     * @param entityId
     *                 the entity ID to be queried.
     * @return
     *         must return the queried entity, if available.
     */
    public ResponseEntity<S> get(String token, Long entityId);

    /**
     * Entity query list API endpoint. Must pass a JWT token in order to authorise
     * the
     * request.
     * 
     * @param pageable - Spring MVC infers Pageable through request parameters.
     * 
     * @param token
     *                 JWT token containing authorization data.
     * @return
     *         must return the queried entity list, if available.
     */
    public ResponseEntity<Page<S>> list(Pageable pageable, String token);

    /**
     * Entity update API endpoint. Must pass a JWT token in order to authorise the
     * request.
     * 
     * @param token
     *                 JWT token containing authorization data.
     * @param entityId
     *                 the entity ID to be updated.
     * @return
     *         must return the updated entity, if available.
     */
    public ResponseEntity<S> update(String token, Long entityId);

    /**
     * Entity removal API endpoint. Must pass a JWT token in order to authorise the
     * request.
     * 
     * @param token
     *                 JWT token containing authorization data.
     * @param entityId
     *                 the entity ID to be deleted.
     * @return
     *         must return the deleted entity, if available.
     */
    public ResponseEntity<S> delete(String token, Long entityId);

    /**
     * Utility method to verify if token contains valid authorization role
     * 
     * @param token
     *                 JWT token containing authorization data.
     * @param entityId
     *                 the entity ID to be deleted.
     * @return
     *         must return the deleted entity, if available.
     */
    public boolean isAuthorized(String token);

}
