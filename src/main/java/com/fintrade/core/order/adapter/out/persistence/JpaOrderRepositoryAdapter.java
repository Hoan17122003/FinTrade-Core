package com.fintrade.core.order.adapter.out.persistence;

import com.fintrade.core.order.adapter.out.persistence.entity.OrderEntity;
import com.fintrade.core.order.adapter.out.persistence.repository.SpringDataOrderRepository;
import com.fintrade.core.order.adapter.out.persistence.specification.OrderSpecifications;
import com.fintrade.core.order.application.port.in.SearchOrdersUseCase.OrderFilter;
import com.fintrade.core.order.application.port.out.OrderRepository;
import com.fintrade.core.order.domain.model.Order;
import com.fintrade.core.shared.domain.Money;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
public class JpaOrderRepositoryAdapter implements OrderRepository {

    private final SpringDataOrderRepository repository;

    public JpaOrderRepositoryAdapter(SpringDataOrderRepository repository) {
        this.repository = repository;
    }

    @Override
    public Order save(Order order) {
        var entity = repository.findById(order.id()).orElseGet(OrderEntity::new);
        entity.setId(order.id());
        entity.setUserId(order.userId());
        entity.setStockId(order.stockId());
        entity.setTicker(order.ticker());
        entity.setSide(order.side());
        entity.setQuantity(order.quantity());
        entity.setLimitPrice(order.price().amount());
        entity.setCurrency(order.price().currency());
        entity.setStatus(order.status());
        entity.setRejectionReason(order.rejectionReason());
        entity.setSubmittedAt(order.submittedAt());
        entity.setAcceptedAt(order.acceptedAt());
        entity.setFilledAt(order.filledAt());
        return toDomain(repository.save(entity));
    }

    @Override
    public Optional<Order> findById(UUID id) {
        return repository.findById(id).map(this::toDomain);
    }

    @Override
    public List<Order> search(OrderFilter filter) {
        return repository.findAll(OrderSpecifications.withFilter(filter)).stream()
                .map(this::toDomain)
                .toList();
    }

    private Order toDomain(OrderEntity entity) {
        return new Order(
                entity.getId(),
                entity.getUserId(),
                entity.getStockId(),
                entity.getTicker(),
                entity.getSide(),
                entity.getQuantity(),
                new Money(entity.getCurrency(), entity.getLimitPrice()),
                entity.getStatus(),
                entity.getRejectionReason(),
                entity.getSubmittedAt(),
                entity.getAcceptedAt(),
                entity.getFilledAt()
        );
    }
}
