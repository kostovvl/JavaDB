package alararestaurant.service.impl;

import alararestaurant.config.GlobalConstants;
import alararestaurant.domain.dtos.seedorderdtos.SeedOrderDto;
import alararestaurant.domain.dtos.seedorderdtos.SeedOrdersDtos;
import alararestaurant.domain.dtos.seedorderdtos.SeedXmlItemDto;
import alararestaurant.domain.entities.Employee;
import alararestaurant.domain.entities.Item;
import alararestaurant.domain.entities.Order;
import alararestaurant.domain.entities.OrderItem;
import alararestaurant.repository.EmployeeRepository;
import alararestaurant.repository.ItemRepository;
import alararestaurant.repository.OrderItemRepository;
import alararestaurant.repository.OrderRepository;
import alararestaurant.service.OrderService;
import alararestaurant.util.FileUtil;
import alararestaurant.util.ValidationUtil;
import alararestaurant.util.XmlParser;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final ItemRepository itemRepository;
    private final EmployeeRepository employeeRepository;
    private final FileUtil reader;
    private final StringBuilder result;
    private final XmlParser parser;
    private final ModelMapper mapper;
    private final ValidationUtil validator;

    @Autowired
    public OrderServiceImpl(OrderRepository orderRepository, ItemRepository itemRepository, OrderItemRepository orderItemRepository,
                            EmployeeRepository employeeRepository, FileUtil reader, StringBuilder result, XmlParser parser, ModelMapper mapper, ValidationUtil validator) {
        this.orderRepository = orderRepository;
        this.itemRepository = itemRepository;
        this.employeeRepository = employeeRepository;
        this.reader = reader;
        this.result = result;
        this.parser = parser;
        this.mapper = mapper;
        this.validator = validator;
    }

    @Override
    public Boolean ordersAreImported() {
       return this.orderRepository.count() > 0;
    }

    @Override
    public String readOrdersXmlFile() throws IOException {
        return  this.reader.readFile(GlobalConstants.ORDERS_URL);
    }

    @Transactional
    @Override
    public String importOrders() throws IOException, JAXBException {
        SeedOrdersDtos seedOrdersDtos = this.parser.importFromXml(SeedOrdersDtos.class, GlobalConstants.ORDERS_URL);

        for (SeedOrderDto orderDto : seedOrdersDtos.getOrders()) {
            System.out.println();
            if (this.validator.isValid(orderDto)) {
                Order order = this.mapper.map(orderDto, Order.class);
                Set<OrderItem> orderItems = createOrderItems(orderDto, order);
                Employee employee = this.employeeRepository.findByName(orderDto.getEmployee());
                order.setOrderItems(orderItems);
                order.setEmployee(employee);
                this.orderRepository.saveAndFlush(order);
                this.result.append(String.format("Order for %s on %s added%n", order.getCustomer(), order.getDateType()));
            } else {
                this.result.append("Invalid data format.").append(System.lineSeparator());
            }
        }


        return this.result.toString();
    }

    private Set<OrderItem> createOrderItems(SeedOrderDto orderDto, Order order) {
        Set<OrderItem> result = new HashSet<>();

        for (SeedXmlItemDto itemDto : orderDto.getItems().getItems()) {
            OrderItem orderItem = new OrderItem();
            Item item = this.itemRepository.findByName(itemDto.getName());
            if (item == null) {
                continue;
            }
            orderItem.setItem(item);
            orderItem.setOrder(order);
            orderItem.setQuantity(itemDto.getQuantity());

            result.add(orderItem);
        }

        return result;
    }


    @Override
    public String exportOrdersFinishedByTheBurgerFlippers() {
        this.result.delete(0, this.result.length());

        List<Order> orders = this.orderRepository.secondExport();

        for (Order order : orders) {
            this.result.append(String.format("Name: %s%n" +
                    "Items:%n", order.getEmployee().getName()));
            for (OrderItem item : order.getOrderItems()) {
                this.result.append(String.format("Name: %s%n" +
                                "      Price: %s%n" +
                                "      Quantity: %d%n", item.getItem().getName(),
                        item.getItem().getPrice(), item.getQuantity()));

            }

        }

        return this.result.toString();
    }
}
