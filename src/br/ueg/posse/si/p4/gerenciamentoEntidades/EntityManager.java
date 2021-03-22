/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.ueg.posse.si.p4.gerenciamentoEntidades;

import br.ueg.posse.si.p4.annotationsCustomizadas.Entity;
import br.ueg.posse.si.p4.db.ConnectionFactory;
import br.ueg.posse.si.p4.ddl.DAO;
import br.ueg.posse.si.p4.test.modelo.Produto;
import java.io.File;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

/**
 *
 * @author pedro
 */
public class EntityManager<T extends Object> {

    List<String> l = new ArrayList<>();
    public static Connection con;
    DAO dao;

    public void createEntityManagerFactory(Connection con) throws ClassNotFoundException, InstantiationException, IllegalAccessException, SQLException {
        this.con = con;
        dao = new DAO<>(this.con);

        l = dao.verificarXml();

        System.out.println(l.size());
        for (int i = 0; i < l.size(); i++) {
            Class<T> classe = (Class<T>) Class.forName(l.get(i).replaceAll(" ", ""));
            if (classe.isAnnotationPresent(Entity.class)) {
                dao.createTable(classe);
            }
        }

    }

    

    public void persist(Object classe) throws SQLException, IllegalArgumentException, IllegalAccessException {
        dao.persist(classe);
    }

    public void merge(Object classe) throws IllegalArgumentException, IllegalAccessException, SQLException {
        dao.merge(classe);
    }

    public void remove(Object classe) throws IllegalArgumentException, IllegalAccessException, SQLException {
        dao.remove(classe);
    }

    public <T> T find(Class<?> classe, int id) throws SQLException, NoSuchMethodException, InstantiationException, IllegalAccessException {

        return (T) dao.find(classe, id);
    }

    public List<T> findAll(Class<T> classe) throws SQLException, InstantiationException, IllegalAccessException {

        return dao.findAll(classe);
    }
}
