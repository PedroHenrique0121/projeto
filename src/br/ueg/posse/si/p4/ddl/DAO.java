package br.ueg.posse.si.p4.ddl;

import br.ueg.posse.si.p4.annotationsCustomizadas.Column;
import br.ueg.posse.si.p4.annotationsCustomizadas.Entity;
import br.ueg.posse.si.p4.annotationsCustomizadas.Id;
import br.ueg.posse.si.p4.db.ColumnField;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
//import org.apache.log4j.Logger;
import br.ueg.posse.si.p4.db.ConnectionFactory;
import br.ueg.posse.si.p4.test.modelo.Produto;
import java.io.File;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import static javafx.scene.input.KeyCode.T;
import javax.swing.JOptionPane;
import javax.swing.SpringLayout;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class DAO<T extends Object> {

    private Connection conn;

    public DAO(Connection con) {
        // TODO Auto-generated constructor stub
        this.conn = con;

    }

    //static Statement stmtOBj = null;
//	public final static Logger logger = Logger.getLogger(ConnectionFactory.class);	
    public <T> void createTable(Class classe) throws SQLException {
            if (classe.isAnnotationPresent(Entity.class)) {
                
                 if (verificarSeExisteTabelas(classe)) {
                    
                 }
                 else{
                String sql = "CREATE TABLE IF NOT EXISTS " + classe.getSimpleName() + "( ";
                Field fieldlist[]
                        = classe.getDeclaredFields();
                for (int i = 1; i <= fieldlist.length; i++) {
                    if(i!= fieldlist.length){
                
                    sql += " " + fieldlist[i - 1].getName() + " " + retornarTipoSql(fieldlist[i - 1], fieldlist[i - 1].getGenericType(), classe) + " , ";
                    }else{
                         sql += " " + fieldlist[i - 1].getName() + " " + retornarTipoSql(fieldlist[i - 1], fieldlist[i - 1].getGenericType(), classe) + " ";
                    }
                }
                for (Field field :classe.getDeclaredFields()) {
                    if(field.isAnnotationPresent(Id.class)){
                         sql += definirChaveEstrangeira(classe);
                    }else{
                       
                    }
                }
               sql +=");";

                System.out.printf(sql +"\n");
                PreparedStatement mandar = conn.prepareStatement(sql);
                mandar.execute();
                 }
            
            } else {
                System.out.println("\n A classe ' <" + classe.getSimpleName() + "> ' nao usa uma anotação para reflertir uma "
                        + "entidade no banco de dados! \n Não é possivel criar a tabela ");
            }
            
        
          
}  
    

    public String retornarTipoSql(Field field, Type t, Class<?> classe) {

        String defaulString = " VARCHAR(250) ";
        String defaultIntgerLong = " BIGINT ";
        String defaultDouble = " DECIMAL(5,2) ";
        int q = 250; //= classe.getAnnotation(Column.class).length();

        if (field.isAnnotationPresent(Column.class)) {
            System.out.println(field.isAnnotationPresent(Column.class));
            Column c = field.getAnnotation(Column.class);
            field.setAccessible(true);
            q = c.value();
            // System.out.println(fieldlist[i].getType().toString());
            if (t.getTypeName().equals("java.lang.String")) {
                return " VARCHAR(" + q + ") ";
            }

        } else if (field.isAnnotationPresent(Id.class)) {

            Id id = field.getAnnotation(Id.class);
            field.setAccessible(true);
            if (t.getTypeName().toString().equals("int") || t.getTypeName().equals("java.lang.Long")) {
                return " BIGINT " + id.GenerationType() + " NOT NULL";
            }
        } /// se nao estiver nenhuma anotação
        else {
            if (t.getTypeName().equals("java.lang.String")) {
                return defaulString;
            } else if (t.getTypeName().equals("int") || t.getTypeName().equals("java.lang.Long")) {
                return defaultIntgerLong;
            }
            if (t.getTypeName().equals("java.lang.Double") || t.getTypeName().equals("double")) {
                return defaultDouble;
            }
        }

        return null;

    }

    public String definirChaveEstrangeira(Class classe) {
        String nome = "";
        String sql = "";
        for (Field field : classe.getDeclaredFields()) {
            if (field.isAnnotationPresent(Id.class)) {
                // field.setAccessible(true);
                sql = ", PRIMARY KEY(" + field.getName() + ")";
            }
        }
        return sql;
    }

    public void persist(T t) throws SQLException, IllegalArgumentException, IllegalAccessException {
        if (t.getClass().isAnnotationPresent(Entity.class)) {

            String sql = "INSERT INTO  " + t.getClass().getSimpleName() + "(";
            Field fieldCampo[] = t.getClass().getDeclaredFields();
            for (int i = 1; i <= fieldCampo.length; i++) {

                if (fieldCampo[i - 1].isAnnotationPresent(Id.class)) {

                } else {

                    if (i != fieldCampo.length) {
                        sql += " " + fieldCampo[i - 1].getName() + ",";
                    } else {
                        sql += " " + fieldCampo[i - 1].getName() + "";
                    }
                }
            }
            sql += ") VALUES( ";
            Field fieldValor[] = t.getClass().getDeclaredFields();
            for (int i = 1; i <= fieldValor.length; i++) {
                fieldValor[i - 1].setAccessible(true);
                if (fieldValor[i - 1].isAnnotationPresent(Id.class)) {

                } else {

                    if (i != fieldValor.length) {
                        if (fieldValor[i - 1].getType().getTypeName().equals("java.lang.String")) {
                            sql += "'" + fieldValor[i - 1].get(t) + "' , ";
                        } else {
                            sql += fieldValor[i - 1].get(t) + " , ";
                        }
                    } else {
                        if (fieldValor[i - 1].getType().getTypeName().equals("java.lang.String")) {
                            sql += "'" + fieldValor[i - 1].get(t) + "'  ";
                        } else {
                            sql += fieldValor[i - 1].get(t) + " ";
                        }
                    }
                }
            }
            sql += " );";

            System.out.println("\n " + sql);
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.execute();
        } else {
            System.out.println("a Classe <" + t.getClass().getSimpleName() + "> objeto <" + t.toString() + ">  Não possui é uma ereferencia de uma entidade e=de um banco de dados");
        }
    }

    public void merge(T t) throws IllegalArgumentException, IllegalAccessException, SQLException {
        String sql = "UPDATE " + t.getClass().getSimpleName() + " SET ";
        Field fieldCampo[] = t.getClass().getDeclaredFields();
        for (int i = 1; i <= fieldCampo.length; i++) {
            fieldCampo[i - 1].setAccessible(true);
            if (i != fieldCampo.length) {
                if (fieldCampo[i - 1].getType().getTypeName().equals("java.lang.String")) {
                    sql += "" + fieldCampo[i - 1].getName() + " = '" + fieldCampo[i - 1].get(t) + "' , ";
                } else {
                    sql += "" + fieldCampo[i - 1].getName() + " = " + fieldCampo[i - 1].get(t) + " , ";
                }
            } else {
                if (fieldCampo[i - 1].getType().getTypeName().equals("java.lang.String")) {
                    sql += "" + fieldCampo[i - 1].getName() + " = '" + fieldCampo[i - 1].get(t) + "'  ";
                } else {
                    sql += "" + fieldCampo[i - 1].getName() + " = " + fieldCampo[i - 1].get(t) + "  ";
                }
            }
        }

        for (Field field : t.getClass().getDeclaredFields()) {
            field.setAccessible(true);
            if (field.isAnnotationPresent(Id.class)) {
                sql += "WHERE " + t.getClass().getSimpleName() + "." + field.getName() + " = " + field.get(t) + " ;";
            }
        }
        System.out.println(sql);
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.execute();
    }

    public void remove(T t) throws IllegalArgumentException, IllegalAccessException, SQLException {

        String sql = "DELETE FROM " + t.getClass().getSimpleName() + "";

        for (Field field : t.getClass().getDeclaredFields()) {
            field.setAccessible(true);
            if (field.isAnnotationPresent(Id.class)) {
                sql += " WHERE " + t.getClass().getSimpleName() + "." + field.getName() + " = " + field.get(t) + " ;";
            }
        }

        System.out.println(sql);
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.execute();
    }

    public <T> T find(Class<T> classe, int id) throws SQLException, NoSuchMethodException, InstantiationException, IllegalAccessException {

        String sql = "SELECT * FROM " + classe.getSimpleName() + " WHERE ";

        for (Field field : classe.getDeclaredFields()) {
            field.setAccessible(true);
            if (field.isAnnotationPresent(Id.class)) {
                sql += classe.getSimpleName() + "." + field.getName() + " = " + id + ";";
            }
        }

        PreparedStatement stmt = conn.prepareStatement(sql);
        ResultSet rs = stmt.executeQuery();

        T t = classe.newInstance();

        if (rs.next()) {

            for (Field field : t.getClass().getDeclaredFields()) {
                field.setAccessible(true);
                if (field.getType().getTypeName().equals("java.lang.String")) {
                    field.set(t, rs.getObject(field.getName()));
                }
                if (field.getType().getTypeName().equals("int")) {
                    field.set(t, rs.getInt(field.getName()));
                }
                if (field.getType().getTypeName().equals("java.lang.Long")) {
                    field.set(t, rs.getInt(field.getName()));
                }
                if (field.getType().getTypeName().equals("java.lang.Double") || field.getType().getTypeName().equals("double")) {
                    field.set(t, rs.getDouble(field.getName()));
                }
            }
        }
        return t;

    }

    public List<T> findAll(Class<T> classe) throws SQLException, InstantiationException, IllegalAccessException {

        String sql = "SELECT * FROM " + classe.getSimpleName() + "; ";

        PreparedStatement stmt = conn.prepareStatement(sql);
        ResultSet rs = stmt.executeQuery();

        List<T> lista = new ArrayList<>();
        while (rs.next()) {
            T t = classe.newInstance();
            for (Field field : t.getClass().getDeclaredFields()) {
                field.setAccessible(true);
                if (field.getType().getTypeName().equals("java.lang.String")) {
                    field.set(t, rs.getObject(field.getName()));
                }
                if (field.getType().getTypeName().equals("int")) {
                    field.set(t, rs.getInt(field.getName()));
                }
                if (field.getType().getTypeName().equals("java.lang.Long")) {
                    field.set(t, rs.getInt(field.getName()));
                }
                if (field.getType().getTypeName().equals("java.lang.Double") || field.getType().getTypeName().equals("double")) {
                    field.set(t, rs.getDouble(field.getName()));
                }

            }

            lista.add(t);
        }
        return lista;

    }
    public boolean verificarSeExisteTabelas(Class<T> classe) throws SQLException{
         String sql = "SHOW Tables FROM " + conn.getCatalog() +";";
         boolean s= false;
          PreparedStatement stmt = conn.prepareStatement(sql);
        ResultSet rs = stmt.executeQuery();
        List<String> lista = new ArrayList<>();
        while(rs.next()){
           lista.add( rs.getString(1));
        }
        for(int i =0;i<lista.size();i++){
            if(lista.get(i).equals(classe.getClass().getSimpleName())){
               s= true;
            }
        }
        return s;
    }
    
    
    public List<T> verificarCamposTabela(Class<T> classe) throws SQLException {
        String sql = " SHOW COLUMNS FROM " + conn.getCatalog() + "." + classe.getSimpleName() + " ;";

        PreparedStatement stmt = conn.prepareStatement(sql);
        ResultSet rs = stmt.executeQuery();

        List<ColumnField> lista = new ArrayList<>();
        while (rs.next()) {
            ColumnField field = new ColumnField();
            field.setField(rs.getString("field"));
            field.setType(rs.getString("type"));

            lista.add(field);
        }

// System.out.println(sql);
        return (List<T>) lista;
    }

    // criar um Entity manager 
    // extender todo dao generico
    // fazer uma verificação função pra quando criar o EntityManager criar tableas de acordo com os modelos que im plementarem o @Entity que eu vou criar
    public List<String> verificarXml() throws ClassNotFoundException {
        List<String> l = new ArrayList<>();
        try {
//creating a constructor of file class and parsing an XML file  
            File file = new File("entidades.xml");
//an instance of factory that gives a document builder  
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
//an instance of builder to parse the specified xml file  
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.parse(file);
            doc.getDocumentElement().normalize();
            // System.out.println("Root element: " + doc.getDocumentElement().getNodeName());
            NodeList nodeList = doc.getElementsByTagName("classe");
// nodeList is not iterable, so we are using for loop  
            for (int itr = 0; itr < nodeList.getLength(); itr++) {
                Node node = nodeList.item(itr);
                //System.out.println("Node Name :" + node.getNodeName());
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element eElement = (Element) node;
                    l.add(eElement.getElementsByTagName("path").item(0).getTextContent());

                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return l;
    }

}

/*try {
                    if(Class.forName(elemNode.getTextContent()).isAnnotationPresent(Entity.class)){
                        System.out.println(" é entidade"+ Class.forName(elemNode.getTextContent().toString()).getSimpleName());
                    }
                } catch (ClassNotFoundException ex) {
                   // Logger.getLogger(DAO.class.getName()).log(Level.SEVERE, null, ex);
                }*/
