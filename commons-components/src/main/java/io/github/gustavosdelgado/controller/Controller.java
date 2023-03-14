package io.github.gustavosdelgado.controller;

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
    public ResponseEntity<S> get(String token, Long entityId);

}
