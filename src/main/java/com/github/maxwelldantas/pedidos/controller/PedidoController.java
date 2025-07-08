package com.github.maxwelldantas.pedidos.controller;

import com.github.maxwelldantas.pedidos.dto.PedidoDto;
import com.github.maxwelldantas.pedidos.dto.StatusDto;
import com.github.maxwelldantas.pedidos.service.PedidoService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/pedidos")
@RequiredArgsConstructor
public class PedidoController {

	private final PedidoService service;

	@GetMapping()
	public List<PedidoDto> listarTodos() {
		return service.obterTodos();
	}

	@GetMapping("/{id}")
	public ResponseEntity<PedidoDto> listarPorId(@PathVariable @NotNull Long id) {
		PedidoDto dto = service.obterPorId(id);

		return ResponseEntity.ok(dto);
	}

	@PostMapping()
	public ResponseEntity<PedidoDto> realizaPedido(@RequestBody @Valid PedidoDto dto, UriComponentsBuilder uriBuilder) {
		PedidoDto pedidoRealizado = service.criarPedido(dto);

		URI endereco = uriBuilder.path("/pedidos/{id}").buildAndExpand(pedidoRealizado.getId()).toUri();

		return ResponseEntity.created(endereco).body(pedidoRealizado);

	}

	@PutMapping("/{id}/status")
	public ResponseEntity<PedidoDto> atualizaStatus(@PathVariable Long id, @RequestBody StatusDto status) {
		PedidoDto dto = service.atualizaStatus(id, status);

		return ResponseEntity.ok(dto);
	}


	@PutMapping("/{id}/pago")
	public ResponseEntity<Void> aprovaPagamento(@PathVariable @NotNull Long id) {
		service.aprovaPagamentoPedido(id);

		return ResponseEntity.ok().build();

	}
}
