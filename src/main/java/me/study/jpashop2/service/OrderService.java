package me.study.jpashop2.service;

import lombok.RequiredArgsConstructor;
import me.study.jpashop2.domain.Delivery;
import me.study.jpashop2.domain.DeliveryStatus;
import me.study.jpashop2.domain.Member;
import me.study.jpashop2.domain.Order;
import me.study.jpashop2.domain.OrderItem;
import me.study.jpashop2.domain.item.Item;
import me.study.jpashop2.repository.ItemRepository;
import me.study.jpashop2.repository.MemberRepository;
import me.study.jpashop2.repository.OrderRepository;
import me.study.jpashop2.repository.OrderSearch;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final MemberRepository memberRepository;
    private final ItemRepository itemRepository;

    //주문
    @Transactional
    public Long order(Long memberId, Long itemId, int count) {
        //엔티티 조회
        Member member = memberRepository.findOne(memberId);
        Item item = itemRepository.findOne(itemId);

        //배송정보 생성
        Delivery delivery = new Delivery();
        delivery.setAddress(member.getAddress());
        delivery.setStatus(DeliveryStatus.READY);

        //주문상품 생성
        OrderItem orderItem = OrderItem.createOrderItem(item, item.getPrice(), count);

        //주문 생성
        Order order = Order.createOrder(member, delivery, orderItem);

        //주문 저장
        orderRepository.save(order);

        return order.getId();
    }

    //취소
    @Transactional
    public void cancelOrder(Long orderId) {
        Order order = orderRepository.findOne(orderId);
        order.cancel();
    }

    // 검색
    public List<Order> findOrders(OrderSearch orderSearch) {
        return orderRepository.findAllByString(orderSearch);
    }
}
