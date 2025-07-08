package com.github.maxwelldantas.pedidos.service;

import com.github.maxwelldantas.pedidos.dto.PedidoDto;
import com.github.maxwelldantas.pedidos.dto.StatusDto;
import com.github.maxwelldantas.pedidos.model.Pedido;
import com.github.maxwelldantas.pedidos.model.Status;
import com.github.maxwelldantas.pedidos.repository.PedidoRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PedidoService {

	private final ModelMapper modelMapper;
	private final PedidoRepository repository;

	public List<PedidoDto> obterTodos() {
		return repository.findAll().stream()
				.map(p -> modelMapper.map(p, PedidoDto.class))
				.toList();
	}

	public PedidoDto obterPorId(Long id) {
		Pedido pedido = repository.findById(id)
				.orElseThrow(EntityNotFoundException::new);

		return modelMapper.map(pedido, PedidoDto.class);
	}

	public PedidoDto criarPedido(PedidoDto dto) {
		Pedido pedido = modelMapper.map(dto, Pedido.class);

		pedido.setDataHora(LocalDateTime.now());
		pedido.setStatus(Status.REALIZADO);
		pedido.getItens().forEach(item -> item.setPedido(pedido));
		repository.save(pedido);

		return modelMapper.map(pedido, PedidoDto.class);
	}

	public PedidoDto atualizaStatus(Long id, StatusDto dto) {

		Pedido pedido = repository.porIdComItens(id);

		if (pedido == null) {
			throw new EntityNotFoundException();
		}

		pedido.setStatus(dto.getStatus());
		repository.atualizaStatus(dto.getStatus(), pedido);
		return modelMapper.map(pedido, PedidoDto.class);
	}

	public void aprovaPagamentoPedido(Long id) {

		Pedido pedido = repository.porIdComItens(id);

		if (pedido == null) {
			throw new EntityNotFoundException();
		}

		pedido.setStatus(Status.PAGO);
		repository.atualizaStatus(Status.PAGO, pedido);
	}
}
