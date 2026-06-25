package br.edu.ufersa.PizzariaDAOeSERVICE.model.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import br.edu.ufersa.PizzariaDAOeSERVICE.model.DAO.PedidoDAO;
import br.edu.ufersa.PizzariaDAOeSERVICE.model.entities.Relatorio;

// Busca no banco os pedidos do período pedido e monta o Relatorio (item c do enunciado).
public class RelatorioService {
    private PedidoDAO dao = new PedidoDAO();

    public Relatorio gerarRelatorio(LocalDate dataInicio, LocalDate dataFim) {
        // dataInicio às 00:00:00 até dataFim às 23:59:59, pra incluir o dia inteiro
        LocalDateTime inicio = dataInicio.atStartOfDay();
        LocalDateTime fim = dataFim.atTime(LocalTime.MAX);

        return new Relatorio(dao.buscarPorPeriodo(inicio, fim), dataInicio, dataFim);
    }
}
