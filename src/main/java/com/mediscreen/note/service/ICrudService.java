package com.mediscreen.note.service;

import java.util.List;

public interface ICrudService<T> {

    /**
     *Add and save to database method
     * @param objet to add
     * @return created object
     */
    T add(T objet);


    /**
     * Update the objects in the database
     * @param id of the object to modify
     * @param objet update
     * @return updating the object
     */
    T update(String id,T objet);


    /**
     * Delete object in the database
     * @param id object to delete
     */
    void delete(String id);

}
