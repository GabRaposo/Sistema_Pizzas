package br.edu.ufersa.PizzariaDAOeSERVICE.model.DAO;
import java.sql.Connection;
import java.util.List;

public interface BaseDAO<E> {

    //SINGLETON:abertura e fechamento de conexão e as credenciais do banco de dados agora estão
    //compactados no connectionmanager
    public static Connection getConnection(){
        return ConnectionManager.getInstance().getConnection();
    }

    public static void closeConnection(Connection con){
        ConnectionManager.getInstance().closeConnection(con);
    }

    //metodos do DAO
    public E inserir (E entity);
    public void deletar(E entity);
    public void alterar (E entity);

    public List<E> buscar (String param);
    public List<E> listar();
}
