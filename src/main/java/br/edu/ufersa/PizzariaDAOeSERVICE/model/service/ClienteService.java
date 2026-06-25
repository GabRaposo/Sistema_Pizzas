package br.edu.ufersa.PizzariaDAOeSERVICE.model.service;

import br.edu.ufersa.PizzariaDAOeSERVICE.model.DAO.BaseDAO;
import br.edu.ufersa.PizzariaDAOeSERVICE.model.DAO.ClienteDAO;
import br.edu.ufersa.PizzariaDAOeSERVICE.model.DAO.PedidoDAO;
import br.edu.ufersa.PizzariaDAOeSERVICE.model.entities.Cliente;
import java.util.List;

public class ClienteService {
    private BaseDAO<Cliente> dao = new ClienteDAO();
    private PedidoDAO pedidoDAO = new PedidoDAO();

    //cadastrando
    public Cliente cadastrarCliente(Cliente cliente){
        List<Cliente> encontrados = dao.buscar(cliente.getCpf());
        if(!encontrados.isEmpty()){
            throw new RuntimeException("Já há um cliente com o CPF: " + cliente.getCpf());
        }
        return dao.inserir(cliente);
    }

    //removendo — verifica se o cliente já não fez algum pedido (excluir quebraria
    //a reconstrução desse pedido e os totais do Relatório)
    public void removerCliente(Cliente cliente){
        List<Cliente> encontrados = dao.buscar(cliente.getCpf());
        if (encontrados.isEmpty()){
            throw new RuntimeException("Cliente "+ cliente.getCpf()+ " não foi encontrado!!!!!!!");
        }
        Cliente existente = encontrados.get(0);
        if (pedidoDAO.existePedidoComCliente(existente.getId())) {
            throw new RuntimeException(
                "Não é possível excluir o cliente '" + existente.getNome() +
                "': ele já possui pedidos registrados."
            );
        }
        dao.deletar(cliente);
    }

    //buscando
    public Cliente buscarCpf(String cpf){
        List<Cliente> encontrados = dao.buscar(cpf);
        if(encontrados.isEmpty()){
            return null;
        }
        return encontrados.get(0);
    }

    //listando
    public List<Cliente> listarClientes(){
        return dao.listar();
    }

    //alterando
    public void alterarCliente(Cliente cliente){
        List<Cliente> encontrados = dao.buscar(cliente.getCpf());
        if(encontrados.isEmpty()){
            throw new RuntimeException("Cliente " + cliente.getCpf() + " não encontrado");
        }
        dao.alterar(cliente);
    }
}
