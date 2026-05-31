package br.edu.ufersa.PizzariaDAOeSERVICE.model.DAO;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;

public interface BaseDAO<E> {
    //tenho q lembrar de colocar a senha e o host aq
    public static final String URL  = "jdbc:mysql://localhost/pizzariadb";
    public static final String USER = "root";
    public static final String PASS = "gadelhameajude";

    //abrir conexão com o banco
    public static Connection getConnection(){
        try{
            return DriverManager.getConnection(URL, USER, PASS);
        }catch(SQLException e){e.printStackTrace();}
        return null;
    }

    public static void closeConnection(Connection con){
        if(con != null){
            try{
                con.close();
            }catch(SQLException e){e.printStackTrace();}
        }
    }

    //metodos do DAO
    public E inserir (E entity);
    public void deletar(E entity);
    public void alterar (E entity);

    public List<E> buscar (String param);
    public List<E> listar();
}
