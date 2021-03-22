package br.ueg.posse.si.p4.db;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import javax.swing.JOptionPane;

public class ConnectionFactory {

	public static Properties loadPropertiesFile() throws Exception {
		Properties prop = new Properties();
		File f = new File("jdbc.properties");		
		InputStream in = new FileInputStream(f.getAbsolutePath());
		prop.load(in);
		in.close();
		return prop;
	}

	public static Connection getConnection() throws Exception {
		Connection con = null;

		try {
			Properties prop = loadPropertiesFile();
			String driverClass = prop.getProperty("MYSQLJDBC.driver");
			String url = prop.getProperty("MYSQLJDBC.url");
			String username = prop.getProperty("MYSQLJDBC.username");
			String password = prop.getProperty("MYSQLJDBC.password");
			Class.forName(driverClass);
			con = DriverManager.getConnection(url, username, password);
			
				System.out.println("Conexao estabelecida com sucesso.");
		
				
			

		} catch (SQLException e) {
			JOptionPane.showMessageDialog(null,"Nao foi possivel conectar ao mysql","Erro ao conectar", JOptionPane.ERROR_MESSAGE);
			
			
		}
		return con;
	}
}
