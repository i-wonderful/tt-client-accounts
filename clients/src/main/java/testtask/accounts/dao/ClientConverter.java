/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package testtask.accounts.dao;

import testtask.accounts.model.Client;

/**
 *
 * @author Olga Grazhdanova <dvl.java@gmail.com> at Jan 25, 2018
 */
public class ClientConverter {

    public static ClientEntity modelToEntity(Client client){
    
        if(client == null) return null;
        
        ClientEntity entity = new ClientEntity();
        
        entity.setId(client.getId());
        entity.setFirstName(client.getFirstName());
        entity.setLastName(client.getLastName());
        entity.setMiddleName(client.getMiddleName());
        entity.setBirthday(client.getBirthday());
        
        return entity;
    }
    
    public static Client entityToModel(ClientEntity clientEntity) {
    
        if(clientEntity == null) return null;
        
        Client client = new Client();
        
        client.setId(clientEntity.getId());
        client.setBirthday(clientEntity.getBirthday());
        client.setFirstName(clientEntity.getFirstName());
        client.setLastName(clientEntity.getLastName());
        client.setMiddleName(clientEntity.getMiddleName());
        
        return client;
    }
}
