package br.ueg.posse.si.p4.test;

import br.ueg.posse.si.p4.annotationsCustomizadas.Entity;
import br.ueg.posse.si.p4.db.ColumnField;
import br.ueg.posse.si.p4.gerenciamentoEntidades.EntityManager;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
//import java.util.logging.Logger;

import org.apache.log4j.Logger;

import br.ueg.posse.si.p4.db.ConnectionFactory;
import br.ueg.posse.si.p4.ddl.DAO;
import br.ueg.posse.si.p4.test.modelo.Produto;
import br.ueg.posse.si.p4.test.modelo.Usuario;
import java.io.File;
import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class Teste {

    static Statement stmtOBj = null;
    public final static Logger logger = Logger.getLogger(ConnectionFactory.class);

    public static void main(String[] args) throws Exception {
        Connection con = ConnectionFactory.getConnection();
        // é preciso definir descrever no arquivo entidades.xml  as classes que irão representar uma entidades no banco de dados
        // è necessario presentear(está presente, conter) a anotação @Entity na classse modelo que representará a entidade no banco de dados
        // So basta isso para começar gerenciar suas entidades  
        EntityManager em = new EntityManager();
        
        // metodo para criar 'automaticamente' as tabelas no banco de dados, 
        //sendo que o Framework criara essas  tabelas de acordo com classes que foram descritas no arquivo entidades.xml, como foi  mencionado acima
        em.createEntityManagerFactory(con);
        
        /* OBS 
           encontrei uma maneira de juntar o util ao agradavel, o metodo acima  possui instruçoes para  ler 
           o arquivo entidades.xml que contem descrições de classes a serem  definidas para representar 
           entidades no banco de dados . Então as tabelas são criadas automaticamante pelo metodo acima , e o
           outro ponto agradavel é que o nome desse metodo representa a criação de uma fabrica de gerente de entidades.
           Ja ficando definidas as classes que podem ser gerenciadas pelo objeto 'em' (EntityManager).
        */
        
        Usuario usuario = new Usuario();
        usuario.setLogin("usuario qualquer");
        usuario.setNome("Usuario para teste");
        usuario.setSenha("senha");
        
        // metodo para persistir um objeto no banco de dados
        em.persist(usuario);
       
        // busca um objeto pelo id 
        usuario = (Usuario) em.find(Usuario.class, 1);
        //lista todos objetos existentes
        em.findAll(Usuario.class);
        
        // metodo para alterar um registro no banco de dados
        usuario.setNome(" alterei meu nome");
        em.merge(usuario);
        //metodo para excluir um registro do banco de dados
        //em.remove(usuario);
    }

}
