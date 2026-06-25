package br.edu.ufersa.PizzariaDAOeSERVICE.facade;

import java.time.LocalDate;
import java.util.List;

import br.edu.ufersa.PizzariaDAOeSERVICE.model.entities.Adicional;
import br.edu.ufersa.PizzariaDAOeSERVICE.model.entities.Carrinho;
import br.edu.ufersa.PizzariaDAOeSERVICE.model.entities.Cliente;
import br.edu.ufersa.PizzariaDAOeSERVICE.model.entities.Dono;
import br.edu.ufersa.PizzariaDAOeSERVICE.model.entities.EstadoPedido;
import br.edu.ufersa.PizzariaDAOeSERVICE.model.entities.Estoque;
import br.edu.ufersa.PizzariaDAOeSERVICE.model.entities.FormaPagamento;
import br.edu.ufersa.PizzariaDAOeSERVICE.model.entities.Funcionario;
import br.edu.ufersa.PizzariaDAOeSERVICE.model.entities.ItemPedido;
import br.edu.ufersa.PizzariaDAOeSERVICE.model.entities.ModoEntrega;
import br.edu.ufersa.PizzariaDAOeSERVICE.model.entities.Pedido;
import br.edu.ufersa.PizzariaDAOeSERVICE.model.entities.Pizza;
import br.edu.ufersa.PizzariaDAOeSERVICE.model.entities.Relatorio;
import br.edu.ufersa.PizzariaDAOeSERVICE.model.entities.TamanhoPizza;
import br.edu.ufersa.PizzariaDAOeSERVICE.model.entities.Usuario;
import br.edu.ufersa.PizzariaDAOeSERVICE.model.service.AdicionalService;
import br.edu.ufersa.PizzariaDAOeSERVICE.model.service.CarrinhoService;
import br.edu.ufersa.PizzariaDAOeSERVICE.model.service.ClienteService;
import br.edu.ufersa.PizzariaDAOeSERVICE.model.service.DonoService;
import br.edu.ufersa.PizzariaDAOeSERVICE.model.service.EstoqueService;
import br.edu.ufersa.PizzariaDAOeSERVICE.model.service.FuncionarioService;
import br.edu.ufersa.PizzariaDAOeSERVICE.model.service.IPizzaService;
import br.edu.ufersa.PizzariaDAOeSERVICE.model.service.LoginService;
import br.edu.ufersa.PizzariaDAOeSERVICE.model.service.PedidoService;
import br.edu.ufersa.PizzariaDAOeSERVICE.model.service.PizzaServiceProxy;
import br.edu.ufersa.PizzariaDAOeSERVICE.model.service.RelatorioService;
import br.edu.ufersa.PizzariaDAOeSERVICE.model.service.SistemaService;

public class PizzariaFacade {

    private final LoginService loginService       = new LoginService();
    private final ClienteService clienteService   = new ClienteService();
    private final EstoqueService estoqueService    = new EstoqueService();
    private final PedidoService pedidoService      = new PedidoService();
    private final CarrinhoService carrinhoService  = new CarrinhoService();
    private final SistemaService sistemaService    = new SistemaService();
    private final AdicionalService adicionalService = new AdicionalService();
    private final DonoService donoService = new DonoService();
    private final FuncionarioService funcionarioService = new FuncionarioService();
    private final RelatorioService relatorioService = new RelatorioService();

    //Facade conhece a interface (IPizzaService), proxy aplica
    private final IPizzaService pizzaService       = new PizzaServiceProxy(loginService);

    //---------------- Autenticação ----------------
    public boolean login(String nome, String senha) {
        return loginService.autenticar(nome, senha);
    }

    public void logout() {
        loginService.logout();
    }

    public Usuario usuarioLogado() {
        return loginService.getUsuarioLogado();
    }

    //cadastra o Dono — só funciona uma vez; a segunda tentativa lança exceção
    public Dono cadastrarDono(String nome, String email, String senha) {
        return donoService.cadastrarDono(new Dono(nome, email, senha));
    }

    public Funcionario cadastrarFuncionario(String nome, String email, String senha) {
        return funcionarioService.cadastrarFuncionario(new Funcionario(nome, email, senha));
    }

    //---------------- Clientes ----------------
    public Cliente cadastrarCliente(String nome, String cpf, String endereco) {
        return clienteService.cadastrarCliente(new Cliente(nome, cpf, endereco));
    }

    public List<Cliente> listarClientes() {
        return clienteService.listarClientes();
    }

    public void alterarCliente(Cliente cliente) {
        clienteService.alterarCliente(cliente);
    }

    public void removerCliente(Cliente cliente) {
        clienteService.removerCliente(cliente);
    }

    //---------------- Pizzas (passa pelo Proxy automaticamente) ----------------

    public Pizza cadastrarPizza(String tipo, double valor) {
        return pizzaService.cadastrarPizza(new Pizza(tipo, valor));
    }

    public void removerPizza(Pizza pizza) {
        pizzaService.removerPizza(pizza);
    }

    public void alterarPizza(Pizza pizza) {
        pizzaService.alterarPizza(pizza);
    }

    public List<Pizza> listarPizzas() {
        return pizzaService.listarPizzas();
    }

    public List<Pizza> buscarPizzasPorTipo(String tipo) {
        return pizzaService.buscarPorTipo(tipo);
    }

    public List<Pizza> buscarPizzasPorCliente(String cpf) {
        return pizzaService.buscarPorCliente(cpf);
    }

    //----------------adicional----------------

    public Adicional cadastrarAdicional(String nome, double valor) {
        return adicionalService.cadastrarAdicional(new Adicional(nome, valor));
    }


    public Adicional cadastrarAdicionalComEstoque(String nome, double valor, int quantidadeInicial) {
        Adicional adicional = adicionalService.cadastrarAdicional(new Adicional(nome, valor));
        estoqueService.cadastrarEstoque(new Estoque(adicional, quantidadeInicial));
        return adicional;
    }

    public void alterarAdicional(Adicional adicional) {
        adicionalService.alterarAdicional(adicional);
    }

    public void removerAdicional(Adicional adicional) {
        adicionalService.removerAdicional(adicional);
    }

    public List<Adicional> listarAdicionais() {
        return adicionalService.listarAdicionais();
    }

    public List<Adicional> buscarAdicionaisPorNome(String nome) {
        return adicionalService.buscarPorNome(nome);
    }

    //-------------Estoque---------------

    //precisa primeiro buscar o objeto Estoque pra montar a chamada)
    public void atualizarEstoque(Adicional adicional, int qtd) {
        List<Estoque> encontrados = estoqueService.buscarPorAdicional(adicional.getNome());
        if (encontrados.isEmpty()) {
            throw new RuntimeException("Adicional '" + adicional.getNome() + "' não tem estoque cadastrado.");
        }
        estoqueService.adicionarQuantidade(encontrados.get(0), qtd);
    }

    public List<Estoque> listarEstoque() {
        return estoqueService.listarEstoques();
    }

    // ----------Carrinho -> Pedido------------

    public Carrinho novoCarrinho(Cliente cliente) {
        return new Carrinho(cliente);
    }

    public ItemPedido adicionarPizzaAoCarrinho(Carrinho carrinho, Pizza pizza, TamanhoPizza tamanho, int quantidade) {
        return carrinhoService.adicionarPizza(carrinho, pizza, tamanho, quantidade);
    }

    public void adicionarAdicionalAoCarrinho(Carrinho carrinho, ItemPedido item, Adicional adicional, int qtd) {
        carrinhoService.adicionarAdicional(carrinho, item, adicional, qtd);
    }

    // esconde os passos: finalizar carrinho (com todas as pizzas, entrega e pagamento) ->
    // registrar pedido -> validar/baixar estoque -> calcular total
    public double fecharPedido(Carrinho carrinho, ModoEntrega modoEntrega, FormaPagamento formaPagamento) {
        Pedido pedido = carrinhoService.finalizarPedido(carrinho, modoEntrega, formaPagamento);
        return sistemaService.processarPedido(pedido);
    }

    public void avancarEstadoPedido(Pedido pedido) {
        sistemaService.avancarEstadoPedido(pedido);
        pedidoService.alterarEstadoPedido(pedido, pedido.getEstado()); // persiste no banco
    }

    public void cancelarPedido(Pedido pedido) {
        sistemaService.cancelarPedido(pedido);
    }

    public List<Pedido> listarPedidos() {
        return pedidoService.listarPedidos();
    }

    public List<Pedido> buscarPedidosPorEstado(EstadoPedido estado) {
        return pedidoService.buscarPorEstado(estado);
    }

    public List<Pedido> buscarPedidosPorPizza(Pizza pizza) {
        return pedidoService.buscarPorPizza(pizza);
    }

    public List<Pedido> buscarPedidosPorCliente(String cpf) {
        return pedidoService.buscarPorCliente(cpf);
    }


    //---Relatorio---

    public Relatorio gerarRelatorio(LocalDate dataInicio, LocalDate dataFim) {
        return relatorioService.gerarRelatorio(dataInicio, dataFim);
    }
}
